
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.*;


import org.junit.Test;

public class ElectionTest {
    // default constructor test
    public void defaultElectionTest() throws IOException {
        Election test1 = new Election();

        assertEquals("", test1.getElectionType());
        assertEquals(0, test1.getTotalVotes());
        List<Ballot> ballots2 = new ArrayList<Ballot>();
        assertEquals(ballots2, test1.getBallots());
        Audit auditFile = new Audit("");
        assertEquals(auditFile, test1.getAudit());
    }

    // overloaded constructor test
    @Test
    public void testElection() throws IOException {
        String electionType1 = "IR";
        int totalVotes1 = 12;
        List<Ballot> ballots = new ArrayList<Ballot>();
        HashMap<String, Integer> ballot1 = new HashMap<>();
        Ballot testBallot = new Ballot(ballot1);
        ballots.add(testBallot);
        Audit auditFile = new Audit("testFile.txt");
        Election test = new Election(electionType1, totalVotes1, ballots, auditFile);
        assertEquals(electionType1, test.getElectionType());
        assertEquals(totalVotes1, test.getTotalVotes());
        assertEquals(ballots, test.getBallots());
        assertEquals(auditFile, test.getAudit());
    }

    @Test
    public void getElectionTypeTest() throws IOException{
        String electionType = "IR";
        Audit auditFile = new Audit("testFile.txt");
        List<Ballot> ballots = new ArrayList<Ballot>();
        HashMap<String, Integer> ballot1 = new HashMap<>();
        Ballot testBallot = new Ballot(ballot1);
        ballots.add(testBallot);
        Election demo = new Election(electionType, 12, ballots, auditFile);
        assertEquals(electionType, demo.getElectionType());

    }

    @Test
    public void getTotalVotesTest() throws IOException {
        int totalVotes = 100;
        Audit auditFile = new Audit("testFile.txt");
        List<Ballot> ballots = new ArrayList<Ballot>();
        HashMap<String, Integer> ballot1 = new HashMap<>();
        Ballot testBallot = new Ballot(ballot1);
        ballots.add(testBallot);
        Election demo = new Election("IR", totalVotes, ballots, auditFile);
        assertEquals(totalVotes, demo.getTotalVotes());
    }

    @Test
    public void getBallotsTest() throws IOException {
        Audit auditFile = new Audit("testFile.txt");
        List<Ballot> ballots = new ArrayList<Ballot>();
        HashMap<String, Integer> ballot1 = new HashMap<>();
        Ballot testBallot = new Ballot(ballot1);
        ballots.add(testBallot);
        Election demo = new Election("IR", 100, ballots, auditFile);
        assertEquals(ballots, demo.getBallots());
    }

    @Test
    public void getAuditTest() throws IOException {
        Audit auditFile = new Audit("testFile.txt");
        List<Ballot> ballots = new ArrayList<Ballot>();
        HashMap<String, Integer> ballot1 = new HashMap<>();
        Ballot testBallot = new Ballot(ballot1);
        ballots.add(testBallot);
        Election demo = new Election("IR", 100, ballots, auditFile);
        assertEquals(auditFile, demo.getAudit());
    }

}
