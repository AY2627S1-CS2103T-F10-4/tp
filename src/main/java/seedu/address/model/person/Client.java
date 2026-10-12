package seedu.address.model.person;

import java.util.Set;

import seedu.address.model.tag.Tag;

/**
 * Represents a Client in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Client extends Person {

    /**
     * Creates a client with the given details.
     */
    public Client(Name name, Phone phone, Email email, Address address, Set<Tag> tags, Remark remark) {
        super(name, phone, email, address, tags, remark);
    }

    /**
     * Creates a client with an empty remark.
     */
    public Client(Name name, Phone phone, Email email,
            Address address, Set<Tag> tags) {
        this(name, phone, email, address, tags, new Remark(""));
    }
}
