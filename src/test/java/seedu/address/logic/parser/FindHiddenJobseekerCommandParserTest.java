package seedu.address.logic.parser;

import java.util.List;

import seedu.address.logic.commands.FindHiddenJobseekerCommand;
import seedu.address.model.person.PersonContainsKeywordsPredicate;

/** Runs the shared parsing contract for {@link FindHiddenJobseekerCommandParser}. */
public class FindHiddenJobseekerCommandParserTest extends AbstractFindCommandParserTest {
    /** Configures the concrete command expected from this parser. */
    public FindHiddenJobseekerCommandParserTest() {
        super(new FindHiddenJobseekerCommandParser(),
                new FindHiddenJobseekerCommand(new PersonContainsKeywordsPredicate(List.of("Alice", "Bob"))),
                FindHiddenJobseekerCommand.MESSAGE_USAGE);
    }
}
