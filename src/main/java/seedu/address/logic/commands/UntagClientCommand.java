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
import seedu.address.model.person.Client;
import seedu.address.model.tag.Tag;

/**
 * Removes tags from a client identified by their displayed index.
 */
public class UntagClientCommand extends Command {

    public static final String COMMAND_WORD = "untagc";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Removes tags from the client identified by their displayed index.\n"
            + "Parameters: INDEX TAG [MORE_TAGS]...\n"
            + "Example: " + COMMAND_WORD + " 1 priority finance";
    public static final String MESSAGE_SUCCESS = "Updated client tags: %1$s";
    public static final String MESSAGE_NO_CHANGE = "This client does not have any of the specified tags.";

    private final Index targetIndex;
    private final Set<Tag> tagsToRemove;

    /**
     * Creates a command to remove {@code tagsToRemove} from the client at {@code targetIndex}.
     * A defensive copy of the tags is stored.
     */
    public UntagClientCommand(Index targetIndex, Set<Tag> tagsToRemove) {
        requireNonNull(targetIndex);
        requireNonNull(tagsToRemove);
        this.targetIndex = targetIndex;
        this.tagsToRemove = new HashSet<>(tagsToRemove);
    }

    /**
     * Removes specified tags from the client at the displayed index, preserving other tags and ignoring missing tags.
     *
     * @return Feedback indicating whether tags were removed.
     * @throws CommandException If the index is outside the currently displayed client list.
     */
    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Client> lastShownList = model.getFilteredClientList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_CLIENT_DISPLAYED_INDEX);
        }

        Client target = lastShownList.get(targetIndex.getZeroBased());
        Set<Tag> updatedTags = new HashSet<>(target.getTags());
        boolean hasChanges = updatedTags.removeAll(tagsToRemove);

        if (!hasChanges) {
            return new CommandResult(MESSAGE_NO_CHANGE);
        }

        Client updatedClient = new Client(target.getName(), target.getPhone(), target.getEmail(),
                target.getAddress(), updatedTags, target.getRemark());
        model.setClient(target, updatedClient);
        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(updatedClient)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof UntagClientCommand otherCommand)) {
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
