package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.UntagClientCommand;
import seedu.address.model.tag.Tag;

public class UntagClientCommandParserTest {

    private final UntagClientCommandParser parser = new UntagClientCommandParser();

    @Test
    public void parse_validArgs_returnsUntagClientCommand() {
        UntagClientCommand expected = new UntagClientCommand(INDEX_FIRST_PERSON,
                Set.of(new Tag("priority"), new Tag("finance")));
        assertParseSuccess(parser, "1 priority finance", expected);
        assertParseSuccess(parser, "  1 \t priority   finance priority  ", expected);
    }

    @Test
    public void parse_missingArguments_throwsParseException() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, UntagClientCommand.MESSAGE_USAGE);
        assertParseFailure(parser, "", expectedMessage);
        assertParseFailure(parser, "   ", expectedMessage);
        assertParseFailure(parser, "1", expectedMessage);
        assertParseFailure(parser, "1   ", expectedMessage);
    }

    @Test
    public void parse_invalidIndex_throwsParseException() {
        assertParseFailure(parser, "0 priority", ParserUtil.MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, "-1 priority", ParserUtil.MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, "abc priority", ParserUtil.MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, "2147483648 priority", ParserUtil.MESSAGE_INVALID_INDEX);
    }

    @Test
    public void parse_invalidTag_throwsParseException() {
        assertParseFailure(parser, "1 priority!", Tag.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1 priority t/finance", Tag.MESSAGE_CONSTRAINTS);
    }
}
