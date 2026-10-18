package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.HideClientCommand;

/**
 * Contains unit tests for {@code HideClientCommandParser}. Index parsing itself, e.g. of "0" or
 * "1 abc", is covered by {@code ParserUtilTest}.
 */
public class HideClientCommandParserTest {

    private HideClientCommandParser parser = new HideClientCommandParser();

    @Test
    public void parse_validArgs_returnsHideClientCommand() {
        assertParseSuccess(parser, "1", new HideClientCommand(INDEX_FIRST_PERSON));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        assertParseFailure(parser, "a",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, HideClientCommand.MESSAGE_USAGE));
    }
}
