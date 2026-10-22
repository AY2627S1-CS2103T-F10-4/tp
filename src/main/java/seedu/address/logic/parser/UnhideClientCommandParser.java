package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.UnhideClientCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new {@link UnhideClientCommand} object.
 */
public class UnhideClientCommandParser implements Parser<UnhideClientCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the
     * {@link UnhideClientCommand} and returns a command for execution.
     *
     * @throws ParseException If the user input does not conform to the expected format.
     */
    public UnhideClientCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new UnhideClientCommand(index);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, UnhideClientCommand.MESSAGE_USAGE), pe);
        }
    }

}
