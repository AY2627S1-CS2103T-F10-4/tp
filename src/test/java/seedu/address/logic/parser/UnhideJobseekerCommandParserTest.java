package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.UnhideJobseekerCommand;

/**
 * Contains unit tests for {@code UnhideJobseekerCommandParser}. Index parsing itself, e.g. of "0" or
 * "1 abc", is covered by {@code ParserUtilTest}.
 */
public class UnhideJobseekerCommandParserTest {

    private UnhideJobseekerCommandParser parser = new UnhideJobseekerCommandParser();

    @Test
    public void parse_validArgs_returnsUnhideJobseekerCommand() {
        assertParseSuccess(parser, "1", new UnhideJobseekerCommand(INDEX_FIRST_PERSON));
        assertParseSuccess(parser, " 1 ", new UnhideJobseekerCommand(INDEX_FIRST_PERSON));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        for (String input : List.of("", " ", "a", "0", "-1", "1.5", "1 abc", "1 2", "2147483648")) {
            assertParseFailure(parser, input,
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnhideJobseekerCommand.MESSAGE_USAGE));
        }
    }
}
