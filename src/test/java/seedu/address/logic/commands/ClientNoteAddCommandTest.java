package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static seedu.address.testutil.TypicalClients.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.client.Client;
import seedu.address.model.note.Note;
import seedu.address.testutil.TypicalClients;


public class ClientNoteAddCommandTest {

    private static final String NOTE_CONTENT_1 = "client note content 1";
    private static final String NOTE_CONTENT_2 = "client note content 2";
    private static final String NOTE_CONTENT_3 = "client note content 3";

    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void constructor_nullArgs_throwsNullPointerException() {
        Index idx = Index.fromOneBased(1);
        Note clientNote = new Note(NOTE_CONTENT_1);
        assertThrows(NullPointerException.class, () -> new ClientNoteAddCommand(idx, null));
        assertThrows(NullPointerException.class, () -> new ClientNoteAddCommand(null, clientNote));
    }

    @Test
    public void execute_duplicateClientNote_throwsCommandException() {
        Client client = TypicalClients.ALICE;
        Index idx = Index.fromOneBased(1);
        Note clientNote = new Note(NOTE_CONTENT_1);
        model.addClientNote(client, clientNote);
        ClientNoteAddCommand clientNoteAddCommand = new ClientNoteAddCommand(idx, clientNote);
        assertThrows(CommandException.class, () -> clientNoteAddCommand.execute(model));
    }

    @Test
    public void execute_notDuplicateClientNote_successfullyAddsClientNote() {
        try {
            Client client = TypicalClients.ALICE;
            Index idx = Index.fromOneBased(1);
            Note clientNote2 = new Note(NOTE_CONTENT_2);
            Note clientNote3 = new Note(NOTE_CONTENT_3);
            assertFalse(model.hasClientNote(client, clientNote2));
            ClientNoteAddCommand clientNoteAddCommand2 = new ClientNoteAddCommand(idx, clientNote2);
            ClientNoteAddCommand clientNoteAddCommand3 = new ClientNoteAddCommand(idx, clientNote3);
            clientNoteAddCommand2.execute(model);
            clientNoteAddCommand3.execute(model);
            assertTrue(model.hasClientNote(client, clientNote2));
            assertTrue(model.hasClientNote(client, clientNote3));
        } catch (CommandException e) {
            fail();
        }
    }

    @Test
    public void equals() {
        Index idx1 = Index.fromOneBased(1);
        Index idx2 = Index.fromOneBased(2);
        Note clientNote1 = new Note(NOTE_CONTENT_1);
        Note clientNote2 = new Note(NOTE_CONTENT_2);
        ClientNoteAddCommand addCommand = new ClientNoteAddCommand(idx1, clientNote1);
        ClientNoteAddCommand addCommandDifferentNote = new ClientNoteAddCommand(idx1, clientNote2);
        ClientNoteAddCommand addCommandDifferentIndex = new ClientNoteAddCommand(idx2, clientNote1);

        // same object -> returns true
        assertTrue(addCommand.equals(addCommand));

        // same values -> returns true
        ClientNoteAddCommand addCommandCopy = new ClientNoteAddCommand(idx1, clientNote1);
        assertTrue(addCommand.equals(addCommandCopy));

        // different types -> returns false
        assertFalse(addCommand.equals(1));

        // null -> returns false
        assertFalse(addCommand.equals(null));

        // different note or index -> returns false
        assertFalse(addCommand.equals(addCommandDifferentNote));
        assertFalse(addCommand.equals(addCommandDifferentIndex));
    }

}
