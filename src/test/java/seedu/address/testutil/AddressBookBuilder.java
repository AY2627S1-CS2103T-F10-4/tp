package seedu.address.testutil;

import seedu.address.model.AddressBook;
import seedu.address.model.person.Client;
import seedu.address.model.person.Jobseeker;

/**
 * A utility class to help with building AddressBook objects.
 * Example usage: <br>
 *     {@code AddressBook ab = new AddressBookBuilder().withJobseeker("John", "Doe").build();}
 */
public class AddressBookBuilder {

    private AddressBook addressBook;

    public AddressBookBuilder() {
        addressBook = new AddressBook();
    }

    public AddressBookBuilder(AddressBook addressBook) {
        this.addressBook = addressBook;
    }

    /**
     * Adds a new {@code Jobseeker} to the {@code AddressBook} that we are building.
     */
    public AddressBookBuilder withJobseeker(Jobseeker jobseeker) {
        addressBook.addJobseeker(jobseeker);
        return this;
    }

    /**
     * Adds a new {@code Client} to the {@code AddressBook} that we are building.
     */
    public AddressBookBuilder withClient(Client client) {
        addressBook.addClient(client);
        return this;
    }

    public AddressBook build() {
        return addressBook;
    }
}
