package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;
import java.util.stream.Stream;

import seedu.address.commons.util.ToStringBuilder;

/**
 * Tests that a {@code Person}'s name, phone number, email or any of their tags contains any of the
 * keywords given, ignoring case. For example, the keyword {@code "lic"} matches the name
 * {@code "Alice Tan"}, {@code "9435"} matches the phone number {@code "94351253"}, and
 * {@code "friend"} matches the tag {@code "friends"}.
 */
public class PersonContainsKeywordsPredicate implements Predicate<Person> {
    public static final String MESSAGE_BLANK_KEYWORD = "Keywords should not be blank";

    private final List<String> keywords;

    /**
     * Creates a predicate that matches any of the given {@code keywords}.
     * The list is copied, so later changes to it by the caller do not affect this predicate.
     *
     * @param keywords Keywords to match; neither the list nor any keyword may be null.
     * @throws IllegalArgumentException If any keyword is empty or only whitespace.
     */
    public PersonContainsKeywordsPredicate(List<String> keywords) {
        requireNonNull(keywords);
        // List.copyOf also throws NullPointerException if any keyword is null.
        this.keywords = List.copyOf(keywords);
        checkArgument(this.keywords.stream().noneMatch(String::isBlank), MESSAGE_BLANK_KEYWORD);
    }

    @Override
    public boolean test(Person person) {
        // Locale.ROOT keeps case folding the same on every machine. If this was omitted, under the
        // default locale on a Turkish system for example, "ALICE".toLowerCase() would have given
        // "alıce" (dotless i), which would not match "alice".
        // Each field is checked on its own, so a keyword cannot match across two fields. Each tag
        // counts as a field of its own too, so a keyword cannot match across two tags either.
        Stream<String> contactFields = Stream.of(
                person.getName().fullName.toLowerCase(Locale.ROOT),
                person.getPhone().value,
                person.getEmail().value.toLowerCase(Locale.ROOT));
        Stream<String> tagNames = person.getTags().stream()
                .map(tag -> tag.tagName.toLowerCase(Locale.ROOT));
        List<String> searchableFields = Stream.concat(contactFields, tagNames).toList();
        return keywords.stream()
                .map(keyword -> keyword.toLowerCase(Locale.ROOT))
                .anyMatch(keyword -> searchableFields.stream().anyMatch(field -> field.contains(keyword)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof PersonContainsKeywordsPredicate otherPersonContainsKeywordsPredicate)) {
            return false;
        }

        return keywords.equals(otherPersonContainsKeywordsPredicate.keywords);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).add("keywords", keywords).toString();
    }
}
