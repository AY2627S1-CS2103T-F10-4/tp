package seedu.address.logic.parser;

import seedu.address.logic.commands.FindHiddenClientCommand;

/** Runs the shared find parser test cases against {@link FindHiddenClientCommandParser}. */
public class FindHiddenClientCommandParserTest extends AbstractFindCommandParserTest {
    /** Supplies the parser, its command's constructor and its usage message to the shared test cases. */
    public FindHiddenClientCommandParserTest() {
        super(new FindHiddenClientCommandParser(), FindHiddenClientCommand::new,
                FindHiddenClientCommand.MESSAGE_USAGE);
    }
}
