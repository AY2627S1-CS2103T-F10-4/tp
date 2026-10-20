package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class CommandHistoryTest {
    private final CommandHistory history = new CommandHistory();

    @Test
    public void navigation_emptyHistory_preservesInput() {
        assertEquals("draft", history.previous("draft"));
        assertEquals("draft", history.next("draft"));
    }

    @Test
    public void navigation_multipleCommands_followsSubmissionOrderAndStopsAtBoundaries() {
        history.add("help");
        history.add("listj");
        assertEquals("listj", history.previous(""));
        assertEquals("help", history.previous("listj"));
        assertEquals("help", history.previous("help"));
        assertEquals("listj", history.next("help"));
        assertEquals("", history.next("listj"));
        assertEquals("", history.next(""));
    }

    @Test
    public void navigation_roundTrip_restoresDraftAndCapturesLaterEdits() {
        history.add("help");
        assertEquals("help", history.previous("unfinished"));
        assertEquals("unfinished", history.next("help"));
        assertEquals("help", history.previous("updated draft"));
        assertEquals("updated draft", history.next("help"));
        assertEquals("updated draft", history.next("updated draft"));
    }

    @Test
    public void navigation_editedRecalledCommand_doesNotChangeStoredHistory() {
        history.add("help");
        history.add("listj");
        assertEquals("listj", history.previous("draft"));
        assertEquals("help", history.previous("edited listj"));
        assertEquals("edited help", history.previous("edited help"));
        assertEquals("listj", history.next("edited help"));
        assertEquals("help", history.previous("listj"));
    }

    @Test
    public void add_whileBrowsing_resetsPositionAndDraft() {
        history.add("help");
        history.add("listj");
        history.previous("old draft");
        history.previous("listj");
        history.add("listc");
        assertEquals("", history.next(""));
        assertEquals("listc", history.previous("new draft"));
        assertEquals("new draft", history.next("listc"));
        assertEquals("listc", history.previous(""));
        assertEquals("listj", history.previous("listc"));
        assertEquals("help", history.previous("listj"));
    }

    @Test
    public void add_blankCommands_omitsEntries() {
        history.add("");
        history.add(" \t ");
        assertEquals("draft", history.previous("draft"));
        history.add("help");
        history.previous("");
        history.add(" ");
        assertEquals("help", history.previous(" "));
        assertEquals(" ", history.next("help"));
    }

    @Test
    public void add_duplicatesAndInvalidCommands_preservesExactSubmissions() {
        history.add("  invalid command  ");
        history.add("help");
        history.add("help");
        assertEquals("help", history.previous(""));
        assertEquals("help", history.previous("help"));
        assertEquals("  invalid command  ", history.previous("help"));
        assertEquals("help", history.next("  invalid command  "));
    }

    @Test
    public void newHistory_doesNotShareEntriesWithAnotherSession() {
        history.add("help");
        CommandHistory anotherSession = new CommandHistory();
        assertEquals("draft", anotherSession.previous("draft"));
    }
}
