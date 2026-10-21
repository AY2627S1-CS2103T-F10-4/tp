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
        assertEquals(List.of("listc", "listhc", "listhj", "listj"), suggestions.suggest("list"));
        assertEquals(List.of("listc", "listhc", "listhj", "listj"), suggestions.suggest("  list"));
        assertEquals(List.of("editj"), suggestions.suggest("edi"));
        assertEquals(List.of("clearc", "clearj"), suggestions.suggest("c"));
    }

    /**
     * Hidden commands are available through both general and hidden-specific prefixes.
     */
    @Test
    public void suggest_hiddenCommandPrefixes_returnsSortedMatches() {
        assertEquals(List.of("listhc", "listhj"), suggestions.suggest("listh"));
        assertEquals(List.of("findc", "findhc", "findhj", "findj"), suggestions.suggest("find"));
        assertEquals(List.of("findhc", "findhj"), suggestions.suggest("  findh"));
    }

    @Test
    public void suggest_completeHiddenCommandsOrArguments_returnsEmpty() {
        for (String command : List.of("listhc", "listhj", "findhc", "findhj")) {
            assertTrue(suggestions.suggest(command).isEmpty(), command);
            assertTrue(suggestions.suggest(command + " Alice").isEmpty(), command);
        }
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
