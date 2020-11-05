package seedu.address.logic.parser;


import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.logic.commands.ClientSampleCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses the specific {@code remove} flag and creates a new {@code ClientSampleCommand}.
 */
public class ClientSampleCommandParser implements Parser<ClientSampleCommand> {

    @Override
    public ClientSampleCommand parse(String userInput) throws ParseException {
        String trimmed = userInput.trim();
        if (trimmed.equals("remove")) {
            return new ClientSampleCommand(true);
        } else if (trimmed.isEmpty()) {
            return new ClientSampleCommand(false);
        } else {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, ClientSampleCommand.MESSAGE_USAGE));
        }
    }
}
