package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.HideJobseekerCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new {@link HideJobseekerCommand} object.
 */
public class HideJobseekerCommandParser implements Parser<HideJobseekerCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the
     * {@link HideJobseekerCommand} and returns a command for execution.
     *
     * @throws ParseException If the user input does not conform to the expected format.
     */
    public HideJobseekerCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new HideJobseekerCommand(index);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, HideJobseekerCommand.MESSAGE_USAGE), pe);
        }
    }

}
