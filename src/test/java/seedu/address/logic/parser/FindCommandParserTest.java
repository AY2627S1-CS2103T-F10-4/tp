package seedu.address.logic.parser;

import java.util.List;

import seedu.address.logic.commands.FindCommand;
import seedu.address.model.person.PersonContainsKeywordsPredicate;

/** Runs the shared parsing contract for {@link FindCommandParser}. */
public class FindCommandParserTest extends AbstractFindCommandParserTest {
    /** Configures the concrete command expected from this parser. */
    public FindCommandParserTest() {
        super(new FindCommandParser(),
                new FindCommand(new PersonContainsKeywordsPredicate(List.of("Alice", "Bob"))),
                FindCommand.MESSAGE_USAGE);
    }
}
