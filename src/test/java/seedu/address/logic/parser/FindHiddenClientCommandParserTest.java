package seedu.address.logic.parser;

import java.util.List;

import seedu.address.logic.commands.FindHiddenClientCommand;
import seedu.address.model.person.PersonContainsKeywordsPredicate;

/** Runs the shared parsing contract for {@link FindHiddenClientCommandParser}. */
public class FindHiddenClientCommandParserTest extends AbstractFindCommandParserTest {
    /** Configures the concrete command expected from this parser. */
    public FindHiddenClientCommandParserTest() {
        super(new FindHiddenClientCommandParser(),
                new FindHiddenClientCommand(new PersonContainsKeywordsPredicate(List.of("Alice", "Bob"))),
                FindHiddenClientCommand.MESSAGE_USAGE);
    }
}
