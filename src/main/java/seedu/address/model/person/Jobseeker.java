package seedu.address.model.person;

import java.util.Set;

import seedu.address.model.tag.Tag;

/**
 * Represents a Jobseeker in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Jobseeker extends Person {

    /**
     * Creates a jobseeker with the given details and an empty remark.
     */
    public Jobseeker(Name name, Phone phone, Email email,
            Address address, Set<Tag> tags) {
        //nonNull values enforced by the parent class.
        this(name, phone, email, address, tags, new Remark(""));
    }

    /**
     * Creates a jobseeker with the given details and remark.
     */
    public Jobseeker(Name name, Phone phone, Email email, Address address, Set<Tag> tags, Remark remark) {
        super(name, phone, email, address, tags, remark);
    }
}
