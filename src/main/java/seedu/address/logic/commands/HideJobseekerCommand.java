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
 * Hides a jobseeker identified using its displayed index, so that {@code listj} and {@code findj}
 * no longer show them. Unlike deleting, hiding keeps the jobseeker and all their details in the
 * address book.
 */
public class HideJobseekerCommand extends Command {

    public static final String COMMAND_WORD = "hidej";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Hides the jobseeker identified by the index number used in the displayed jobseeker list.\n"
            + "Parameters: <index> (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_HIDE_JOBSEEKER_SUCCESS = "Hidden jobseeker: %1$s";

    public static final String MESSAGE_NO_CHANGE = "This jobseeker is already hidden.";

    private final Index targetIndex;

    /**
     * Creates a command that hides the jobseeker at the specified index.
     *
     * @param targetIndex Index of the jobseeker to hide, in the displayed jobseeker list.
     */
    public HideJobseekerCommand(Index targetIndex) {
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

        Jobseeker jobseekerToHide = lastShownJobseekerList.get(targetIndex.getZeroBased());
        if (jobseekerToHide.isHidden()) {
            return new CommandResult(MESSAGE_NO_CHANGE);
        }

        Jobseeker hiddenJobseeker = new Jobseeker(jobseekerToHide.getName(), jobseekerToHide.getPhone(),
                jobseekerToHide.getEmail(), jobseekerToHide.getAddress(), jobseekerToHide.getTags(),
                jobseekerToHide.getRemark(), true);
        // The displayed list only shows unhidden jobseekers, so the hidden jobseeker drops out of it
        // by itself; there is no need to update the filter here.
        model.setJobseeker(jobseekerToHide, hiddenJobseeker);
        return new CommandResult(String.format(MESSAGE_HIDE_JOBSEEKER_SUCCESS,
                Messages.format(hiddenJobseeker)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof HideJobseekerCommand otherHideJobseekerCommand)) {
            return false;
        }

        return targetIndex.equals(otherHideJobseekerCommand.targetIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }
}
