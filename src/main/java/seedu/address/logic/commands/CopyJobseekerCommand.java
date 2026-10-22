package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.stream.Collectors;

import seedu.address.logic.Messages;
import seedu.address.model.Model;

/**
 * Copies the currently displayed jobseekers to the system clipboard.
 */
public class CopyJobseekerCommand extends Command {

    public static final String COMMAND_WORD = "cpj";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Copies all currently displayed jobseekers to the "
            + "clipboard.\n"
            + "Example: " + COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = "Copied %1$d jobseeker(s) to clipboard.";

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);

        String copiedText = model.getFilteredJobseekerList().stream()
                .map(Messages::format)
                .collect(Collectors.joining(System.lineSeparator()));
        int copiedJobseekerCount = model.getFilteredJobseekerList().size();

        return new CommandResult(String.format(MESSAGE_SUCCESS, copiedJobseekerCount), copiedText);
    }
}
