package seedu.address.testutil;

import java.util.HashSet;
import java.util.Set;

import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Jobseeker;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;
import seedu.address.model.util.SampleDataUtil;

/**
 * A utility class to help with building Jobseeker objects.
 */
public class JobseekerBuilder {

    public static final String DEFAULT_NAME = "Amy Bee";
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_EMAIL = "amy@gmail.com";
    public static final String DEFAULT_ADDRESS = "123, Jurong West Ave 6, #08-111";

    private Name name;
    private Phone phone;
    private Email email;
    private Address address;
    private Set<Tag> tags;

    /**
     * Creates a {@code JobseekerBuilder} with the default details.
     */
    public JobseekerBuilder() {
        name = new Name(DEFAULT_NAME);
        phone = new Phone(DEFAULT_PHONE);
        email = new Email(DEFAULT_EMAIL);
        address = new Address(DEFAULT_ADDRESS);
        tags = new HashSet<>();
    }

    /**
     * Initializes the JobseekerBuilder with the data of {@code jobseekerToCopy}.
     */
    public JobseekerBuilder(Jobseeker jobseekerToCopy) {
        name = jobseekerToCopy.getName();
        phone = jobseekerToCopy.getPhone();
        email = jobseekerToCopy.getEmail();
        address = jobseekerToCopy.getAddress();
        tags = new HashSet<>(jobseekerToCopy.getTags());
    }

    /**
     * Sets the {@code Name} of the {@code Jobseeker} that we are building.
     */
    public JobseekerBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and sets it to the {@code Jobseeker} that we are building.
     */
    public JobseekerBuilder withTags(String ... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code Jobseeker} that we are building.
     */
    public JobseekerBuilder withAddress(String address) {
        this.address = new Address(address);
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code Jobseeker} that we are building.
     */
    public JobseekerBuilder withPhone(String phone) {
        this.phone = new Phone(phone);
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code Jobseeker} that we are building.
     */
    public JobseekerBuilder withEmail(String email) {
        this.email = new Email(email);
        return this;
    }

    public Jobseeker build() {
        return new Jobseeker(name, phone, email, address, tags);
    }
}
