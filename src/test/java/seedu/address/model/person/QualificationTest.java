package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class QualificationTest {

    @Test
    public void constructor_optionalText_acceptsEmptyAndQualifications() {
        assertEquals("", new Qualification("").value);
        assertEquals("Bachelor's Degree (Computing)", new Qualification("Bachelor's Degree (Computing)").toString());
        assertThrows(NullPointerException.class, () -> new Qualification(null));
    }

    @Test
    public void equals_comparesText() {
        Qualification qualification = new Qualification("Diploma");
        Qualification copy = new Qualification("Diploma");
        assertEquals(qualification, copy);
        assertEquals(qualification.hashCode(), copy.hashCode());
        assertNotEquals(qualification, new Qualification("Degree"));
        assertNotEquals(qualification, null);
        assertNotEquals(qualification, "Diploma");
    }
}
