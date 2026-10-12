package seedu.address.model.person;

import java.util.Set;

import seedu.address.model.tag.Tag;

/**
 * Represents a Jobseeker in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Jobseeker extends Person {

    /**
     * Creates a jobseeker with an empty remark.
     */
    public Jobseeker(Name name, Phone phone, Email email,
            Address address, Set<Tag> tags) {
        this(name, phone, email, address, tags, new Remark(""));
    }

    /**
     * Creates a jobseeker with the given details and remark.
     */
    public Jobseeker(Name name, Phone phone, Email email, Address address, Set<Tag> tags, Remark remark) {
        super(name, phone, email, address, tags, remark);
    }
}
