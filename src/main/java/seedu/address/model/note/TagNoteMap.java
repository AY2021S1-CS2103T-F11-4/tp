package seedu.address.model.note;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.logic.parser.ParserUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.client.Client;
import seedu.address.model.tag.Tag;

/**
 * Manages the relationship between Tags and Notes.
 */
public class TagNoteMap {

    private static final Logger logger = LogsCenter.getLogger(TagNoteMap.class);

    /**
     * A map that contains the mapping from any tag to a unique tag.
     * A map is used instead of a set because the set does not offer the option of getting objects inside it.
     */
    private final Map<Tag, Tag> uniqueTagMap = new HashMap<>();
    private final LinkedHashSet<Note> noteSet = new LinkedHashSet<>();
    private final Map<Tag, List<Note>> tagToNotesMap = new HashMap<>();
    private final Map<Note, LinkedHashSet<Tag>> noteToTagsMap = new HashMap<>();

    /**
     * Constructor ensures our unique tag map has the UNTAGGED tag.
     */
    public TagNoteMap() {
        uniqueTagMap.put(Tag.UNTAGGED, Tag.UNTAGGED);
        tagToNotesMap.put(Tag.UNTAGGED, new ArrayList<>());
    }

    private void initTagNoteMapFromNotes(List<Note> notes) {
        noteSet.addAll(notes);
        for (Note clientNote : notes) {
            Set<Tag> tags = clientNote.getTags();
            for (Tag tag : tags) {
                uniqueTagMap.put(tag, tag);
            }
            addTagsForNote(tags, clientNote);
        }
    }

    /**
     * Initialises the TagNoteMap from a list of clients.
     *
     * @param clients The list of clients, each containing their notes and associated tags.
     */
    public void initTagNoteMapFromClients(List<Client> clients) {
        requireAllNonNull(clients);
        for (Client client : clients) {
            List<Note> clientNotes = client.getClientNotesAsUnmodifiableList();
            initTagNoteMapFromNotes(clientNotes);
        }
        logger.info("--------------[TagNoteMap initialized from clients]");
    }

    /**
     * Initialises the TagNoteMap from a list of countries.
     *
     * @param countryNotes The set of countries, each containing their notes and associated tags.
     */
    public void initTagNoteMapFromCountryNotes(List<Note> countryNotes) {
        requireAllNonNull(countryNotes);
        initTagNoteMapFromNotes(countryNotes);
        logger.info("--------------[TagNoteMap initialized from country notes]");
    }

    /**
     * Get a set of unique tag objects, based on the tagStrings.
     * If any tag is not inside the tag set, we add it to the tag set.
     * If tagStrings is empty, we return a set containing only the UNTAGGED tag.
     */
    public Set<Tag> getUniqueTags(List<String> tagStrings) throws ParseException {
        Set<Tag> uniqueTags = new HashSet<>();
        if (tagStrings.isEmpty()) {
            uniqueTags.add(Tag.UNTAGGED);
            return uniqueTags;
        }
        for (String tagString : tagStrings) {
            Tag tag = ParserUtil.parseTag(tagString);
            if (uniqueTagMap.containsKey(tag)) {
                uniqueTags.add(uniqueTagMap.get(tag));
            } else {
                uniqueTagMap.put(tag, tag);
                uniqueTags.add(tag);
            }
        }
        return Collections.unmodifiableSet(uniqueTags);
    }

    /** Retrieves all {@code Tag} objects that are associated to a particular {@code Note}.
     *
     * @param note The particular {@code Note} to get associated tags for.
     * @return Set of {@code Tag} objects that are associated to the note.
     */
    public Set<Tag> getTagsForNote(Note note) {
        return Collections.unmodifiableSet(noteToTagsMap.getOrDefault(note, new LinkedHashSet<>()));
    }

    /** Retrieves all {@code Note} objects that are associated to a particular {@code Tag}.
     *
     * @param tag The particular {@code Tag} to get associated notes for.
     * @return List of {@code Note} objects that are associated to the tag.
     */
    public List<Note> getNotesForTag(Tag tag) {
        return Collections.unmodifiableList(tagToNotesMap.getOrDefault(tag, new ArrayList<>()));
    }

