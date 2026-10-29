package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.CopyClientCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/** Parses arguments for {@link CopyClientCommand}. */
public class CopyClientCommandParser implements Parser<CopyClientCommand> {

    @Override
    public CopyClientCommand parse(String args) throws ParseException {
        if (args.trim().isEmpty()) {
            return new CopyClientCommand();
        }

        try {
            Index index = ParserUtil.parseIndex(args);
            return new CopyClientCommand(index);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, CopyClientCommand.MESSAGE_USAGE), pe);
        }
    }
}
