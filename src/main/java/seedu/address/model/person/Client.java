package seedu.address.model.person;

import java.util.Set;

import seedu.address.model.tag.Tag;

/**
 * Represents a Client in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Client extends Person {

    /**
     * Creates a client with the given details, remark and hidden status ({@code isHidden}).
     */
    public Client(Name name, Phone phone, Email email, Address address, Set<Tag> tags, Remark remark,
            boolean isHidden) {
        //nonNull values enforced by the parent class.
        super(name, phone, email, address, tags, remark, isHidden);
    }

    /**
     * Creates a client with the given details, hidden status ({@code isHidden}) and an empty remark.
     */
    public Client(Name name, Phone phone, Email email,
            Address address, Set<Tag> tags, boolean isHidden) {
        this(name, phone, email, address, tags, new Remark(""), isHidden);
    }
}
