package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.List;
import java.util.function.Function;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AbstractFindCommand;
import seedu.address.model.person.PersonContainsKeywordsPredicate;

/**
 * Contains unit tests for the parsing that all find command parsers share.
 * JUnit runs these test cases once per subclass, and each subclass supplies its parser, the constructor
 * of the command that parser should create, and that command's usage message.
 * The constructor makes the success cases fail if a parser creates the wrong kind of find command.
 * The usage message makes the failure cases fail if a parser reports another find command's usage.
 */
public abstract class AbstractFindCommandParserTest {
    private final Parser<? extends AbstractFindCommand> parser;
    private final Function<PersonContainsKeywordsPredicate, AbstractFindCommand> commandFactory;
    private final AbstractFindCommand expectedAliceBobCommand;
    private final String messageUsage;

    /** Supplies the concrete parser, the constructor of the command it creates and its usage message. */
    protected AbstractFindCommandParserTest(Parser<? extends AbstractFindCommand> parser,
            Function<PersonContainsKeywordsPredicate, AbstractFindCommand> commandFactory,
            String messageUsage) {
        this.parser = parser;
        this.commandFactory = commandFactory;
        this.expectedAliceBobCommand = commandFactory.apply(
                new PersonContainsKeywordsPredicate(List.of("Alice", "Bob")));
        this.messageUsage = messageUsage;
    }

    @Test
    public void parse_validArgs_returnsExpectedCommand() {
        // no leading and trailing whitespaces
        assertParseSuccess(parser, "Alice Bob", expectedAliceBobCommand);

        // multiple whitespaces between keywords
        assertParseSuccess(parser, " \n Alice \n \t Bob  \t", expectedAliceBobCommand);
    }

    /**
     * Checks that find commands do not treat {@code n/} or {@code t/} as prefixes, unlike the add and
     * edit commands. The text stays part of the keyword, so this fails if the parser starts stripping
     * prefixes, which would turn {@code n/Alice} into a search for {@code Alice}.
     */
    @Test
    public void parse_prefixStyleArgs_keptAsLiteralKeywords() {
        AbstractFindCommand expectedCommand = commandFactory.apply(
                new PersonContainsKeywordsPredicate(List.of("n/Alice", "t/friends")));
        assertParseSuccess(parser, "n/Alice t/friends", expectedCommand);
    }

    /**
     * Checks that input with no keywords is rejected with this command's usage message, whether the
     * input is empty or contains only spaces.
     */
    @Test
    public void parse_emptyArg_throwsParseException() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, messageUsage);
        assertParseFailure(parser, "", expectedMessage);
        assertParseFailure(parser, "     ", expectedMessage);
    }

    /**
     * Checks that input with only Unicode whitespace counts as having no keywords. An em space (U+2003)
     * is whitespace that {@code trim()} keeps, so this fails if the parser goes back to {@code trim()},
     * as the input would then no longer count as empty and would not give the expected ParseException.
     */
    @Test
    public void parse_unicodeWhitespaceOnlyArg_throwsParseException() {
        assertParseFailure(parser, "  ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, messageUsage));
    }

    /**
     * Checks that an em space separates keywords like an ordinary space, so this fails if the
     * parser splits on {@code \s}, which does not match an em space and would keep Alice and Bob
     * together as one keyword.
     */
    @Test
    public void parse_unicodeWhitespaceBetweenKeywords_splitsKeywords() {
        assertParseSuccess(parser, " Alice Bob ", expectedAliceBobCommand);
    }
}
