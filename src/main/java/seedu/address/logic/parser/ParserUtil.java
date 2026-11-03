package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_MISSING_REQUIRED_FIELDS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

import seedu.address.commons.core.LogsCenter;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.StringUtil;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.job.Job;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Qualification;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "Index must be a positive integer.";
    private static final Logger logger = LogsCenter.getLogger(ParserUtil.class);

    /**
     * Checks required add-command prefixes in name, phone, email, address order.
     * A present prefix with an empty value is left to the field's value validation.
     *
     * @throws ParseException listing only the missing required fields, if any.
     */
    public static void verifyRequiredAddFields(ArgumentMultimap arguments) throws ParseException {
        requireNonNull(arguments);
        List<String> missing = new ArrayList<>();
        if (arguments.getValue(PREFIX_NAME).isEmpty()) {
            missing.add(PREFIX_NAME + " (name)");
        }
        if (arguments.getValue(PREFIX_PHONE).isEmpty()) {
            missing.add(PREFIX_PHONE + " (phone)");
        }
        if (arguments.getValue(PREFIX_EMAIL).isEmpty()) {
            missing.add(PREFIX_EMAIL + " (email)");
        }
        if (arguments.getValue(PREFIX_ADDRESS).isEmpty()) {
            missing.add(PREFIX_ADDRESS + " (address)");
        }
        if (!missing.isEmpty()) {
            logger.fine("Required add-command fields missing: " + missing);
            throw new ParseException(String.format(MESSAGE_MISSING_REQUIRED_FIELDS, String.join(", ", missing)));
        }
    }

    /**
     * Parses an optional qualification, trimming surrounding whitespace. Empty text clears the qualification.
     */
    public static Qualification parseQualification(String qualification) {
        requireNonNull(qualification);
        return new Qualification(qualification.trim());
    }

    /**
     * Parses job names into a set, trimming whitespace and collapsing duplicates.
     * No values or a single empty value represent no jobs. Empty values mixed with other jobs are invalid.
     *
     * @throws ParseException if a job name is blank, unless it is the sole value used to clear jobs.
     */
    public static Set<Job> parseJobs(Collection<String> jobs) throws ParseException {
        requireNonNull(jobs);
        if (jobs.size() == 1 && jobs.iterator().next().isBlank()) {
            return Set.of();
        }
        Set<Job> parsedJobs = new HashSet<>();
        for (String job : jobs) {
            String trimmedName = job.trim();
            if (!Job.isValidJobName(trimmedName)) {
                throw new ParseException(Job.MESSAGE_CONSTRAINTS);
            }
            parsedJobs.add(new Job(trimmedName));
        }
        return parsedJobs;
    }

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     *
     * @throws ParseException if the specified index is invalid (not a non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses a {@code String name} into a {@code Name}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code name} is invalid.
     */
    public static Name parseName(String name) throws ParseException {
        requireNonNull(name);
        String trimmedName = name.trim();
        if (!Name.isValidName(trimmedName)) {
            throw new ParseException(Name.MESSAGE_CONSTRAINTS);
        }
        return new Name(trimmedName);
    }

    /**
     * Parses a {@code String phone} into a {@code Phone}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code phone} is invalid.
     */
    public static Phone parsePhone(String phone) throws ParseException {
        requireNonNull(phone);
        String trimmedPhone = phone.trim();
        if (!Phone.isValidPhone(trimmedPhone)) {
            throw new ParseException(Phone.MESSAGE_CONSTRAINTS);
        }
        return new Phone(trimmedPhone);
    }

    /**
     * Parses a {@code String address} into an {@code Address}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code address} is invalid.
     */
    public static Address parseAddress(String address) throws ParseException {
        requireNonNull(address);
        String trimmedAddress = address.trim();
        if (!Address.isValidAddress(trimmedAddress)) {
            throw new ParseException(Address.MESSAGE_CONSTRAINTS);
        }
        return new Address(trimmedAddress);
    }

    /**
     * Parses a {@code String email} into an {@code Email}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code email} is invalid.
     */
    public static Email parseEmail(String email) throws ParseException {
        requireNonNull(email);
        String trimmedEmail = email.trim();
        if (!Email.isValidEmail(trimmedEmail)) {
            throw new ParseException(Email.MESSAGE_CONSTRAINTS);
        }
        return new Email(trimmedEmail);
    }

    /**
     * Parses a {@code String tag} into a {@code Tag}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code tag} is invalid.
     */
    public static Tag parseTag(String tag) throws ParseException {
        requireNonNull(tag);
        String trimmedTag = tag.trim();
        if (!Tag.isValidTagName(trimmedTag)) {
            throw new ParseException(Tag.MESSAGE_CONSTRAINTS);
        }
        return new Tag(trimmedTag);
    }

    /**
     * Parses whitespace-separated tags from each {@code t/} value in an add or edit command.
     * Repeated prefixes are supported and duplicate tags are removed.
     *
     * @throws ParseException if a value is empty or any individual tag is invalid.
     */
    public static Set<Tag> parseSpaceSeparatedTags(Collection<String> values) throws ParseException {
        requireNonNull(values);
        Set<Tag> tags = new HashSet<>();
        for (String value : values) {
            for (String tagName : value.trim().split("\\s+")) {
                tags.add(parseTag(tagName));
            }
        }
        return tags;
    }

    /**
     * Parses tags for an edit command, distinguishing absent tags from a request to clear them.
     */
    public static Optional<Set<Tag>> parseTagsForEdit(Collection<String> tags) throws ParseException {
        requireNonNull(tags);
        if (tags.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> tagSet = tags.size() == 1 && tags.contains("") ? Collections.emptySet() : tags;
        return Optional.of(parseSpaceSeparatedTags(tagSet));
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>}.
     */
    public static Set<Tag> parseTags(Collection<String> tags) throws ParseException {
        requireNonNull(tags);
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(parseTag(tagName));
        }
        return tagSet;
    }
}
