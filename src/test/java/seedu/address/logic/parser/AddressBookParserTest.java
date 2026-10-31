package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.commons.core.InputLimits.MAX_COMMAND_LENGTH;
import static seedu.address.logic.Messages.MESSAGE_COMMAND_TOO_LONG;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddClientCommand;
import seedu.address.logic.commands.AddJobseekerCommand;
import seedu.address.logic.commands.ClearClientCommand;
import seedu.address.logic.commands.ClearJobseekerCommand;
import seedu.address.logic.commands.CopyClientCommand;
import seedu.address.logic.commands.CopyJobseekerCommand;
import seedu.address.logic.commands.DeleteClientCommand;
import seedu.address.logic.commands.DeleteJobseekerCommand;
import seedu.address.logic.commands.EditClientCommand;
import seedu.address.logic.commands.EditJobseekerCommand;
import seedu.address.logic.commands.EditJobseekerCommand.EditJobseekerDescriptor;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FindClientCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.FindHiddenClientCommand;
import seedu.address.logic.commands.FindHiddenJobseekerCommand;
import seedu.address.logic.commands.FindJobseekerCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.HideClientCommand;
import seedu.address.logic.commands.HideJobseekerCommand;
import seedu.address.logic.commands.ListClientCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.ListHiddenClientCommand;
import seedu.address.logic.commands.ListHiddenJobseekerCommand;
import seedu.address.logic.commands.ListJobseekerCommand;
import seedu.address.logic.commands.TagClientCommand;
import seedu.address.logic.commands.TagJobseekerCommand;
import seedu.address.logic.commands.UnhideClientCommand;
import seedu.address.logic.commands.UnhideJobseekerCommand;
import seedu.address.logic.commands.UntagClientCommand;
import seedu.address.logic.commands.UntagJobseekerCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.person.Client;
import seedu.address.model.person.Jobseeker;
import seedu.address.model.person.PersonContainsKeywordsPredicate;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.ClientBuilder;
import seedu.address.testutil.EditClientDescriptorBuilder;
import seedu.address.testutil.EditJobseekerDescriptorBuilder;
import seedu.address.testutil.JobseekerBuilder;
import seedu.address.testutil.PersonUtil;

public class AddressBookParserTest {

    private final AddressBookParser parser = new AddressBookParser();

    @Test
    public void parseCommand_editc_returnsClientCommand() throws Exception {
        assertEquals(new EditClientCommand(INDEX_FIRST_PERSON,
                new EditClientDescriptorBuilder().withRemark("Hiring now").build()),
                parser.parseCommand("editc 1 r/Hiring now"));
    }

