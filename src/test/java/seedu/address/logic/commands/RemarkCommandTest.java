package seedu.address.logic.commands;

import static seedu.address.logic.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.person.Remark;

public class RemarkCommandTest {

    private final Model model = new ModelManager();

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        Remark remark = new Remark("Some remark");

        assertCommandFailure(new RemarkCommand(INDEX_FIRST_PERSON, remark),
                model, MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }
}
