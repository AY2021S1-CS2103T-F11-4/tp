package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.storage.JsonAdaptedClient.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalClients.BENSON;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.client.Address;
import seedu.address.model.client.Client;
import seedu.address.model.client.ContractExpiryDate;
import seedu.address.model.client.Email;
import seedu.address.model.client.Name;
import seedu.address.model.client.Phone;
import seedu.address.model.client.Timezone;
import seedu.address.model.country.Country;
import seedu.address.model.country.CountryCodeVerifier;
import seedu.address.model.note.Note;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.ClientBuilder;

public class JsonAdaptedClientTest {

    private static final String INVALID_NAME = "R@chel";
    private static final String INVALID_PHONE = "99";
    private static final String INVALID_ADDRESS = " ";
    private static final String INVALID_EMAIL = "example.com";
    private static final String INVALID_COUNTRY = "ZZ";
    private static final String INVALID_TIMEZONE = "GT+8";
    private static final String INVALID_CONTRACT_EXPIRY_DATE = "1,2,2020";
    private static final String INVALID_LAST_MODIFIED_INSTANT = "2020/20/20T12:12:12Z";

    private static final String VALID_NAME = BENSON.getName().toString();
    private static final String VALID_PHONE = BENSON.getPhone().toString();
    private static final String VALID_EMAIL = BENSON.getEmail().toString();
    private static final String VALID_ADDRESS = BENSON.getAddress().toString();
    private static final String VALID_COUNTRY = BENSON.getCountry().getCountryCode();
    private static final String VALID_TIMEZONE = BENSON.getTimezone().toString();
    private static final String VALID_CONTRACT_EXPIRY_DATE = BENSON.getContractExpiryDate().toString();
    private static final String VALID_LAST_MODIFIED_INSTANT = BENSON.getLastModifiedInstant().toString();
    private final List<JsonAdaptedNote> clientNotes = new ArrayList<>();

    @Test
    public void toModelType_validClientDetails_returnsClient() throws Exception {
        JsonAdaptedClient client = new JsonAdaptedClient(BENSON);
        assertEquals(BENSON, client.toModelType());
    }

