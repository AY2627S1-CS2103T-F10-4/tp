package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.model.person.PersonContainsKeywordsPredicate.MESSAGE_BLANK_KEYWORD;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.ClientBuilder;
import seedu.address.testutil.JobseekerBuilder;

public class PersonContainsKeywordsPredicateTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new PersonContainsKeywordsPredicate(null));
        assertThrows(NullPointerException.class, () ->
                new PersonContainsKeywordsPredicate(Arrays.asList("Alice", null)));
    }

    /**
     * Checks that an empty or whitespace-only keyword is rejected when the predicate is created.
     * This fails if the blank-keyword check is removed from the constructor.
     */
    @Test
    public void constructor_blankKeyword_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, MESSAGE_BLANK_KEYWORD, () ->
                new PersonContainsKeywordsPredicate(List.of("")));
        assertThrows(IllegalArgumentException.class, MESSAGE_BLANK_KEYWORD, () ->
                new PersonContainsKeywordsPredicate(List.of("Alice", " ")));
    }

    /**
     * Checks that changing the caller's keyword list after PersonContainsKeywordsPredicate construction
     * does not change what the predicate matches, and that the constructor makes a copy of the list.
     */
    @Test
    public void constructor_listChangedAfterwards_predicateUnaffected() {
        List<String> keywords = new ArrayList<>(List.of("Alice"));
        PersonContainsKeywordsPredicate predicate = new PersonContainsKeywordsPredicate(keywords);
        keywords.add("Bob");
        assertFalse(predicate.test(new JobseekerBuilder().withName("Bob").build()));
    }

    @Test
    public void equals() {
        List<String> firstPredicateKeywordList = List.of("first");
        List<String> secondPredicateKeywordList = List.of("first", "second");

        PersonContainsKeywordsPredicate firstPredicate =
                new PersonContainsKeywordsPredicate(firstPredicateKeywordList);
        PersonContainsKeywordsPredicate secondPredicate =
                new PersonContainsKeywordsPredicate(secondPredicateKeywordList);

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        PersonContainsKeywordsPredicate firstPredicateCopy =
                new PersonContainsKeywordsPredicate(firstPredicateKeywordList);
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
        PersonContainsKeywordsPredicate predicate = new PersonContainsKeywordsPredicate(List.of("Alice"));
        assertTrue(predicate.test(new JobseekerBuilder().withName("Alice Bob").build()));

        // Multiple keywords
        predicate = new PersonContainsKeywordsPredicate(List.of("Alice", "Bob"));
        assertTrue(predicate.test(new JobseekerBuilder().withName("Alice Bob").build()));

        // Only one matching keyword
        predicate = new PersonContainsKeywordsPredicate(List.of("Bob", "Carol"));
        assertTrue(predicate.test(new JobseekerBuilder().withName("Alice Carol").build()));

        // Mixed-case keywords
        predicate = new PersonContainsKeywordsPredicate(List.of("aLIce", "bOB"));
        assertTrue(predicate.test(new JobseekerBuilder().withName("Alice Bob").build()));
    }

    /**
     * Guards substring matching: each keyword here is only part of a word, so these test cases will fail
     * if matching by name reverts to whole words only.
     */
    @Test
    public void test_keywordIsSubstringOfName_returnsTrue() {
        // Middle of a word
        PersonContainsKeywordsPredicate predicate = new PersonContainsKeywordsPredicate(List.of("lic"));
        assertTrue(predicate.test(new JobseekerBuilder().withName("Alice Bob").build()));

        // Start of the second word
        predicate = new PersonContainsKeywordsPredicate(List.of("Bo"));
        assertTrue(predicate.test(new JobseekerBuilder().withName("Alice Bob").build()));

        // Mixed-case partial keyword
        predicate = new PersonContainsKeywordsPredicate(List.of("aLI"));
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
            PersonContainsKeywordsPredicate predicate = new PersonContainsKeywordsPredicate(List.of("ALICE"));
            assertTrue(predicate.test(new JobseekerBuilder().withName("Alice Bob").build()));
        } finally {
            Locale.setDefault(originalLocale);
        }
    }

    @Test
    public void test_nameDoesNotContainKeywords_returnsFalse() {
        // Zero keywords
        PersonContainsKeywordsPredicate predicate = new PersonContainsKeywordsPredicate(List.of());
        assertFalse(predicate.test(new JobseekerBuilder().withName("Alice").build()));

        // Non-matching keyword
        predicate = new PersonContainsKeywordsPredicate(List.of("Carol"));
        assertFalse(predicate.test(new JobseekerBuilder().withName("Alice Bob").build()));

        // Keyword is longer than any matching part of the name
        predicate = new PersonContainsKeywordsPredicate(List.of("Alicia"));
        assertFalse(predicate.test(new JobseekerBuilder().withName("Alice Bob").build()));

        // Keywords match address only, which is not searched
        predicate = new PersonContainsKeywordsPredicate(List.of("Main", "Street"));
        assertFalse(predicate.test(new JobseekerBuilder().withName("Alice").withPhone("12345")
                .withEmail("alice@email.com").withAddress("Main Street").build()));
    }

    @Test
    public void test_keywordMatchesPhone_returnsTrue() {
        // Full phone number
        PersonContainsKeywordsPredicate predicate = new PersonContainsKeywordsPredicate(List.of("12345"));
        assertTrue(predicate.test(new JobseekerBuilder().withName("Alice").withPhone("12345").build()));

        // Part of the phone number
        predicate = new PersonContainsKeywordsPredicate(List.of("234"));
        assertTrue(predicate.test(new JobseekerBuilder().withName("Alice").withPhone("12345").build()));
    }

    @Test
    public void test_keywordMatchesEmail_returnsTrue() {
        // Part of the email, mixed case
        PersonContainsKeywordsPredicate predicate = new PersonContainsKeywordsPredicate(List.of("EMAIL.com"));
        assertTrue(predicate.test(new JobseekerBuilder().withName("Bob")
                .withEmail("alice@email.com").build()));
    }

    /**
     * Checks that tags are searched. Each keyword here matches nothing but a tag, so this fails if tags
     * are dropped from the searched fields.
     */
    @Test
    public void test_keywordMatchesTag_returnsTrue() {
        // Full tag name
        PersonContainsKeywordsPredicate predicate = new PersonContainsKeywordsPredicate(List.of("friends"));
        assertTrue(predicate.test(new JobseekerBuilder().withName("Alice").withTags("friends").build()));

        // Part of a tag name, mixed case
        predicate = new PersonContainsKeywordsPredicate(List.of("MONEY"));
        assertTrue(predicate.test(new JobseekerBuilder().withName("Alice")
                .withTags("friends", "owesMoney").build()));
    }

    /**
     * Guards checking each tag separately: each keyword spans the end of one tag name and the start of
     * the other, so this fails if the tag names are joined into one string before matching. Tags are
     * held in a set with no fixed order, so there is one keyword for each order they could be joined in.
     */
    @Test
    public void test_keywordSpansTwoTags_returnsFalse() {
        PersonContainsKeywordsPredicate predicate =
                new PersonContainsKeywordsPredicate(List.of("friendsowes", "moneyfriends"));
        assertFalse(predicate.test(new JobseekerBuilder().withName("Alice")
                .withTags("friends", "owesMoney").build()));
    }

    /**
     * Checks that a client's job openings are searched. Each keyword here matches nothing but a job name,
     * so this fails if job openings are dropped from the searched fields.
     */
    @Test
    public void test_keywordMatchesClientJob_returnsTrue() {
        // Full job name
        PersonContainsKeywordsPredicate predicate = new PersonContainsKeywordsPredicate(List.of("Tester"));
        assertTrue(predicate.test(new ClientBuilder().withName("Adrian").withAvailableJobs("Tester").build()));

        // One word of a multi-word job name, mixed case
        predicate = new PersonContainsKeywordsPredicate(List.of("eNGINEER"));
        assertTrue(predicate.test(new ClientBuilder().withName("Adrian")
                .withAvailableJobs("QA Tester", "Software Engineer").build()));

        // Part of a word in a job name
        predicate = new PersonContainsKeywordsPredicate(List.of("soft"));
        assertTrue(predicate.test(new ClientBuilder().withName("Adrian")
                .withAvailableJobs("Software Engineer").build()));
    }

    @Test
    public void test_keywordMatchesNoClientJob_returnsFalse() {
        PersonContainsKeywordsPredicate predicate = new PersonContainsKeywordsPredicate(List.of("Designer"));
        assertFalse(predicate.test(new ClientBuilder().withName("Adrian")
                .withAvailableJobs("Software Engineer").build()));
        assertFalse(predicate.test(new ClientBuilder().withName("Adrian").build()));
    }

    /**
     * Guards checking each job separately, as for tags: each keyword spans the end of one job name and
     * the start of the other, one for each order the jobs could be joined in, so this fails if the job
     * names are joined into one string before matching.
     */
    @Test
    public void test_keywordSpansTwoJobs_returnsFalse() {
        PersonContainsKeywordsPredicate predicate =
                new PersonContainsKeywordsPredicate(List.of("chefcleaner", "cleanerchef"));
        assertFalse(predicate.test(new ClientBuilder().withName("Adrian")
                .withAvailableJobs("Chef", "Cleaner").build()));
    }

    /**
     * Checks that a jobseeker's highest qualification is searched. Each keyword here matches nothing but
     * the qualification, so this fails if qualifications are dropped from the searched fields.
     */
    @Test
    public void test_keywordMatchesJobseekerQualification_returnsTrue() {
        // One word of the qualification, mixed case
        PersonContainsKeywordsPredicate predicate = new PersonContainsKeywordsPredicate(List.of("dIPLOMA"));
        assertTrue(predicate.test(new JobseekerBuilder().withName("Alice")
                .withHighestQualification("Diploma in IT").build()));

        // Part of a word in the qualification
        predicate = new PersonContainsKeywordsPredicate(List.of("comput"));
        assertTrue(predicate.test(new JobseekerBuilder().withName("Alice")
                .withHighestQualification("Degree in Computing").build()));
    }

    @Test
    public void test_keywordMatchesNoJobseekerQualification_returnsFalse() {
        PersonContainsKeywordsPredicate predicate = new PersonContainsKeywordsPredicate(List.of("Masters"));
        assertFalse(predicate.test(new JobseekerBuilder().withName("Alice")
                .withHighestQualification("Diploma in IT").build()));
        // Unspecified (empty) qualification
        assertFalse(predicate.test(new JobseekerBuilder().withName("Alice")
                .withHighestQualification("").build()));
    }

    /**
     * Guards checking each field separately: {@code "Alice12345"} spans the end of the name and the
     * start of the phone number, so this fails if the fields are joined into one string before matching.
     */
    @Test
    public void test_keywordSpansNameAndPhone_returnsFalse() {
        PersonContainsKeywordsPredicate predicate =
                new PersonContainsKeywordsPredicate(List.of("Alice12345"));
        assertFalse(predicate.test(new JobseekerBuilder().withName("Alice").withPhone("12345")
                .withEmail("bob@email.com").build()));
    }

    @Test
    public void toStringMethod() {
        List<String> keywords = List.of("keyword1", "keyword2");
        PersonContainsKeywordsPredicate predicate = new PersonContainsKeywordsPredicate(keywords);

        String expected = PersonContainsKeywordsPredicate.class.getCanonicalName()
                + "{keywords=" + keywords + "}";
        assertEquals(expected, predicate.toString());
    }
}
