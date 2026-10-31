package seedu.address.logic.parser;

import seedu.address.logic.commands.FindClientCommand;

/** Runs the shared find parser test cases against {@link FindClientCommandParser}. */
public class FindClientCommandParserTest extends AbstractFindCommandParserTest {
    /** Supplies the parser, its command's constructor and its usage message to the shared test cases. */
    public FindClientCommandParserTest() {
        super(new FindClientCommandParser(), FindClientCommand::new, FindClientCommand.MESSAGE_USAGE);
    }
}
