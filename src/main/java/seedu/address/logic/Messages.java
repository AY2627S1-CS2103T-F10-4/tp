package seedu.address.logic;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.parser.Prefix;
import seedu.address.model.person.Jobseeker;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command.";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format!\n%1$s";
    public static final String MESSAGE_INVALID_JOBSEEKER_DISPLAYED_INDEX = "The jobseeker index provided is invalid.";
    public static final String MESSAGE_JOBSEEKERS_LISTED_OVERVIEW = "%1$d jobseeker(s) listed!";
    public static final String MESSAGE_DUPLICATE_FIELDS =
                "Multiple values specified for the following single-valued field(s): ";

    /**
     * Returns an error message indicating the duplicate prefixes.
     */
    public static String getErrorMessageForDuplicatePrefixes(Prefix... duplicatePrefixes) {
        assert duplicatePrefixes.length > 0;

        Set<String> duplicateFields =
                Stream.of(duplicatePrefixes).map(Prefix::toString).collect(Collectors.toSet());

        return MESSAGE_DUPLICATE_FIELDS + String.join(" ", duplicateFields);
    }

    /**
     * Formats the {@code jobseeker} for display to the user.
     */
    public static String format(Jobseeker jobseeker) {
        final StringBuilder builder = new StringBuilder();
        builder.append(jobseeker.getName())
                .append("; Phone: ")
                .append(jobseeker.getPhone())
                .append("; Email: ")
                .append(jobseeker.getEmail())
                .append("; Address: ")
                .append(jobseeker.getAddress())
                .append("; Tags: ");
        jobseeker.getTags().forEach(builder::append);
        return builder.toString();
    }

}
