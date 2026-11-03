package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_JOB_FIELD_NOT_ALLOWED;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_JOB;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_QUALIFICATION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMARK;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditJobseekerCommand;
import seedu.address.logic.commands.EditJobseekerCommand.EditJobseekerDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Remark;

/**
 * Parses input arguments and creates a new {@link EditJobseekerCommand} object.
 */
public class EditCommandParser implements Parser<EditJobseekerCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the
     * {@link EditJobseekerCommand} and returns a command for execution.
     *
     * @throws ParseException if the user input does not conform to the expected format.
     */
    public EditJobseekerCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS,
                        PREFIX_TAG, PREFIX_REMARK, PREFIX_QUALIFICATION, PREFIX_JOB);

        if (argMultimap.getValue(PREFIX_JOB).isPresent()) {
            throw new ParseException(MESSAGE_JOB_FIELD_NOT_ALLOWED);
        }

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    EditJobseekerCommand.MESSAGE_USAGE), pe);
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS,
                PREFIX_REMARK, PREFIX_QUALIFICATION);

        EditJobseekerDescriptor editJobseekerDescriptor = new EditJobseekerDescriptor();

        if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
            editJobseekerDescriptor.setName(ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get()));
        }
        if (argMultimap.getValue(PREFIX_PHONE).isPresent()) {
            editJobseekerDescriptor.setPhone(ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get()));
        }
        if (argMultimap.getValue(PREFIX_EMAIL).isPresent()) {
            editJobseekerDescriptor.setEmail(ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL).get()));
        }
        if (argMultimap.getValue(PREFIX_ADDRESS).isPresent()) {
            editJobseekerDescriptor.setAddress(ParserUtil.parseAddress(argMultimap.getValue(PREFIX_ADDRESS).get()));
        }
        argMultimap.getValue(PREFIX_REMARK).map(Remark::new).ifPresent(editJobseekerDescriptor::setRemark);
        ParserUtil.parseTagsForEdit(argMultimap.getAllValues(PREFIX_TAG)).ifPresent(editJobseekerDescriptor::setTags);
        argMultimap.getValue(PREFIX_QUALIFICATION).map(ParserUtil::parseQualification)
                .ifPresent(editJobseekerDescriptor::setHighestQualification);

        if (!editJobseekerDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditJobseekerCommand.MESSAGE_NOT_EDITED);
        }

        return new EditJobseekerCommand(index, editJobseekerDescriptor);
    }

}
