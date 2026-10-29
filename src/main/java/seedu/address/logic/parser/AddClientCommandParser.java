package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_QUALIFICATION_FIELD_NOT_ALLOWED;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_JOB;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_QUALIFICATION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMARK;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Set;

import seedu.address.logic.commands.AddClientCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.job.Job;
import seedu.address.model.person.Address;
import seedu.address.model.person.Client;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Remark;
import seedu.address.model.tag.Tag;

/**
 * Parses input arguments and creates a new {@link AddClientCommand} object.
 */
public class AddClientCommandParser implements Parser<AddClientCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the
     * {@link AddClientCommand} and returns a command for execution.
     * @throws ParseException if the user input does not conform to the expected format
     */
    public AddClientCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS,
                        PREFIX_TAG, PREFIX_REMARK, PREFIX_JOB, PREFIX_QUALIFICATION);

        if (argMultimap.getValue(PREFIX_QUALIFICATION).isPresent()) {
            throw new ParseException(MESSAGE_QUALIFICATION_FIELD_NOT_ALLOWED);
        }

        if (!argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    AddClientCommand.MESSAGE_USAGE));
        }

        ParserUtil.verifyRequiredAddFields(argMultimap);

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS,
                PREFIX_REMARK);
        Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get());
        Phone phone = ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get());
        Email email = ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL).get());
        Address address = ParserUtil.parseAddress(argMultimap.getValue(PREFIX_ADDRESS).get());
        Set<Tag> tagList = ParserUtil.parseSpaceSeparatedTags(argMultimap.getAllValues(PREFIX_TAG));

        Remark remark = new Remark(argMultimap.getValue(PREFIX_REMARK).orElse(""));
        Set<Job> jobs = ParserUtil.parseJobs(argMultimap.getAllValues(PREFIX_JOB));
        // A newly added client always starts unhidden; only a later hide command can hide them.
        boolean isHidden = false;
        Client client = new Client(name, phone, email, address, tagList, remark, jobs, isHidden);

        return new AddClientCommand(client);
    }

}
