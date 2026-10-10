package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.ReadOnlyUserPrefs;
import seedu.address.model.person.Client;
import seedu.address.model.person.Jobseeker;
import seedu.address.testutil.JobseekerBuilder;

/** Contains unit tests for {@link AddJobseekerCommand}. */
public class AddJobseekerCommandTest {

    @Test
    public void constructor_nullJobseeker_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new AddJobseekerCommand(null));
    }

    @Test
    public void execute_jobseekerAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingJobseekerAdded modelStub = new ModelStubAcceptingJobseekerAdded();
        Jobseeker validJobseeker = new JobseekerBuilder().build();

        CommandResult commandResult = new AddJobseekerCommand(validJobseeker).execute(modelStub);

        assertEquals(String.format(AddJobseekerCommand.MESSAGE_SUCCESS, Messages.format(validJobseeker)),
                commandResult.getFeedbackToUser());
        assertEquals(List.of(validJobseeker), modelStub.jobseekersAdded);
    }

    @Test
    public void execute_duplicateJobseeker_throwsCommandException() {
        Jobseeker validJobseeker = new JobseekerBuilder().build();
        AddJobseekerCommand addCommand = new AddJobseekerCommand(validJobseeker);
        ModelStub modelStub = new ModelStubWithJobseeker(validJobseeker);

        assertThrows(CommandException.class,
                AddJobseekerCommand.MESSAGE_DUPLICATE_JOBSEEKER, () -> addCommand.execute(modelStub));
    }

    @Test
    public void equals() {
        Jobseeker alice = new JobseekerBuilder().withName("Alice").build();
        Jobseeker bob = new JobseekerBuilder().withName("Bob").build();
        AddJobseekerCommand addAliceCommand = new AddJobseekerCommand(alice);
        AddJobseekerCommand addBobCommand = new AddJobseekerCommand(bob);

        // same object -> returns true
        assertTrue(addAliceCommand.equals(addAliceCommand));

        // same values -> returns true
        AddJobseekerCommand addAliceCommandCopy = new AddJobseekerCommand(alice);
        assertTrue(addAliceCommand.equals(addAliceCommandCopy));

        // different types -> returns false
        assertFalse(addAliceCommand.equals(1));

        // null -> returns false
        assertFalse(addAliceCommand.equals(null));

        // different jobseeker -> returns false
        assertFalse(addAliceCommand.equals(addBobCommand));
    }

    @Test
    public void toStringMethod() {
        AddJobseekerCommand addCommand = new AddJobseekerCommand(ALICE);
        String expected = AddJobseekerCommand.class.getCanonicalName() + "{toAdd=" + ALICE + "}";
        assertEquals(expected, addCommand.toString());
    }

    /**
     * A default model stub that has all of the methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public GuiSettings getGuiSettings() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setGuiSettings(GuiSettings guiSettings) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setAddressBook(ReadOnlyAddressBook newData) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasJobseeker(Jobseeker jobseeker) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean hasClient(Client client) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addJobseeker(Jobseeker jobseeker) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void addClient(Client client) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setJobseeker(Jobseeker target, Jobseeker editedJobseeker) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void setClient(Client target, Client editedClient) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteJobseeker(Jobseeker target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void deleteClient(Client target) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Jobseeker> getFilteredJobseekerList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredJobseekerList(Predicate<? super Jobseeker> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public ObservableList<Client> getFilteredClientList() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateFilteredClientList(Predicate<? super Client> predicate) {
            throw new AssertionError("This method should not be called.");
        }
    }

    /**
     * A Model stub that contains a single jobseeker.
     */
    private class ModelStubWithJobseeker extends ModelStub {
        private final Jobseeker jobseeker;

        ModelStubWithJobseeker(Jobseeker jobseeker) {
            requireNonNull(jobseeker);
            this.jobseeker = jobseeker;
        }

        @Override
        public boolean hasJobseeker(Jobseeker jobseeker) {
            return this.jobseeker.isSamePerson(jobseeker);
        }
    }

    /**
     * A Model stub that always accepts the jobseeker being added.
     */
    private class ModelStubAcceptingJobseekerAdded extends ModelStub {
        final ArrayList<Jobseeker> jobseekersAdded = new ArrayList<>();

        @Override
        public boolean hasJobseeker(Jobseeker jobseeker) {
            return jobseekersAdded.stream().anyMatch(jobseeker::isSamePerson);
        }

        @Override
        public void addJobseeker(Jobseeker jobseeker) {
            jobseekersAdded.add(jobseeker);
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook();
        }
    }

}
