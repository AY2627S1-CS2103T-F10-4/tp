package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.ArrayList;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.person.Client;
import seedu.address.model.person.Jobseeker;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.testutil.EditJobseekerDescriptorBuilder;

/**
 * Contains helper methods for testing commands.
 */
public class CommandTestUtil {

    public static final String VALID_NAME_AMY = "Amy Bee";
    public static final String VALID_NAME_BOB = "Bob Choo";
    public static final String VALID_PHONE_AMY = "11111111";
    public static final String VALID_PHONE_BOB = "22222222";
    public static final String VALID_EMAIL_AMY = "amy@example.com";
    public static final String VALID_EMAIL_BOB = "bob@example.com";
    public static final String VALID_ADDRESS_AMY = "Block 312, Amy Street 1";
    public static final String VALID_ADDRESS_BOB = "Block 123, Bobby Street 3";
    public static final String VALID_TAG_HUSBAND = "husband";
    public static final String VALID_TAG_FRIEND = "friend";

    public static final String VALID_NAME_JACK = "Jack Tan";
    public static final String VALID_NAME_KATE = "Kate Lim";
    public static final String VALID_PHONE_JACK = "81110008";
    public static final String VALID_PHONE_KATE = "81110009";
    public static final String VALID_EMAIL_JACK = "jack@example.com";
    public static final String VALID_EMAIL_KATE = "kate@example.com";
    public static final String VALID_ADDRESS_JACK = "8 Tampines Avenue";
    public static final String VALID_ADDRESS_KATE = "9 Bedok Road";
    public static final String VALID_TAG_EMPLOYER = "employer";
    public static final String VALID_TAG_PARTNER = "partner";

    public static final String NAME_DESC_AMY = " " + PREFIX_NAME + VALID_NAME_AMY;
    public static final String NAME_DESC_BOB = " " + PREFIX_NAME + VALID_NAME_BOB;
    public static final String PHONE_DESC_AMY = " " + PREFIX_PHONE + VALID_PHONE_AMY;
    public static final String PHONE_DESC_BOB = " " + PREFIX_PHONE + VALID_PHONE_BOB;
    public static final String EMAIL_DESC_AMY = " " + PREFIX_EMAIL + VALID_EMAIL_AMY;
    public static final String EMAIL_DESC_BOB = " " + PREFIX_EMAIL + VALID_EMAIL_BOB;
    public static final String ADDRESS_DESC_AMY = " " + PREFIX_ADDRESS + VALID_ADDRESS_AMY;
    public static final String ADDRESS_DESC_BOB = " " + PREFIX_ADDRESS + VALID_ADDRESS_BOB;
    public static final String TAG_DESC_FRIEND = " " + PREFIX_TAG + VALID_TAG_FRIEND;
    public static final String TAG_DESC_HUSBAND = " " + PREFIX_TAG + VALID_TAG_HUSBAND;

    public static final String NAME_DESC_JACK = " " + PREFIX_NAME + VALID_NAME_JACK;
    public static final String NAME_DESC_KATE = " " + PREFIX_NAME + VALID_NAME_KATE;
    public static final String PHONE_DESC_JACK = " " + PREFIX_PHONE + VALID_PHONE_JACK;
    public static final String PHONE_DESC_KATE = " " + PREFIX_PHONE + VALID_PHONE_KATE;
    public static final String EMAIL_DESC_JACK = " " + PREFIX_EMAIL + VALID_EMAIL_JACK;
    public static final String EMAIL_DESC_KATE = " " + PREFIX_EMAIL + VALID_EMAIL_KATE;
    public static final String ADDRESS_DESC_JACK = " " + PREFIX_ADDRESS + VALID_ADDRESS_JACK;
    public static final String ADDRESS_DESC_KATE = " " + PREFIX_ADDRESS + VALID_ADDRESS_KATE;
    public static final String TAG_DESC_EMPLOYER = " " + PREFIX_TAG + VALID_TAG_EMPLOYER;
    public static final String TAG_DESC_PARTNER = " " + PREFIX_TAG + VALID_TAG_PARTNER;

