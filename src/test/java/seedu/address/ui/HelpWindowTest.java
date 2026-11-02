package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javafx.application.Platform;
import javafx.event.Event;
import seedu.address.testutil.JavaFxTestUtil;

/**
 * Checks the links between help pages. A link names its page in plain FXML text, so the compiler
 * cannot catch a link to a page that does not exist or a page that nothing links to.
 */
public class HelpWindowTest {
    private static final Path VIEW_FOLDER = Path.of("src", "main", "resources", "view");
    private static final Path HELP_WINDOW_FXML = VIEW_FOLDER.resolve("HelpWindow.fxml");
    private static final Pattern LINK_TEXT_PATTERN = Pattern.compile("<SectionHyperlink[^>]*\\btext=\"([^\"]*)\"");

    private static HelpWindow helpWindow;

    @BeforeAll
    public static void setUp() throws Exception {
        JavaFxTestUtil.startJavaFx();
        runOnFxThread(() -> helpWindow = new HelpWindow());
    }

    @Test
    public void sectionHyperlinks_inHelpFxmlFiles_openExistingPages() throws Exception {
        // EP: links in the table of contents
        for (String linkText : getLinkTexts(HELP_WINDOW_FXML)) {
            assertOpensPage(linkText);
        }

        // EP: links inside help pages
        for (Path helpPageFile : getHelpPageFiles()) {
            for (String linkText : getLinkTexts(helpPageFile)) {
                assertOpensPage(linkText);
            }
        }
    }

    /**
     * Guards reachability: without a link in the table of contents, a page can only be opened
     * from another page, and the two clearing pages were once not linked from anywhere.
     */
    @Test
    public void tableOfContents_linksEveryHelpPage() throws Exception {
        assertEquals(getHelpPageNames(), getLinkTexts(HELP_WINDOW_FXML));
    }

    /**
     * Guards the exact match on page names, which the developer guide tells developers to rely on.
     */
    @Test
    public void navigationEvent_pageNameVariations_opensOnlyExactMatch() throws Exception {
        // EP: exact page name
        assertOpensPage("Hiding a jobseeker");

        // EP: no page name
        runOnFxThread(() -> assertDoesNotThrow(() -> fireNavigationEvent(new NavigationEvent())));

        // EP: name of a page that does not exist
        assertNoSuchPage("Tag details");

        // EP: page name that is almost exact
        assertNoSuchPage(""); // Boundary value
        assertNoSuchPage(" Hiding a jobseeker"); // Boundary value
        assertNoSuchPage("Hiding a jobseeke"); // Boundary value
        assertNoSuchPage("hiding a jobseeker");
    }

    /**
     * Guards against a page showing the other list's command: its copy button would then
     * put a command that wipes the wrong list on the user's clipboard.
     */
    @Test
    public void clearingPages_showOwnCommandOnly() throws Exception {
        // EP: client page
        String clientPage = Files.readString(VIEW_FOLDER.resolve("HelpPage_ClearingAllClients.fxml"));
        assertTrue(clientPage.contains("code=\"clearc\""));
        assertFalse(clientPage.contains("clearj"));
        assertFalse(clientPage.contains("jobseeker"));

        // EP: jobseeker page
        String jobseekerPage = Files.readString(VIEW_FOLDER.resolve("HelpPage_ClearingAllJobseekers.fxml"));
        assertTrue(jobseekerPage.contains("code=\"clearj\""));
        assertFalse(jobseekerPage.contains("clearc"));
        assertFalse(jobseekerPage.contains("client"));
    }

    private static void assertOpensPage(String pageName) throws Exception {
        runOnFxThread(() -> assertDoesNotThrow(() -> fireNavigationEvent(new NavigationEvent(pageName)),
                "No help page named \"" + pageName + "\""));
    }

    private static void assertNoSuchPage(String pageName) throws Exception {
        runOnFxThread(() -> assertThrows(NullPointerException.class, "Page does not exist.", () ->
                fireNavigationEvent(new NavigationEvent(pageName))));
    }

    private static void fireNavigationEvent(NavigationEvent event) {
        Event.fireEvent(helpWindow.getRoot().getScene(), event);
    }

    private static Set<String> getLinkTexts(Path fxmlFile) throws IOException {
        return LINK_TEXT_PATTERN.matcher(Files.readString(fxmlFile))
                .results()
                .map(match -> match.group(1))
                .collect(Collectors.toSet());
    }

    private static List<Path> getHelpPageFiles() throws IOException {
        try (Stream<Path> files = Files.list(VIEW_FOLDER)) {
            return files.filter(file -> file.getFileName().toString().startsWith("HelpPage_")).toList();
        }
    }

    /**
     * Returns the page names in {@code HelpPage.PAGES}. Reads them by reflection because
     * {@code HelpPage} and its map are private, and a test does not justify widening their access.
     */
    private static Set<String> getHelpPageNames() throws ReflectiveOperationException {
        Field pagesField = Class.forName("seedu.address.ui.HelpWindow$HelpPage").getDeclaredField("PAGES");
        pagesField.setAccessible(true);
        return ((Map<?, ?>) pagesField.get(null)).keySet().stream()
                .map(String::valueOf)
                .collect(Collectors.toSet());
    }

    private static void runOnFxThread(Runnable action) throws Exception {
        FutureTask<Void> task = new FutureTask<>(action, null);
        Platform.runLater(task);
        task.get(10, TimeUnit.SECONDS);
    }
}
