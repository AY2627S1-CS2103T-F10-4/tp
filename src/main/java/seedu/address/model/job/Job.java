package seedu.address.model.job;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents an available job offered by a client.
 * Guarantees: immutable; name is valid as declared in {@link #isValidJobName(String)}.
 */
public class Job {

    public static final String MESSAGE_CONSTRAINTS = "Job names should not be blank";

    public final String jobName;

    /**
     * Constructs a {@code Job} with a nonblank name. Spaces and punctuation are allowed.
     */
    public Job(String jobName) {
        requireNonNull(jobName);
        checkArgument(isValidJobName(jobName), MESSAGE_CONSTRAINTS);
        this.jobName = jobName;
    }

    /**
     * Returns true if a given string is a valid job name.
     */
    public static boolean isValidJobName(String test) {
        return !test.isBlank();
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Job otherJob && jobName.equals(otherJob.jobName);
    }

    @Override
    public int hashCode() {
        return jobName.hashCode();
    }

    @Override
    public String toString() {
        return '[' + jobName + ']';
    }
}
