package seedu.address.logic.parser;

import seedu.address.logic.commands.FindHiddenJobseekerCommand;

/** Runs the shared find parser test cases against {@link FindHiddenJobseekerCommandParser}. */
public class FindHiddenJobseekerCommandParserTest extends AbstractFindCommandParserTest {
    /** Supplies the parser, its command's constructor and its usage message to the shared test cases. */
    public FindHiddenJobseekerCommandParserTest() {
        super(new FindHiddenJobseekerCommandParser(), FindHiddenJobseekerCommand::new,
                FindHiddenJobseekerCommand.MESSAGE_USAGE);
    }
}
