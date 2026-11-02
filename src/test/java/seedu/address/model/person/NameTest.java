package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class NameTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Name(null));
    }

    @Test
    public void constructor_invalidName_throwsIllegalArgumentException() {
        String invalidName = "";
        assertThrows(IllegalArgumentException.class, () -> new Name(invalidName));
    }

    @Test
    public void isValidName() {
        // EP: null
        assertThrows(NullPointerException.class, () -> Name.isValidName(null));

        // EP: blank
        assertFalse(Name.isValidName("")); // Boundary value
        assertFalse(Name.isValidName(" ")); // Boundary value

        // EP: leading whitespace
        assertFalse(Name.isValidName(" peter"));

        // EP: '/' at either end of the name
        assertTrue(Name.isValidName("/")); // Boundary value
        assertTrue(Name.isValidName("/peter")); // Boundary value
        assertTrue(Name.isValidName("peter/")); // Boundary value

        // EP: '/' next to whitespace
        assertTrue(Name.isValidName("ABC / XYZ Holdings"));

        // EP: '/' between two non-whitespace characters
        assertTrue(Name.isValidName("a/b")); // Boundary value
        assertTrue(Name.isValidName("peter/jack"));
        assertTrue(Name.isValidName("Raj s/o Kumar"));
        assertTrue(Name.isValidName("Priya D/O Kumar"));
        assertTrue(Name.isValidName("Muthu A/L Raju"));
        assertTrue(Name.isValidName("Raj s/o Kumar s/o Bala")); // more than one '/'

        // EP: letters and digits only
        assertTrue(Name.isValidName("peter jack")); // alphabets only
        assertTrue(Name.isValidName("12345")); // numbers only
        assertTrue(Name.isValidName("peter the 2nd")); // alphanumeric characters
        assertTrue(Name.isValidName("Capital Tan")); // with capital letters
        assertTrue(Name.isValidName("David Roger Jackson Ray Jr 2nd")); // long names

        // EP: special and non-ASCII characters
        assertTrue(Name.isValidName("^")); // Boundary value
        assertTrue(Name.isValidName("Hershey's")); // apostrophe
        assertTrue(Name.isValidName("McDonald's Pte. Ltd.")); // apostrophe and full stops
        assertTrue(Name.isValidName("X Æ A-Xii")); // non-ASCII letter and hyphen
        assertTrue(Name.isValidName("Zoë Núñez")); // accented letters
        assertTrue(Name.isValidName("张伟")); // non-Latin script
    }

    @Test
    public void equals() {
        Name name = new Name("Valid Name");

        // same values -> returns true
        assertTrue(name.equals(new Name("Valid Name")));

        // same object -> returns true
        assertTrue(name.equals(name));

        // null -> returns false
        assertFalse(name.equals(null));

        // different types -> returns false
        assertFalse(name.equals(5.0f));

        // different values -> returns false
        assertFalse(name.equals(new Name("Other Valid Name")));
    }
}
