package seedu.address.logic.parser;

import seedu.address.logic.commands.FindJobseekerCommand;

/** Runs the shared find parser test cases against {@link FindJobseekerCommandParser}. */
public class FindJobseekerCommandParserTest extends AbstractFindCommandParserTest {
    /** Supplies the parser, its command's constructor and its usage message to the shared test cases. */
    public FindJobseekerCommandParserTest() {
        super(new FindJobseekerCommandParser(), FindJobseekerCommand::new,
                FindJobseekerCommand.MESSAGE_USAGE);
    }
}
