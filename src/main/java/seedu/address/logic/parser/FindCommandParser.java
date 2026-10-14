package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.util.List;

import seedu.address.logic.commands.FindJobseekerCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.NameContainsKeywordsPredicate;

/**
 * Parses input arguments and creates a new FindJobseekerCommand object
 */
public class FindCommandParser implements Parser<FindJobseekerCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindJobseekerCommand
     * and returns a FindJobseekerCommand object for execution.
     *
     * @throws ParseException if the user input does not conform to the expected format.
     */
    public FindJobseekerCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindJobseekerCommand.MESSAGE_USAGE));
        }

        String[] nameKeywords = trimmedArgs.split("\\s+");
        // trimmedArgs is non-empty and has no leading whitespace, so the split yields at least one
        // keyword and none of them is blank.
        assert nameKeywords.length > 0;

        return new FindJobseekerCommand(new NameContainsKeywordsPredicate(List.of(nameKeywords)));
    }

}
