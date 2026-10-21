package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.model.Model;

/**
 * Lists all hidden jobseekers in the address book to the user, so that they can be viewed again,
 * e.g. to be unhidden. The client list is left unchanged.
 */
public class ListHiddenJobseekerCommand extends Command {

    public static final String COMMAND_WORD = "listhj";

    public static final String MESSAGE_SUCCESS = "Listed all hidden jobseekers.";

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredJobseekerList(Model.PREDICATE_SHOW_HIDDEN_JOBSEEKERS);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
