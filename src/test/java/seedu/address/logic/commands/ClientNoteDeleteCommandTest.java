package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

class ClientNoteDeleteCommandTest {

    private static final String NOTE_CONTENT_1 = "client note content 1";
    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_invalidClientIndex_throwsCommandException() {
        Index invalidClientIndex = Index.fromOneBased(model.getFilteredClientList().size() + 1);
        ClientNoteDeleteCommand clientNoteDeleteCommand =
                new ClientNoteDeleteCommand(invalidClientIndex, Index.fromOneBased(1));
        assertThrows(CommandException.class, () -> clientNoteDeleteCommand.execute(model));
    }

    @Test
    public void execute_validClientIndexInvalidClientNoteIndex_throwsCommandException() {
        Client client = TypicalClients.ALICE;
        Note clientNote = new Note(NOTE_CONTENT_1);
        Index idx = Index.fromOneBased(1);
        model.addClientNote(client, clientNote);
        Index invalidClientNoteIndex = Index.fromOneBased(model.getFilteredClientNotesList().size() + 1);
        ClientNoteDeleteCommand clientNoteDeleteCommand =
                new ClientNoteDeleteCommand(idx, invalidClientNoteIndex);
        assertThrows(CommandException.class, () -> clientNoteDeleteCommand.execute(model));
    }

    @Test
    public void testEquals() {
        ClientNoteDeleteCommand clientNoteDeleteCommand1 =
                new ClientNoteDeleteCommand(Index.fromOneBased(1), Index.fromOneBased(1));
        ClientNoteDeleteCommand clientNoteDeleteCommand1Duplicate =
                new ClientNoteDeleteCommand(Index.fromOneBased(1), Index.fromOneBased(1));
        ClientNoteDeleteCommand clientNoteDeleteCommandClient2 =
                new ClientNoteDeleteCommand(Index.fromOneBased(2), Index.fromOneBased(1));
        ClientNoteDeleteCommand clientNoteDeleteCommand1ClientNote2 =
                new ClientNoteDeleteCommand(Index.fromOneBased(1), Index.fromOneBased(2));

        // same object -> returns true
        assertEquals(clientNoteDeleteCommand1, clientNoteDeleteCommand1);

        // same values -> returns true
        assertEquals(clientNoteDeleteCommand1, clientNoteDeleteCommand1Duplicate);

        // diff values (diff client) --> returns false
        assertNotEquals(clientNoteDeleteCommand1, clientNoteDeleteCommandClient2);

        // diff values (diff client note) --> returns false
        assertNotEquals(clientNoteDeleteCommand1, clientNoteDeleteCommand1ClientNote2);

    }
}