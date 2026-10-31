package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.storage.JsonAdaptedJobseeker.MISSING_FIELD_MESSAGE_FORMAT;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.BENSON;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.commons.util.JsonUtil;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Jobseeker;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.JobseekerBuilder;

public class JsonAdaptedJobseekerTest {
    private static final String INVALID_NAME = "R/chel";
    private static final String INVALID_PHONE = "+651234";
    private static final String INVALID_ADDRESS = " ";
    private static final String INVALID_EMAIL = "example.com";
    private static final String INVALID_TAG = "#friend";

    private static final String VALID_NAME = BENSON.getName().toString();
    private static final String VALID_PHONE = BENSON.getPhone().toString();
    private static final String VALID_EMAIL = BENSON.getEmail().toString();
    private static final String VALID_ADDRESS = BENSON.getAddress().toString();
    private static final List<JsonAdaptedTag> VALID_TAGS = BENSON.getTags().stream()
            .map(JsonAdaptedTag::new)
            .collect(Collectors.toList());

    @Test
    public void toModelType_validJobseekerDetails_returnsJobseeker() throws Exception {
        JsonAdaptedJobseeker jobseeker = new JsonAdaptedJobseeker(BENSON);
        assertEquals(BENSON, jobseeker.toModelType());
    }

    @Test
    public void toModelType_invalidName_throwsIllegalValueException() {
        JsonAdaptedJobseeker jobseeker =
                new JsonAdaptedJobseeker(INVALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS, VALID_TAGS);
        String expectedMessage = Name.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, jobseeker::toModelType);
    }

    @Test
    public void toModelType_nullName_throwsIllegalValueException() {
        JsonAdaptedJobseeker jobseeker = new JsonAdaptedJobseeker(null, VALID_PHONE, VALID_EMAIL,
                VALID_ADDRESS, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, jobseeker::toModelType);
    }

    @Test
    public void toModelType_invalidPhone_throwsIllegalValueException() {
        JsonAdaptedJobseeker jobseeker =
                new JsonAdaptedJobseeker(VALID_NAME, INVALID_PHONE, VALID_EMAIL, VALID_ADDRESS, VALID_TAGS);
        String expectedMessage = Phone.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, jobseeker::toModelType);
    }

    @Test
    public void toModelType_nullPhone_throwsIllegalValueException() {
        JsonAdaptedJobseeker jobseeker = new JsonAdaptedJobseeker(VALID_NAME, null, VALID_EMAIL,
                VALID_ADDRESS, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, jobseeker::toModelType);
    }

    @Test
    public void toModelType_invalidEmail_throwsIllegalValueException() {
        JsonAdaptedJobseeker jobseeker =
                new JsonAdaptedJobseeker(VALID_NAME, VALID_PHONE, INVALID_EMAIL, VALID_ADDRESS, VALID_TAGS);
        String expectedMessage = Email.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, jobseeker::toModelType);
    }

    @Test
    public void toModelType_nullEmail_throwsIllegalValueException() {
        JsonAdaptedJobseeker jobseeker = new JsonAdaptedJobseeker(VALID_NAME, VALID_PHONE, null,
                VALID_ADDRESS, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, jobseeker::toModelType);
    }

    @Test
    public void toModelType_invalidAddress_throwsIllegalValueException() {
        JsonAdaptedJobseeker jobseeker =
                new JsonAdaptedJobseeker(VALID_NAME, VALID_PHONE, VALID_EMAIL, INVALID_ADDRESS, VALID_TAGS);
        String expectedMessage = Address.MESSAGE_CONSTRAINTS;
        assertThrows(IllegalValueException.class, expectedMessage, jobseeker::toModelType);
    }

    @Test
    public void toModelType_nullAddress_throwsIllegalValueException() {
        JsonAdaptedJobseeker jobseeker = new JsonAdaptedJobseeker(VALID_NAME, VALID_PHONE, VALID_EMAIL,
                null, VALID_TAGS);
        String expectedMessage = String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName());
        assertThrows(IllegalValueException.class, expectedMessage, jobseeker::toModelType);
    }

    @Test
    public void toModelType_invalidTags_throwsIllegalValueException() {
        List<JsonAdaptedTag> invalidTags = new ArrayList<>(VALID_TAGS);
        invalidTags.add(new JsonAdaptedTag(INVALID_TAG));
        JsonAdaptedJobseeker jobseeker =
                new JsonAdaptedJobseeker(VALID_NAME, VALID_PHONE, VALID_EMAIL, VALID_ADDRESS, invalidTags);
        assertThrows(IllegalValueException.class, jobseeker::toModelType);
    }

    /**
     * Goes through real JSON text because a JSON null in the list arrives as a null entry, rather than as
     * a tag with a null name. It must be rejected as invalid data instead of causing a crash at startup.
     */
    @Test
    public void toModelType_nullTagEntry_throwsIllegalValueException() throws Exception {
        // EP: tag list containing a null entry
        assertNullTagEntryRejected("[null]"); // Boundary value
        assertNullTagEntryRejected("[\"friends\", null]");
    }

    private void assertNullTagEntryRejected(String tagsJson) throws Exception {
        String json = "{\"name\": \"" + VALID_NAME + "\", \"phone\": \"" + VALID_PHONE
                + "\", \"email\": \"" + VALID_EMAIL + "\", \"address\": \"" + VALID_ADDRESS
                + "\", \"tags\": " + tagsJson + "}";
        JsonAdaptedJobseeker jobseeker = JsonUtil.fromJsonString(json, JsonAdaptedJobseeker.class);
        assertThrows(IllegalValueException.class, Tag.MESSAGE_CONSTRAINTS, jobseeker::toModelType);
    }

    /**
     * Goes through real JSON text, not just the adapter object, and checks the key name, because the
     * save file format promises a "hidden" key, which Jackson would otherwise name after the field.
     */
    @Test
    public void toModelType_hiddenJobseekerThroughJson_staysHidden() throws Exception {
        Jobseeker hiddenJobseeker = new JobseekerBuilder(BENSON).withHidden(true).build();
        String json = JsonUtil.toJsonString(new JsonAdaptedJobseeker(hiddenJobseeker));
        assertTrue(json.contains("\"hidden\""));
        JsonAdaptedJobseeker readBack = JsonUtil.fromJsonString(json, JsonAdaptedJobseeker.class);
        assertEquals(hiddenJobseeker, readBack.toModelType());
    }

    /**
     * Save files written before hiding existed have no "hidden" field; they must still load,
     * with everyone unhidden.
     */
    @Test
    public void toModelType_missingHiddenField_unhidden() throws Exception {
        String json = "{\"name\": \"" + VALID_NAME + "\", \"phone\": \"" + VALID_PHONE
                + "\", \"email\": \"" + VALID_EMAIL + "\", \"address\": \"" + VALID_ADDRESS
                + "\", \"tags\": []}";
        JsonAdaptedJobseeker jobseeker = JsonUtil.fromJsonString(json, JsonAdaptedJobseeker.class);
        assertFalse(jobseeker.toModelType().isHidden());
    }
}
