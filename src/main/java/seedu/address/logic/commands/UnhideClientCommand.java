package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Client;

/**
 * Unhides a client identified using its displayed index, restoring them to normal lists and searches.
 * All existing details are preserved.
 */
public class UnhideClientCommand extends Command {

    public static final String COMMAND_WORD = "unhidec";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Unhides the client identified by the index number used in the displayed client list.\n"
            + "Parameters: <index> (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_UNHIDE_CLIENT_SUCCESS = "Unhidden client: %1$s";

    public static final String MESSAGE_NO_CHANGE = "This client is already unhidden.";

    private final Index targetIndex;

    /**
     * Creates a command that unhides the client at the specified index.
     *
     * @param targetIndex Index of the client to unhide, in the displayed client list.
     */
    public UnhideClientCommand(Index targetIndex) {
        requireNonNull(targetIndex);

        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Client> lastShownClientList = model.getFilteredClientList();

        if (targetIndex.getZeroBased() >= lastShownClientList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_CLIENT_DISPLAYED_INDEX);
        }

        Client clientToUnhide = lastShownClientList.get(targetIndex.getZeroBased());
        if (!clientToUnhide.isHidden()) {
            return new CommandResult(MESSAGE_NO_CHANGE);
        }

        Client unhiddenClient = new Client(clientToUnhide.getName(), clientToUnhide.getPhone(),
                clientToUnhide.getEmail(), clientToUnhide.getAddress(), clientToUnhide.getTags(),
                clientToUnhide.getRemark(), clientToUnhide.getAvailableJobs(), false);
        // Preserve the current filter; restored clients drop out of hidden lists and searches.
        model.setClient(clientToUnhide, unhiddenClient);
        return new CommandResult(String.format(MESSAGE_UNHIDE_CLIENT_SUCCESS, Messages.format(unhiddenClient)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof UnhideClientCommand otherUnhideClientCommand)) {
            return false;
        }

        return targetIndex.equals(otherUnhideClientCommand.targetIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }
}
