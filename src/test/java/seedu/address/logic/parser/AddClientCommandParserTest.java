package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
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
    private static final String REQUIRED_FIELDS = " n/Amy Bee p/85355255 e/amy@gmail.com"
            + " a/123, Jurong West Ave 6, #08-111";
    private final AddClientCommandParser parser = new AddClientCommandParser();

    @Test
    public void parse_withRemarkAndTags_success() {
        Client expected = new ClientBuilder().withRemark("Hiring software engineers")
                .withTags("employer", "technology").build();
        assertParseSuccess(parser, REQUIRED_FIELDS + " r/Hiring software engineers t/employer t/technology",
                new AddClientCommand(expected));
    }

    @Test
    public void parse_optionalFieldsMissing_success() {
        assertParseSuccess(parser, REQUIRED_FIELDS, new AddClientCommand(new ClientBuilder().build()));
    }

    @Test
    public void parse_emptyRemark_success() {
        assertParseSuccess(parser, REQUIRED_FIELDS + " r/", new AddClientCommand(new ClientBuilder().build()));
    }

    @Test
    public void parse_duplicateSingleValueFields_failure() {
        for (String field : new String[]{" n/Other", " p/91234567", " e/other@example.com", " a/Other"}) {
            assertParseFailure(parser, REQUIRED_FIELDS + field,
                    Messages.getErrorMessageForDuplicatePrefixes(new Prefix(field.substring(1, 3))));
        }
        assertParseFailure(parser, REQUIRED_FIELDS + " r/First r/Second",
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_REMARK));
    }

    @Test
    public void parse_missingRequiredFields_failure() {
        for (String prefix : new String[]{"n/", "p/", "e/", "a/"}) {
            assertParseFailure(parser, REQUIRED_FIELDS.replace(prefix, ""),
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddClientCommand.MESSAGE_USAGE));
        }
    }

    @Test
    public void parse_invalidPhone_failure() {
        assertParseFailure(parser, REQUIRED_FIELDS.replace("85355255", "invalid"), Phone.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_invalidName_failure() {
        assertParseFailure(parser, REQUIRED_FIELDS.replace("Amy Bee", "Amy!"), Name.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_invalidEmail_failure() {
        assertParseFailure(parser, REQUIRED_FIELDS.replace("amy@gmail.com", "invalid"), Email.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_invalidAddress_failure() {
        assertParseFailure(parser, REQUIRED_FIELDS.replace("123, Jurong West Ave 6, #08-111", ""),
                Address.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_invalidTag_failure() {
        assertParseFailure(parser, REQUIRED_FIELDS + " t/employer!", Tag.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, REQUIRED_FIELDS + " t/", Tag.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_nonEmptyPreamble_failure() {
        assertParseFailure(parser, "unexpected" + REQUIRED_FIELDS,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddClientCommand.MESSAGE_USAGE));
    }

    @Test
    public void parseCommand_addClientWithRemark_addsOnlyClient() throws Exception {
        Client expected = new ClientBuilder().withRemark("Hiring engineers").build();
        Model model = new ModelManager();
        new AddressBookParser().parseCommand("addc" + REQUIRED_FIELDS + " r/Hiring engineers").execute(model);
        assertEquals(java.util.List.of(expected), model.getAddressBook().getClientList());
        assertEquals(0, model.getAddressBook().getJobseekerList().size());
    }
}
