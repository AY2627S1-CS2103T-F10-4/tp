package seedu.address.ui;

import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import seedu.address.commons.core.LogsCenter;

/**
 * Controller for a help page
 */
public class HelpWindow extends UiPart<Stage> {

    private static final Logger logger = LogsCenter.getLogger(HelpWindow.class);
    private static final String FXML = "HelpWindow.fxml";

    @FXML
    private ScrollPane content;

    /**
     * Creates a new HelpWindow.
     *
     * @param root Stage to use as the root of the HelpWindow.
     */
    public HelpWindow(Stage root) {
        super(FXML, root);

        getRoot().getScene().addEventHandler(NavigationEvent.ANY, event -> {
            if (event.getPageName() != null) {
                navigateToPage(event.getPageName());
            }
            event.consume();
        });
    }

    /**
     * Creates a new HelpWindow.
     */
    public HelpWindow() {
        this(new Stage());
    }

    /**
     * Shows the help window.
     *
     * @throws IllegalStateException
     *     <ul>
     *         <li>
     *             if this method is called on a thread other than the JavaFX Application Thread.
     *         </li>
     *         <li>
     *             if this method is called during animation or layout processing.
     *         </li>
     *         <li>
     *             if this method is called on the primary stage.
     *         </li>
     *         <li>
     *             if {@code dialogStage} is already showing.
     *         </li>
     *     </ul>
     */
    public void show() {
        logger.fine("Showing help page about the application.");
        getRoot().show();
        getRoot().centerOnScreen();
    }

    /**
     * Returns true if the help window is currently being shown.
     */
    public boolean isShowing() {
        return getRoot().isShowing();
    }

    /**
     * Hides the help window.
     */
    public void hide() {
        getRoot().hide();
    }

    /**
     * Focuses on the help window.
     */
    public void focus() {
        getRoot().requestFocus();
    }

    private static class HelpPage extends UiPart<VBox> {

        private static final Map<String, String> PAGES = Map.ofEntries(
                Map.entry("Notes about the command format", "HelpPage_NotesAboutTheCommandFormat.fxml"),
                Map.entry("Viewing help", "HelpPage_ViewingHelp.fxml"),
                Map.entry("Exiting the application", "HelpPage_ExitingTheApplication.fxml"),
                Map.entry("Common info about contacts", "HelpPage_CommonInfoAboutContacts.fxml"),
                Map.entry("Adding a jobseeker", "HelpPage_AddingAJobseeker.fxml"),
                Map.entry("Adding a client", "HelpPage_AddingAClient.fxml"),
                Map.entry("Editing a jobseeker", "HelpPage_EditingAJobseeker.fxml"),
                Map.entry("Editing a client", "HelpPage_EditingAClient.fxml"),
                Map.entry("Deleting a jobseeker", "HelpPage_DeletingAJobseeker.fxml"),
                Map.entry("Deleting a client", "HelpPage_DeletingAClient.fxml"),
                Map.entry("Clearing all jobseekers", "HelpPage_ClearingAllJobseekers.fxml"),
                Map.entry("Clearing all clients", "HelpPage_ClearingAllClients.fxml"),
                Map.entry("Viewing contacts", "HelpPage_ViewingContacts.fxml"),
                Map.entry("Listing jobseekers and clients", "HelpPage_ListingJobseekersAndClients.fxml"),
                Map.entry("Listing jobseekers", "HelpPage_ListingJobseekers.fxml"),
                Map.entry("Listing clients", "HelpPage_ListingClients.fxml"),
                Map.entry("Finding jobseekers", "HelpPage_FindingJobseekers.fxml"),
                Map.entry("Finding clients", "HelpPage_FindingClients.fxml"),
                Map.entry("Finding jobseekers and clients", "HelpPage_FindingJobseekersAndClients.fxml"),
                Map.entry("Listing hidden jobseekers", "HelpPage_ListingHiddenJobseekers.fxml"),
                Map.entry("Listing hidden clients", "HelpPage_ListingHiddenClients.fxml"),
                Map.entry("Hiding a jobseeker", "HelpPage_HidingAJobseeker.fxml"),
                Map.entry("Hiding a clients", "HelpPage_HidingAClient.fxml"),
                Map.entry("Unhiding a jobseeker", "HelpPage_UnhidingAJobseeker.fxml"),
                Map.entry("Unhiding a clients", "HelpPage_UnhidingAClient.fxml"),
                Map.entry("Finding hidden jobseekers", "HelpPage_FindingHiddenJobseekers.fxml"),
                Map.entry("Finding hidden clients", "HelpPage_FindingHiddenClients.fxml"),
                Map.entry("Managing tags", "HelpPage_ManagingTags.fxml"),
                Map.entry("Adding jobseeker tags", "HelpPage_AddingJobseekerTags.fxml"),
                Map.entry("Adding client tags", "HelpPage_AddingClientTags.fxml"),
                Map.entry("Removing jobseeker tags", "HelpPage_RemovingJobseekerTags.fxml"),
                Map.entry("Removing client tags", "HelpPage_RemovingClientTags.fxml"),
                Map.entry("Copying displayed jobseekers", "HelpPage_CopyingDisplayedJobseekers.fxml"),
                Map.entry("Copying displayed clients", "HelpPage_CopyingDisplayedClients.fxml"),
                Map.entry("FAQ and Known issues", "HelpPage_FAQAndKnownIssues.fxml"),
                Map.entry("Command utilities", "HelpPage_CommandUtilities.fxml")
        );

        public HelpPage(String pageName) {
            super(Objects.requireNonNull(PAGES.get(pageName), "Page does not exist."));
        }
    }

    private void navigateToPage(String pageName) {
        content.setContent(new HelpPage(pageName).getRoot());
        content.setVvalue(0);
    }
}
