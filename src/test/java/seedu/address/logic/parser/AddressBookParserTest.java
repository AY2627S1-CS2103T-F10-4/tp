package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddJobseekerCommand;
import seedu.address.logic.commands.ClearClientCommand;
import seedu.address.logic.commands.ClearJobseekerCommand;
import seedu.address.logic.commands.DeleteClientCommand;
import seedu.address.logic.commands.DeleteJobseekerCommand;
import seedu.address.logic.commands.EditJobseekerCommand;
import seedu.address.logic.commands.EditJobseekerCommand.EditJobseekerDescriptor;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FindClientCommand;
import seedu.address.logic.commands.FindJobseekerCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.ListClientCommand;
import seedu.address.logic.commands.ListJobseekerCommand;
import seedu.address.logic.commands.TagClientCommand;
import seedu.address.logic.commands.TagJobseekerCommand;
import seedu.address.logic.commands.UntagClientCommand;
import seedu.address.logic.commands.UntagJobseekerCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Jobseeker;
import seedu.address.model.person.PersonContainsKeywordsPredicate;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.EditJobseekerDescriptorBuilder;
import seedu.address.testutil.JobseekerBuilder;
import seedu.address.testutil.PersonUtil;

public class AddressBookParserTest {

    private final AddressBookParser parser = new AddressBookParser();

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
    public void parseCommand_add() throws Exception {
        Jobseeker jobseeker = new JobseekerBuilder().build();
        AddJobseekerCommand command = (AddJobseekerCommand) parser.parseCommand(
                PersonUtil.getAddJobseekerCommand(jobseeker));
        assertEquals(new AddJobseekerCommand(jobseeker), command);
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
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3") instanceof HelpCommand);
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
    public void parseCommand_unrecognisedInput_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE), ()
            -> parser.parseCommand(""));
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_UNKNOWN_COMMAND, () -> parser.parseCommand("unknownCommand"));
    }
}
