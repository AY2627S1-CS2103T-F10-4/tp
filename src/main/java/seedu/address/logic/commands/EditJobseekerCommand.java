package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Jobseeker;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;

/**
 * Edits the details of an existing jobseeker in the address book.
 */
public class EditJobseekerCommand extends Command {

    public static final String COMMAND_WORD = "editj";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the jobseeker identified "
            + "by the index number used in the displayed jobseeker list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_EMAIL + "EMAIL] "
            + "[" + PREFIX_ADDRESS + "ADDRESS] "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_PHONE + "91234567 "
            + PREFIX_EMAIL + "johndoe@example.com";

    public static final String MESSAGE_EDIT_JOBSEEKER_SUCCESS = "Edited jobseeker: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_JOBSEEKER = "This jobseeker already exists in the address book.";

    private final Index index;
    private final EditJobseekerDescriptor editJobseekerDescriptor;

    /**
     * @param index of the jobseeker in the filtered jobseeker list to edit
     * @param editJobseekerDescriptor details to edit the jobseeker with
     */
    public EditJobseekerCommand(Index index, EditJobseekerDescriptor editJobseekerDescriptor) {
        requireNonNull(index);
        requireNonNull(editJobseekerDescriptor);

        this.index = index;
        this.editJobseekerDescriptor = new EditJobseekerDescriptor(editJobseekerDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Jobseeker> lastShownJobseekerList = model.getFilteredJobseekerList();

        if (index.getZeroBased() >= lastShownJobseekerList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_JOBSEEKER_DISPLAYED_INDEX);
        }

        Jobseeker jobseekerToEdit = lastShownJobseekerList.get(index.getZeroBased());
        Jobseeker editedJobseeker = createEditedJobseeker(jobseekerToEdit, editJobseekerDescriptor);

        if (!jobseekerToEdit.isSamePerson(editedJobseeker) && model.hasJobseeker(editedJobseeker)) {
            throw new CommandException(MESSAGE_DUPLICATE_JOBSEEKER);
        }

        model.setJobseeker(jobseekerToEdit, editedJobseeker);
        model.updateFilteredJobseekerList(jobseeker -> true);
        return new CommandResult(String.format(MESSAGE_EDIT_JOBSEEKER_SUCCESS,
                Messages.format(editedJobseeker)));
    }

    /**
     * Creates and returns a {@code Jobseeker} with the details of {@code jobseekerToEdit}
     * edited with {@code editJobseekerDescriptor}.
     */
    private static Jobseeker createEditedJobseeker(Jobseeker jobseekerToEdit,
            EditJobseekerDescriptor editJobseekerDescriptor) {
        assert jobseekerToEdit != null;

        Name updatedName = editJobseekerDescriptor.getName().orElse(jobseekerToEdit.getName());
        Phone updatedPhone = editJobseekerDescriptor.getPhone().orElse(jobseekerToEdit.getPhone());
        Email updatedEmail = editJobseekerDescriptor.getEmail().orElse(jobseekerToEdit.getEmail());
        Address updatedAddress = editJobseekerDescriptor.getAddress().orElse(jobseekerToEdit.getAddress());
        Set<Tag> updatedTags = editJobseekerDescriptor.getTags().orElse(jobseekerToEdit.getTags());

        return new Jobseeker(updatedName, updatedPhone, updatedEmail, updatedAddress, updatedTags);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditJobseekerCommand otherEditJobseekerCommand)) {
            return false;
        }

        return index.equals(otherEditJobseekerCommand.index)
                && editJobseekerDescriptor.equals(otherEditJobseekerCommand.editJobseekerDescriptor);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("editJobseekerDescriptor", editJobseekerDescriptor)
                .toString();
    }

    /**
     * Stores the details to edit the jobseeker with. Each non-empty field value will replace the
     * corresponding field value of the jobseeker.
     */
    public static class EditJobseekerDescriptor {
        private Name name;
        private Phone phone;
        private Email email;
        private Address address;
        private Set<Tag> tags;

        /** Creates an empty descriptor. */
        public EditJobseekerDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditJobseekerDescriptor(EditJobseekerDescriptor toCopy) {
            setName(toCopy.name);
            setPhone(toCopy.phone);
            setEmail(toCopy.email);
            setAddress(toCopy.address);
            setTags(toCopy.tags);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, phone, email, address, tags);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setPhone(Phone phone) {
            this.phone = phone;
        }

        public Optional<Phone> getPhone() {
            return Optional.ofNullable(phone);
        }

        public void setEmail(Email email) {
            this.email = email;
        }

        public Optional<Email> getEmail() {
            return Optional.ofNullable(email);
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public Optional<Address> getAddress() {
            return Optional.ofNullable(address);
        }

        /**
         * Sets {@code tags} to this object's {@code tags}.
         * A defensive copy of {@code tags} is used internally.
         */
        public void setTags(Set<Tag> tags) {
            this.tags = (tags != null) ? new HashSet<>(tags) : null;
        }

        /**
         * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code tags} is null.
         */
        public Optional<Set<Tag>> getTags() {
            return (tags != null) ? Optional.of(Collections.unmodifiableSet(tags)) : Optional.empty();
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditJobseekerDescriptor otherEditJobseekerDescriptor)) {
                return false;
            }

            return Objects.equals(name, otherEditJobseekerDescriptor.name)
                    && Objects.equals(phone, otherEditJobseekerDescriptor.phone)
                    && Objects.equals(email, otherEditJobseekerDescriptor.email)
                    && Objects.equals(address, otherEditJobseekerDescriptor.address)
                    && Objects.equals(tags, otherEditJobseekerDescriptor.tags);
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("name", name)
                    .add("phone", phone)
                    .add("email", email)
                    .add("address", address)
                    .add("tags", tags)
                    .toString();
        }
    }
}
