package seedu.address.model;

import javafx.collections.ObservableList;
import seedu.address.model.person.Client;
import seedu.address.model.person.Jobseeker;

/**
 * Unmodifiable view of an address book
 */
public interface ReadOnlyAddressBook {

    /** Returns an unmodifiable view of the jobseeker list without duplicates. */
    ObservableList<Jobseeker> getJobseekerList();

    /** Returns an unmodifiable view of the client list without duplicates. */
    ObservableList<Client> getClientList();

}
