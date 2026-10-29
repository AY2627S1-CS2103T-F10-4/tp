package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditClientCommand.EditClientDescriptor;
import seedu.address.logic.commands.EditJobseekerCommand.EditJobseekerDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.job.Job;
import seedu.address.model.person.Qualification;

/**
 * Checks parsing through command execution for optional contact-specific fields.
 */
public class ContactFieldsParserTest {
    private static final String REQUIRED_FIELDS = " n/Alice p/91234567 e/alice@example.com a/Clementi";
    private final AddressBookParser parser = new AddressBookParser();

    @Test
    public void parse_jobseekerWithJobs_rejectedForAddAndEdit() {
        for (String command : new String[]{"addj" + REQUIRED_FIELDS, "editj 1"}) {
            for (String fields : new String[]{" j/Engineer", " j/", " r/Available j/Engineer",
                " j/Engineer r/Available", " q/Diploma j/Engineer", " j/Engineer j/Tester"}) {
                ParseException exception = assertThrows(
                        ParseException.class, () -> parser.parseCommand(command + fields), command + fields);
                assertEquals(Messages.MESSAGE_JOB_FIELD_NOT_ALLOWED, exception.getMessage());
            }
        }
    }

    @Test
    public void parse_clientWithQualification_rejectedForAddAndEdit() {
        for (String command : new String[]{"addc" + REQUIRED_FIELDS, "editc 1"}) {
            for (String fields : new String[]{" q/Degree", " q/", " r/Hiring q/Degree",
                " q/Degree r/Hiring", " j/Engineer q/Degree", " q/Diploma q/Degree"}) {
                ParseException exception = assertThrows(
                        ParseException.class, () -> parser.parseCommand(command + fields), command + fields);
                assertEquals(Messages.MESSAGE_QUALIFICATION_FIELD_NOT_ALLOWED, exception.getMessage());
            }
        }
    }

    @Test
    public void parse_prefixTextWithinWords_preservesRemarksForAddAndEdit() throws Exception {
        Model model = new ModelManager();
        parser.parseCommand("addj" + REQUIRED_FIELDS + " r/See example.org/j/Engineer").execute(model);
        parser.parseCommand("addc" + REQUIRED_FIELDS + " r/See example.org/q/Degree").execute(model);
        assertEquals("See example.org/j/Engineer", model.getFilteredJobseekerList().getFirst().getRemark().value);
        assertEquals("See example.org/q/Degree", model.getFilteredClientList().getFirst().getRemark().value);

        parser.parseCommand("editj 1 r/Updated example.org/j/Engineer").execute(model);
        parser.parseCommand("editc 1 r/Updated example.org/q/Degree").execute(model);
        assertEquals("Updated example.org/j/Engineer", model.getFilteredJobseekerList().getFirst().getRemark().value);
        assertEquals("Updated example.org/q/Degree", model.getFilteredClientList().getFirst().getRemark().value);
    }

    @Test
    public void addJobseeker_qualificationWithSpaces_parsesValueAndOtherFields() throws Exception {
        Model model = new ModelManager();
        parser.parseCommand("addj" + REQUIRED_FIELDS + " q/  Bachelor's Degree (Computing)  r/Available t/java")
                .execute(model);
        var person = model.getFilteredJobseekerList().getFirst();
        assertEquals(new Qualification("Bachelor's Degree (Computing)"), person.getHighestQualification());
        assertEquals("Clementi", person.getAddress().value);
        assertEquals("Available", person.getRemark().value);
        assertEquals(1, person.getTags().size());
    }

    @Test
    public void add_optionalFieldsOmittedOrEmpty_defaultsToEmpty() throws Exception {
        for (String suffix : new String[]{"", " q/   "}) {
            Model model = new ModelManager();
            parser.parseCommand("addj" + REQUIRED_FIELDS + suffix).execute(model);
            assertEquals(new Qualification(""), model.getFilteredJobseekerList().getFirst().getHighestQualification());
        }
        for (String suffix : new String[]{"", " j/   "}) {
            Model model = new ModelManager();
            parser.parseCommand("addc" + REQUIRED_FIELDS + suffix).execute(model);
            assertTrue(model.getFilteredClientList().getFirst().getAvailableJobs().isEmpty());
        }
    }

    @Test
    public void addClient_multipleJobs_trimsValuesAndCollapsesDuplicates() throws Exception {
        Model model = new ModelManager();
        parser.parseCommand("addc" + REQUIRED_FIELDS
                + " j/ C++ Developer j/Data Analyst j/C++ Developer r/Hiring").execute(model);
        var client = model.getFilteredClientList().getFirst();
        assertEquals(Set.of(new Job("C++ Developer"), new Job("Data Analyst")), client.getAvailableJobs());
        assertEquals("Clementi", client.getAddress().value);
        assertEquals("Hiring", client.getRemark().value);
    }