    @Test
    public void parseCommand_editMissingArguments_reportsMatchingUsage() {
        String clientUsage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditClientCommand.MESSAGE_USAGE);
        String jobseekerUsage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditJobseekerCommand.MESSAGE_USAGE);
        assertThrows(ParseException.class, clientUsage, () -> parser.parseCommand("editc"));
        assertThrows(ParseException.class, jobseekerUsage, () -> parser.parseCommand("editj"));
    }

    @Test
    public void parseAndExecute_editRemarks_updatesClearsAndPreservesBothTypes() throws Exception {
        Model model = new ModelManager();
        model.addClient(new ClientBuilder().withRemark("Old client remark").build());
        model.addJobseeker(new JobseekerBuilder().withRemark("Old jobseeker remark").build());
        parser.parseCommand("editc 1 r/Hiring now").execute(model);
        parser.parseCommand("editj 1 r/Available now").execute(model);
        parser.parseCommand("editc 1 n/New Client").execute(model);
        parser.parseCommand("editj 1 n/New Jobseeker").execute(model);
        assertEquals("Hiring now", model.getFilteredClientList().get(0).getRemark().value);
        assertEquals("Available now", model.getFilteredJobseekerList().get(0).getRemark().value);
        parser.parseCommand("editc 1 r/").execute(model);
        parser.parseCommand("editj 1 r/").execute(model);
        assertEquals("", model.getFilteredClientList().get(0).getRemark().value);
        assertEquals("", model.getFilteredJobseekerList().get(0).getRemark().value);
    }

    @Test
    public void parseCommand_tagc() throws Exception {
        assertEquals(new TagClientCommand(INDEX_FIRST_PERSON, Set.of(new Tag("priority"), new Tag("finance"))),
                parser.parseCommand("tagc 1 priority finance"));
    }

    @Test
    public void parseCommand_untagc() throws Exception {
        assertEquals(new UntagClientCommand(INDEX_FIRST_PERSON, Set.of(new Tag("priority"), new Tag("finance"))),
                parser.parseCommand("untagc 1 priority finance"));
    }

    @Test
    public void parseCommand_untagj() throws Exception {
        assertEquals(new UntagJobseekerCommand(INDEX_FIRST_PERSON, Set.of(new Tag("java"), new Tag("teamwork"))),
                parser.parseCommand("untagj 1 java teamwork"));
    }

    @Test
    public void parseCommand_tagj() throws Exception {
        assertEquals(new TagJobseekerCommand(INDEX_FIRST_PERSON, Set.of(new Tag("java"), new Tag("teamwork"))),
                parser.parseCommand("tagj 1 java teamwork"));
    }

    @Test
    public void parseCommand_addj() throws Exception {
        Jobseeker jobseeker = new JobseekerBuilder().build();
        AddJobseekerCommand command = (AddJobseekerCommand) parser.parseCommand(
                PersonUtil.getAddJobseekerCommand(jobseeker));
        assertEquals(new AddJobseekerCommand(jobseeker), command);
    }

    @Test
    public void parseCommand_addc() throws Exception {
        Client client = new ClientBuilder().withName(VALID_NAME_AMY).withPhone(VALID_PHONE_AMY)
                .withEmail(VALID_EMAIL_AMY).withAddress(VALID_ADDRESS_AMY).build();
        assertEquals(new AddClientCommand(client), parser.parseCommand(AddClientCommand.COMMAND_WORD
                + NAME_DESC_AMY + PHONE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY));
    }

    @Test
    public void parseCommand_clearj() throws Exception {
        assertTrue(parser.parseCommand(ClearJobseekerCommand.COMMAND_WORD) instanceof ClearJobseekerCommand);
        assertTrue(parser.parseCommand(ClearJobseekerCommand.COMMAND_WORD + " 3") instanceof ClearJobseekerCommand);
    }

    @Test
    public void parseCommand_clearc() throws Exception {
        assertTrue(parser.parseCommand(ClearClientCommand.COMMAND_WORD) instanceof ClearClientCommand);
        assertTrue(parser.parseCommand(ClearClientCommand.COMMAND_WORD + " 3") instanceof ClearClientCommand);
    }

    @Test
    public void parseCommand_cpc() throws Exception {
        assertTrue(parser.parseCommand(CopyClientCommand.COMMAND_WORD) instanceof CopyClientCommand);
        assertTrue(parser.parseCommand(CopyClientCommand.COMMAND_WORD + " 1") instanceof CopyClientCommand);
    }

    @Test
    public void parseCommand_cpj() throws Exception {
        assertTrue(parser.parseCommand(CopyJobseekerCommand.COMMAND_WORD) instanceof CopyJobseekerCommand);
        assertTrue(parser.parseCommand(CopyJobseekerCommand.COMMAND_WORD + " 1") instanceof CopyJobseekerCommand);
    }

    @Test
    public void parseCommand_copyInvalidIndex_reportsMatchingUsage() {
        String clientUsage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, CopyClientCommand.MESSAGE_USAGE);
        String jobseekerUsage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, CopyJobseekerCommand.MESSAGE_USAGE);

        assertThrows(ParseException.class, clientUsage, () -> parser.parseCommand("cpc -1"));
        assertThrows(ParseException.class, jobseekerUsage, () -> parser.parseCommand("cpj 1 2"));
    }

    @Test
    public void parseCommand_deletej() throws Exception {
        DeleteJobseekerCommand command = (DeleteJobseekerCommand) parser.parseCommand(
                DeleteJobseekerCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new DeleteJobseekerCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_deletec() throws Exception {
        DeleteClientCommand command = (DeleteClientCommand) parser.parseCommand(
                DeleteClientCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new DeleteClientCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_hidej() throws Exception {
        HideJobseekerCommand command = (HideJobseekerCommand) parser.parseCommand(
                HideJobseekerCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new HideJobseekerCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_hidec() throws Exception {
        HideClientCommand command = (HideClientCommand) parser.parseCommand(
                HideClientCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new HideClientCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_unhidej() throws Exception {
        UnhideJobseekerCommand command = (UnhideJobseekerCommand) parser.parseCommand(
                UnhideJobseekerCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new UnhideJobseekerCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_unhidec() throws Exception {
        UnhideClientCommand command = (UnhideClientCommand) parser.parseCommand(
                UnhideClientCommand.COMMAND_WORD + " " + INDEX_FIRST_PERSON.getOneBased());
        assertEquals(new UnhideClientCommand(INDEX_FIRST_PERSON), command);
    }

    @Test
    public void parseCommand_edit() throws Exception {
        Jobseeker jobseeker = new JobseekerBuilder().build();
        EditJobseekerDescriptor descriptor = new EditJobseekerDescriptorBuilder(jobseeker)
                .build();
        String editCommandText = EditJobseekerCommand.COMMAND_WORD + " "
                + INDEX_FIRST_PERSON.getOneBased() + " " + PersonUtil.getEditJobseekerDescriptorDetails(descriptor);
        EditJobseekerCommand command = (EditJobseekerCommand) parser.parseCommand(editCommandText);
        assertEquals(new EditJobseekerCommand(INDEX_FIRST_PERSON, descriptor), command);
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3") instanceof ExitCommand);
    }

    @Test
    public void parseCommand_find() throws Exception {
        List<String> keywords = List.of("foo", "bar", "baz");
        FindCommand command = (FindCommand) parser.parseCommand(
                FindCommand.COMMAND_WORD + " " + String.join(" ", keywords));
        assertEquals(new FindCommand(new PersonContainsKeywordsPredicate(keywords)), command);
    }

    @Test
    public void parseCommand_findj() throws Exception {
        List<String> keywords = List.of("foo", "bar", "baz");
        FindJobseekerCommand command = (FindJobseekerCommand) parser.parseCommand(
                FindJobseekerCommand.COMMAND_WORD + " " + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindJobseekerCommand(new PersonContainsKeywordsPredicate(keywords)), command);
    }

    @Test
    public void parseCommand_findc() throws Exception {
        List<String> keywords = List.of("foo", "bar", "baz");
        FindClientCommand command = (FindClientCommand) parser.parseCommand(
                FindClientCommand.COMMAND_WORD + " " + String.join(" ", keywords));
        assertEquals(new FindClientCommand(new PersonContainsKeywordsPredicate(keywords)), command);
    }

    @Test
    public void parseCommand_findhj() throws Exception {
        List<String> keywords = List.of("foo", "bar", "baz");
        FindHiddenJobseekerCommand command = (FindHiddenJobseekerCommand) parser.parseCommand(
                FindHiddenJobseekerCommand.COMMAND_WORD + " " + String.join(" ", keywords));
        assertEquals(new FindHiddenJobseekerCommand(new PersonContainsKeywordsPredicate(keywords)), command);
    }

    @Test
    public void parseCommand_findhc() throws Exception {
        List<String> keywords = List.of("foo", "bar", "baz");
        FindHiddenClientCommand command = (FindHiddenClientCommand) parser.parseCommand(
                FindHiddenClientCommand.COMMAND_WORD + " " + String.join(" ", keywords));
        assertEquals(new FindHiddenClientCommand(new PersonContainsKeywordsPredicate(keywords)), command);
    }

    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_list() throws Exception {
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD) instanceof ListCommand);
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD + " 3") instanceof ListCommand);
    }

    @Test
    public void parseCommand_listj() throws Exception {
        assertTrue(parser.parseCommand(ListJobseekerCommand.COMMAND_WORD) instanceof ListJobseekerCommand);
        assertTrue(parser.parseCommand(ListJobseekerCommand.COMMAND_WORD + " 3") instanceof ListJobseekerCommand);
    }

    @Test
    public void parseCommand_listc() throws Exception {
        assertTrue(parser.parseCommand(ListClientCommand.COMMAND_WORD) instanceof ListClientCommand);
        assertTrue(parser.parseCommand(ListClientCommand.COMMAND_WORD + " 3") instanceof ListClientCommand);
    }

    @Test
    public void parseCommand_listhj() throws Exception {
        assertTrue(parser.parseCommand(ListHiddenJobseekerCommand.COMMAND_WORD)
                instanceof ListHiddenJobseekerCommand);
        assertTrue(parser.parseCommand(ListHiddenJobseekerCommand.COMMAND_WORD + " 3")
                instanceof ListHiddenJobseekerCommand);
    }

    @Test
    public void parseCommand_listhc() throws Exception {
        assertTrue(parser.parseCommand(ListHiddenClientCommand.COMMAND_WORD)
                instanceof ListHiddenClientCommand);
        assertTrue(parser.parseCommand(ListHiddenClientCommand.COMMAND_WORD + " 3")
                instanceof ListHiddenClientCommand);
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE), ()
            -> parser.parseCommand(""));
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_UNKNOWN_COMMAND, () -> parser.parseCommand("unknownCommand"));
    }

    @Test
    public void parseCommand_inputTooLong_throwsParseException() {
        String input = "a".repeat(MAX_COMMAND_LENGTH + 1);
        assertThrows(ParseException.class, MESSAGE_COMMAND_TOO_LONG, () -> parser.parseCommand(input));
    }

    @Test
    public void parseCommand_inputAtMaximumLength_isParsed() throws Exception {
        String input = "find " + "a".repeat(MAX_COMMAND_LENGTH - "find ".length());
        assertTrue(parser.parseCommand(input) instanceof FindCommand);
    }
}
