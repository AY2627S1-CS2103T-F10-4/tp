package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.List;
import java.util.function.Function;

import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.FindClientCommand;
import seedu.address.logic.commands.FindHiddenClientCommand;
import seedu.address.logic.commands.FindHiddenJobseekerCommand;
import seedu.address.logic.commands.FindJobseekerCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.PersonContainsKeywordsPredicate;

/**
 * Parses the keywords of a findj/findc/findhj/findhc command and creates that command.
 * These commands take the same arguments and differ only in the command they create, so this one
 * parser serves all of them. Use {@link #forJobseekers()}, {@link #forClients()},
 * {@link #forHiddenJobseekers()} or {@link #forHiddenClients()} to get one.
 *
 * @param <T> Type of find command this parser creates.
 */
public class FindCommandParser<T extends Command> implements Parser<T> {

    /** Creates the command from the parsed keywords, e.g. {@code FindJobseekerCommand::new}. */
    private final Function<PersonContainsKeywordsPredicate, T> commandFactory;

    /** Usage message of the command, shown when the user gives no keywords. */
    private final String messageUsage;

    private FindCommandParser(Function<PersonContainsKeywordsPredicate, T> commandFactory,
            String messageUsage) {
        this.commandFactory = commandFactory;
        this.messageUsage = messageUsage;
    }

    /**
     * Returns a parser that creates a {@code FindJobseekerCommand}.
     */
    public static FindCommandParser<FindJobseekerCommand> forJobseekers() {
        return new FindCommandParser<>(FindJobseekerCommand::new, FindJobseekerCommand.MESSAGE_USAGE);
    }

    /**
     * Returns a parser that creates a {@code FindClientCommand}.
     */
    public static FindCommandParser<FindClientCommand> forClients() {
        return new FindCommandParser<>(FindClientCommand::new, FindClientCommand.MESSAGE_USAGE);
    }

    /**
     * Returns a parser that creates a {@code FindHiddenJobseekerCommand}.
     */
    public static FindCommandParser<FindHiddenJobseekerCommand> forHiddenJobseekers() {
        return new FindCommandParser<>(FindHiddenJobseekerCommand::new,
                FindHiddenJobseekerCommand.MESSAGE_USAGE);
    }

    /**
     * Returns a parser that creates a {@code FindHiddenClientCommand}.
     */
    public static FindCommandParser<FindHiddenClientCommand> forHiddenClients() {
        return new FindCommandParser<>(FindHiddenClientCommand::new,
                FindHiddenClientCommand.MESSAGE_USAGE);
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
