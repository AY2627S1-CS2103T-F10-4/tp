package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.EditClientCommand.EditClientDescriptor;
import seedu.address.model.person.Client;
import seedu.address.model.person.Remark;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.ClientBuilder;

public class EditClientDescriptorTest {
    @Test
    public void newDescriptor_hasNoEdits() {
        EditClientDescriptor descriptor = new EditClientDescriptor();
        assertFalse(descriptor.isAnyFieldEdited());
        assertTrue(descriptor.getName().isEmpty());
        assertTrue(descriptor.getPhone().isEmpty());
        assertTrue(descriptor.getEmail().isEmpty());
        assertTrue(descriptor.getAddress().isEmpty());
        assertTrue(descriptor.getTags().isEmpty());
        assertTrue(descriptor.getRemark().isEmpty());
    }

    @Test
    public void eachField_countsAsEditAndParticipatesInEqualityAndCopy() {
        Client client = new ClientBuilder().build();
        List<Consumer<EditClientDescriptor>> edits = List.of(
                descriptor -> descriptor.setName(client.getName()),
                descriptor -> descriptor.setPhone(client.getPhone()),
                descriptor -> descriptor.setEmail(client.getEmail()),
                descriptor -> descriptor.setAddress(client.getAddress()),
                descriptor -> descriptor.setTags(Set.of()),
                descriptor -> descriptor.setRemark(new Remark("")));
        for (Consumer<EditClientDescriptor> edit : edits) {
            EditClientDescriptor descriptor = new EditClientDescriptor();
            edit.accept(descriptor);
            assertTrue(descriptor.isAnyFieldEdited());
            assertNotEquals(new EditClientDescriptor(), descriptor);
            assertEquals(descriptor, new EditClientDescriptor(descriptor));
        }
    }

    @Test
    public void tags_defensiveCopyAndUnmodifiableGetter() {
        Set<Tag> tags = new HashSet<>(Set.of(new Tag("priority")));
        EditClientDescriptor descriptor = new EditClientDescriptor();
        descriptor.setTags(tags);
        tags.clear();
        assertEquals(Set.of(new Tag("priority")), descriptor.getTags().orElseThrow());
        assertThrows(UnsupportedOperationException.class, () -> descriptor.getTags().orElseThrow().clear());

        EditClientDescriptor copy = new EditClientDescriptor(descriptor);
        descriptor.setTags(null);
        assertTrue(descriptor.getTags().isEmpty());
        assertEquals(Set.of(new Tag("priority")), copy.getTags().orElseThrow());
    }

    @Test
    public void remark_copyRetainsValueWhenOriginalChanges() {
        EditClientDescriptor descriptor = new EditClientDescriptor();
        descriptor.setRemark(new Remark("Hiring now"));
        EditClientDescriptor copy = new EditClientDescriptor(descriptor);
        descriptor.setRemark(null);

        assertFalse(descriptor.isAnyFieldEdited());
        assertEquals(new Remark("Hiring now"), copy.getRemark().orElseThrow());
        assertNotEquals(descriptor, copy);
        assertEquals(copy, copy);
        assertNotEquals(copy, null);
        assertNotEquals(copy, new EditJobseekerCommand.EditJobseekerDescriptor());
    }
}
