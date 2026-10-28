package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_ADDRESS_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_EMAIL_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PHONE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.address.logic.commands.CommandTestUtil.REMARK_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_REMARK_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMARK;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalPersons.AMY;
import static seedu.address.testutil.TypicalPersons.BOB;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.AddJobseekerCommand;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Jobseeker;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.JobseekerBuilder;

public class AddJobseekerCommandParserTest {
    private static final String REQUIRED_FIELDS = NAME_DESC_AMY + PHONE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY;
    private final AddJobseekerCommandParser parser = new AddJobseekerCommandParser();

    @Test
    public void parse_spaceSeparatedTags_success() {
        Jobseeker expected = new JobseekerBuilder(AMY).withRemark(VALID_REMARK_AMY)
                .withTags("friend", "colleague").build();
        for (String tags : new String[]{" t/friend colleague", " t/ friend   colleague ",
            " t/friend\tcolleague", " t/friend colleague friend", " t/friend t/colleague",
            " t/friend colleague t/friend"}) {
            assertParseSuccess(parser, REQUIRED_FIELDS + tags + REMARK_DESC_AMY, new AddJobseekerCommand(expected));
            assertParseSuccess(parser, tags + REQUIRED_FIELDS + REMARK_DESC_AMY, new AddJobseekerCommand(expected));
        }
    }

    @Test
    public void parse_emptyOrInvalidTagInGroup_failure() {
        for (String tags : new String[]{" t/", " t/   ", " t/friend invalid!", " t/invalid! friend",
            " t/friend t/", " t/ t/friend"}) {
            assertParseFailure(parser, REQUIRED_FIELDS + tags, Tag.MESSAGE_CONSTRAINTS);
        }
    }

    @Test
    public void parse_withRemarkAndTags_success() {
        Jobseeker expectedJobseeker = new JobseekerBuilder(AMY).withRemark(VALID_REMARK_AMY)
                .withTags(VALID_TAG_FRIEND, VALID_TAG_HUSBAND).build();
        assertParseSuccess(parser, REQUIRED_FIELDS + REMARK_DESC_AMY + TAG_DESC_FRIEND + TAG_DESC_HUSBAND,
                new AddJobseekerCommand(expectedJobseeker));
    }

    @Test
    public void parse_emptyRemark_success() {
        Jobseeker expectedJobseeker = new JobseekerBuilder(AMY).withRemark("").withTags().build();
        assertParseSuccess(parser, REQUIRED_FIELDS + " " + PREFIX_REMARK,
                new AddJobseekerCommand(expectedJobseeker));
    }

