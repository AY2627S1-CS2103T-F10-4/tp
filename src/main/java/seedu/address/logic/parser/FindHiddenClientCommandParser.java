package seedu.address.logic.parser;

import seedu.address.logic.commands.FindHiddenClientCommand;

/** Parses arguments for {@link FindHiddenClientCommand}. */
public class FindHiddenClientCommandParser extends AbstractFindCommandParser<FindHiddenClientCommand> {
    /** Creates a parser for this command's arguments and usage message. */
    public FindHiddenClientCommandParser() {
        super(FindHiddenClientCommand::new, FindHiddenClientCommand.MESSAGE_USAGE);
    }
}
