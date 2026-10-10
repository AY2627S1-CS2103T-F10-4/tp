package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.model.Model;

/**
 * Lists all jobseekers and clients in the address book to the user.
 */
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";

    public static final String MESSAGE_SUCCESS = "Listed all jobseekers and clients.";


    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredJobseekerList(jobseeker -> true);
        model.updateFilteredClientList(client -> true);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
