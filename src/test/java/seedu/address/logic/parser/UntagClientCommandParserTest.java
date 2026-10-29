package seedu.address.logic.parser;

import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.Set;

import seedu.address.logic.commands.UntagClientCommand;
import seedu.address.model.tag.Tag;

/** Runs the shared parsing contract for {@link UntagClientCommandParser}. */
public class UntagClientCommandParserTest extends TagCommandParserTest {
    /** Configures the concrete command expected from this parser. */
    public UntagClientCommandParserTest() {
        super(new UntagClientCommandParser(),
                new UntagClientCommand(INDEX_FIRST_PERSON, Set.of(new Tag("java"), new Tag("teamwork"))),
                UntagClientCommand.MESSAGE_USAGE);
    }
}
