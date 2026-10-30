package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AbstractFindCommand;

/**
 * Shared parsing contract, run against each concrete find parser.
 * Each subclass supplies the command its parser should create for the keywords "Alice Bob", so the
 * success cases fail if a parser creates the wrong kind of find command, and its usage message, so
 * the failure cases fail if a parser reports another find command's usage.
 */
public abstract class AbstractFindCommandParserTest {
    private final Parser<? extends AbstractFindCommand> parser;
    private final AbstractFindCommand expectedAliceBobCommand;
    private final String messageUsage;

    /** Supplies the concrete parser, the command expected for "Alice Bob" and its usage message. */
    protected AbstractFindCommandParserTest(Parser<? extends AbstractFindCommand> parser,
            AbstractFindCommand expectedAliceBobCommand, String messageUsage) {
        this.parser = parser;
        this.expectedAliceBobCommand = expectedAliceBobCommand;
        this.messageUsage = messageUsage;
    }

    @Test
    public void parse_validArgs_returnsExpectedCommand() {
        // no leading and trailing whitespaces
        assertParseSuccess(parser, "Alice Bob", expectedAliceBobCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " \n Alice \n \t Bob  \t", expectedAliceBobCommand);
    }

    @Test
    public void parse_emptyArg_throwsParseException() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, messageUsage);
        assertParseFailure(parser, "", expectedMessage);
        assertParseFailure(parser, "     ", expectedMessage);
    }

    /**
     * Guards against Unicode whitespace that {@code trim()} keeps: an em space (U+2003) must give
     * a ParseException, so this fails if the parser goes back to {@code trim()}, where the
     * predicate would throw IllegalArgumentException instead.
     */
    @Test
    public void parse_unicodeWhitespaceOnlyArg_throwsParseException() {
        assertParseFailure(parser, "\u2003\u2003",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, messageUsage));
    }

    /**
     * Checks that an em space separates keywords like an ordinary space, so this fails if the
     * parser splits on {@code \s}, which would leave "Alice Bob" as one keyword.
     */
    @Test
    public void parse_unicodeWhitespaceBetweenKeywords_splitsKeywords() {
        assertParseSuccess(parser, "\u2003Alice\u2003Bob\u2003", expectedAliceBobCommand);
    }
}
