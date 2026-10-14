package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.model.person.NameContainsKeywordsPredicate.MESSAGE_BLANK_KEYWORD;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.JobseekerBuilder;

public class NameContainsKeywordsPredicateTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new NameContainsKeywordsPredicate(null));
        assertThrows(NullPointerException.class, () ->
                new NameContainsKeywordsPredicate(Arrays.asList("Alice", null)));
    }

    /**
     * Checks that an empty or whitespace-only keyword is rejected when the predicate is created.
     * This fails if the blank-keyword check is removed from the constructor.
     */
    @Test
    public void constructor_blankKeyword_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, MESSAGE_BLANK_KEYWORD, () ->
                new NameContainsKeywordsPredicate(List.of("")));
        assertThrows(IllegalArgumentException.class, MESSAGE_BLANK_KEYWORD, () ->
                new NameContainsKeywordsPredicate(List.of("Alice", " ")));
    }

    /**
     * Checks that changing the caller's keyword list after NameContainsKeywordsPredicate construction
     * does not change what the predicate matches, and that the constructor makes a copy of the list.
     */
    @Test
    public void constructor_listChangedAfterwards_predicateUnaffected() {
        List<String> keywords = new ArrayList<>(List.of("Alice"));
        NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(keywords);
        keywords.add("Bob");
        assertFalse(predicate.test(new JobseekerBuilder().withName("Bob").build()));
    }

    @Test
    public void equals() {
        List<String> firstPredicateKeywordList = List.of("first");
        List<String> secondPredicateKeywordList = List.of("first", "second");

        NameContainsKeywordsPredicate firstPredicate = new NameContainsKeywordsPredicate(firstPredicateKeywordList);
        NameContainsKeywordsPredicate secondPredicate = new NameContainsKeywordsPredicate(secondPredicateKeywordList);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        NameContainsKeywordsPredicate firstPredicateCopy = new NameContainsKeywordsPredicate(firstPredicateKeywordList);
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different jobseeker -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_nameContainsKeywords_returnsTrue() {
        // One keyword
        NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(List.of("Alice"));
        assertTrue(predicate.test(new JobseekerBuilder().withName("Alice Bob").build()));

        // Multiple keywords
        predicate = new NameContainsKeywordsPredicate(List.of("Alice", "Bob"));
        assertTrue(predicate.test(new JobseekerBuilder().withName("Alice Bob").build()));

        // Only one matching keyword
        predicate = new NameContainsKeywordsPredicate(List.of("Bob", "Carol"));
        assertTrue(predicate.test(new JobseekerBuilder().withName("Alice Carol").build()));

        // Mixed-case keywords
        predicate = new NameContainsKeywordsPredicate(List.of("aLIce", "bOB"));
        assertTrue(predicate.test(new JobseekerBuilder().withName("Alice Bob").build()));
    }

    /**
     * Guards substring matching: each keyword here is only part of a word, so these test cases will fail
     * if matching by name reverts to whole words only.
     */
    @Test
    public void test_keywordIsSubstringOfName_returnsTrue() {
        // Middle of a word
        NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(List.of("lic"));
        assertTrue(predicate.test(new JobseekerBuilder().withName("Alice Bob").build()));

        // Start of the second word
        predicate = new NameContainsKeywordsPredicate(List.of("Bo"));
        assertTrue(predicate.test(new JobseekerBuilder().withName("Alice Bob").build()));

        // Mixed-case partial keyword
        predicate = new NameContainsKeywordsPredicate(List.of("aLI"));
        assertTrue(predicate.test(new JobseekerBuilder().withName("Alice Bob").build()));
    }

    /**
     * Guards the use of {@code Locale.ROOT}: under the Turkish locale, {@code "ALICE".toLowerCase()}
     * gives a dotless i, so this fails if the predicate lower-cases with the default locale.
     */
    @Test
    public void test_turkishDefaultLocale_stillMatchesIgnoringCase() {
        Locale originalLocale = Locale.getDefault();
        try {
            Locale.setDefault(Locale.forLanguageTag("tr-TR"));
            NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(List.of("ALICE"));
            assertTrue(predicate.test(new JobseekerBuilder().withName("Alice Bob").build()));
        } finally {
            Locale.setDefault(originalLocale);
        }
    }

    @Test
    public void test_nameDoesNotContainKeywords_returnsFalse() {
        // Zero keywords
        NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(List.of());
        assertFalse(predicate.test(new JobseekerBuilder().withName("Alice").build()));

        // Non-matching keyword
        predicate = new NameContainsKeywordsPredicate(List.of("Carol"));
        assertFalse(predicate.test(new JobseekerBuilder().withName("Alice Bob").build()));

        // Keyword is longer than any matching part of the name
        predicate = new NameContainsKeywordsPredicate(List.of("Alicia"));
        assertFalse(predicate.test(new JobseekerBuilder().withName("Alice Bob").build()));

        // Keywords match address only, which is not searched
        predicate = new NameContainsKeywordsPredicate(List.of("Main", "Street"));
        assertFalse(predicate.test(new JobseekerBuilder().withName("Alice").withPhone("12345")
                .withEmail("alice@email.com").withAddress("Main Street").build()));
    }

    @Test
    public void test_keywordMatchesPhone_returnsTrue() {
        // Full phone number
        NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(List.of("12345"));
        assertTrue(predicate.test(new JobseekerBuilder().withName("Alice").withPhone("12345").build()));

        // Part of the phone number
        predicate = new NameContainsKeywordsPredicate(List.of("234"));
        assertTrue(predicate.test(new JobseekerBuilder().withName("Alice").withPhone("12345").build()));
    }

    @Test
    public void test_keywordMatchesEmail_returnsTrue() {
        // Part of the email, mixed case
        NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(List.of("EMAIL.com"));
        assertTrue(predicate.test(new JobseekerBuilder().withName("Bob")
                .withEmail("alice@email.com").build()));
    }

    /**
     * Guards checking each field separately: {@code "Alice12345"} spans the end of the name and the
     * start of the phone number, so this fails if the fields are joined into one string before matching.
     */
    @Test
    public void test_keywordSpansNameAndPhone_returnsFalse() {
        NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(List.of("Alice12345"));
        assertFalse(predicate.test(new JobseekerBuilder().withName("Alice").withPhone("12345")
                .withEmail("bob@email.com").build()));
    }

    @Test
    public void toStringMethod() {
        List<String> keywords = List.of("keyword1", "keyword2");
        NameContainsKeywordsPredicate predicate = new NameContainsKeywordsPredicate(keywords);

        String expected = NameContainsKeywordsPredicate.class.getCanonicalName() + "{keywords=" + keywords + "}";
        assertEquals(expected, predicate.toString());
    }
}
