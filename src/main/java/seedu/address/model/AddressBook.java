package seedu.address.model;

import static java.util.Objects.requireNonNull;

import javafx.collections.ObservableList;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.person.Client;
import seedu.address.model.person.Jobseeker;
import seedu.address.model.person.UniquePersonList;

/**
 * Wraps all data at the address-book level.
 * Duplicates are not allowed (by .isSamePerson comparison).
 */
public class AddressBook implements ReadOnlyAddressBook {

    private final UniquePersonList<Jobseeker> jobseekers = new UniquePersonList<>();
    private final UniquePersonList<Client> clients = new UniquePersonList<>();

    public AddressBook() {}

    /**
     * Creates an AddressBook using the jobseekers and clients in {@code toBeCopied}.
     */
    public AddressBook(ReadOnlyAddressBook toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    /**
     * Replaces the contents of the jobseeker list with {@code jobseekers}.
     * {@code jobseekers} must not contain duplicate jobseekers.
     */
    public void setJobseekers(java.util.List<Jobseeker> jobseekers) {
        this.jobseekers.setPersons(jobseekers);
    }

    public void setClients(java.util.List<Client> clients) {
        this.clients.setPersons(clients);
    }

    /**
     * Resets the existing data of this {@code AddressBook} with {@code newData}.
     */
    public void resetData(ReadOnlyAddressBook newData) {
        requireNonNull(newData);

        setJobseekers(newData.getJobseekerList());
        setClients(newData.getClientList());
    }

    /**
     * Returns true if the given jobseeker exists in the address book.
     */
    public boolean hasJobseeker(Jobseeker jobseeker) {
        requireNonNull(jobseeker);
        return jobseekers.contains(jobseeker);
    }

    /**
     * Returns true if the given client exists in the address book.
     */
    public boolean hasClient(Client client) {
        requireNonNull(client);
        return clients.contains(client);
    }

    public void addJobseeker(Jobseeker jobseeker) {
        jobseekers.add(jobseeker);
    }

    public void addClient(Client client) {
        clients.add(client);
    }

    public void setJobseeker(Jobseeker target, Jobseeker editedJobseeker) {
        jobseekers.setPerson(target, editedJobseeker);
    }

    public void setClient(Client target, Client editedClient) {
        clients.setPerson(target, editedClient);
    }

    public void removeJobseeker(Jobseeker jobseeker) {
        jobseekers.remove(jobseeker);
    }

    public void removeClient(Client client) {
        clients.remove(client);
    }

    //// util methods

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("jobseekers", jobseekers)
                .add("clients", clients)
                .toString();
    }

    @Override
    public ObservableList<Jobseeker> getJobseekerList() {
        return jobseekers.asUnmodifiableObservableList();
    }

    @Override
    public ObservableList<Client> getClientList() {
        return clients.asUnmodifiableObservableList();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddressBook otherAddressBook)) {
            return false;
        }

        return jobseekers.equals(otherAddressBook.jobseekers)
                && clients.equals(otherAddressBook.clients);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(jobseekers, clients);
    }
}
