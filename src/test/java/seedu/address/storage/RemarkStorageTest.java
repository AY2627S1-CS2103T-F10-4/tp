package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import seedu.address.commons.util.JsonUtil;
import seedu.address.model.person.Client;
import seedu.address.model.person.Jobseeker;
import seedu.address.model.person.Remark;
import seedu.address.testutil.ClientBuilder;
import seedu.address.testutil.JobseekerBuilder;

public class RemarkStorageTest {
    private static final String LEGACY_JSON = "{\"name\":\"Alice\",\"phone\":\"91234567\","
            + "\"email\":\"alice@example.com\",\"address\":\"Main Street\",\"tags\":[]}";

    @Test
    public void jsonRoundTrip_preservesRemarksForBothPersonTypes() throws Exception {
        String text = "Prefers email; available \"soon\"\nFollow up next week";
        Jobseeker jobseeker = new JobseekerBuilder().withRemark(text).build();
        String jobseekerJson = JsonUtil.toJsonString(new JsonAdaptedJobseeker(jobseeker));
        assertEquals(jobseeker, JsonUtil.fromJsonString(jobseekerJson, JsonAdaptedJobseeker.class).toModelType());

        Client client = new ClientBuilder().withRemark(text).build();
        String clientJson = JsonUtil.toJsonString(new JsonAdaptedClient(client));
        assertEquals(client, JsonUtil.fromJsonString(clientJson, JsonAdaptedClient.class).toModelType());
    }

    @Test
    public void legacyJson_missingRemark_defaultsToEmpty() throws Exception {
        assertEquals(new Remark(""),
                JsonUtil.fromJsonString(LEGACY_JSON, JsonAdaptedJobseeker.class).toModelType().getRemark());
        assertEquals(new Remark(""),
                JsonUtil.fromJsonString(LEGACY_JSON, JsonAdaptedClient.class).toModelType().getRemark());
    }

    @Test
    public void json_nullRemark_defaultsToEmpty() throws Exception {
        String json = LEGACY_JSON.replace("\"tags\":[]", "\"tags\":[],\"remark\":null");
        assertEquals(new Remark(""),
                JsonUtil.fromJsonString(json, JsonAdaptedJobseeker.class).toModelType().getRemark());
        assertEquals(new Remark(""),
                JsonUtil.fromJsonString(json, JsonAdaptedClient.class).toModelType().getRemark());
    }
}
