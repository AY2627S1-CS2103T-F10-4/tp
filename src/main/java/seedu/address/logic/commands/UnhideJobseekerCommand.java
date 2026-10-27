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
 * Unhides a jobseeker identified using its displayed index, restoring them to normal lists and searches.
 * All existing details are preserved.
 */
public class UnhideJobseekerCommand extends Command {

    public static final String COMMAND_WORD = "unhidej";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Unhides the jobseeker identified by the index number used in the displayed jobseeker list.\n"
            + "Parameters: <index> (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_UNHIDE_JOBSEEKER_SUCCESS = "Unhidden jobseeker: %1$s";

    public static final String MESSAGE_NO_CHANGE = "This jobseeker is already unhidden.";

    private final Index targetIndex;

    /**
     * Creates a command that unhides the jobseeker at the specified index.
     *
     * @param targetIndex Index of the jobseeker to unhide, in the displayed jobseeker list.
     */
    public UnhideJobseekerCommand(Index targetIndex) {
        requireNonNull(targetIndex);

        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Jobseeker> lastShownJobseekerList = model.getFilteredJobseekerList();

        if (targetIndex.getZeroBased() >= lastShownJobseekerList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_JOBSEEKER_DISPLAYED_INDEX);
        }

        Jobseeker jobseekerToUnhide = lastShownJobseekerList.get(targetIndex.getZeroBased());
        if (!jobseekerToUnhide.isHidden()) {
            return new CommandResult(MESSAGE_NO_CHANGE);
        }

        Jobseeker unhiddenJobseeker = new Jobseeker(jobseekerToUnhide.getName(), jobseekerToUnhide.getPhone(),
                jobseekerToUnhide.getEmail(), jobseekerToUnhide.getAddress(), jobseekerToUnhide.getTags(),
                jobseekerToUnhide.getRemark(), jobseekerToUnhide.getHighestQualification(), false);
        // Preserve the current filter; restored jobseekers drop out of hidden lists and searches.
        model.setJobseeker(jobseekerToUnhide, unhiddenJobseeker);
        return new CommandResult(String.format(MESSAGE_UNHIDE_JOBSEEKER_SUCCESS,
                Messages.format(unhiddenJobseeker)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof UnhideJobseekerCommand otherUnhideJobseekerCommand)) {
            return false;
        }

        return targetIndex.equals(otherUnhideJobseekerCommand.targetIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }
}
