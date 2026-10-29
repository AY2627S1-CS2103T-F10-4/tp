package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Jobseeker;

/**
 * Hides a jobseeker identified using its displayed index, so that {@code listj} and {@code findj}
 * no longer show them. Unlike deleting, hiding keeps the jobseeker and all their details in the
 * address book.
 */
public class HideJobseekerCommand extends AbstractHideCommand {

    public static final String COMMAND_WORD = "hidej";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Hides the jobseeker identified by the index number used in the displayed jobseeker list.\n"
            + "Parameters: <index> (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_HIDE_JOBSEEKER_SUCCESS = "Hidden jobseeker: %1$s";

    public static final String MESSAGE_NO_CHANGE = "This jobseeker is already hidden.";

    /**
     * Creates a command that hides the jobseeker at the specified index.
     *
     * @param targetIndex Index of the jobseeker to hide, in the displayed jobseeker list.
     */
    public HideJobseekerCommand(Index targetIndex) {
        super(targetIndex, HideOperation.HIDE);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        Jobseeker jobseekerToHide = getTarget(model.getFilteredJobseekerList(),
                Messages.MESSAGE_INVALID_JOBSEEKER_DISPLAYED_INDEX);
        if (isAlreadyApplied(jobseekerToHide)) {
            return new CommandResult(MESSAGE_NO_CHANGE);
        }

        Jobseeker hiddenJobseeker = new Jobseeker(jobseekerToHide.getName(), jobseekerToHide.getPhone(),
                jobseekerToHide.getEmail(), jobseekerToHide.getAddress(), jobseekerToHide.getTags(),
                jobseekerToHide.getRemark(), jobseekerToHide.getHighestQualification(), shouldBeHidden());
        // The displayed list only shows unhidden jobseekers, so the hidden jobseeker drops out of it
        // by itself; there is no need to update the filter here.
        model.setJobseeker(jobseekerToHide, hiddenJobseeker);
        return new CommandResult(String.format(MESSAGE_HIDE_JOBSEEKER_SUCCESS,
                Messages.format(hiddenJobseeker)));
    }
}
