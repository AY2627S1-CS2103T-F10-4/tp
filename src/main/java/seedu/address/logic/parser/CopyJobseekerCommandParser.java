package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.CopyJobseekerCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/** Parses arguments for {@link CopyJobseekerCommand}. */
public class CopyJobseekerCommandParser implements Parser<CopyJobseekerCommand> {

    @Override
    public CopyJobseekerCommand parse(String args) throws ParseException {
        if (args.trim().isEmpty()) {
            return new CopyJobseekerCommand();
        }

        try {
            Index index = ParserUtil.parseIndex(args);
            return new CopyJobseekerCommand(index);
        } catch (ParseException pe) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, CopyJobseekerCommand.MESSAGE_USAGE), pe);
        }
    }
}
