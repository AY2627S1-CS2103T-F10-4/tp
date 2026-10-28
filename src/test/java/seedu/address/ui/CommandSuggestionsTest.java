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
        assertEquals(List.of("list", "listc", "listhc", "listhj", "listj"), suggestions.suggest("lis"));
        assertEquals(List.of("list", "listc", "listhc", "listhj", "listj"), suggestions.suggest("  lis"));
        assertEquals(List.of("editc", "editj"), suggestions.suggest("edi"));
        assertEquals(List.of("clearc", "clearj", "cpc", "cpj"), suggestions.suggest("c"));
        assertEquals(List.of("cpc", "cpj"), suggestions.suggest("cp"));
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

    /**
     * Unhide commands share a prefix with untag commands and retain alphabetical ordering.
     */
    @Test
    public void suggest_unhidePrefixes_returnsSortedMatches() {
        assertEquals(List.of("unhidec", "unhidej", "untagc", "untagj"), suggestions.suggest("un"));
        assertEquals(List.of("unhidec", "unhidej"), suggestions.suggest("unh"));
        assertEquals(List.of("unhidec", "unhidej"), suggestions.suggest("unhide"));
        assertEquals(List.of("unhidec", "unhidej"), suggestions.suggest("  unhide"));
        assertEquals(List.of("untagc", "untagj"), suggestions.suggest("unt"));
    }

    /**
     * Completed unhide commands and arguments must not reopen command suggestions.
     */
    @Test
    public void suggest_completeUnhideCommandsOrArguments_returnsEmpty() {
        for (String command : List.of("unhidec", "unhidej")) {
            assertTrue(suggestions.suggest(command).isEmpty(), command);
            assertTrue(suggestions.suggest("  " + command).isEmpty(), command);
            assertTrue(suggestions.suggest(command + " ").isEmpty(), command);
            assertTrue(suggestions.suggest(command + " 1").isEmpty(), command);
        }
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
        for (String input : List.of("", "  ", "list", "  list", "listc", "LIST", "unknown", "list ",
                "list 3", "addj n/Alice", "add\t")) {
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
