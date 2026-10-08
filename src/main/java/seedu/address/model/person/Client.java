package seedu.address.model.person;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.tag.Tag;

import java.util.Set;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

/**
 * Represents a Client in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Client extends Person {
    public Client(Name name, Phone phone, Email email, Address address, Set<Tag> tags) {
        requireAllNonNull(name, phone, email, address, tags);
        super(name, phone, email, address, tags);
    }

    @Override
    public String toString() {
        return "[C]" + super.toString();
    }
}
