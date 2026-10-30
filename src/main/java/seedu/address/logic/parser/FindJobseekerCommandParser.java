package seedu.address.logic.parser;

import seedu.address.logic.commands.FindJobseekerCommand;

/** Parses arguments for {@link FindJobseekerCommand}. */
public class FindJobseekerCommandParser extends AbstractFindCommandParser<FindJobseekerCommand> {
    /** Creates a parser for this command's arguments and usage message. */
    public FindJobseekerCommandParser() {
        super(FindJobseekerCommand::new, FindJobseekerCommand.MESSAGE_USAGE);
    }
}
