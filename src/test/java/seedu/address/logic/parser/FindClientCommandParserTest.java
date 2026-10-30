package seedu.address.logic.parser;

import java.util.List;

import seedu.address.logic.commands.FindClientCommand;
import seedu.address.model.person.PersonContainsKeywordsPredicate;

/** Runs the shared parsing contract for {@link FindClientCommandParser}. */
public class FindClientCommandParserTest extends AbstractFindCommandParserTest {
    /** Configures the concrete command expected from this parser. */
    public FindClientCommandParserTest() {
        super(new FindClientCommandParser(),
                new FindClientCommand(new PersonContainsKeywordsPredicate(List.of("Alice", "Bob"))),
                FindClientCommand.MESSAGE_USAGE);
    }
}
