package seedu.address.logic.parser;

import seedu.address.logic.commands.FindCommand;

/** Runs the shared find parser test cases against {@link FindCommandParser}. */
public class FindCommandParserTest extends AbstractFindCommandParserTest {
    /** Supplies the parser, its command's constructor and its usage message to the shared test cases. */
    public FindCommandParserTest() {
        super(new FindCommandParser(), FindCommand::new, FindCommand.MESSAGE_USAGE);
    }
}
