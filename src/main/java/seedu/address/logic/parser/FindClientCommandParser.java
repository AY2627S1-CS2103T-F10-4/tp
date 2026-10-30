package seedu.address.logic.parser;

import seedu.address.logic.commands.FindClientCommand;

/** Parses arguments for {@link FindClientCommand}. */
public class FindClientCommandParser extends AbstractFindCommandParser<FindClientCommand> {
    /** Creates a parser for this command's arguments and usage message. */
    public FindClientCommandParser() {
        super(FindClientCommand::new, FindClientCommand.MESSAGE_USAGE);
    }
}
