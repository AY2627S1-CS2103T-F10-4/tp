package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FindClientCommand;
import seedu.address.logic.commands.FindHiddenClientCommand;
import seedu.address.logic.commands.FindHiddenJobseekerCommand;
import seedu.address.logic.commands.FindJobseekerCommand;
import seedu.address.model.person.PersonContainsKeywordsPredicate;

public class FindCommandParserTest {

    private FindCommandParser<FindJobseekerCommand> jobseekerParser = FindCommandParser.forJobseekers();
    private FindCommandParser<FindClientCommand> clientParser = FindCommandParser.forClients();
    private FindCommandParser<FindHiddenJobseekerCommand> hiddenJobseekerParser =
            FindCommandParser.forHiddenJobseekers();
    private FindCommandParser<FindHiddenClientCommand> hiddenClientParser =
            FindCommandParser.forHiddenClients();

    /**
     * Checks each parser reports its own command's usage, so this fails if a factory method is
     * given the other command's usage message.
     */
    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(jobseekerParser, "     ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindJobseekerCommand.MESSAGE_USAGE));
        assertParseFailure(clientParser, "     ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindClientCommand.MESSAGE_USAGE));
    }

    /**
     * Guards against Unicode whitespace that {@code trim()} keeps: an em space (U+2003) must give
     * a ParseException, so this fails if the parser goes back to {@code trim()}, where the
     * predicate would throw IllegalArgumentException instead.
     */
    @Test
    public void parse_unicodeWhitespaceOnlyArg_throwsParseException() {
        assertParseFailure(jobseekerParser, "    ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindJobseekerCommand.MESSAGE_USAGE));
        assertParseFailure(clientParser, " ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindClientCommand.MESSAGE_USAGE));
    }

    /**
     * Checks that an em space separates keywords like an ordinary space, so this fails if the
     * parser splits on {@code \s}, which would leave "Alice Bob" as one keyword.
     */
    @Test
    public void parse_unicodeWhitespaceBetweenKeywords_splitsKeywords() {
        FindJobseekerCommand expectedFindJobseekerCommand =
                new FindJobseekerCommand(new PersonContainsKeywordsPredicate(List.of("Alice", "Bob")));
        assertParseSuccess(jobseekerParser, " Alice Bob ", expectedFindJobseekerCommand);
    }

    @Test
    public void parse_validArgs_returnsFindJobseekerCommand() {
        // no leading and trailing whitespaces
        FindJobseekerCommand expectedFindJobseekerCommand =
                new FindJobseekerCommand(new PersonContainsKeywordsPredicate(List.of("Alice", "Bob")));
        assertParseSuccess(jobseekerParser, "Alice Bob", expectedFindJobseekerCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(jobseekerParser, " \n Alice \n \t Bob  \t", expectedFindJobseekerCommand);
    }

    @Test
    public void parse_validArgs_returnsFindClientCommand() {
        // no leading and trailing whitespaces
        FindClientCommand expectedFindClientCommand =
                new FindClientCommand(new PersonContainsKeywordsPredicate(List.of("Adrian", "Brenda")));
        assertParseSuccess(clientParser, "Adrian Brenda", expectedFindClientCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(clientParser, " \n Adrian \n \t Brenda  \t", expectedFindClientCommand);
    }

    /**
     * Checks the parsers for hidden people report their own command's usage, so this fails if a factory
     * method is given the usage message of another find command.
     */
    @Test
    public void parse_emptyArgForHiddenPeople_throwsParseException() {
        assertParseFailure(hiddenJobseekerParser, "     ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindHiddenJobseekerCommand.MESSAGE_USAGE));
        assertParseFailure(hiddenClientParser, "     ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindHiddenClientCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsFindHiddenJobseekerCommand() {
        FindHiddenJobseekerCommand expectedCommand =
                new FindHiddenJobseekerCommand(new PersonContainsKeywordsPredicate(List.of("Alice", "Bob")));
        assertParseSuccess(hiddenJobseekerParser, " \n Alice \n \t Bob  \t", expectedCommand);
    }

    @Test
    public void parse_validArgs_returnsFindHiddenClientCommand() {
        FindHiddenClientCommand expectedCommand =
                new FindHiddenClientCommand(new PersonContainsKeywordsPredicate(List.of("Adrian", "Brenda")));
        assertParseSuccess(hiddenClientParser, " \n Adrian \n \t Brenda  \t", expectedCommand);
    }
}