    /**
     * Deletes a note from the TagNoteMap.
     * Modifies existing {@code noteSet, tagToNotesMap, noteToTagsMap, uniqueTagMap}.
     *
     * @param note The note to be deleted from the TagNoteMap.
     */
    public void deleteNote(Note note) {
        assert noteSet.contains(note) : "trying to remove note that doesn't exist in noteSet";
        assert noteToTagsMap.containsKey(note) : "trying to remove note that doesn't exist in noteToTagsMap";
        noteSet.remove(note);
        Set<Tag> associatedTags = this.noteToTagsMap.get(note);
        for (Tag tag : associatedTags) { // remove note for relevant tags
            List<Note> notes = this.tagToNotesMap.get(tag);
            notes.remove(note);
            if (notes.isEmpty() && !tag.equals(Tag.UNTAGGED)) {
                // other than default Tag.UNTAGGED, remove the tag itself from tagToNotesMap and uniqueTagMap:
                this.tagToNotesMap.remove(tag);
                this.uniqueTagMap.remove(tag);
            }
        }
        noteToTagsMap.remove(note);
    }

    /**
     * Edits a note from the TagNoteMap.
     * Modifies existing {@code noteSet, tagToNotesMap, noteToTagsMap, uniqueTagMap}.
     *
     * @param noteToEdit The note to be edited from the TagNoteMap.
     * @param newNote The new note to replace it with.
     */
    public void editNote(Note noteToEdit, Note newNote) {
        assert noteSet.contains(noteToEdit) : "trying to remove note that doesn't exist in noteSet";
        assert noteToTagsMap.containsKey(noteToEdit) : "trying to remove note that doesn't exist in noteToTagsMap";
        noteSet.remove(noteToEdit);
        Set<Tag> associatedTags = this.noteToTagsMap.get(noteToEdit);
        // remove this note's association for relevant tags:
        for (Tag tag : associatedTags) {
            List<Note> notes = this.tagToNotesMap.get(tag);
            notes.remove(noteToEdit);
            // other than default Tag.UNTAGGED, remove the tag itself from tagToNotesMap and uniqueTagMap:
            if (notes.isEmpty() && !tag.equals(Tag.UNTAGGED)) {
                this.tagToNotesMap.remove(tag);
                this.uniqueTagMap.remove(tag);
            }
        }
        noteSet.add(newNote);
        noteToTagsMap.remove(noteToEdit);
        addTagsForNote(newNote.getTags(), newNote);
    }

    /**
     * Adds a set of tags to a note, the note will contain a union of its current tag set and the input tag set
     * after this operation.
     * Method is public for ModelManager to use.
     *
     * @param newTags The tags to associate with a particular note.
     * @param note    The note to associate the tag with.
     */
    public void addTagsForNote(Set<Tag> newTags, Note note) {
        requireAllNonNull(newTags, note);
        for (Tag newTag : newTags) {
            // update the notes set for each of the tags:
            if (tagToNotesMap.containsKey(newTag)) { // if that tag exists
                tagToNotesMap.get(newTag).add(note);
            } else { // new tag:
                this.uniqueTagMap.put(newTag, newTag);
                List<Note> notes = new ArrayList<>();
                notes.add(note);
                tagToNotesMap.put(newTag, notes);
            }
        }
        // update the tags set for the note:
        LinkedHashSet<Tag> currentTags = noteToTagsMap.getOrDefault(note, new LinkedHashSet<>());
        currentTags.addAll(newTags);
        noteToTagsMap.put(note, currentTags);
        noteSet.add(note);
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object:
        if (obj == this) {
            return true;
        }
        // instance of handles nulls
        if (!(obj instanceof TagNoteMap)) {
            return false;
        }
        // state check:
        TagNoteMap other = (TagNoteMap) obj;
        return this.uniqueTagMap.equals(other.uniqueTagMap)
                && this.noteSet.equals(other.noteSet)
                && this.tagToNotesMap.equals(other.tagToNotesMap)
                && this.noteToTagsMap.equals(other.noteToTagsMap);
    }

}