    @Test
    public void parse_duplicateQualification_rejectedForAddAndEdit() {
        for (String command : new String[]{"addj" + REQUIRED_FIELDS, "editj 1"}) {
            ParseException exception = assertThrows(
                    ParseException.class, () -> parser.parseCommand(command + " q/Diploma q/Degree"));
            assertEquals(Messages.getErrorMessageForDuplicatePrefixes(CliSyntax.PREFIX_QUALIFICATION),
                    exception.getMessage());
        }
    }

    @Test
    public void parse_emptyJobMixedWithOtherJobs_rejectedForAddAndEdit() {
        for (String command : new String[]{"addc" + REQUIRED_FIELDS, "editc 1"}) {
            for (String jobs : new String[]{" j/ j/Developer", " j/Developer j/", " j/ j/"}) {
                ParseException exception = assertThrows(
                        ParseException.class, () -> parser.parseCommand(command + jobs));
                assertEquals(Job.MESSAGE_CONSTRAINTS, exception.getMessage());
            }
        }
    }

    @Test
    public void editJobseeker_qualification_updatesPreservesAndClears() throws Exception {
        Model model = new ModelManager();
        parser.parseCommand("addj" + REQUIRED_FIELDS + " q/Diploma").execute(model);
        parser.parseCommand("editj 1 q/Degree").execute(model);
        assertEquals(new Qualification("Degree"),
                model.getFilteredJobseekerList().getFirst().getHighestQualification());
        parser.parseCommand("editj 1 r/Available").execute(model);
        assertEquals(new Qualification("Degree"),
                model.getFilteredJobseekerList().getFirst().getHighestQualification());
        parser.parseCommand("editj 1 q/").execute(model);
        assertEquals(new Qualification(""), model.getFilteredJobseekerList().getFirst().getHighestQualification());
        assertEquals("Available", model.getFilteredJobseekerList().getFirst().getRemark().value);
    }

    @Test
    public void editClient_jobs_replacesPreservesAndClears() throws Exception {
        Model model = new ModelManager();
        parser.parseCommand("addc" + REQUIRED_FIELDS + " j/Developer").execute(model);
        parser.parseCommand("editc 1 j/Analyst j/Manager j/Analyst").execute(model);
        Set<Job> expected = Set.of(new Job("Analyst"), new Job("Manager"));
        assertEquals(expected, model.getFilteredClientList().getFirst().getAvailableJobs());
        parser.parseCommand("editc 1 r/Hiring").execute(model);
        assertEquals(expected, model.getFilteredClientList().getFirst().getAvailableJobs());
        parser.parseCommand("editc 1 j/").execute(model);
        assertTrue(model.getFilteredClientList().getFirst().getAvailableJobs().isEmpty());
        assertEquals("Hiring", model.getFilteredClientList().getFirst().getRemark().value);
    }

    @Test
    public void otherCommands_contactFields_preserved() throws Exception {
        Model model = new ModelManager();
        parser.parseCommand("addj" + REQUIRED_FIELDS + " q/Diploma").execute(model);
        parser.parseCommand("addc" + REQUIRED_FIELDS + " j/Developer").execute(model);
        for (String command : new String[]{"tagj 1 java", "untagj 1 java", "hidej 1", "listhj", "unhidej 1",
                "tagc 1 java", "untagc 1 java", "hidec 1", "listhc", "unhidec 1"}) {
            parser.parseCommand(command).execute(model);
            assertEquals(new Qualification("Diploma"),
                    model.getAddressBook().getJobseekerList().getFirst().getHighestQualification(), command);
            assertEquals(Set.of(new Job("Developer")),
                    model.getAddressBook().getClientList().getFirst().getAvailableJobs(), command);
        }
    }

    @Test
    public void descriptors_newFields_copiedAndCompared() {
        EditJobseekerDescriptor jobseeker = new EditJobseekerDescriptor();
        jobseeker.setHighestQualification(new Qualification("Degree"));
        assertTrue(jobseeker.isAnyFieldEdited());
        assertEquals(jobseeker, new EditJobseekerDescriptor(jobseeker));
        assertNotEquals(jobseeker, new EditJobseekerDescriptor());

        EditClientDescriptor client = new EditClientDescriptor();
        Set<Job> jobs = new HashSet<>(Set.of(new Job("Developer")));
        client.setAvailableJobs(jobs);
        EditClientDescriptor copy = new EditClientDescriptor(client);
        jobs.clear();
        assertEquals(Set.of(new Job("Developer")), copy.getAvailableJobs().orElseThrow());
        assertEquals(client, copy);
        assertNotEquals(client, new EditClientDescriptor());
        assertThrows(UnsupportedOperationException.class, () -> copy.getAvailableJobs().orElseThrow().clear());
        client.setAvailableJobs(Set.of());
        assertTrue(client.isAnyFieldEdited());
        assertNotEquals(client, copy);
    }
}
