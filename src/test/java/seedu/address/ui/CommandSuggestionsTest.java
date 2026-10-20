package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class CommandSuggestionsTest {
    private final CommandSuggestions suggestions = new CommandSuggestions();

    @Test
    public void suggest_prefix_returnsSortedMatches() {
        assertEquals(List.of("listc", "listj"), suggestions.suggest("list"));
        assertEquals(List.of("listc", "listj"), suggestions.suggest("  list"));
        assertEquals(List.of("editj"), suggestions.suggest("edi"));
        assertEquals(List.of("clearc", "clearj"), suggestions.suggest("c"));
    }

    @Test
    public void suggest_blankExactUnknownOrArguments_returnsEmpty() {
        for (String input : List.of("", "  ", "listc", "LIST", "unknown", "list ", "addj n/Alice", "add\t")) {
            assertTrue(suggestions.suggest(input).isEmpty(), input);
        }
    }

    @Test
    public void suggest_largeCatalogue_returnsAllMatchesInOrderAndCopiesSource() {
        List<String> commands = new ArrayList<>(List.of(
                "listg", "listf", "liste", "listd", "listc", "listb", "lista", "help"));
        CommandSuggestions matcher = new CommandSuggestions(commands);
        commands.clear();
        assertEquals(List.of("lista", "listb", "listc", "listd", "liste", "listf", "listg"),
                matcher.suggest("list"));
    }
}
