package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.CopyClientCommand;

/** Contains unit tests for {@link CopyClientCommandParser}. */
public class CopyClientCommandParserTest {

    private final CopyClientCommandParser parser = new CopyClientCommandParser();

    @Test
    public void parse_noIndex_returnsBulkCopyCommand() throws Exception {
        // EP: empty index argument (bulk-copy mode)
        assertTrue(parser.parse("") instanceof CopyClientCommand);

        // EP: whitespace-only index argument (boundary variant of empty input)
        assertTrue(parser.parse("   ") instanceof CopyClientCommand);
    }

    @Test
    public void parse_positiveIndex_returnsIndexedCopyCommand() throws Exception {
        // EP: positive integer index; 1 is the lower boundary of valid input.
        assertTrue(parser.parse("1") instanceof CopyClientCommand);
    }

    @Test
    public void parse_invalidIndex_reportsUsage() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, CopyClientCommand.MESSAGE_USAGE);

        // EP: negative integer
        assertParseFailure(parser, "-1", expectedMessage);

        // EP: zero; boundary immediately below the valid positive-integer partition
        assertParseFailure(parser, "0", expectedMessage);

        // EP: non-numeric input
        assertParseFailure(parser, "abc", expectedMessage);

        // EP: more than one argument
        assertParseFailure(parser, "1 2", expectedMessage);
    }
}
