package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.parser.AddressBookParser;
import seedu.address.logic.parser.CliSyntax;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.person.Jobseeker;
import seedu.address.model.person.Remark;
import seedu.address.testutil.JobseekerBuilder;

/**
 * Exercises remark input through parsing and command execution.
 */
public class RemarkIntegrationTest {
    private static final String ADD_DETAILS = " n/Alice p/91234567 e/alice@example.com a/Main Street";

    private final AddressBookParser parser = new AddressBookParser();
    private final Model model = new ModelManager();

    @Test
    public void add_optionalRemark_success() throws Exception {
        parser.parseCommand("addj" + ADD_DETAILS + " r/Available immediately").execute(model);
        assertEquals(new Remark("Available immediately"), model.getFilteredJobseekerList().get(0).getRemark());

        Model withoutRemark = new ModelManager();
        parser.parseCommand("addj" + ADD_DETAILS).execute(withoutRemark);
        assertEquals(new Remark(""), withoutRemark.getFilteredJobseekerList().get(0).getRemark());

        Model emptyRemark = new ModelManager();
        parser.parseCommand("addj" + ADD_DETAILS + " r/").execute(emptyRemark);
        assertEquals(new Remark(""), emptyRemark.getFilteredJobseekerList().get(0).getRemark());
    }

    @Test
    public void edit_otherFields_preservesRemark() throws Exception {
        Jobseeker original = new JobseekerBuilder().withRemark("Original remark").build();
        model.addJobseeker(original);

        parser.parseCommand("editj 1 p/98765432").execute(model);
        assertEquals(original.getRemark(), model.getFilteredJobseekerList().get(0).getRemark());

    }

    @Test
    public void parse_duplicateRemarkPrefixes_rejected() {
        String expected = Messages.getErrorMessageForDuplicatePrefixes(CliSyntax.PREFIX_REMARK);
        assertThrows(ParseException.class, expected, () ->
                parser.parseCommand("addj" + ADD_DETAILS + " r/First r/Second"));
    }
}
