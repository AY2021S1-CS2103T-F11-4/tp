package seedu.address.model.note;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.model.tag.Tag;

/**
 * Generic Note class for country and client notes.
 */
public class Note {
    public static final String MESSAGE_CONSTRAINTS = "Notes should not be blank";
    private static final Logger logger = LogsCenter.getLogger(Note.class);
    private final String noteContent;
    private final Set<Tag> tags = new HashSet<>();

    /**
     * Constructs a Note object with some content in it.
     *
     * @param content to be added
     */
    public Note(String content) {
        requireNonNull(content);
        noteContent = content;
        logger.info(String.format("--------------[New Note created with contents: %s]", noteContent));
    }

    /**
     * Sets the tags of this {@code Note} to the {@code tags} passed in.
     *
     * @param tags The set of {@code Tag} objects to be associated with this {@code Note}.
     */
    public void setTags(Set<Tag> tags) {
        requireAllNonNull(tags);
        this.tags.clear();
        this.tags.addAll(tags);
    }

    /**
     * Gets the content of the note.
     *
     * @return The content of the note.
     */
    public String getNoteContent() {
        return noteContent;
    }

    /**
     * Returns whether this note is a client note.
     *
     * @return True if this note is a client note, false otherwise.
     */
    public boolean isClientNote() {
        return true;
    }

    /**
     * Gets the set of tags that is related to this Note.
     *
     * @return The set of tags that is related to this Note.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Returns true if a given string is a valid note.
     */
    public static boolean isValidNote(String test) {
        return !test.trim().isEmpty();
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof Note)) {
            return false;
        }

        // state check
        Note c = (Note) obj;
        boolean hasSameTags = this.tags.equals(c.tags);

        return this.noteContent.equals(c.noteContent) && hasSameTags;
    }

    @Override
    public String toString() {
        return noteContent;
    }

    @Override
    public int hashCode() {
        return Objects.hash(noteContent, tags);
    }
}
