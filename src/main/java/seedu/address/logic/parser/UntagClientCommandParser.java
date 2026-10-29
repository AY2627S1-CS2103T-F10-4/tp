package seedu.address.logic.parser;

import seedu.address.logic.commands.UntagClientCommand;

/** Parses arguments for {@link UntagClientCommand}. */
public class UntagClientCommandParser extends TagCommandParser<UntagClientCommand> {
    /** Creates a parser for this command's arguments and usage message. */
    public UntagClientCommandParser() {
        super(UntagClientCommand::new, UntagClientCommand.MESSAGE_USAGE);
    }
}
