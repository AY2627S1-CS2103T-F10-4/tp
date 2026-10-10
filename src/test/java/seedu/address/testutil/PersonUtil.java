package seedu.address.testutil;

import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Set;

import seedu.address.logic.commands.AddJobseekerCommand;
import seedu.address.logic.commands.EditJobseekerCommand.EditJobseekerDescriptor;
import seedu.address.model.person.Jobseeker;
import seedu.address.model.tag.Tag;

/**
 * A utility class for jobseekers.
 */
public class PersonUtil {

    /**
     * Returns an add command string for adding the {@code jobseeker}.
     */
    public static String getAddJobseekerCommand(Jobseeker jobseeker) {
        return AddJobseekerCommand.COMMAND_WORD + " " + getJobseekerDetails(jobseeker);
    }

    /**
     * Returns the part of command string for the given {@code jobseeker}'s details.
     */
    public static String getJobseekerDetails(Jobseeker jobseeker) {
        StringBuilder sb = new StringBuilder();
        sb.append(PREFIX_NAME + jobseeker.getName().fullName + " ");
        sb.append(PREFIX_PHONE + jobseeker.getPhone().value + " ");
        sb.append(PREFIX_EMAIL + jobseeker.getEmail().value + " ");
        sb.append(PREFIX_ADDRESS + jobseeker.getAddress().value + " ");
        jobseeker.getTags().stream().forEach(
            s -> sb.append(PREFIX_TAG + s.tagName + " ")
        );
        return sb.toString();
    }

    /**
     * Returns the part of command string for the given {@code EditJobseekerDescriptor}'s details.
     */
    public static String getEditJobseekerDescriptorDetails(EditJobseekerDescriptor descriptor) {
        StringBuilder sb = new StringBuilder();
        descriptor.getName().ifPresent(name -> sb.append(PREFIX_NAME).append(name.fullName).append(" "));
        descriptor.getPhone().ifPresent(phone -> sb.append(PREFIX_PHONE).append(phone.value).append(" "));
        descriptor.getEmail().ifPresent(email -> sb.append(PREFIX_EMAIL).append(email.value).append(" "));
        descriptor.getAddress().ifPresent(address -> sb.append(PREFIX_ADDRESS).append(address.value).append(" "));
        if (descriptor.getTags().isPresent()) {
            Set<Tag> tags = descriptor.getTags().get();
            if (tags.isEmpty()) {
                sb.append(PREFIX_TAG);
            } else {
                tags.forEach(s -> sb.append(PREFIX_TAG).append(s.tagName).append(" "));
            }
        }
        return sb.toString();
    }
}
