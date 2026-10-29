package seedu.address.logic.parser;

import seedu.address.logic.commands.UntagJobseekerCommand;

/** Parses arguments for {@link UntagJobseekerCommand}. */
public class UntagJobseekerCommandParser extends TagCommandParser<UntagJobseekerCommand> {
    /** Creates a parser for this command's arguments and usage message. */
    public UntagJobseekerCommandParser() {
        super(UntagJobseekerCommand::new, UntagJobseekerCommand.MESSAGE_USAGE);
    }
}
