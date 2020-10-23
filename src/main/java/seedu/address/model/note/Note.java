package seedu.address.model.note;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import seedu.address.model.tag.Tag;

/**
 * Generic Note class for country and client notes.
 */
public class Note {
    public static final String MESSAGE_CONSTRAINTS = "Notes should not be blank";
    private final String noteContents;
    private final Set<Tag> tags = new HashSet<>();

    /**
     * Constructs a Note object with some content in it.
     *
     * @param content to be added
     */
    public Note(String content) {
        requireNonNull(content);
        noteContents = content;
    }

    /**
     * Sets the tags of this note to the {@code tags} passed in.
     *
     * @param tags The tag to be added.
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
    public String getNoteContents() {
        return noteContents;
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

        return this.noteContents.equals(c.noteContents) && hasSameTags;
    }

    @Override
    public String toString() {
        return noteContents;
    }

    @Override
    public int hashCode() {
        return Objects.hash(noteContents, tags);
    }
}
