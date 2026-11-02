package seedu.address.commons.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertEqualWithSameHashCode;

import org.junit.jupiter.api.Test;

public class GuiSettingsTest {
    @Test
    public void hashCode_equalSettings_sameHashCode() {
        // EP: no window coordinates
        assertEqualWithSameHashCode(new GuiSettings(), new GuiSettings()); // Boundary value

        // EP: with window coordinates
        assertEqualWithSameHashCode(new GuiSettings(1000, 500, 300, 100),
                new GuiSettings(1000, 500, 300, 100));
        assertEqualWithSameHashCode(new GuiSettings(0, 0, 0, 0),
                new GuiSettings(0, 0, 0, 0)); // Boundary value
    }

    @Test
    public void toStringMethod() {
        GuiSettings guiSettings = new GuiSettings();
        String expected = GuiSettings.class.getCanonicalName() + "{windowWidth=" + guiSettings.getWindowWidth()
                + ", windowHeight=" + guiSettings.getWindowHeight() + ", windowCoordinates="
                + guiSettings.getWindowCoordinates() + "}";
        assertEquals(expected, guiSettings.toString());
    }
}
