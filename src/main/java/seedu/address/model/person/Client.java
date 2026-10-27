package seedu.address.model.person;

import java.util.Objects;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.job.Job;
import seedu.address.model.tag.Tag;

/**
 * Represents a Client in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Client extends Person {

    private final Set<Job> availableJobs;

    /**
     * Creates a client with the given details, remark and hidden status ({@code isHidden}).
     */
    public Client(Name name, Phone phone, Email email, Address address, Set<Tag> tags, Remark remark,
            boolean isHidden) {
        this(name, phone, email, address, tags, remark, Set.of(), isHidden);
    }

    /**
     * Creates a client with the given details, hidden status ({@code isHidden}) and an empty remark.
     */
    public Client(Name name, Phone phone, Email email,
            Address address, Set<Tag> tags, boolean isHidden) {
        this(name, phone, email, address, tags, new Remark(""), isHidden);
    }

    /**
     * Creates a client with the given details and available jobs.
     * An empty set represents no specified jobs. The set and its elements must not be null.
     */
    public Client(Name name, Phone phone, Email email, Address address, Set<Tag> tags, Remark remark,
            Set<Job> availableJobs, boolean isHidden) {
        super(name, phone, email, address, tags, remark, isHidden);
        this.availableJobs = Set.copyOf(availableJobs);
    }

    /**
     * Returns an immutable set of the client's available jobs.
     */
    public Set<Job> getAvailableJobs() {
        return availableJobs;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Client otherClient
                && super.equals(otherClient)
                && availableJobs.equals(otherClient.availableJobs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), availableJobs);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", getName())
                .add("phone", getPhone())
                .add("email", getEmail())
                .add("address", getAddress())
                .add("tags", getTags())
                .add("remark", getRemark())
                .add("isHidden", isHidden())
                .add("availableJobs", availableJobs)
                .toString();
    }
}
