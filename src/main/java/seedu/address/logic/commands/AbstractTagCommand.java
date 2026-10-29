package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

/**
 * Shares input handling and tag updates for tag and untag commands.
 * Concrete commands retain responsibility for copying and saving their person type.
 */
public abstract class AbstractTagCommand extends Command {
    /** The change to apply to a copy of the person's tags. */
    protected enum TagOperation {
        ADD, REMOVE
    }

    private final Index targetIndex;
    private final Set<Tag> tags;
    private final TagOperation operation;

    /** Creates a command with a defensive copy of the requested tags. */
    protected AbstractTagCommand(Index targetIndex, Set<Tag> tags, TagOperation operation) {
        this.targetIndex = requireNonNull(targetIndex);
        this.tags = new HashSet<>(requireNonNull(tags));
        this.operation = requireNonNull(operation);
    }

    /** Resolves the index against the currently displayed list. */
    protected final <T extends Person> T getTarget(List<T> displayedPersons, String invalidIndexMessage)
            throws CommandException {
        if (targetIndex.getZeroBased() >= displayedPersons.size()) {
            throw new CommandException(invalidIndexMessage);
        }
        return displayedPersons.get(targetIndex.getZeroBased());
    }

    /**
     * Returns changed tags, or an empty optional if the operation has no effect.
     * An optional containing an empty set means all tags were removed.
     */
    protected final Optional<Set<Tag>> getUpdatedTags(Person target) {
        Set<Tag> updatedTags = new HashSet<>(target.getTags());
        boolean hasChanges = switch (operation) {
            case ADD -> updatedTags.addAll(tags);
            case REMOVE -> updatedTags.removeAll(tags);
        };
        return hasChanges ? Optional.of(updatedTags) : Optional.empty();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (other == null || other.getClass() != getClass()) {
            return false;
        }
        AbstractTagCommand otherCommand = (AbstractTagCommand) other;
        return targetIndex.equals(otherCommand.targetIndex)
                && tags.equals(otherCommand.tags)
                && operation == otherCommand.operation;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass(), targetIndex.getZeroBased(), tags, operation);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .add(operation == TagOperation.ADD ? "tagsToAdd" : "tagsToRemove", tags)
                .toString();
    }
}
