package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.storage.JsonAdaptedNote.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.country.Country;
import seedu.address.model.country.CountryCodeVerifier;
import seedu.address.model.note.CountryNote;
import seedu.address.model.note.Note;
import seedu.address.model.tag.Tag;

public class JsonAdaptedNoteTest {

    private static final String INVALID_NOTE = "";
    private static final String INVALID_COUNTRY_CODE = "ZZ";
    private static final String NULL_COUNTRY_CODE = "NULL_CC";
    private static final String INVALID_TAG = "@@@";

    private static final String VALID_NOTE = "some note";
    private static final String VALID_COUNTRY = "SG";
    private static final Tag VALID_TAG = new Tag("tag");

    private Set<Tag> getDefaultTagSet() {
        Set<Tag> tagSet = new HashSet<>();
        tagSet.add(VALID_TAG);
        return tagSet;
    }

    private Set<JsonAdaptedTag> getDefaultJsonAdaptedTagSet() {
        Set<JsonAdaptedTag> jsonAdaptedTagSet = new HashSet<>();
        jsonAdaptedTagSet.add(new JsonAdaptedTag(VALID_TAG));
        return jsonAdaptedTagSet;
    }

    @Test
    public void toModelType_validTaggedClientNote_returnsTaggedClientNote() throws IllegalValueException {
        Note taggedClientNote = new Note(VALID_NOTE);
        Set<Tag> associatedTags = getDefaultTagSet();
        taggedClientNote.setTags(associatedTags);
        JsonAdaptedNote jsonAdaptedNote = new JsonAdaptedNote(taggedClientNote);
        Note deserializedNote = jsonAdaptedNote.toModelType();
        assertTrue(deserializedNote.isClientNote());
        assertTrue(deserializedNote.getTags().equals(associatedTags));
    }

    @Test
    public void toModelType_validCountryNote_returnsCountryNote() throws IllegalValueException {
        JsonAdaptedNote jsonAdaptedNote =
                new JsonAdaptedNote(new CountryNote(VALID_NOTE, new Country(VALID_COUNTRY)));
        assertFalse(jsonAdaptedNote.toModelType().isClientNote());
    }

    @Test
    public void toModelType_validClientNoteWithJsonCreator_returnsClientNote() throws IllegalValueException {
        Set<Tag> associatedTags = getDefaultTagSet();
        Set<JsonAdaptedTag> jsonAdaptedTagSet = getDefaultJsonAdaptedTagSet();

        JsonAdaptedNote jsonAdaptedNote = new JsonAdaptedNote(VALID_NOTE, NULL_COUNTRY_CODE, jsonAdaptedTagSet);

        Note deserializedNote = jsonAdaptedNote.toModelType();
        assertTrue(deserializedNote.isClientNote());
        assertTrue(deserializedNote.getTags().equals(associatedTags));
        assertTrue(deserializedNote.getNoteContent().equals(VALID_NOTE));
    }

    @Test
    public void toModelType_validCountryNoteWithJsonCreator_returnsCountryNote() throws IllegalValueException {
        Set<Tag> associatedTags = getDefaultTagSet();
        Set<JsonAdaptedTag> jsonAdaptedTagSet = getDefaultJsonAdaptedTagSet();

        JsonAdaptedNote jsonAdaptedNote = new JsonAdaptedNote(VALID_NOTE, VALID_COUNTRY, jsonAdaptedTagSet);
        Note deserializedNote = jsonAdaptedNote.toModelType();
        assertFalse(deserializedNote.isClientNote());
        assertTrue(deserializedNote.getTags().equals(associatedTags));
        assertTrue(deserializedNote.getNoteContent().equals(VALID_NOTE));
    }

    @Test
    public void toModelType_invalidNote_throwsIllegalValueException() {
        Set<JsonAdaptedTag> jsonAdaptedTagSet = getDefaultJsonAdaptedTagSet();
        JsonAdaptedNote jsonAdaptedNote = new JsonAdaptedNote(INVALID_NOTE, VALID_COUNTRY, jsonAdaptedTagSet);
        String expectedMessage = Note.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, jsonAdaptedNote::toModelType);
    }

    @Test
    public void toModelType_nullNote_throwsIllegalValueException() {
        Set<JsonAdaptedTag> jsonAdaptedTagSet = getDefaultJsonAdaptedTagSet();
        JsonAdaptedNote jsonAdaptedNote = new JsonAdaptedNote(null, VALID_COUNTRY, jsonAdaptedTagSet);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, "content");
        assertThrows(IllegalValueException.class, expectedMessage, jsonAdaptedNote::toModelType);
    }

    @Test
    public void toModelType_invalidCountry_throwsIllegalValueException() {
        Set<JsonAdaptedTag> jsonAdaptedTagSet = getDefaultJsonAdaptedTagSet();
        JsonAdaptedNote jsonAdaptedNote = new JsonAdaptedNote(VALID_NOTE, INVALID_COUNTRY_CODE, jsonAdaptedTagSet);
        String expectedMessage = CountryCodeVerifier.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, jsonAdaptedNote::toModelType);
    }

    @Test
    public void toModelType_nullCountry_throwsIllegalValueException() {
        Set<JsonAdaptedTag> jsonAdaptedTagSet = getDefaultJsonAdaptedTagSet();
        JsonAdaptedNote jsonAdaptedNote = new JsonAdaptedNote(VALID_NOTE, null, jsonAdaptedTagSet);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT,
                Country.class.getSimpleName().toLowerCase());
        assertThrows(IllegalValueException.class, expectedMessage, jsonAdaptedNote::toModelType);
    }

    @Test
    public void toModelType_invalidTags_throwsIllegalValueException() {
        Set<JsonAdaptedTag> jsonAdaptedTagSet = new HashSet<>();
        jsonAdaptedTagSet.add(new JsonAdaptedTag(INVALID_TAG));
        JsonAdaptedNote jsonAdaptedNote = new JsonAdaptedNote(VALID_NOTE, VALID_COUNTRY, jsonAdaptedTagSet);
        String expectedMessage = Tag.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, jsonAdaptedNote::toModelType);
    }

    @Test
    public void toModelType_nullTagsCountryNote_returnsCountryNoteWithoutTags() throws IllegalValueException {
        JsonAdaptedNote jsonAdaptedNote = new JsonAdaptedNote(VALID_NOTE, VALID_COUNTRY, null);
        Note deserializedNote = jsonAdaptedNote.toModelType();
        Set<Tag> expectedTags = new HashSet<>();
        assertFalse(deserializedNote.isClientNote());
        assertTrue(deserializedNote.getTags().equals(expectedTags));
        assertTrue(deserializedNote.getNoteContent().equals(VALID_NOTE));
    }

}
