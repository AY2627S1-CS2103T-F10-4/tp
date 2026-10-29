package seedu.address.logic.parser;

import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.Set;

import seedu.address.logic.commands.TagJobseekerCommand;
import seedu.address.model.tag.Tag;

/** Runs the shared parsing contract for {@link TagJobseekerCommandParser}. */
public class TagJobseekerCommandParserTest extends TagCommandParserTest {
    /** Configures the concrete command expected from this parser. */
    public TagJobseekerCommandParserTest() {
        super(new TagJobseekerCommandParser(),
                new TagJobseekerCommand(INDEX_FIRST_PERSON, Set.of(new Tag("java"), new Tag("teamwork"))),
                TagJobseekerCommand.MESSAGE_USAGE);
    }
}
