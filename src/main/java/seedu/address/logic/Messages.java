package seedu.address.logic;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.parser.Prefix;
import seedu.address.model.person.Client;
import seedu.address.model.person.Jobseeker;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command.";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format!\n%1$s";
    public static final String MESSAGE_MISSING_REQUIRED_FIELDS = "Missing required fields: %s.";
    public static final String MESSAGE_JOB_FIELD_NOT_ALLOWED =
            "The j/ field is only supported for clients (addc/editc), not jobseekers.";
    public static final String MESSAGE_QUALIFICATION_FIELD_NOT_ALLOWED =
            "The q/ field is only supported for jobseekers (addj/editj), not clients.";
    public static final String MESSAGE_INVALID_JOBSEEKER_DISPLAYED_INDEX = "The jobseeker index provided is invalid.";
    public static final String MESSAGE_INVALID_CLIENT_DISPLAYED_INDEX = "The client index provided is invalid.";
    public static final String MESSAGE_JOBSEEKERS_LISTED_OVERVIEW = "%1$d jobseeker(s) listed!";
    public static final String MESSAGE_CLIENTS_LISTED_OVERVIEW = "%1$d client(s) listed!";
    public static final String MESSAGE_PERSONS_LISTED_OVERVIEW = "%1$d jobseeker(s) and %2$d client(s) listed!";
    public static final String MESSAGE_HIDDEN_JOBSEEKERS_LISTED_OVERVIEW = "%1$d hidden jobseeker(s) listed!";
    public static final String MESSAGE_HIDDEN_CLIENTS_LISTED_OVERVIEW = "%1$d hidden client(s) listed!";
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
                .append("; Remark: ")
                .append(jobseeker.getRemark())
                .append("; Tags: ");
        jobseeker.getTags().forEach(builder::append);
        if (!jobseeker.getHighestQualification().value.isEmpty()) {
            builder.append("; Highest qualification: ").append(jobseeker.getHighestQualification());
        }
        return builder.toString();
    }

    /**
     * Formats the {@code client} for display to the user.
     */
    public static String format(Client client) {
        final StringBuilder builder = new StringBuilder();
        builder.append(client.getName())
                .append("; Phone: ")
                .append(client.getPhone())
                .append("; Email: ")
                .append(client.getEmail())
                .append("; Address: ")
                .append(client.getAddress())
                .append("; Remark: ")
                .append(client.getRemark())
                .append("; Tags: ");
        client.getTags().forEach(builder::append);
        if (!client.getAvailableJobs().isEmpty()) {
            builder.append("; Available jobs: ").append(String.join(", ", client.getSortedJobNames()));
        }
        return builder.toString();
    }

}
