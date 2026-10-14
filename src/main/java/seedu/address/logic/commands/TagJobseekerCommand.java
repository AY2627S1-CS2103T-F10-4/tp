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
 * Adds tags to a jobseeker identified by their displayed index.
 */
public class TagJobseekerCommand extends Command {

    public static final String COMMAND_WORD = "tagj";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Adds tags to the jobseeker identified by their displayed index.\n"
            + "Parameters: INDEX TAG [MORE_TAGS]...\n"
            + "Example: " + COMMAND_WORD + " 1 java teamwork";
    public static final String MESSAGE_SUCCESS = "Updated jobseeker tags: %1$s";
    public static final String MESSAGE_NO_CHANGE = "This jobseeker already has all the specified tags.";

    private final Index targetIndex;
    private final Set<Tag> tagsToAdd;

    /**
     * Creates a command to add {@code tagsToAdd} to the jobseeker at {@code targetIndex}.
     * A defensive copy of the tags is stored.
     */
    public TagJobseekerCommand(Index targetIndex, Set<Tag> tagsToAdd) {
        requireNonNull(targetIndex);
        requireNonNull(tagsToAdd);
        this.targetIndex = targetIndex;
        this.tagsToAdd = new HashSet<>(tagsToAdd);
    }

    /**
     * Adds tags to the jobseeker at the displayed index, preserving existing tags and ignoring duplicates.
     *
     * @return Feedback indicating whether tags were added.
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
        boolean hasChanges = updatedTags.addAll(tagsToAdd);

        if (!hasChanges) {
            return new CommandResult(MESSAGE_NO_CHANGE);
        }

        Jobseeker updatedJobseeker = new Jobseeker(target.getName(), target.getPhone(), target.getEmail(),
                target.getAddress(), updatedTags, target.getRemark());
        model.setJobseeker(target, updatedJobseeker);
        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(updatedJobseeker)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof TagJobseekerCommand otherCommand)) {
            return false;
        }

        return targetIndex.equals(otherCommand.targetIndex)
                && tagsToAdd.equals(otherCommand.tagsToAdd);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .add("tagsToAdd", tagsToAdd)
                .toString();
    }
}
