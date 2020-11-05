package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.logic.parser.ParserUtil;
import seedu.address.model.client.Address;
import seedu.address.model.client.Client;
import seedu.address.model.client.ContractExpiryDate;
import seedu.address.model.client.Email;
import seedu.address.model.client.LastModifiedInstant;
import seedu.address.model.client.Name;
import seedu.address.model.client.Phone;
import seedu.address.model.client.Timezone;
import seedu.address.model.country.Country;
import seedu.address.model.country.CountryCodeVerifier;
import seedu.address.model.note.Note;

/**
 * Jackson-friendly version of {@link Client}.
 */
class JsonAdaptedClient {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Client's %s field is missing!";

    private final String name;
    private final String phone;
    private final String email;
    private final String address;
    private final String country;
    private final String timezone;
    private final String contractExpiryDate;
    private final String lastModifiedInstant;
    private final List<JsonAdaptedNote> clientNotes = new ArrayList<>();

    /**
     * Constructs a {@code JsonAdaptedClient} with the given client details.
     */
    @JsonCreator
    public JsonAdaptedClient(@JsonProperty("name") String name,
            @JsonProperty("phone") String phone, @JsonProperty("email") String email,
            @JsonProperty("address") String address, @JsonProperty("country") String country,
            @JsonProperty("timezone") String timezone,
            @JsonProperty("contractExpiryDate") String contractExpiryDate,
            @JsonProperty("lastModifiedInstant") String lastModifiedInstant,
            @JsonProperty("clientNotes") List<JsonAdaptedNote> clientNotes) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.country = country;
        this.timezone = timezone;
        this.contractExpiryDate = contractExpiryDate;
        this.lastModifiedInstant = lastModifiedInstant;
        if (clientNotes != null) {
            this.clientNotes.addAll(clientNotes);
        }
    }

    /**
     * Converts a given {@code Client} into this class for Jackson use.
     */
    public JsonAdaptedClient(Client source) {
        name = source.getName().fullName;
        phone = source.getPhone().value;
        email = source.getEmail().value;
        address = source.getAddress().value;
        country = source.getCountry().getCountryCode();
        timezone = source.getTimezone().toString();
        contractExpiryDate = source.getContractExpiryDate().value;
        lastModifiedInstant = source.getLastModifiedInstant().toString();
        clientNotes.addAll(source.getClientNotesAsUnmodifiableList().stream().map(JsonAdaptedNote::new)
                .collect(Collectors.toList()));
    }

    /**
     * Converts this Jackson-friendly adapted client object into the model's {@code Client} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted client.
     */
    public Client toModelType() throws IllegalValueException {
        final List<Note> clientNotes = new ArrayList<>();
        for (JsonAdaptedNote note : this.clientNotes) {
            clientNotes.add(note.toModelType());
        }
        //================  checks that all required fields are non-null and valid: =============================
        if (name == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        if (phone == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName()));
        }
        if (!Phone.isValidPhone(phone)) {
            throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
        }
        final Phone modelPhone = new Phone(phone);

        if (email == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName()));
        }
        if (!Email.isValidEmail(email)) {
            throw new IllegalValueException(Email.MESSAGE_CONSTRAINTS);
        }
        final Email modelEmail = new Email(email);

        if (address == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName()));
        }
        if (!Address.isValidAddress(address)) {
            throw new IllegalValueException(Address.MESSAGE_CONSTRAINTS);
        }
        final Address modelAddress = new Address(address);

        if (country == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, Country.class.getSimpleName()));
        }
        if (!CountryCodeVerifier.isValidCountryCode(country)) {
            throw new IllegalValueException(CountryCodeVerifier.MESSAGE_CONSTRAINTS);
        }
        final Country modelCountry = new Country(country);

        if (timezone == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, Timezone.class.getSimpleName()));
        }
        if (!Timezone.isValidTimezone(timezone)) {
            throw new IllegalValueException(Timezone.MESSAGE_CONSTRAINTS);
        }
        final Timezone modelTimezone = new Timezone(timezone);

        if (contractExpiryDate == null) {
            throw new IllegalValueException(
                    String.format(MISSING_FIELD_MESSAGE_FORMAT, ContractExpiryDate.class.getSimpleName()));
        }
        /*
         * It's possible for {@code contractExpiryDate} to have an empty String as a value, which would indicate
         * the client has no contractExpiryDate set, and {@code ParserUtil.parseContractExpiryDate} will
         * parse it into a {@code ContractExpiryDate.NULL_DATE}
         */
        if (!contractExpiryDate.isEmpty() && !ContractExpiryDate.isValidDate(contractExpiryDate)) {
            throw new IllegalValueException(ContractExpiryDate.MESSAGE_CONSTRAINTS);
        }
        final ContractExpiryDate modelContractExpiryContractExpiryDate =
                ParserUtil.parseContractExpiryDate(contractExpiryDate);
        /*
         * Does not throw an exception if lastModifiedInstant is missing/invalid due to corruption of data.
         * This field is merely metadata for us and is not significant enough to discard client's data due
         * to this field being missing.
         */
        LastModifiedInstant modelLastModifiedInstant;
        if (lastModifiedInstant == null) {
            modelLastModifiedInstant = new LastModifiedInstant();
        } else {
            modelLastModifiedInstant = new LastModifiedInstant(lastModifiedInstant);
        }
        // =============================================================================================

        Client modelClient = new Client(modelName, modelPhone, modelEmail, modelAddress, modelCountry, modelTimezone,
                    modelContractExpiryContractExpiryDate, modelLastModifiedInstant);
        for (Note note : clientNotes) {
            modelClient.addClientNote(note);
        }
        return modelClient;
    }

}
