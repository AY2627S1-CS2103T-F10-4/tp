package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.Arrays;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.UntagJobseekerCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.tag.Tag;

/**
 * Parses an index followed by one or more whitespace-separated tags.
 */
public class UntagJobseekerCommandParser implements Parser<UntagJobseekerCommand> {

    /**
     * Parses the arguments and creates a command to remove tags from a jobseeker.
     *
     * @throws ParseException If the index or tags are missing or invalid.
     */
    @Override
    public UntagJobseekerCommand parse(String args) throws ParseException {
        requireNonNull(args);
        String[] parts = args.trim().split("\\s+", 2);

        if (parts.length < 2) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    UntagJobseekerCommand.MESSAGE_USAGE));
        }

        Index index = ParserUtil.parseIndex(parts[0]);
        Set<Tag> tags = ParserUtil.parseTags(Arrays.asList(parts[1].split("\\s+")));
        return new UntagJobseekerCommand(index, tags);
    }
}
