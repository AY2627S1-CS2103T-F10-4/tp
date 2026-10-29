package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.person.PersonContainsKeywordsPredicate;

/**
 * Represents a command that lists the persons matching a set of keywords, such as {@code find},
 * {@code findj}, {@code findc}, {@code findhj} and {@code findhc}.
 * It holds the keyword predicate and the {@code equals} and {@code toString} logic that every
 * find command shares. Each subclass decides in {@link #execute} which list(s) to filter and which
 * persons (hidden or unhidden) to search.
 */
public abstract class AbstractFindCommand extends Command {

    /** Predicate that decides which persons match the user's keywords. */
    protected final PersonContainsKeywordsPredicate predicate;

    /**
     * Creates a find command that lists the persons that match {@code predicate}.
     *
     * @param predicate Predicate that decides which persons are listed; must not be null.
     */
    protected AbstractFindCommand(PersonContainsKeywordsPredicate predicate) {
        requireNonNull(predicate);
        this.predicate = predicate;
    }

    /**
     * Returns true if {@code other} is the same kind of find command with an equal predicate.
     * The classes are compared with {@code getClass()} rather than {@code instanceof}, because
     * {@code findj alice} and {@code findc alice} have equal predicates but search different lists,
     * so they must not be equal.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // getClass() also handles nulls, as other == null fails this check before other is used
        if (other == null || other.getClass() != getClass()) {
            return false;
        }

        AbstractFindCommand otherFindCommand = (AbstractFindCommand) other;
        return predicate.equals(otherFindCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}
