package seedu.address.model.person;

import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Person}'s {@code Name} contains any of the keywords given, ignoring case.
 * For example, the keyword {@code "lic"} matches the name {@code "Alice Tan"}.
 */
public class NameContainsKeywordsPredicate implements Predicate<Person> {
    private final List<String> keywords;

    public NameContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(Person person) {
        // Locale.ROOT keeps case folding the same on every machine. If this was omitted, under the
        // default locale on a Turkish system for example, "ALICE".toLowerCase() would have given
        // "alıce" (dotless i), which would not match "alice".
        String lowerCaseName = person.getName().fullName.toLowerCase(Locale.ROOT);
        return keywords.stream()
                .anyMatch(keyword -> lowerCaseName.contains(keyword.toLowerCase(Locale.ROOT)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof NameContainsKeywordsPredicate otherNameContainsKeywordsPredicate)) {
            return false;
        }

        return keywords.equals(otherNameContainsKeywordsPredicate.keywords);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("keywords", keywords).toString();
    }
}
