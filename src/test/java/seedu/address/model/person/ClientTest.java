package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.TypicalPersons.ALICE;

import org.junit.jupiter.api.Test;

/** Tests for the client subtype. */
public class ClientTest {
    @Test
    public void constructor_preservesPersonFields() {
        Client client = new Client(ALICE.getName(), ALICE.getPhone(), ALICE.getEmail(),
                ALICE.getAddress(), ALICE.getTags());

        assertEquals(ALICE.getName(), client.getName());
        assertEquals(ALICE.getPhone(), client.getPhone());
        assertEquals(ALICE.getEmail(), client.getEmail());
        assertEquals(ALICE.getAddress(), client.getAddress());
        assertEquals(ALICE.getTags(), client.getTags());
    }
}
