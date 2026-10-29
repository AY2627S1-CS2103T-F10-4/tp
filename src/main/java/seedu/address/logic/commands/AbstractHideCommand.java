package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Objects;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.person.Person;

/**
 * Shares input handling and the hidden-status check for the hide and unhide commands.
 * Concrete commands retain responsibility for copying and saving their person type.
 */
public abstract class AbstractHideCommand extends Command {
    /** The change to apply to the person's hidden status. */
    protected enum HideOperation {
        HIDE, UNHIDE
    }

    private final Index targetIndex;
    private final HideOperation operation;

    /** Creates a command that applies {@code operation} to the person at {@code targetIndex}. */
    protected AbstractHideCommand(Index targetIndex, HideOperation operation) {
        this.targetIndex = requireNonNull(targetIndex);
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

    /** Returns the hidden status the target should have once this command has run. */
    protected final boolean shouldBeHidden() {
        return operation == HideOperation.HIDE;
    }

    /** Returns true if {@code target} already has the hidden status this command would give it. */
    protected final boolean isAlreadyApplied(Person target) {
        return target.isHidden() == shouldBeHidden();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        // Comparing classes, not instanceof, keeps HideClientCommand and HideJobseekerCommand unequal
        if (other == null || other.getClass() != getClass()) {
            return false;
        }
        AbstractHideCommand otherCommand = (AbstractHideCommand) other;
        return targetIndex.equals(otherCommand.targetIndex)
                && operation == otherCommand.operation;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getClass(), targetIndex.getZeroBased(), operation);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .toString();
    }
}
