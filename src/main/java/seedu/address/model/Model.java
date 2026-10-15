package seedu.address.model;

import java.util.List;
import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.model.person.Client;
import seedu.address.model.person.Jobseeker;

/**
 * The API of the Model component.
 */
public interface Model {
    /** {@code Predicate} that always evaluates to true for jobseekers */
    Predicate<Jobseeker> PREDICATE_SHOW_ALL_JOBSEEKERS = jobseeker -> true;

    /** {@code Predicate} that always evaluates to true for clients */
    Predicate<Client> PREDICATE_SHOW_ALL_CLIENTS = client -> true;

    /**
     * Returns the user prefs.
     */
    ReadOnlyUserPrefs getUserPrefs();

    /**
     * Returns the user prefs' GUI settings.
     */
    GuiSettings getGuiSettings();

    /**
     * Sets the user prefs' GUI settings.
     */
    void setGuiSettings(GuiSettings guiSettings);

    /**
     * Replaces address book data with the data in {@code addressBook}.
     */
    void setAddressBook(ReadOnlyAddressBook addressBook);

    /** Returns the AddressBook */
    ReadOnlyAddressBook getAddressBook();

    /**
     * @see AddressBook#setJobseekers(List)
     */
    void setJobseekers(List<Jobseeker> jobseekers);

    /**
     * @see AddressBook#setClients(List)
     */
    void setClients(List<Client> clients);

    /**
     * @see AddressBook#hasJobseeker(Jobseeker)
     */
    boolean hasJobseeker(Jobseeker jobseeker);

    /**
     * @see AddressBook#hasClient(Client)
     */
    boolean hasClient(Client client);

    /**
     * @see AddressBook#addJobseeker(Jobseeker)
     */
    void addJobseeker(Jobseeker jobseeker);

    /**
     * @see AddressBook#addClient(Client)
     */
    void addClient(Client client);

    /**
     * @see AddressBook#setJobseeker(Jobseeker, Jobseeker)
     */
    void setJobseeker(Jobseeker target, Jobseeker editedJobseeker);

    /**
     * @see AddressBook#setClient(Client, Client)
     */
    void setClient(Client target, Client editedClient);

    /**
     * @see AddressBook#removeJobseeker(Jobseeker)
     */
    void deleteJobseeker(Jobseeker target);

    /**
     * @see AddressBook#removeClient(Client)
     */
    void deleteClient(Client target);

    /** Returns an unmodifiable view of the filtered list containing only jobseekers. */
    ObservableList<Jobseeker> getFilteredJobseekerList();

    /** Returns an unmodifiable view of the filtered list containing only clients. */
    ObservableList<Client> getFilteredClientList();

    void updateFilteredJobseekerList(Predicate<? super Jobseeker> predicate);

    void updateFilteredClientList(Predicate<? super Client> predicate);
}
