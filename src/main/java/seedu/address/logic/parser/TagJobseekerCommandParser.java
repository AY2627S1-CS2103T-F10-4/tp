package seedu.address.logic.parser;

import seedu.address.logic.commands.TagJobseekerCommand;

/** Parses arguments for {@link TagJobseekerCommand}. */
public class TagJobseekerCommandParser extends TagCommandParser<TagJobseekerCommand> {
    /** Creates a parser for this command's arguments and usage message. */
    public TagJobseekerCommandParser() {
        super(TagJobseekerCommand::new, TagJobseekerCommand.MESSAGE_USAGE);
    }
}