    public static final String INVALID_NAME_DESC = " " + PREFIX_NAME + "James&"; // '&' not allowed in names
    public static final String INVALID_PHONE_DESC = " " + PREFIX_PHONE + "911a"; // 'a' not allowed in phones
    public static final String INVALID_EMAIL_DESC = " " + PREFIX_EMAIL + "bob!yahoo"; // missing '@' symbol
    public static final String INVALID_ADDRESS_DESC = " " + PREFIX_ADDRESS; // empty string not allowed for addresses
    public static final String INVALID_TAG_DESC = " " + PREFIX_TAG + "hubby*"; // '*' not allowed in tags

    public static final String PREAMBLE_WHITESPACE = "\t  \r  \n";
    public static final String PREAMBLE_NON_EMPTY = "NonEmptyPreamble";

    public static final EditJobseekerCommand.EditJobseekerDescriptor DESC_AMY;
    public static final EditJobseekerCommand.EditJobseekerDescriptor DESC_BOB;

    static {
        DESC_AMY = new EditJobseekerDescriptorBuilder().withName(VALID_NAME_AMY)
                .withPhone(VALID_PHONE_AMY).withEmail(VALID_EMAIL_AMY).withAddress(VALID_ADDRESS_AMY)
                .withTags(VALID_TAG_FRIEND).build();
        DESC_BOB = new EditJobseekerDescriptorBuilder().withName(VALID_NAME_BOB)
                .withPhone(VALID_PHONE_BOB).withEmail(VALID_EMAIL_BOB).withAddress(VALID_ADDRESS_BOB)
                .withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND).build();
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - the returned {@link CommandResult} matches {@code expectedCommandResult} <br>
     * - the {@code actualModel} matches {@code expectedModel} according to its {@code equals} method
     * <p>Filtered list contents must be asserted separately if they are not included in model equality.
     */
    public static void assertCommandSuccess(Command command, Model actualModel, CommandResult expectedCommandResult,
            Model expectedModel) {
        try {
            CommandResult result = command.execute(actualModel);
            assertEquals(expectedCommandResult, result);
            assertEquals(expectedModel, actualModel);
        } catch (CommandException ce) {
            throw new AssertionError("Execution of command should not fail.", ce);
        }
    }

    /**
     * Convenience wrapper to {@link #assertCommandSuccess(Command, Model, CommandResult, Model)}
     * that takes a string {@code expectedMessage}.
     */
    public static void assertCommandSuccess(Command command, Model actualModel, String expectedMessage,
            Model expectedModel) {
        CommandResult expectedCommandResult = new CommandResult(expectedMessage);
        assertCommandSuccess(command, actualModel, expectedCommandResult, expectedModel);
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - a {@code CommandException} is thrown <br>
     * - the CommandException message matches {@code expectedMessage} <br>
     * - the address book and the contents of both filtered lists in {@code actualModel} remain unchanged
     * <p>Compares visible list contents, not the filter predicates or calls made to update them.
     *
     * @throws AssertionError if the expected exception or message does not match, or the checked state changes
     */
    public static void assertCommandFailure(Command command, Model actualModel, String expectedMessage) {
        // we are unable to defensively copy the model for comparison later, so we can
        // only do so by copying its components.
        AddressBook expectedAddressBook = new AddressBook(actualModel.getAddressBook());
        List<Jobseeker> expectedFilteredList = new ArrayList<>(actualModel.getFilteredJobseekerList());
        List<Client> expectedFilteredClients = new ArrayList<>(actualModel.getFilteredClientList());

        assertThrows(CommandException.class, expectedMessage, () -> command.execute(actualModel));
        assertEquals(expectedAddressBook, actualModel.getAddressBook());
        assertEquals(expectedFilteredList, actualModel.getFilteredJobseekerList());
        assertEquals(expectedFilteredClients, actualModel.getFilteredClientList());
    }
    /**
     * Filters jobseekers using the first name token of the jobseeker at {@code targetIndex}
     * in the current filtered list.
     * Asserts that the resulting list contains exactly one jobseeker.
     */
    public static void showPersonAtIndex(Model model, Index targetIndex) {
        assertTrue(targetIndex.getZeroBased() < model.getFilteredJobseekerList().size());

        Jobseeker jobseeker = model.getFilteredJobseekerList().get(targetIndex.getZeroBased());
        final String[] splitName = jobseeker.getName().fullName.split("\\s+");
        model.updateFilteredJobseekerList(new NameContainsKeywordsPredicate(List.of(splitName[0])));

        assertEquals(1, model.getFilteredJobseekerList().size());
    }

}
