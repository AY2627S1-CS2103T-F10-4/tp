package seedu.address.logic.parser;

import seedu.address.logic.commands.TagClientCommand;

/** Parses arguments for {@link TagClientCommand}. */
public class TagClientCommandParser extends TagCommandParser<TagClientCommand> {
    /** Creates a parser for this command's arguments and usage message. */
    public TagClientCommandParser() {
        super(TagClientCommand::new, TagClientCommand.MESSAGE_USAGE);
    }
}
