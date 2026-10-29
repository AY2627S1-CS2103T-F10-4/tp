package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

/** Tests edit tag syntax through parsing and model updates for both contact types. */
public class EditTagsIntegrationTest {
    private final AddressBookParser parser = new AddressBookParser();

    @Test
    public void execute_groupedRepeatedAndMixedTags_replacesTags() throws Exception {
        for (String type : new String[]{"j", "c"}) {
            for (String input : new String[]{"t/java python", "t/java t/python",
                "t/java python t/java", "t/ java   python ", "t/java\tpython"}) {
                Model model = createModel(type);
                parser.parseCommand("edit" + type + " 1 " + input).execute(model);
                assertEquals(Set.of(new Tag("java"), new Tag("python")), getPerson(model, type).getTags());
            }
        }
    }

    @Test
    public void execute_duplicateAndCaseDistinctTags_keepsUniqueCaseSensitiveTags() throws Exception {
        for (String type : new String[]{"j", "c"}) {
            Model model = createModel(type);
            parser.parseCommand("edit" + type + " 1 t/java Java java t/Java").execute(model);
            assertEquals(Set.of(new Tag("java"), new Tag("Java")), getPerson(model, type).getTags());
        }
    }

    @Test
    public void execute_omittedTagsPreservedAndSingleEmptyPrefixClears() throws Exception {
        for (String type : new String[]{"j", "c"}) {
            Model model = createModel(type);
            parser.parseCommand("edit" + type + " 1 r/Updated").execute(model);
            assertEquals(Set.of(new Tag("original")), getPerson(model, type).getTags());
            parser.parseCommand("edit" + type + " 1 t/   ").execute(model);
            assertEquals(Set.of(), getPerson(model, type).getTags());
        }
    }

    @Test
    public void execute_invalidOrMixedEmptyTags_rejectedWithoutChangingContact() throws Exception {
        for (String type : new String[]{"j", "c"}) {
            for (String input : new String[]{"t/java invalid!", "t/invalid! java", "t/java python t/",
                "t/ t/java python", "t/ t/"}) {
                Model model = createModel(type);
                Person original = getPerson(model, type);
                assertThrows(ParseException.class, () -> parser.parseCommand(
                        "edit" + type + " 1 n/Changed " + input).execute(model));
                assertEquals(original, getPerson(model, type));
            }
        }
    }

    private Model createModel(String type) throws Exception {
        Model model = new ModelManager();
        parser.parseCommand("add" + type
                + " n/Alice p/91234567 e/alice@example.com a/Clementi t/original").execute(model);
        return model;
    }

    private Person getPerson(Model model, String type) {
        return type.equals("j") ? model.getAddressBook().getJobseekerList().getFirst()
                : model.getAddressBook().getClientList().getFirst();
    }
}
