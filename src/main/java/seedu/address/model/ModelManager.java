package seedu.address.model;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.function.Predicate;
import java.util.logging.Logger;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import seedu.address.commons.core.GuiSettings;
import seedu.address.commons.core.LogsCenter;
import seedu.address.model.person.Client;
import seedu.address.model.person.Jobseeker;

/**
 * Represents the in-memory model of the address book data.
 */
public class ModelManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);

    private final AddressBook addressBook;
    private final UserPrefs userPrefs;
    private final FilteredList<Jobseeker> filteredJobseekers;
    private final FilteredList<Client> filteredClients;

    /**
     * Initializes a ModelManager with the given addressBook and userPrefs.
     */
    public ModelManager(ReadOnlyAddressBook addressBook, ReadOnlyUserPrefs userPrefs) {
        requireAllNonNull(addressBook, userPrefs);

        logger.fine("Initializing with address book: " + addressBook + " and user prefs " + userPrefs);

        this.addressBook = new AddressBook(addressBook);
        this.userPrefs = new UserPrefs(userPrefs);
        filteredJobseekers = new FilteredList<>(this.addressBook.getJobseekerList());
        filteredClients = new FilteredList<>(this.addressBook.getClientList());
        updateFilteredJobseekerList(jobseeker -> true);
        updateFilteredClientList(client -> true);
    }

    public ModelManager() {
        this(new AddressBook(), new UserPrefs());
    }

    //=========== UserPrefs ==================================================================================

    @Override
    public ReadOnlyUserPrefs getUserPrefs() {
        return userPrefs;
    }

    @Override
    public GuiSettings getGuiSettings() {
        return userPrefs.getGuiSettings();
    }

    @Override
    public void setGuiSettings(GuiSettings guiSettings) {
        requireNonNull(guiSettings);
        userPrefs.setGuiSettings(guiSettings);
    }

    //=========== AddressBook ================================================================================

    @Override
    public void setAddressBook(ReadOnlyAddressBook addressBook) {
        this.addressBook.resetData(addressBook);
    }

    @Override
    public ReadOnlyAddressBook getAddressBook() {
        return addressBook;
    }

    @Override
    public boolean hasJobseeker(Jobseeker jobseeker) {
        return addressBook.hasJobseeker(jobseeker);
    }

    @Override
    public boolean hasClient(Client client) {
        return addressBook.hasClient(client);
    }

    @Override
    public void deleteJobseeker(Jobseeker target) {
        addressBook.removeJobseeker(target);
    }

    @Override
    public void deleteClient(Client target) {
        addressBook.removeClient(target);
    }

    @Override
    public void addJobseeker(Jobseeker jobseeker) {
        addressBook.addJobseeker(jobseeker);
    }

    @Override
    public void addClient(Client client) {
        addressBook.addClient(client);
    }

    @Override
    public void setJobseeker(Jobseeker target, Jobseeker editedJobseeker) {
        addressBook.setJobseeker(target, editedJobseeker);
    }

    @Override
    public void setClient(Client target, Client editedClient) {
        addressBook.setClient(target, editedClient);
    }

    //=========== Filtered Person List Accessors =============================================================

    /**
     * Returns an unmodifiable view containing only the filtered jobseekers in the address book.
     */
    @Override
    @SuppressWarnings("unchecked")
    public ObservableList<Jobseeker> getFilteredJobseekerList() {
        return filteredJobseekers;
    }

    /**
     * Returns an unmodifiable view containing only the filtered clients in the address book.
     */
    @Override
    @SuppressWarnings("unchecked")
    public ObservableList<Client> getFilteredClientList() {
        return filteredClients;
    }

    @Override
    public void updateFilteredJobseekerList(Predicate<? super Jobseeker> predicate) {
        requireNonNull(predicate);
        filteredJobseekers.setPredicate(predicate);
    }

    @Override
    public void updateFilteredClientList(Predicate<? super Client> predicate) {
        requireNonNull(predicate);
        filteredClients.setPredicate(predicate);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ModelManager otherModelManager)) {
            return false;
        }

        return addressBook.equals(otherModelManager.addressBook)
                && userPrefs.equals(otherModelManager.userPrefs);
    }

}
