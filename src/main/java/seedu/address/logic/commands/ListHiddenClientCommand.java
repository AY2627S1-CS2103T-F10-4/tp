package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.model.Model;

/**
 * Lists all hidden clients in the address book to the user, so that they can be viewed again,
 * e.g. to be unhidden. The jobseeker list is left unchanged.
 */
public class ListHiddenClientCommand extends Command {

    public static final String COMMAND_WORD = "listhc";

    public static final String MESSAGE_SUCCESS = "Listed all hidden clients.";

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredClientList(Model.PREDICATE_SHOW_HIDDEN_CLIENTS);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
