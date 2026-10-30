package seedu.address.logic.parser;

import seedu.address.logic.commands.FindHiddenJobseekerCommand;

/** Parses arguments for {@link FindHiddenJobseekerCommand}. */
public class FindHiddenJobseekerCommandParser extends AbstractFindCommandParser<FindHiddenJobseekerCommand> {
    /** Creates a parser for this command's arguments and usage message. */
    public FindHiddenJobseekerCommandParser() {
        super(FindHiddenJobseekerCommand::new, FindHiddenJobseekerCommand.MESSAGE_USAGE);
    }
}
