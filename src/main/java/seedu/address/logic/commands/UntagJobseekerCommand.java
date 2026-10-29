package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Jobseeker;
import seedu.address.model.tag.Tag;

/**
 * Removes tags from a jobseeker identified by their displayed index.
 */
public class UntagJobseekerCommand extends Command {

    public static final String COMMAND_WORD = "untagj";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Removes tags from the jobseeker identified by their displayed index.\n"
            + "Parameters: <index> <tag>...\n"
            + "Example: " + COMMAND_WORD + " 1 java teamwork";
    public static final String MESSAGE_SUCCESS = "Updated jobseeker tags: %1$s";
    public static final String MESSAGE_NO_CHANGE = "This jobseeker does not have any of the specified tags.";

    private final Index targetIndex;
    private final Set<Tag> tagsToRemove;

    /**
     * Creates a command to remove {@code tagsToRemove} from the jobseeker at {@code targetIndex}.
     * A defensive copy of the tags is stored.
     */
    public UntagJobseekerCommand(Index targetIndex, Set<Tag> tagsToRemove) {
        requireNonNull(targetIndex);
        requireNonNull(tagsToRemove);
        this.targetIndex = targetIndex;
        this.tagsToRemove = new HashSet<>(tagsToRemove);
    }

    /**
     * Removes specified tags from the jobseeker at the displayed index, preserve other tags and ignore missing tags.
     *
     * @return Feedback indicating whether tags were removed.
     * @throws CommandException If the index is outside the currently displayed jobseeker list.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Jobseeker> lastShownList = model.getFilteredJobseekerList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_JOBSEEKER_DISPLAYED_INDEX);
        }

        Jobseeker target = lastShownList.get(targetIndex.getZeroBased());
        Set<Tag> updatedTags = new HashSet<>(target.getTags());
        boolean hasChanges = updatedTags.removeAll(tagsToRemove);

        if (!hasChanges) {
            return new CommandResult(MESSAGE_NO_CHANGE);
        }

        Jobseeker updatedJobseeker = new Jobseeker(target.getName(), target.getPhone(), target.getEmail(),
                target.getAddress(), updatedTags, target.getRemark(), target.getHighestQualification(),
                target.isHidden());
        model.setJobseeker(target, updatedJobseeker);
        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(updatedJobseeker)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof UntagJobseekerCommand otherCommand)) {
            return false;
        }

        return targetIndex.equals(otherCommand.targetIndex)
                && tagsToRemove.equals(otherCommand.tagsToRemove);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .add("tagsToRemove", tagsToRemove)
                .toString();
    }
}
