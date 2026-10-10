package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Set;

import seedu.address.model.tag.Tag;

/**
 * Represents a Jobseeker in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Jobseeker extends Person {
    /**
     * Creates a jobseeker with the given details.
     */
    public Jobseeker(Name name, Phone phone, Email email, Address address, Set<Tag> tags) {
        requireAllNonNull(name, phone, email, address, tags);
        super(name, phone, email, address, tags);
    }
}
