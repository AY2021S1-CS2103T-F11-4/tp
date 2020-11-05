package seedu.address.logic.commands;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.client.Client;
import seedu.address.model.client.exceptions.ClientNotFoundException;
import seedu.address.model.client.exceptions.DuplicateClientException;
import seedu.address.model.util.SampleDataUtil;

public class ClientSampleCommand extends Command {

    /** The command word for this command. */
    public static final String COMMAND_WORD = "client sample";

    /** Proper usage of client sample command. */
    public static final String MESSAGE_USAGE = COMMAND_WORD + " [remove] : Adds sample clients to TBM, if remove flag"
            + " is indicated, removes all sample clients from TBM. Any modifications to the sample clients will result"
            + " in it not being a sample client anymore.";
    private static final String MESSAGE_SAMPLE_SUCCESS = "Successfully added sample clients to TBM";
    private static final String MESSAGE_REMOVE_SUCCESS = "Successfully cleaned TBM of sample clients";
    private boolean remove;

    /**
     * Constructs the {@code ClientSampleCommand}.
     *
     * @param remove Indicated whether the sample clients are to be removed or not
     */
    public ClientSampleCommand(boolean remove) {
        this.remove = remove;
    }

    /**
     * Executes the command and returns the result message.
     *
     * @param model {@code Model} which the command should operate on.
     * @return feedback message of the operation result for display
     * @throws CommandException If an error occurs during command execution.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        if (!remove) {
            for (Client client : SampleDataUtil.getSampleClients()) {
                try {
                    model.addClient(client);
                } catch (DuplicateClientException e) {
                    continue;
                }
            }
            return new CommandResult(MESSAGE_SAMPLE_SUCCESS);
        } else {
            for (Client client : SampleDataUtil.getSampleClients()) {
                try {
                    model.deleteClient(client);
                } catch (ClientNotFoundException e) {
                    continue;
                }
            }
            return new CommandResult(MESSAGE_REMOVE_SUCCESS);
        }
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ClientSampleCommand)) {
            return false;
        }

        // state check
        ClientSampleCommand c = (ClientSampleCommand) other;

        return this.remove == c.remove;
    }
}
