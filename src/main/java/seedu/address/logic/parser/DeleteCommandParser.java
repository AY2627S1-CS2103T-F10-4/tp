package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteJobseekerCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new {@link DeleteJobseekerCommand} object.
 */
public class DeleteCommandParser implements Parser<DeleteJobseekerCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the
     * {@link DeleteJobseekerCommand} and returns a command for execution.
     * @throws ParseException if the user input does not conform to the expected format
     */
    public DeleteJobseekerCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new DeleteJobseekerCommand(index);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteJobseekerCommand.MESSAGE_USAGE), pe);
        }
    }

}
