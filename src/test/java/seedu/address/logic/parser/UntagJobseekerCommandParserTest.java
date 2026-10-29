package seedu.address.logic.parser;

import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.Set;

import seedu.address.logic.commands.UntagJobseekerCommand;
import seedu.address.model.tag.Tag;

/** Runs the shared parsing contract for {@link UntagJobseekerCommandParser}. */
public class UntagJobseekerCommandParserTest extends TagCommandParserTest {
    /** Configures the concrete command expected from this parser. */
    public UntagJobseekerCommandParserTest() {
        super(new UntagJobseekerCommandParser(),
                new UntagJobseekerCommand(INDEX_FIRST_PERSON, Set.of(new Tag("java"), new Tag("teamwork"))),
                UntagJobseekerCommand.MESSAGE_USAGE);
    }
}
