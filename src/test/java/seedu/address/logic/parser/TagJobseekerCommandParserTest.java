package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.TagJobseekerCommand;
import seedu.address.model.tag.Tag;

public class TagJobseekerCommandParserTest {

    private final TagJobseekerCommandParser parser = new TagJobseekerCommandParser();

    @Test
    public void parse_validArgs_returnsTagJobseekerCommand() {
        TagJobseekerCommand expected = new TagJobseekerCommand(INDEX_FIRST_PERSON,
                Set.of(new Tag("java"), new Tag("teamwork")));
        assertParseSuccess(parser, "1 java teamwork", expected);
        assertParseSuccess(parser, "  1 \t java   teamwork java  ", expected);
    }

    @Test
    public void parse_missingArguments_throwsParseException() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, TagJobseekerCommand.MESSAGE_USAGE);
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
