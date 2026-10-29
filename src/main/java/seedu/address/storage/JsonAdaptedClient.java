package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.job.Job;
import seedu.address.model.person.Address;
import seedu.address.model.person.Client;
import seedu.address.model.person.Email;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Remark;
import seedu.address.model.tag.Tag;

/**
 * Jackson-friendly version of {@link Client}.
 */
class JsonAdaptedClient {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Client's %s field is missing!";

    private final String name;
    private final String phone;
    private final String email;
    private final String address;
    private final String remark;
    private final List<String> availableJobs;
    private final List<JsonAdaptedTag> tags = new ArrayList<>();

    // Jackson saves fields under their own names (see JsonUtil), so without this annotation the save
    // file would use the key "isHidden" instead of "hidden", the key the constructor below reads.
    @JsonProperty("hidden")
    private final boolean isHidden;

    /**
     * Constructs a {@code JsonAdaptedClient} with the given client details.
     */
    public JsonAdaptedClient(String name, String phone, String email, String address,
            List<JsonAdaptedTag> tags) {
        this(name, phone, email, address, tags, null, false);
    }

    /**
     * Constructs an adapted client with an optional remark and hidden status.
     * A missing {@code hidden} field (e.g. in a save file written before hiding existed) is read as
     * unhidden, so older save files still load.
     */
    public JsonAdaptedClient(String name, String phone, String email, String address,
            List<JsonAdaptedTag> tags, String remark, Boolean isHidden) {
        this(name, phone, email, address, tags, remark, isHidden, null);
    }

    /**
     * Constructs a {@code JsonAdaptedClient} from the given client details。
     * Missing tags and available jobs default to empty lists, a missing remark to an empty string,
     * and a missing hidden status to false.
     */
    @JsonCreator
    public JsonAdaptedClient(@JsonProperty("name") String name, @JsonProperty("phone") String phone,
            @JsonProperty("email") String email, @JsonProperty("address") String address,
            @JsonProperty("tags") List<JsonAdaptedTag> tags, @JsonProperty("remark") String remark,
            @JsonProperty("hidden") Boolean isHidden,
            @JsonProperty("availableJobs") List<String> availableJobs) {
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.remark = remark == null ? "" : remark;
        this.isHidden = Boolean.TRUE.equals(isHidden);
        if (tags != null) {
            this.tags.addAll(tags);
        }
        this.availableJobs = availableJobs == null ? new ArrayList<>() : new ArrayList<>(availableJobs);
    }

    /**
     * Converts a given {@code Client} into this class for Jackson use.
     */
    public JsonAdaptedClient(Client source) {
        name = source.getName().fullName;
        phone = source.getPhone().value;
        email = source.getEmail().value;
        address = source.getAddress().value;
        remark = source.getRemark().value;
        isHidden = source.isHidden();
        availableJobs = source.getSortedJobNames();
        tags.addAll(source.getTags().stream()
                .map(JsonAdaptedTag::new)
                .collect(Collectors.toList()));
    }

    /**
     * Converts this Jackson-friendly adapted client into the model's {@code Client} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted client.
     */
    public Client toModelType() throws IllegalValueException {
        final List<Tag> clientTags = new ArrayList<>();
        for (JsonAdaptedTag tag : tags) {
            clientTags.add(tag.toModelType());
        }

        Name modelName = parseName();
        Phone modelPhone = parsePhone();
        Email modelEmail = parseEmail();
        Address modelAddress = parseAddress();
        Set<Tag> modelTags = new HashSet<>(clientTags);
        return new Client(modelName, modelPhone, modelEmail, modelAddress, modelTags, new Remark(remark),
                parseJobs(), isHidden);
    }

    private Set<Job> parseJobs() throws IllegalValueException {
        Set<Job> jobs = new HashSet<>();
        for (String jobName : availableJobs) {
            if (jobName == null || !Job.isValidJobName(jobName)) {
                throw new IllegalValueException(Job.MESSAGE_CONSTRAINTS);
            }
            jobs.add(new Job(jobName));
        }
        return jobs;
    }

    private Name parseName() throws IllegalValueException {
        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        return new Name(name);
    }

    private Phone parsePhone() throws IllegalValueException {
        if (phone == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName()));
        }
        if (!Phone.isValidPhone(phone)) {
            throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
        }
        return new Phone(phone);
    }

    private Email parseEmail() throws IllegalValueException {
        if (email == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName()));
        }
        if (!Email.isValidEmail(email)) {
            throw new IllegalValueException(Email.MESSAGE_CONSTRAINTS);
        }
        return new Email(email);
    }

    private Address parseAddress() throws IllegalValueException {
        if (address == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Address.class.getSimpleName()));
        }
        if (!Address.isValidAddress(address)) {
            throw new IllegalValueException(Address.MESSAGE_CONSTRAINTS);
        }
        return new Address(address);
    }
}
