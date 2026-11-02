package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import static seedu.address.logic.commands.CommandTestUtil.REMARK_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_EMPLOYER;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_PARTNER;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_REMARK_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_EMPLOYER;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_PARTNER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_REMARK;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.AddClientCommand;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.person.Address;
import seedu.address.model.person.Client;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.ClientBuilder;

public class AddClientCommandParserTest {
    private static final String REQUIRED_FIELDS = NAME_DESC_AMY + PHONE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY;
    private static final Client AMY = new ClientBuilder().withName(VALID_NAME_AMY).withPhone(VALID_PHONE_AMY)
            .withEmail(VALID_EMAIL_AMY).withAddress(VALID_ADDRESS_AMY).build();
    private final AddClientCommandParser parser = new AddClientCommandParser();

    @Test
    public void parse_spaceSeparatedTags_success() {
        Client expected = new ClientBuilder(AMY).withRemark(VALID_REMARK_AMY)
                .withTags("friend", "colleague").build();
        for (String tags : new String[]{" t/friend colleague", " t/ friend   colleague ",
            " t/friend\tcolleague", " t/friend colleague friend", " t/friend t/colleague",
            " t/friend colleague t/friend"}) {
            assertParseSuccess(parser, REQUIRED_FIELDS + tags + REMARK_DESC_AMY, new AddClientCommand(expected));
            assertParseSuccess(parser, tags + REQUIRED_FIELDS + REMARK_DESC_AMY, new AddClientCommand(expected));
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
        Client expected = new ClientBuilder(AMY).withRemark(VALID_REMARK_AMY)
                .withTags(VALID_TAG_EMPLOYER, VALID_TAG_PARTNER).build();
        assertParseSuccess(parser, REQUIRED_FIELDS + REMARK_DESC_AMY + TAG_DESC_EMPLOYER + TAG_DESC_PARTNER,
                new AddClientCommand(expected));
    }

    @Test
    public void parse_optionalFieldsMissing_success() {
        assertParseSuccess(parser, REQUIRED_FIELDS, new AddClientCommand(new ClientBuilder(AMY).build()));
    }

    @Test
    public void parse_emptyRemark_success() {
        assertParseSuccess(parser, REQUIRED_FIELDS + " r/", new AddClientCommand(new ClientBuilder(AMY).build()));
    }

    @Test
    public void parse_duplicateSingleValueFields_failure() {
        for (String field : new String[]{NAME_DESC_BOB, PHONE_DESC_BOB, EMAIL_DESC_BOB, ADDRESS_DESC_BOB}) {
            assertParseFailure(parser, REQUIRED_FIELDS + field,
                    Messages.getErrorMessageForDuplicatePrefixes(new Prefix(field.substring(1, 3))));
        }
        assertParseFailure(parser, REQUIRED_FIELDS + REMARK_DESC_AMY + REMARK_DESC_AMY,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_REMARK));
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

    /**
     * Clients share {@code Name} with jobseekers, but go through their own parser.
     */
    @Test
    public void parse_nameWithSlash_success() {
        // EP: the letter before '/' is not a prefix
        assertNameParsedWhole("Raj s/o Kumar");

        // EP: the letter before '/' is a prefix in the wrong case, as prefixes are case-sensitive
        assertNameParsedWhole("ABC P/L");
        assertNameParsedWhole("John Smith T/A Smith Plumbing");

        // EP: '/' next to a space, so there is no letter to form a prefix with
        assertNameParsedWhole("ABC / XYZ Holdings");
    }

    private void assertNameParsedWhole(String name) {
        Client expected = new ClientBuilder(AMY).withName(name).withRemark("").withTags().build();
        assertParseSuccess(parser, " n/" + name + PHONE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY,
                new AddClientCommand(expected));
    }

    /**
     * Documents known limitations, covered in the user guide: a lowercase word such as "p/l" or "t/a"
     * cannot be told apart from the prefix it starts with.
     */
    @Test
    public void parse_lowercasePrefixInName_readAsPrefix() {
        // EP: prefix of a required field, so the command is rejected as a repeated prefix
        assertParseFailure(parser, " n/ABC p/l" + PHONE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // EP: prefix of an optional field, so the rest of the name silently becomes tags
        Client expected = new ClientBuilder(AMY).withName("John Smith").withRemark("")
                .withTags("a", "Smith", "Plumbing").build();
        assertParseSuccess(parser, " n/John Smith t/a Smith Plumbing" + PHONE_DESC_AMY + EMAIL_DESC_AMY
                + ADDRESS_DESC_AMY, new AddClientCommand(expected));
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
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddClientCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidPhone_failure() {
        assertParseFailure(parser, REQUIRED_FIELDS.replace(PHONE_DESC_AMY, INVALID_PHONE_DESC),
                Phone.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_invalidName_failure() {
        assertParseFailure(parser, REQUIRED_FIELDS.replace(NAME_DESC_AMY, INVALID_NAME_DESC), Name.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_invalidEmail_failure() {
        assertParseFailure(parser, REQUIRED_FIELDS.replace(EMAIL_DESC_AMY, INVALID_EMAIL_DESC),
                Email.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_invalidAddress_failure() {
        assertParseFailure(parser, REQUIRED_FIELDS.replace(ADDRESS_DESC_AMY, INVALID_ADDRESS_DESC),
                Address.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_invalidTag_failure() {
        assertParseFailure(parser, REQUIRED_FIELDS + INVALID_TAG_DESC, Tag.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, REQUIRED_FIELDS + " t/", Tag.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_nonEmptyPreamble_failure() {
        assertParseFailure(parser, PREAMBLE_NON_EMPTY + REQUIRED_FIELDS,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddClientCommand.MESSAGE_USAGE));
    }

    @Test
    public void parseCommand_addClientWithRemark_addsOnlyClient() throws Exception {
        Client expected = new ClientBuilder(AMY).withRemark(VALID_REMARK_AMY).build();
        Model model = new ModelManager();
        new AddressBookParser().parseCommand(AddClientCommand.COMMAND_WORD + REQUIRED_FIELDS + REMARK_DESC_AMY)
                .execute(model);
        assertEquals(java.util.List.of(expected), model.getAddressBook().getClientList());
        assertEquals(0, model.getAddressBook().getJobseekerList().size());
    }

    /**
     * A newly added client must always start unhidden; only a later hide command may hide them.
     */
    @Test
    public void parse_allFieldsPresent_addsUnhiddenClient() throws Exception {
        Model model = new ModelManager();
        parser.parse(REQUIRED_FIELDS + REMARK_DESC_AMY + TAG_DESC_EMPLOYER).execute(model);
        assertFalse(model.getAddressBook().getClientList().get(0).isHidden());
    }
}