    @Test
    public void parse_repeatedRemark_failure() {
        assertParseFailure(parser, REQUIRED_FIELDS + REMARK_DESC_AMY + REMARK_DESC_AMY,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_REMARK));
    }

    @Test
    public void parse_allFieldsPresent_success() {
        Jobseeker expectedJobseeker = new JobseekerBuilder(BOB).withTags(VALID_TAG_FRIEND).build();

        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + TAG_DESC_FRIEND, new AddJobseekerCommand(expectedJobseeker));


        // multiple tags - all accepted
        Jobseeker expectedJobseekerMultipleTags = new JobseekerBuilder(BOB)
                .withTags(VALID_TAG_FRIEND, VALID_TAG_HUSBAND)
                .build();
        assertParseSuccess(parser,
                NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB + TAG_DESC_HUSBAND + TAG_DESC_FRIEND,
                new AddJobseekerCommand(expectedJobseekerMultipleTags));
    }

    @Test
    public void parse_repeatedNonTagValue_failure() {
        String validExpectedPersonString = NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + TAG_DESC_FRIEND;

        // multiple names
        assertParseFailure(parser, NAME_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // multiple phones
        assertParseFailure(parser, PHONE_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // multiple emails
        assertParseFailure(parser, EMAIL_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EMAIL));

        // multiple addresses
        assertParseFailure(parser, ADDRESS_DESC_AMY + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ADDRESS));

        // multiple fields repeated
        assertParseFailure(parser,
                validExpectedPersonString + PHONE_DESC_AMY + EMAIL_DESC_AMY + NAME_DESC_AMY + ADDRESS_DESC_AMY
                        + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME, PREFIX_ADDRESS, PREFIX_EMAIL, PREFIX_PHONE));

        // invalid value followed by valid value

        // invalid name
        assertParseFailure(parser, INVALID_NAME_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // invalid email
        assertParseFailure(parser, INVALID_EMAIL_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EMAIL));

        // invalid phone
        assertParseFailure(parser, INVALID_PHONE_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // invalid address
        assertParseFailure(parser, INVALID_ADDRESS_DESC + validExpectedPersonString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ADDRESS));

        // valid value followed by invalid value

        // invalid name
        assertParseFailure(parser, validExpectedPersonString + INVALID_NAME_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // invalid email
        assertParseFailure(parser, validExpectedPersonString + INVALID_EMAIL_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EMAIL));

        // invalid phone
        assertParseFailure(parser, validExpectedPersonString + INVALID_PHONE_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // invalid address
        assertParseFailure(parser, validExpectedPersonString + INVALID_ADDRESS_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ADDRESS));
    }

    @Test
    public void parse_optionalFieldsMissing_success() {
        // omitted remark defaults to empty, with zero tags
        Jobseeker expectedJobseeker = new JobseekerBuilder(AMY).withRemark("").withTags().build();
        assertParseSuccess(parser, REQUIRED_FIELDS,
                new AddJobseekerCommand(expectedJobseeker));
    }

    @Test
    public void parse_singleRequiredFieldMissing_listsOnlyMissingField() {
        assertParseFailure(parser, PHONE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY,
                "Missing required fields: n/ (name).");
        assertParseFailure(parser, NAME_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY,
                "Missing required fields: p/ (phone).");
        assertParseFailure(parser, NAME_DESC_AMY + PHONE_DESC_AMY + ADDRESS_DESC_AMY,
                "Missing required fields: e/ (email).");
        assertParseFailure(parser, NAME_DESC_AMY + PHONE_DESC_AMY + EMAIL_DESC_AMY,
                "Missing required fields: a/ (address).");
    }

    @Test
    public void parse_multipleRequiredFieldsMissing_listsMissingFieldsInOrder() {
        assertParseFailure(parser, NAME_DESC_AMY + PHONE_DESC_AMY,
                "Missing required fields: e/ (email), a/ (address).");
        assertParseFailure(parser, ADDRESS_DESC_AMY + EMAIL_DESC_AMY + REMARK_DESC_AMY,
                "Missing required fields: n/ (name), p/ (phone).");
        for (String args : new String[]{"", "   ", " r/Available t/intern"}) {
            assertParseFailure(parser, args,
                    "Missing required fields: n/ (name), p/ (phone), e/ (email), a/ (address).");
        }
    }

    @Test
    public void parse_emptyRequiredField_reportsValueError() {
        assertParseFailure(parser, " n/" + PHONE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY,
                Name.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, NAME_DESC_AMY + " p/" + EMAIL_DESC_AMY + ADDRESS_DESC_AMY,
                Phone.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, NAME_DESC_AMY + PHONE_DESC_AMY + " e/" + ADDRESS_DESC_AMY,
                Email.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, NAME_DESC_AMY + PHONE_DESC_AMY + EMAIL_DESC_AMY + " a/",
                Address.MESSAGE_CONSTRAINTS);
        // A supplied but empty email is not a missing prefix; only address is missing here.
        assertParseFailure(parser, NAME_DESC_AMY + PHONE_DESC_AMY + " e/",
                "Missing required fields: a/ (address).");
    }

    @Test
    public void parse_nonEmptyPreambleWithMissingFields_reportsInvalidFormat() {
        assertParseFailure(parser, PREAMBLE_NON_EMPTY + NAME_DESC_AMY,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddJobseekerCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid name
        assertParseFailure(parser, INVALID_NAME_DESC + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Name.MESSAGE_CONSTRAINTS);

        // invalid phone
        assertParseFailure(parser, NAME_DESC_BOB + INVALID_PHONE_DESC + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Phone.MESSAGE_CONSTRAINTS);

        // invalid email
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + INVALID_EMAIL_DESC + ADDRESS_DESC_BOB
                + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Email.MESSAGE_CONSTRAINTS);

        // invalid address
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + INVALID_ADDRESS_DESC
                + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Address.MESSAGE_CONSTRAINTS);

        // invalid tag
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + INVALID_TAG_DESC + VALID_TAG_FRIEND, Tag.MESSAGE_CONSTRAINTS);

        // two invalid values, only first invalid value reported
        assertParseFailure(parser, INVALID_NAME_DESC + PHONE_DESC_BOB + EMAIL_DESC_BOB + INVALID_ADDRESS_DESC,
                Name.MESSAGE_CONSTRAINTS);

        // non-empty preamble
        assertParseFailure(parser, PREAMBLE_NON_EMPTY + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + TAG_DESC_HUSBAND + TAG_DESC_FRIEND,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddJobseekerCommand.MESSAGE_USAGE));
    }

    /**
     * A newly added jobseeker must always start unhidden; only a later hide command may hide them.
     */
    @Test
    public void parse_allFieldsPresent_addsUnhiddenJobseeker() throws Exception {
        Model model = new ModelManager();
        parser.parse(REQUIRED_FIELDS + REMARK_DESC_AMY + TAG_DESC_FRIEND).execute(model);
        assertFalse(model.getAddressBook().getJobseekerList().get(0).isHidden());
    }
}
