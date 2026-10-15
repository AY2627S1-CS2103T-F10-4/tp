package seedu.address.testutil;

import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_JACK;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_KATE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_JACK;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_KATE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_JACK;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_KATE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_JACK;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_KATE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_EMPLOYER;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_PARTNER;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.AddressBook;
import seedu.address.model.person.Client;
import seedu.address.model.person.Jobseeker;

/**
 * A utility class containing typical {@code Jobseeker} and {@code Client} objects to be used in tests.
 */
public class TypicalPersons {

    public static final Jobseeker ALICE = new JobseekerBuilder().withName("Alice Pauline")
            .withAddress("123, Jurong West Ave 6, #08-111").withEmail("alice@example.com")
            .withPhone("94351253")
            .withTags("friends").build();
    public static final Jobseeker BENSON = new JobseekerBuilder().withName("Benson Meier")
            .withAddress("311, Clementi Ave 2, #02-25")
            .withEmail("johnd@example.com").withPhone("98765432")
            .withTags("owesMoney", "friends").build();
    public static final Jobseeker CARL = new JobseekerBuilder().withName("Carl Kurz").withPhone("95352563")
            .withEmail("heinz@example.com").withAddress("wall street").build();
    public static final Jobseeker DANIEL = new JobseekerBuilder().withName("Daniel Meier").withPhone("87652533")
            .withEmail("cornelia@example.com").withAddress("10th street").withTags("friends").build();
    public static final Jobseeker ELLE = new JobseekerBuilder().withName("Elle Meyer").withPhone("9482224")
            .withEmail("werner@example.com").withAddress("michegan ave").build();
    public static final Jobseeker FIONA = new JobseekerBuilder().withName("Fiona Kunz").withPhone("9482427")
            .withEmail("lydia@example.com").withAddress("little tokyo").build();
    public static final Jobseeker GEORGE = new JobseekerBuilder().withName("George Best").withPhone("9482442")
            .withEmail("anna@example.com").withAddress("4th street").build();

    // Manually added
    public static final Jobseeker HOON = new JobseekerBuilder().withName("Hoon Meier").withPhone("8482424")
            .withEmail("stefan@example.com").withAddress("little india").build();
    public static final Jobseeker IDA = new JobseekerBuilder().withName("Ida Mueller").withPhone("8482131")
            .withEmail("hans@example.com").withAddress("chicago ave").build();

    // Manually added - jobseeker details found in {@code CommandTestUtil}
    public static final Jobseeker AMY = new JobseekerBuilder().withName(VALID_NAME_AMY).withPhone(VALID_PHONE_AMY)
            .withEmail(VALID_EMAIL_AMY).withAddress(VALID_ADDRESS_AMY).withTags(VALID_TAG_FRIEND).build();
    public static final Jobseeker BOB = new JobseekerBuilder().withName(VALID_NAME_BOB).withPhone(VALID_PHONE_BOB)
            .withEmail(VALID_EMAIL_BOB).withAddress(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND, VALID_TAG_FRIEND)
            .build();

    public static final Client ADRIAN = new ClientBuilder().withName("Adrian Tan")
            .withPhone("81110001").withEmail("adrian@example.com")
            .withAddress("1 Raffles Place").withTags("employer").build();
    public static final Client BRENDA = new ClientBuilder().withName("Brenda Lim")
            .withPhone("81110002").withEmail("brenda@example.com")
            .withAddress("2 Shenton Way").withTags("employer", "partner").build();
    public static final Client CEDRIC = new ClientBuilder().withName("Cedric Ng")
            .withPhone("81110003").withEmail("cedric@example.com")
            .withAddress("3 Robinson Road").build();
    public static final Client DIANA = new ClientBuilder().withName("Diana Lee")
            .withPhone("81110004").withEmail("diana@example.com")
            .withAddress("4 Cecil Street").withTags("partner").build();
    public static final Client ETHAN = new ClientBuilder().withName("Ethan Goh")
            .withPhone("81110005").withEmail("ethan@example.com")
            .withAddress("5 Orchard Road").build();
    public static final Client FELICIA = new ClientBuilder().withName("Felicia Teo")
            .withPhone("81110006").withEmail("felicia@example.com")
            .withAddress("6 Jurong East Street").build();
    public static final Client GAVIN = new ClientBuilder().withName("Gavin Koh")
            .withPhone("81110007").withEmail("gavin@example.com")
            .withAddress("7 Paya Lebar Road").build();

    // Additional clients whose details are defined in CommandTestUtil.
    // Excluded from the typical client list.
    public static final Client JACK = new ClientBuilder().withName(VALID_NAME_JACK)
            .withPhone(VALID_PHONE_JACK).withEmail(VALID_EMAIL_JACK)
            .withAddress(VALID_ADDRESS_JACK).withTags(VALID_TAG_EMPLOYER).build();
    public static final Client KATE = new ClientBuilder().withName(VALID_NAME_KATE)
            .withPhone(VALID_PHONE_KATE).withEmail(VALID_EMAIL_KATE)
            .withAddress(VALID_ADDRESS_KATE).withTags(VALID_TAG_EMPLOYER, VALID_TAG_PARTNER).build();

    public static final String KEYWORD_MATCHING_MEIER = "Meier"; // A keyword that matches MEIER

    private TypicalPersons() {} // prevents instantiation

    /**
     * Returns a new {@code AddressBook} with the seven typical jobseekers and seven typical clients.
     * Additional fixtures such as {@code AMY}, {@code BOB}, {@code JACK} and {@code KATE} are excluded.
     */
    public static AddressBook getTypicalAddressBook() {
        AddressBook ab = new AddressBook();
        for (Jobseeker jobseeker : getTypicalJobseekers()) {
            ab.addJobseeker(jobseeker);
        }
        for (Client client : getTypicalClients()) {
            ab.addClient(client);
        }
        return ab;
    }

    /**
     * Returns a new list containing the seven typical jobseekers.
     */
    public static List<Jobseeker> getTypicalJobseekers() {
        return new ArrayList<>(Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE));
    }

    /**
     * Returns a new list containing the seven typical clients.
     */
    public static List<Client> getTypicalClients() {
        return new ArrayList<>(Arrays.asList(ADRIAN, BRENDA, CEDRIC, DIANA, ETHAN, FELICIA, GAVIN));
    }
}
