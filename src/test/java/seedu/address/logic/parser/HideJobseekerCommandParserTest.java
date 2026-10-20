package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.HideJobseekerCommand;

/**
 * Contains unit tests for {@code HideJobseekerCommandParser}. Index parsing itself, e.g. of "0" or
 * "1 abc", is covered by {@code ParserUtilTest}.
 */
public class HideJobseekerCommandParserTest {

    private HideJobseekerCommandParser parser = new HideJobseekerCommandParser();

    @Test
    public void parse_validArgs_returnsHideJobseekerCommand() {
        assertParseSuccess(parser, "1", new HideJobseekerCommand(INDEX_FIRST_PERSON));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "a",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, HideJobseekerCommand.MESSAGE_USAGE));
    }
}
