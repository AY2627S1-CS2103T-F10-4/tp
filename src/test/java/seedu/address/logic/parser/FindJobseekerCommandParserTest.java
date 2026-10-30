package seedu.address.logic.parser;

import java.util.List;

import seedu.address.logic.commands.FindJobseekerCommand;
import seedu.address.model.person.PersonContainsKeywordsPredicate;

/** Runs the shared parsing contract for {@link FindJobseekerCommandParser}. */
public class FindJobseekerCommandParserTest extends AbstractFindCommandParserTest {
    /** Configures the concrete command expected from this parser. */
    public FindJobseekerCommandParserTest() {
        super(new FindJobseekerCommandParser(),
                new FindJobseekerCommand(new PersonContainsKeywordsPredicate(List.of("Alice", "Bob"))),
                FindJobseekerCommand.MESSAGE_USAGE);
    }
}
