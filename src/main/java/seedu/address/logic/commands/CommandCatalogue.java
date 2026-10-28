package seedu.address.logic.commands;

import java.util.List;

/**
 * Command names available for completion.
 * When adding or removing commands, keep this catalogue in sync with
 * {@link seedu.address.logic.parser.AddressBookParser}.
 */
public final class CommandCatalogue {
    /** Immutable command names sourced from the command classes rather than duplicated string literals. */
    public static final List<String> COMMAND_WORDS = List.of(
            AddClientCommand.COMMAND_WORD, AddJobseekerCommand.COMMAND_WORD,
            ClearClientCommand.COMMAND_WORD, ClearJobseekerCommand.COMMAND_WORD,
            CopyClientCommand.COMMAND_WORD, CopyJobseekerCommand.COMMAND_WORD,
            DeleteClientCommand.COMMAND_WORD, DeleteJobseekerCommand.COMMAND_WORD,
            EditClientCommand.COMMAND_WORD, EditJobseekerCommand.COMMAND_WORD, ExitCommand.COMMAND_WORD,
            TagClientCommand.COMMAND_WORD, TagJobseekerCommand.COMMAND_WORD,
            UntagClientCommand.COMMAND_WORD, UntagJobseekerCommand.COMMAND_WORD,
            UnhideClientCommand.COMMAND_WORD, UnhideJobseekerCommand.COMMAND_WORD,
            FindClientCommand.COMMAND_WORD, FindJobseekerCommand.COMMAND_WORD,
            FindHiddenClientCommand.COMMAND_WORD, FindHiddenJobseekerCommand.COMMAND_WORD,
            HelpCommand.COMMAND_WORD, HideClientCommand.COMMAND_WORD, HideJobseekerCommand.COMMAND_WORD,
            ListCommand.COMMAND_WORD, ListClientCommand.COMMAND_WORD, ListJobseekerCommand.COMMAND_WORD,
            ListHiddenClientCommand.COMMAND_WORD, ListHiddenJobseekerCommand.COMMAND_WORD);

    private CommandCatalogue() {
    }
}