    @Test
    public void toModelType_validClientWithTaggedClientNote_returnsTrue() throws IllegalValueException {
        Client taggedClient = new ClientBuilder(BENSON).build();
        Note taggedClientNote = new Note("some note");
        Set<Tag> associatedTags = new HashSet<>();
        associatedTags.add(new Tag("someTag"));
        taggedClientNote.setTags(associatedTags);
        taggedClient.addClientNote(taggedClientNote);
        JsonAdaptedClient client = new JsonAdaptedClient(taggedClient);
        assertDoesNotThrow(client::toModelType);
        assertTrue(taggedClient.equals(client.toModelType()));
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        JsonAdaptedClient client = new JsonAdaptedClient(INVALID_NAME, VALID_PHONE, VALID_EMAIL,
                VALID_ADDRESS, VALID_COUNTRY, VALID_TIMEZONE, VALID_CONTRACT_EXPIRY_DATE, VALID_LAST_MODIFIED_INSTANT,
                clientNotes);
        String expectedMessage = Name.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, client::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedClient client = new JsonAdaptedClient(null, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                VALID_COUNTRY, VALID_TIMEZONE, VALID_CONTRACT_EXPIRY_DATE, VALID_LAST_MODIFIED_INSTANT,
                clientNotes);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, client::toModelType);
    }

    @Test
    public void toModelType_invalidPhone_throwsIllegalValueException() {
        JsonAdaptedClient client = new JsonAdaptedClient(VALID_NAME, INVALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                VALID_COUNTRY, VALID_TIMEZONE, VALID_CONTRACT_EXPIRY_DATE, VALID_LAST_MODIFIED_INSTANT,
                clientNotes);
        String expectedMessage = Phone.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, client::toModelType);
    }

    @Test
    public void toModelType_nullPhone_throwsIllegalValueException() {
        JsonAdaptedClient client = new JsonAdaptedClient(VALID_NAME, null, VALID_EMAIL, VALID_ADDRESS,
                VALID_COUNTRY, VALID_TIMEZONE, VALID_CONTRACT_EXPIRY_DATE, VALID_LAST_MODIFIED_INSTANT,
                clientNotes);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, client::toModelType);
    }

    @Test
    public void toModelType_invalidEmail_throwsIllegalValueException() {
        JsonAdaptedClient client = new JsonAdaptedClient(VALID_NAME, VALID_PHONE, INVALID_EMAIL, VALID_ADDRESS,
                VALID_COUNTRY, VALID_TIMEZONE, VALID_CONTRACT_EXPIRY_DATE, VALID_LAST_MODIFIED_INSTANT,
                clientNotes);
        String expectedMessage = Email.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, client::toModelType);
    }

    @Test
    public void toModelType_nullEmail_throwsIllegalValueException() {
        JsonAdaptedClient client = new JsonAdaptedClient(VALID_NAME, VALID_PHONE, null, VALID_ADDRESS,
                VALID_COUNTRY, VALID_TIMEZONE, VALID_CONTRACT_EXPIRY_DATE, VALID_LAST_MODIFIED_INSTANT,
                clientNotes);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, client::toModelType);
    }

    @Test
    public void toModelType_invalidAddress_throwsIllegalValueException() {
        JsonAdaptedClient client = new JsonAdaptedClient(VALID_NAME, VALID_PHONE, VALID_EMAIL, INVALID_ADDRESS,
                VALID_COUNTRY, VALID_TIMEZONE, VALID_CONTRACT_EXPIRY_DATE, VALID_LAST_MODIFIED_INSTANT,
                clientNotes);
        String expectedMessage = Address.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, client::toModelType);
    }

    @Test
    public void toModelType_nullAddress_throwsIllegalValueException() {
        JsonAdaptedClient client = new JsonAdaptedClient(VALID_NAME, VALID_PHONE, VALID_EMAIL, null,
                VALID_COUNTRY, VALID_TIMEZONE, VALID_CONTRACT_EXPIRY_DATE, VALID_LAST_MODIFIED_INSTANT,
                clientNotes);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, client::toModelType);
    }

    @Test
    public void toModelType_invalidCountry_throwsIllegalValueException() {
        JsonAdaptedClient client = new JsonAdaptedClient(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                INVALID_COUNTRY, VALID_TIMEZONE, VALID_CONTRACT_EXPIRY_DATE, VALID_LAST_MODIFIED_INSTANT,
                clientNotes);
        String expectedMessage = CountryCodeVerifier.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, client::toModelType);
    }

    @Test
    public void toModelType_nullCountry_throwsIllegalValueException() {
        JsonAdaptedClient client = new JsonAdaptedClient(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS, null,
                VALID_TIMEZONE, VALID_CONTRACT_EXPIRY_DATE, VALID_LAST_MODIFIED_INSTANT, clientNotes);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Country.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, client::toModelType);
    }

    @Test
    public void toModelType_invalidTimezone_throwsIllegalValueException() {
        JsonAdaptedClient client = new JsonAdaptedClient(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                VALID_COUNTRY, INVALID_TIMEZONE, VALID_CONTRACT_EXPIRY_DATE, VALID_LAST_MODIFIED_INSTANT,
                clientNotes);
        String expectedMessage = Timezone.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, client::toModelType);
    }

    @Test
    public void toModelType_nullTimezone_throwsIllegalValueException() {
        JsonAdaptedClient client = new JsonAdaptedClient(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                VALID_COUNTRY, null, VALID_CONTRACT_EXPIRY_DATE, VALID_LAST_MODIFIED_INSTANT,
                clientNotes);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Timezone.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, client::toModelType);
    }

    @Test
    public void toModelType_emptyContractExpiryDate_doesNotThrowException() {
        JsonAdaptedClient client = new JsonAdaptedClient(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                VALID_COUNTRY, VALID_TIMEZONE, "", VALID_LAST_MODIFIED_INSTANT, clientNotes);
        assertDoesNotThrow(client::toModelType);
    }

    @Test
    public void toModelType_invalidContractExpiryDate_throwsIllegalValueException() {
        JsonAdaptedClient client = new JsonAdaptedClient(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                VALID_COUNTRY, VALID_TIMEZONE, INVALID_CONTRACT_EXPIRY_DATE, VALID_LAST_MODIFIED_INSTANT,
                clientNotes);
        String expectedMessage = ContractExpiryDate.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, client::toModelType);
    }

    @Test
    public void toModelType_nullContractExpiryDate_throwsIllegalValueException() {
        JsonAdaptedClient client = new JsonAdaptedClient(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                VALID_COUNTRY, VALID_TIMEZONE, null, VALID_LAST_MODIFIED_INSTANT, clientNotes);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, ContractExpiryDate.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, client::toModelType);
    }

    @Test
    public void toModelType_invalidLastModifiedInstant_doesNotThrowException() {
        JsonAdaptedClient client = new JsonAdaptedClient(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                VALID_COUNTRY, VALID_TIMEZONE, VALID_CONTRACT_EXPIRY_DATE, INVALID_LAST_MODIFIED_INSTANT,
                clientNotes);
        assertDoesNotThrow(client::toModelType);
    }

    @Test
    public void toModelType_nullLastModifiedInstant_doesNotThrowException() {
        JsonAdaptedClient client = new JsonAdaptedClient(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS,
                VALID_COUNTRY, VALID_TIMEZONE, VALID_CONTRACT_EXPIRY_DATE, null,
                clientNotes);
        assertDoesNotThrow(client::toModelType);
    }

}
