package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Jobseeker;

/**
 * Copies the currently displayed jobseekers, or a selected displayed jobseeker, to the system clipboard.
 */
public class CopyJobseekerCommand extends Command {

    public static final String COMMAND_WORD = "cpj";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Copies the currently displayed jobseekers, or the "
            + "jobseeker at the specified index, to the clipboard.\n"
            + "Parameters: [INDEX] (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_SUCCESS = "Copied %1$d jobseeker(s) to clipboard.";
    public static final String MESSAGE_SUCCESS_SINGLE = "Copied jobseeker to clipboard.";

    private final Optional<Index> targetIndex;

    /** Creates a command that copies all currently displayed jobseekers. */
    public CopyJobseekerCommand() {
        this(Optional.empty());
    }

    /** Creates a command that copies the jobseeker at the specified displayed index. */
    public CopyJobseekerCommand(Index targetIndex) {
        this(Optional.of(requireNonNull(targetIndex)));
    }

    /** Stores the optional displayed index; an empty value means copy all displayed jobseekers. */
    private CopyJobseekerCommand(Optional<Index> targetIndex) {
        this.targetIndex = requireNonNull(targetIndex);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        List<Jobseeker> displayedJobseekers = model.getFilteredJobseekerList();
        if (targetIndex.isPresent()) {
            int zeroBasedIndex = targetIndex.get().getZeroBased();
            if (zeroBasedIndex >= displayedJobseekers.size()) {
                throw new CommandException(Messages.MESSAGE_INVALID_JOBSEEKER_DISPLAYED_INDEX);
            }
            return new CommandResult(MESSAGE_SUCCESS_SINGLE,
                    Messages.format(displayedJobseekers.get(zeroBasedIndex)));
        }

        String copiedText = displayedJobseekers.stream()
                .map(Messages::format)
                .collect(Collectors.joining(System.lineSeparator()));

        return new CommandResult(String.format(MESSAGE_SUCCESS, displayedJobseekers.size()), copiedText);
    }
}
