package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.model.tag.Tag;

/** Tests the equality contract shared by the four tag and untag commands. */
public class AbstractTagCommandTest {
    private List<AbstractTagCommand> createCommands() {
        Index index = Index.fromOneBased(1);
        Set<Tag> tags = Set.of(new Tag("java"));
        return List.of(new TagClientCommand(index, tags), new TagJobseekerCommand(index, tags),
                new UntagClientCommand(index, tags), new UntagJobseekerCommand(index, tags));
    }

    @Test
    public void equals_sameArguments_distinguishesAllCommandTypes() {
        List<AbstractTagCommand> commands = createCommands();
        List<AbstractTagCommand> equalCommands = createCommands();
        for (int i = 0; i < commands.size(); i++) {
            for (int j = 0; j < equalCommands.size(); j++) {
                if (i == j) {
                    assertEquals(commands.get(i), equalCommands.get(j));
                    assertEquals(commands.get(i).hashCode(), equalCommands.get(j).hashCode());
                } else {
                    assertNotEquals(commands.get(i), equalCommands.get(j));
                }
            }
        }
    }
}
