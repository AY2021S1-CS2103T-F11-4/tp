package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.ClientNoteDeleteCommand;
import seedu.address.logic.parser.exceptions.ParseException;

class ClientNoteDeleteCommandParserTest {
    private static final String EXPECTED_PARSE_FAILURE_MESSAGE =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, ClientNoteDeleteCommand.MESSAGE_USAGE);

    private final ClientNoteDeleteCommandParser parser = new ClientNoteDeleteCommandParser();

    @Test
    public void parse_emptyUserInput_throwsParseException() {
        String userInput = "";
        assertParseFailure(parser, userInput, EXPECTED_PARSE_FAILURE_MESSAGE);
    }

    @Test
    public void parse_inCorrectlyDelimitedUserInput_throwsParseException() {
        String userInput1 = "client notedelete 11";
        String userInput2 = "clientnote delete 1 1";
        assertParseFailure(parser, userInput1, EXPECTED_PARSE_FAILURE_MESSAGE);
        assertParseFailure(parser, userInput2, EXPECTED_PARSE_FAILURE_MESSAGE);
    }

    @Test
    public void parse_correctUserInput_doesNotThrowExceptionReturnsTrue() throws ParseException {
        Index expectedClientIndex = Index.fromOneBased(1);
        Index expectedClientNoteIndex = Index.fromOneBased(12);
        ClientNoteDeleteCommand expectedCommand =
                new ClientNoteDeleteCommand(expectedClientIndex, expectedClientNoteIndex);
        String validUserInput = "client note delete 1 12";
        assertDoesNotThrow(() -> parser.parse(validUserInput));
        ClientNoteDeleteCommand actualCommand = parser.parse(validUserInput);
        assertEquals(actualCommand, expectedCommand);
    }


}