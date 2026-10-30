package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.List;
import java.util.function.Function;

import seedu.address.logic.commands.AbstractFindCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.PersonContainsKeywordsPredicate;

/**
 * Parses the keywords shared by all find commands (find, findj, findc, findhj and findhc).
 * These commands take the same arguments and differ only in the command they create, so each
 * concrete parser just passes its command's constructor and usage message to this class.
 *
 * @param <T> Type of find command to create.
 */
public abstract class AbstractFindCommandParser<T extends AbstractFindCommand> implements Parser<T> {

    /** Creates the command from the parsed keywords, e.g. {@code FindJobseekerCommand::new}. */
    private final Function<PersonContainsKeywordsPredicate, T> commandFactory;

    /** Usage message of the command, shown when the user gives no keywords. */
    private final String messageUsage;

    /** Creates a parser with the concrete command's factory and usage message. */
    protected AbstractFindCommandParser(Function<PersonContainsKeywordsPredicate, T> commandFactory,
            String messageUsage) {
        this.commandFactory = requireNonNull(commandFactory);
        this.messageUsage = requireNonNull(messageUsage);
    }

    /**
     * Parses the given {@code String} of arguments into keywords and returns the find command
     * for those keywords.
     *
     * @throws ParseException If the user input contains no keywords.
     */
    @Override
    public T parse(String args) throws ParseException {
        String trimmedArgs = args.strip();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, messageUsage));
        }

        String[] keywords = trimmedArgs.split("\\p{javaWhitespace}+");
        // trimmedArgs is non-empty and has no leading whitespace, so the split yields at least one
        // keyword and none of them is blank.
        assert keywords.length > 0;

        return commandFactory.apply(new PersonContainsKeywordsPredicate(List.of(keywords)));
    }

}
