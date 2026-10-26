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
 * Hides a client identified using its displayed index, so that {@code listc} and {@code findc}
 * no longer show them. Unlike deleting, hiding keeps the client and all their details in the
 * address book.
 */
public class HideClientCommand extends Command {

    public static final String COMMAND_WORD = "hidec";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Hides the client identified by the index number used in the displayed client list.\n"
            + "Parameters: <index> (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_HIDE_CLIENT_SUCCESS = "Hidden client: %1$s";

    public static final String MESSAGE_NO_CHANGE = "This client is already hidden.";

    private final Index targetIndex;

    /**
     * Creates a command that hides the client at the specified index.
     *
     * @param targetIndex Index of the client to hide, in the displayed client list.
     */
    public HideClientCommand(Index targetIndex) {
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

        Client clientToHide = lastShownClientList.get(targetIndex.getZeroBased());
        if (clientToHide.isHidden()) {
            return new CommandResult(MESSAGE_NO_CHANGE);
        }

        Client hiddenClient = new Client(clientToHide.getName(), clientToHide.getPhone(),
                clientToHide.getEmail(), clientToHide.getAddress(), clientToHide.getTags(),
                clientToHide.getRemark(), true);
        // The displayed list only shows unhidden clients, so the hidden client drops out of it by
        // itself; there is no need to update the filter here.
        model.setClient(clientToHide, hiddenClient);
        return new CommandResult(String.format(MESSAGE_HIDE_CLIENT_SUCCESS, Messages.format(hiddenClient)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof HideClientCommand otherHideClientCommand)) {
            return false;
        }

        return targetIndex.equals(otherHideClientCommand.targetIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }
}
