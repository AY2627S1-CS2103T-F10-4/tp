package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

/**
 * Represents a jobseeker's highest qualification. An empty value means it has not been specified.
 * Guarantees: immutable and not null.
 */
public class Qualification {

    public final String value;

    /**
     * Constructs a {@code Qualification} from text, which may be empty.
     */
    public Qualification(String qualification) {
        requireNonNull(qualification);
        value = qualification;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Qualification otherQualification && value.equals(otherQualification.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
