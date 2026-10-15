package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.getEmptyUnmodifiableList;

import seedu.address.model.Model;

/**
 * Clears the client list in the address book.
 */
public class ClearClientCommand extends Command {

    public static final String COMMAND_WORD = "clearc";
    public static final String MESSAGE_SUCCESS = "The client list has been cleared!";


    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.setClients(getEmptyUnmodifiableList());
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
