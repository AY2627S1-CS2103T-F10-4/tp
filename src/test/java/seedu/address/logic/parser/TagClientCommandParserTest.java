package seedu.address.logic.parser;

import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.Set;

import seedu.address.logic.commands.TagClientCommand;
import seedu.address.model.tag.Tag;

/** Runs the shared parsing contract for {@link TagClientCommandParser}. */
public class TagClientCommandParserTest extends TagCommandParserTest {
    /** Configures the concrete command expected from this parser. */
    public TagClientCommandParserTest() {
        super(new TagClientCommandParser(),
                new TagClientCommand(INDEX_FIRST_PERSON, Set.of(new Tag("java"), new Tag("teamwork"))),
                TagClientCommand.MESSAGE_USAGE);
    }
}
