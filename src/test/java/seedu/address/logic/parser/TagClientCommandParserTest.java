package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.TagClientCommand;
import seedu.address.model.tag.Tag;

public class TagClientCommandParserTest {

    private final TagClientCommandParser parser = new TagClientCommandParser();

    @Test
    public void parse_validArgs_returnsTagClientCommand() {
        TagClientCommand expected = new TagClientCommand(INDEX_FIRST_PERSON,
                Set.of(new Tag("priority"), new Tag("finance")));
        assertParseSuccess(parser, "1 priority finance", expected);
        assertParseSuccess(parser, "  1 \t priority   finance priority  ", expected);
    }

    @Test
    public void parse_missingArguments_throwsParseException() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagClientCommand.MESSAGE_USAGE);
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
