package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Objects;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Represents the result of a command execution.
 */
public class CommandResult {

    private final String feedbackToUser;

    /** Text to copy to the system clipboard, or {@code null} if no clipboard action is required. */
    private final String clipboardText;

    /** Help information should be shown to the user. */
    private final boolean shouldShowHelp;

    /** The application should exit. */
    private final boolean shouldExit;

    /**
     * Constructs a {@code CommandResult} with the specified fields.
     */
    public CommandResult(String feedbackToUser, boolean shouldShowHelp, boolean shouldExit) {
        this(feedbackToUser, shouldShowHelp, shouldExit, null);
    }

    /**
     * Constructs a {@code CommandResult} with clipboard text.
     */
    public CommandResult(String feedbackToUser, String clipboardText) {
        this(feedbackToUser, false, false, requireNonNull(clipboardText));
    }

    private CommandResult(String feedbackToUser, boolean shouldShowHelp, boolean shouldExit, String clipboardText) {
        this.feedbackToUser = requireNonNull(feedbackToUser);
        this.shouldShowHelp = shouldShowHelp;
        this.shouldExit = shouldExit;
        this.clipboardText = clipboardText;
    }

    /**
     * Constructs a {@code CommandResult} with the specified {@code feedbackToUser},
     * and other fields set to their default value.
     */
    public CommandResult(String feedbackToUser) {
        this(feedbackToUser, false, false);
    }

    public String getFeedbackToUser() {
        return feedbackToUser;
    }

    public boolean isShowHelp() {
        return shouldShowHelp;
    }

    public boolean isExit() {
        return shouldExit;
    }

    public String getClipboardText() {
        return clipboardText;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof CommandResult otherCommandResult)) {
            return false;
        }

        return feedbackToUser.equals(otherCommandResult.feedbackToUser)
                && shouldShowHelp == otherCommandResult.shouldShowHelp
                && shouldExit == otherCommandResult.shouldExit
                && Objects.equals(clipboardText, otherCommandResult.clipboardText);
    }

    @Override
    public int hashCode() {
        return Objects.hash(feedbackToUser, shouldShowHelp, shouldExit, clipboardText);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("feedbackToUser", feedbackToUser)
                .add("shouldShowHelp", shouldShowHelp)
                .add("shouldExit", shouldExit)
                .add("clipboardText", clipboardText)
                .toString();
    }

}
