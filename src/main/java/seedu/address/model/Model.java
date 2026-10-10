package seedu.address.model;

import java.util.function.Predicate;

import javafx.collections.ObservableList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.model.person.Client;
import seedu.address.model.person.Jobseeker;

/**
 * The API of the Model component.
 */
public interface Model {
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

    boolean hasJobseeker(Jobseeker jobseeker);

    boolean hasClient(Client client);

    void addJobseeker(Jobseeker jobseeker);

    void addClient(Client client);

    void setJobseeker(Jobseeker target, Jobseeker editedJobseeker);

    void setClient(Client target, Client editedClient);

    void deleteJobseeker(Jobseeker target);

    void deleteClient(Client target);

    /** Returns an unmodifiable view of the filtered list containing only jobseekers. */
    ObservableList<Jobseeker> getFilteredJobseekerList();

    /** Returns an unmodifiable view of the filtered list containing only clients. */
    ObservableList<Client> getFilteredClientList();

    void updateFilteredJobseekerList(Predicate<? super Jobseeker> predicate);

    void updateFilteredClientList(Predicate<? super Client> predicate);
}
