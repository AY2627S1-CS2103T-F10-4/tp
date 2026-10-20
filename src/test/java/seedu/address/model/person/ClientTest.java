package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.TypicalPersons.ALICE;

import org.junit.jupiter.api.Test;

/** Tests for the client subtype. */
public class ClientTest {
    @Test
    public void constructor_preservesPersonFields() {
        Client client = new Client(ALICE.getName(), ALICE.getPhone(), ALICE.getEmail(),
                ALICE.getAddress(), ALICE.getTags(), false);

        assertEquals(ALICE.getName(), client.getName());
        assertEquals(ALICE.getPhone(), client.getPhone());
        assertEquals(ALICE.getEmail(), client.getEmail());
        assertEquals(ALICE.getAddress(), client.getAddress());
        assertEquals(ALICE.getTags(), client.getTags());
        assertEquals(new Remark(""), client.getRemark());
        assertFalse(client.isHidden());
    }

    /**
     * Checks both constructors, so that neither can ignore {@code isHidden} and hard-code a status.
     */
    @Test
    public void constructor_withHiddenStatus_keepsHiddenStatus() {
        assertTrue(new Client(ALICE.getName(), ALICE.getPhone(), ALICE.getEmail(),
                ALICE.getAddress(), ALICE.getTags(), true).isHidden());
        assertTrue(new Client(ALICE.getName(), ALICE.getPhone(), ALICE.getEmail(),
                ALICE.getAddress(), ALICE.getTags(), ALICE.getRemark(), true).isHidden());
    }
}
