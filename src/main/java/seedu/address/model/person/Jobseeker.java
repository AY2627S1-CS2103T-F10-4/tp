package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

import java.util.Objects;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.tag.Tag;

/**
 * Represents a Jobseeker in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Jobseeker extends Person {

    private final Qualification highestQualification;

    /**
     * Creates a jobseeker with the given details, hidden status ({@code isHidden}) and an empty remark.
     */
    public Jobseeker(Name name, Phone phone, Email email,
            Address address, Set<Tag> tags, boolean isHidden) {
        //nonNull values enforced by the parent class.
        this(name, phone, email, address, tags, new Remark(""), isHidden);
    }

    /**
     * Creates a jobseeker with the given details, remark and hidden status ({@code isHidden}).
     */
    public Jobseeker(Name name, Phone phone, Email email, Address address, Set<Tag> tags, Remark remark,
            boolean isHidden) {
        this(name, phone, email, address, tags, remark, new Qualification(""), isHidden);
    }

    /**
     * Creates a jobseeker with the given details and highest qualification.
     * An empty qualification represents an unspecified qualification.
     */
    public Jobseeker(Name name, Phone phone, Email email, Address address, Set<Tag> tags, Remark remark,
            Qualification highestQualification, boolean isHidden) {
        super(name, phone, email, address, tags, remark, isHidden);
        this.highestQualification = requireNonNull(highestQualification);
    }

    public Qualification getHighestQualification() {
        return highestQualification;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Jobseeker otherJobseeker
                && super.equals(otherJobseeker)
                && highestQualification.equals(otherJobseeker.highestQualification);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), highestQualification);
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
                .add("highestQualification", highestQualification)
                .toString();
    }
}
