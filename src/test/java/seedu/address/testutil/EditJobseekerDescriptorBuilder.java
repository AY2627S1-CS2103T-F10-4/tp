package seedu.address.testutil;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.commands.EditJobseekerCommand.EditJobseekerDescriptor;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Jobseeker;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;

/**
 * A utility class to help with building EditJobseekerDescriptor objects.
 */
public class EditJobseekerDescriptorBuilder {

    private EditJobseekerDescriptor descriptor;

    public EditJobseekerDescriptorBuilder() {
        descriptor = new EditJobseekerDescriptor();
    }

    public EditJobseekerDescriptorBuilder(EditJobseekerDescriptor descriptor) {
        this.descriptor = new EditJobseekerDescriptor(descriptor);
    }

    /**
     * Returns an {@code EditJobseekerDescriptor} with fields containing {@code jobseeker}'s details.
     */
    public EditJobseekerDescriptorBuilder(Jobseeker jobseeker) {
        descriptor = new EditJobseekerDescriptor();
        descriptor.setName(jobseeker.getName());
        descriptor.setPhone(jobseeker.getPhone());
        descriptor.setEmail(jobseeker.getEmail());
        descriptor.setAddress(jobseeker.getAddress());
        descriptor.setTags(jobseeker.getTags());
    }

    /**
     * Sets the {@code Name} of the {@code EditJobseekerDescriptor} that we are building.
     */
    public EditJobseekerDescriptorBuilder withName(String name) {
        descriptor.setName(new Name(name));
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code EditJobseekerDescriptor} that we are building.
     */
    public EditJobseekerDescriptorBuilder withPhone(String phone) {
        descriptor.setPhone(new Phone(phone));
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code EditJobseekerDescriptor} that we are building.
     */
    public EditJobseekerDescriptorBuilder withEmail(String email) {
        descriptor.setEmail(new Email(email));
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code EditJobseekerDescriptor} that we are building.
     */
    public EditJobseekerDescriptorBuilder withAddress(String address) {
        descriptor.setAddress(new Address(address));
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and sets it to the {@code EditJobseekerDescriptor}
     * that we are building.
     */
    public EditJobseekerDescriptorBuilder withTags(String... tags) {
        Set<Tag> tagSet = Stream.of(tags).map(Tag::new).collect(Collectors.toSet());
        descriptor.setTags(tagSet);
        return this;
    }

    public EditJobseekerDescriptor build() {
        return descriptor;
    }
}
