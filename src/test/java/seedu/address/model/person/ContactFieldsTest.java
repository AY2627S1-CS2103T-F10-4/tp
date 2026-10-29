package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.model.job.Job;

public class ContactFieldsTest {

    @Test
    public void constructors_omittedFields_defaultToEmpty() {
        Jobseeker jobseeker = new Jobseeker(ALICE.getName(), ALICE.getPhone(), ALICE.getEmail(),
                ALICE.getAddress(), ALICE.getTags(), true);
        Jobseeker withRemark = new Jobseeker(ALICE.getName(), ALICE.getPhone(), ALICE.getEmail(),
                ALICE.getAddress(), ALICE.getTags(), ALICE.getRemark(), true);
        assertEquals(new Qualification(""), jobseeker.getHighestQualification());
        assertEquals(new Qualification(""), withRemark.getHighestQualification());
        assertTrue(jobseeker.isHidden());
        assertTrue(withRemark.isHidden());

        Client client = new Client(ALICE.getName(), ALICE.getPhone(), ALICE.getEmail(),
                ALICE.getAddress(), ALICE.getTags(), true);
        Client clientWithRemark = new Client(ALICE.getName(), ALICE.getPhone(), ALICE.getEmail(),
                ALICE.getAddress(), ALICE.getTags(), ALICE.getRemark(), true);
        assertTrue(client.getAvailableJobs().isEmpty());
        assertTrue(clientWithRemark.getAvailableJobs().isEmpty());
        assertTrue(client.isHidden());
        assertTrue(clientWithRemark.isHidden());
    }

    @Test
    public void jobseeker_qualification_affectsEqualityButNotIdentity() {
        Jobseeker original = jobseeker(new Qualification("Diploma"));
        Jobseeker copy = jobseeker(new Qualification("Diploma"));
        Jobseeker edited = jobseeker(new Qualification("Degree"));
        assertEquals(new Qualification("Diploma"), original.getHighestQualification());
        assertEquals(original, copy);
        assertEquals(original.hashCode(), copy.hashCode());
        assertNotEquals(original, edited);
        assertTrue(original.isSamePerson(edited));
        assertTrue(original.toString().contains("highestQualification=Diploma"));
        assertThrows(NullPointerException.class, () -> jobseeker(null));
    }

    @Test
    public void client_jobs_areDefensivelyCopiedAndImmutable() {
        Job job = new Job("Software Engineer");
        Set<Job> jobs = new HashSet<>(Set.of(job));
        Client client = client(jobs);
        jobs.clear();
        assertEquals(Set.of(job), client.getAvailableJobs());
        assertThrows(UnsupportedOperationException.class, () -> client.getAvailableJobs().clear());
        assertThrows(NullPointerException.class, () -> client(null));
        jobs.add(null);
        assertThrows(NullPointerException.class, () -> client(jobs));
    }

    @Test
    public void client_jobs_affectEqualityButNotIdentity() {
        Set<Job> jobs = Set.of(new Job("Software Engineer"), new Job("Data Analyst"));
        Client original = client(jobs);
        Client copy = client(new HashSet<>(jobs));
        Client edited = client(Set.of());
        assertEquals(original, copy);
        assertEquals(original.hashCode(), copy.hashCode());
        assertNotEquals(original, edited);
        assertTrue(original.isSamePerson(edited));
        assertTrue(original.toString().contains("availableJobs="));
    }

    @Test
    public void equals_differentContactTypes_isSymmetric() {
        Client client = client(Set.of());
        Jobseeker jobseeker = jobseeker(new Qualification(""));
        assertNotEquals(client, jobseeker);
        assertNotEquals(jobseeker, client);
    }

    private static Jobseeker jobseeker(Qualification qualification) {
        return new Jobseeker(ALICE.getName(), ALICE.getPhone(), ALICE.getEmail(), ALICE.getAddress(),
                ALICE.getTags(), ALICE.getRemark(), qualification, ALICE.isHidden());
    }

    private static Client client(Set<Job> jobs) {
        return new Client(ALICE.getName(), ALICE.getPhone(), ALICE.getEmail(), ALICE.getAddress(),
                ALICE.getTags(), ALICE.getRemark(), jobs, ALICE.isHidden());
    }
}
