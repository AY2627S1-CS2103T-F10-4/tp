package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Jobseeker;

/**
 * Deletes a jobseeker identified using its displayed index from the address book.
 */
public class DeleteJobseekerCommand extends Command {

    public static final String COMMAND_WORD = "deletej";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the jobseeker identified by the index number used in the displayed jobseeker list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_JOBSEEKER_SUCCESS = "Deleted jobseeker: %1$s";

    private final Index targetIndex;

    /**
     * Creates a command that deletes the jobseeker at the specified index.
     *
     * @param targetIndex index of the jobseeker to delete
     */
    public DeleteJobseekerCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Jobseeker> lastShownJobseekerList = model.getFilteredJobseekerList();

        if (targetIndex.getZeroBased() >= lastShownJobseekerList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_JOBSEEKER_DISPLAYED_INDEX);
        }

        Jobseeker jobseekerToDelete = lastShownJobseekerList.get(targetIndex.getZeroBased());
        model.deleteJobseeker(jobseekerToDelete);
        return new CommandResult(String.format(MESSAGE_DELETE_JOBSEEKER_SUCCESS,
                Messages.format(jobseekerToDelete)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteJobseekerCommand otherDeleteJobseekerCommand)) {
            return false;
        }

        return targetIndex.equals(otherDeleteJobseekerCommand.targetIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }
}
