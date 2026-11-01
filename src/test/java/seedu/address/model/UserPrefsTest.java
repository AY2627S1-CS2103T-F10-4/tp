package seedu.address.model;

import static seedu.address.testutil.Assert.assertEqualWithSameHashCode;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.GuiSettings;

public class UserPrefsTest {

    @Test
    public void setGuiSettings_nullGuiSettings_throwsNullPointerException() {
        UserPrefs userPref = new UserPrefs();
        assertThrows(NullPointerException.class, () -> userPref.setGuiSettings(null));
    }

    @Test
    public void hashCode_equalPrefs_sameHashCode() {
        // EP: default settings
        assertEqualWithSameHashCode(new UserPrefs(), new UserPrefs());

        // EP: custom settings
        UserPrefs customPrefs = new UserPrefs();
        customPrefs.setGuiSettings(new GuiSettings(1000, 500, 300, 100));
        assertEqualWithSameHashCode(customPrefs, new UserPrefs(customPrefs));
    }

}
