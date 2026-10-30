package seedu.address.logic.parser;

import seedu.address.logic.commands.FindCommand;

/** Parses arguments for {@link FindCommand}. */
public class FindCommandParser extends AbstractFindCommandParser<FindCommand> {
    /** Creates a parser for this command's arguments and usage message. */
    public FindCommandParser() {
        super(FindCommand::new, FindCommand.MESSAGE_USAGE);
    }
}
