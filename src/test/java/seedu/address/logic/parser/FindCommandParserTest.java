package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FindClientCommand;
import seedu.address.logic.commands.FindJobseekerCommand;
import seedu.address.model.person.PersonContainsKeywordsPredicate;

public class FindCommandParserTest {

    private FindCommandParser<FindJobseekerCommand> jobseekerParser = FindCommandParser.forJobseekers();
    private FindCommandParser<FindClientCommand> clientParser = FindCommandParser.forClients();

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

}
