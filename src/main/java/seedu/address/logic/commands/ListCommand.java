package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.model.Model;

/**
 * Lists all unhidden jobseekers and clients in the address book.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";

    public static final String MESSAGE_SUCCESS = "Listed all unhidden jobseekers and clients.";

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredJobseekerList(Model.PREDICATE_SHOW_UNHIDDEN_JOBSEEKERS);
        model.jobseekerFilterDescriptionProperty().set("Showing all unhidden jobseekers");
        model.updateFilteredClientList(Model.PREDICATE_SHOW_UNHIDDEN_CLIENTS);
        model.clientFilterDescriptionProperty().set("Showing all unhidden clients");
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
