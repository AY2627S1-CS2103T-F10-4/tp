package seedu.address.logic.commands;

import seedu.address.model.Model;

import static java.util.Objects.requireNonNull;

/**
 * Lists all jobseekers and clients in the address book to the user.
 */
public class ListClientCommand extends Command {

    public static final String COMMAND_WORD = "listc";

    public static final String MESSAGE_SUCCESS = "Listed all clients.";


    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredClientList(Model.PREDICATE_SHOW_ALL_CLIENTS);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
