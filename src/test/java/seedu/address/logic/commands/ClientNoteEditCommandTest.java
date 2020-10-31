package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TestUtil.basicEqualsTests;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.client.Client;
import seedu.address.model.note.Note;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.ClientBuilder;

class ClientNoteEditCommandTest {

    private static final String NOTE_CONTENT_1 = "client note content 1";
    private static final String NOTE_CONTENT_2 = "client note content 2";

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager();
    }

    @Test
    public void execute_validClientIdxValidNoteIdx_generatesClientNoteEditCommandSuccessfully() {
        Index clientIdx = Index.fromOneBased(1);
        Index clientNoteIdx = Index.fromOneBased(1);
        Note clientNote1 = new Note(NOTE_CONTENT_1);
        Note clientNote2 = new Note(NOTE_CONTENT_2);
        Note newEditNote = new Note("dummy note to edit previous note");
        Client client1 = new ClientBuilder().withName("client1").build();
        model.addClient(client1);
        model.addClientNote(client1, clientNote1);
        model.addClientNote(client1, clientNote2);

        Model expectedModel = new ModelManager();
        Client client1Copy = new ClientBuilder().withName("client1").build();
        expectedModel.addClient(client1Copy);
        expectedModel.addClientNote(client1Copy, newEditNote);
        expectedModel.addClientNote(client1Copy, clientNote2);

        CommandResult expectedResult = new CommandResult(ClientNoteEditCommand.MESSAGE_EDIT_CLIENT_NOTE_SUCCESS);
        ClientNoteEditCommand clientNoteEditCommand = new ClientNoteEditCommand(clientIdx, clientNoteIdx, newEditNote);
        assertCommandSuccess(clientNoteEditCommand, model, expectedResult, expectedModel);
    }

    @Test
    public void execute_editExistingUntaggedNoteToAddTag_discardsDefaultUntaggedTag() {
        Tag oldTag = Tag.UNTAGGED;
        Set<Tag> oldTagSet = new HashSet<>();
        oldTagSet.add(oldTag);
        Note oldClientNote = new Note(NOTE_CONTENT_1);
        oldClientNote.setTags(oldTagSet);

        Tag newTag = new Tag("FreshTag");
        Set<Tag> newTagSet = new HashSet<>();
        newTagSet.add(newTag);
        Note newClientNote = new Note(NOTE_CONTENT_2);
        newClientNote.setTags(newTagSet);

        Client client = new ClientBuilder().withName("client1").build();
        model.addClient(client);
        model.addClientNote(client, oldClientNote);

        Model expectedModel = new ModelManager();
        Client clientCopy = new ClientBuilder().withName("client1").build();
        expectedModel.addClient(clientCopy);
        expectedModel.addClientNote(clientCopy, newClientNote);

        Index clientIdx = Index.fromOneBased(1);
        Index clientNoteIdx = Index.fromOneBased(1);
        CommandResult expectedResult = new CommandResult(ClientNoteEditCommand.MESSAGE_EDIT_CLIENT_NOTE_SUCCESS);
        ClientNoteEditCommand clientNoteEditCommand = new ClientNoteEditCommand(clientIdx,
                clientNoteIdx, newClientNote);
        assertCommandSuccess(clientNoteEditCommand, model, expectedResult, expectedModel);
        assert model.hasClientNote(client, newClientNote);
        Note resultNote = model.getSortedFilteredClientNotesList().get(0);
        assertFalse(resultNote.getTags().contains(Tag.UNTAGGED)); // default tag has been removed successfully
    }

    @Test
    public void execute_validClientIdxValidNoteIdxNewTaggedNote_preservesTagHistory() {
        Tag oldTag = new Tag("historicalTag");
        Tag newTag = new Tag("newTag");
        Set<Tag> newTagSet = new HashSet<>();
        newTagSet.add(newTag);
        Set<Tag> oldTagSet = new HashSet<>();
        oldTagSet.add(oldTag);
        Set<Tag> expectedTags = new HashSet<>(oldTagSet); // expected to preserve old tags and have new tags
        expectedTags.add(newTag);
        Note oldClientNote = new Note(NOTE_CONTENT_1);
        oldClientNote.setTags(oldTagSet);
        Note clientNote2 = new Note(NOTE_CONTENT_2);
        Note newClientNote = new Note("dummy note to edit previous note");
        newClientNote.setTags(newTagSet);
        Client client1 = new ClientBuilder().withName("client1").build();
        model.addClient(client1);
        model.addClientNote(client1, oldClientNote);
        model.addClientNote(client1, clientNote2);
        Model expectedModel = new ModelManager();
        Client client1Copy = new ClientBuilder().withName("client1").build();
        expectedModel.addClient(client1Copy);
        newClientNote.setTags(expectedTags); // containing old and new tags
        expectedModel.addClientNote(client1Copy, newClientNote);
        expectedModel.addClientNote(client1Copy, clientNote2);

        Index clientIdx = Index.fromOneBased(1);
        Index clientNoteIdx = Index.fromOneBased(1);
        CommandResult expectedResult = new CommandResult(ClientNoteEditCommand.MESSAGE_EDIT_CLIENT_NOTE_SUCCESS);
        ClientNoteEditCommand clientNoteEditCommand = new ClientNoteEditCommand(clientIdx,
                clientNoteIdx, newClientNote);
        assertCommandSuccess(clientNoteEditCommand, model, expectedResult, expectedModel);
    }

    @Test
    public void testEquals() {
        Note testNote = new Note("need for speed");
        ClientNoteEditCommand clientNoteEditCommand1 = new ClientNoteEditCommand(Index.fromOneBased(1),
                Index.fromOneBased(1), testNote);
        ClientNoteEditCommand clientNoteEditCommand1Duplicate = new ClientNoteEditCommand(Index.fromOneBased(1),
                Index.fromOneBased(1), testNote);
        ClientNoteEditCommand clientNoteEditCommandClient2 = new ClientNoteEditCommand(Index.fromOneBased(2),
                Index.fromOneBased(1), testNote);
        ClientNoteEditCommand clientNoteEditCommand1ClientNote2 = new ClientNoteEditCommand(Index.fromOneBased(1),
                Index.fromOneBased(2), testNote);

        // basic equals tests
        basicEqualsTests(clientNoteEditCommand1);

        // same values -> returns true
        assertTrue(clientNoteEditCommand1.equals(clientNoteEditCommand1Duplicate));

        // diff values (diff client) --> returns false
        assertFalse(clientNoteEditCommand1.equals(clientNoteEditCommandClient2));

        // diff values (diff client note) --> returns false
        assertFalse(clientNoteEditCommand1.equals(clientNoteEditCommand1ClientNote2));
    }

}
