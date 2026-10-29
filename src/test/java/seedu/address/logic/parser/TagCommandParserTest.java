package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AbstractTagCommand;
import seedu.address.model.tag.Tag;

/** Shared parsing contract, run against each concrete tag/untag parser. */
public abstract class TagCommandParserTest {
    private final Parser<? extends AbstractTagCommand> parser;
    private final AbstractTagCommand expected;
    private final String messageUsage;

    /** Supplies the concrete parser, expected command and its usage message. */
    protected TagCommandParserTest(Parser<? extends AbstractTagCommand> parser,
            AbstractTagCommand expected, String messageUsage) {
        this.parser = parser;
        this.expected = expected;
        this.messageUsage = messageUsage;
    }


    @Test
    public void parse_validArgs_returnsExpectedCommand() {
        assertParseSuccess(parser, "1 java teamwork", expected);
        assertParseSuccess(parser, "  1 \t java   teamwork java  ", expected);
    }

    @Test
    public void parse_missingArguments_throwsParseException() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, messageUsage);
        assertParseFailure(parser, "", expectedMessage);
        assertParseFailure(parser, "   ", expectedMessage);
        assertParseFailure(parser, "1", expectedMessage);
        assertParseFailure(parser, "1   ", expectedMessage);
    }

    @Test
    public void parse_invalidIndex_throwsParseException() {
        assertParseFailure(parser, "0 java", ParserUtil.MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, "-1 java", ParserUtil.MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, "abc java", ParserUtil.MESSAGE_INVALID_INDEX);
        assertParseFailure(parser, "2147483648 java", ParserUtil.MESSAGE_INVALID_INDEX);
    }

    @Test
    public void parse_invalidTag_throwsParseException() {
        assertParseFailure(parser, "1 java!", Tag.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, "1 java t/teamwork", Tag.MESSAGE_CONSTRAINTS);
    }
}
