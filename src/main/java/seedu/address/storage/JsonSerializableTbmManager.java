package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.ReadOnlyTbmManager;
import seedu.address.model.TbmManager;
import seedu.address.model.client.Client;
import seedu.address.model.note.CountryNote;
import seedu.address.model.note.Note;

/**
 * An Immutable TbmManager that is serializable to JSON format.
 */
@JsonRootName(value = "tbmManager")
class JsonSerializableTbmManager {

    public static final String MESSAGE_DUPLICATE_CLIENT = "Clients list contains duplicate client(s).";

    private final List<JsonAdaptedClient> clients = new ArrayList<>();
    private final List<JsonAdaptedNote> countryNotes = new ArrayList<>();

    /**
     * Constructs a {@code JsonSerializableTbmManager} with the given {@code clients} and {@code countryNotes} for
     * Jackson to use.
     */
    @JsonCreator
    public JsonSerializableTbmManager(@JsonProperty("clients") List<JsonAdaptedClient> clients,
                                      @JsonProperty("countryNotes") List<JsonAdaptedNote> countryNotes) {
        this.clients.addAll(clients);
        this.countryNotes.addAll(countryNotes);
    }

    /**
     * Converts a given {@code ReadOnlyTbmManager} into this class for Jackson use.
     *
     * @param source future changes to this will not affect the created {@code JsonSerializableTbmManager}.
     */
    public JsonSerializableTbmManager(ReadOnlyTbmManager source) {
        List<JsonAdaptedClient> clientsList =
                source.getClientList().stream().map(JsonAdaptedClient::new).collect(Collectors.toList());
        List<JsonAdaptedNote> notesList = source.getCountryNoteList().stream().map(JsonAdaptedNote::new)
                .collect(Collectors.toList());
        clients.addAll(clientsList);
        countryNotes.addAll(notesList);
    }

    /**
     * Converts this address book into the model's {@code TbmManager} object.
     *
     * @throws IllegalValueException if there were any data constraints violated.
     */
    public TbmManager toModelType() throws IllegalValueException {
        TbmManager tbmManager = new TbmManager();
        for (JsonAdaptedClient jsonAdaptedClient : clients) {
            Client client = jsonAdaptedClient.toModelType();
            if (tbmManager.hasClient(client)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_CLIENT);
            }
            tbmManager.addClient(client);
        }
        for (JsonAdaptedNote jsonAdaptedCountryNote : countryNotes) {
            Note modelNote = jsonAdaptedCountryNote.toModelType();
            // since client notes are stored inside client only
            assert !modelNote.isClientNote() : "converting a non-country note into a country note";
            tbmManager.addCountryNote((CountryNote) modelNote);
        }
        return tbmManager;
    }
}
