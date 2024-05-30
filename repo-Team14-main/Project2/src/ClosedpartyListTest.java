import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.io.IOException;
import java.util.List;
import java.util.*;

import org.junit.Test;

public class ClosedpartyListTest {
    // constructor test
    @Test
    public void testClosedpartyList() throws IOException {
        int totalVotes = 10;
        List<Ballot> ballots = new ArrayList<Ballot>();
        HashMap<String, Integer> ballot1 = new HashMap<>();
        Ballot testBallot = new Ballot(ballot1);
        ballots.add(testBallot);
        int seatsAvailable = 1;
        List<PoliticalParty> parties = new ArrayList<>();

        Audit auditFile = new Audit("testFile.txt");
        ClosedpartyList demo = new ClosedpartyList(totalVotes, ballots, seatsAvailable, parties, auditFile);

        assertEquals(totalVotes, demo.getTotalVotes());
        assertEquals(seatsAvailable, demo.getSeatsAvailable());
        assertEquals(ballots, demo.getBallots());
        assertEquals(auditFile, demo.getAudit());
    }

    @Test
    public void getSeatsAvailableTest() throws IOException {
        List<Ballot> ballots = new ArrayList<Ballot>();
        HashMap<String, Integer> ballot1 = new HashMap<>();
        Ballot testBallot = new Ballot(ballot1);
        ballots.add(testBallot);
        List<PoliticalParty> parties = new ArrayList<>();

        Audit auditFile = new Audit("testFile.txt");
        ClosedpartyList demo = new ClosedpartyList(0, ballots, 5, parties, auditFile);
        assertNotEquals(10, demo.getSeatsAvailable());

        demo = new ClosedpartyList(0, ballots, 10, parties, auditFile);
        assertEquals(10, demo.getSeatsAvailable());
    }

    @Test
    public void calculateFirstAllocTest() throws IOException{

        List<Ballot> ballots = new ArrayList<Ballot>();
        List<PoliticalParty> parties = new ArrayList<>();
        PoliticalParty party1 = new PoliticalParty("catparty", null);
        PoliticalParty party2 = new PoliticalParty("dogparty", null);
        PoliticalParty party3 = new PoliticalParty("ratparty", null);
        parties.add(party1);
        parties.add(party2);
        parties.add(party3);

        HashMap<String, Integer> ballot1 = new HashMap<>();
        ballot1.put("catparty", 1);
        HashMap<String, Integer> ballot2 = new HashMap<>();
        ballot2.put("dogparty", 1);
        HashMap<String, Integer> ballot3 = new HashMap<>();
        ballot3.put("catparty", 1);
        HashMap<String, Integer> ballot4 = new HashMap<>();
        ballot3.put("ratparty", 1);
        HashMap<String, Integer> ballot5 = new HashMap<>();
        ballot3.put("dogparty", 1);

        Ballot testBallot1 = new Ballot(ballot1);
        Ballot testBallot2 = new Ballot(ballot2);
        Ballot testBallot3 = new Ballot(ballot3);
        Ballot testBallot4 = new Ballot(ballot4);
        Ballot testBallot5 = new Ballot(ballot5);

        ballots.add(testBallot1);
        ballots.add(testBallot2);
        ballots.add(testBallot3);
        ballots.add(testBallot4);
        ballots.add(testBallot5);

        Audit auditFile = new Audit("testFile.txt");
        String testtxt1 = "\n---- First round of seat allocations ----\n" +
                            "catparty awarded 1 seats from 2 votes.\n" +
                            "dogparty awarded 1 seats from 2 votes.\n" +
                            "ratparty awarded 0 seats from 1 votes.\n" +
                            "\n"+
                            "---- Second round of seat allocations ----\n";
        ClosedpartyList test1 = new ClosedpartyList(5, ballots, 2, parties, auditFile);
        test1.calculateFirstAlloc();
        assertEquals(testtxt1, test1.getResults());

        HashMap<String, Integer> ballot6 = new HashMap<>();
        ballot6.put("cat", 1);
        HashMap<String, Integer> ballot7 = new HashMap<>();
        ballot7.put("dog", 1);
        HashMap<String, Integer> ballot8 = new HashMap<>();
        ballot8.put("dog", 1);
        HashMap<String, Integer> ballot9 = new HashMap<>();
        ballot9.put("rat", 1);
        Ballot testBallot6 = new Ballot(ballot6);
        Ballot testBallot7 = new Ballot(ballot7);
        Ballot testBallot8 = new Ballot(ballot8);
        Ballot testBallot9 = new Ballot(ballot9);
        ballots.add(testBallot6);
        ballots.add(testBallot7);
        ballots.add(testBallot8);
        ballots.add(testBallot9);

        String testtxt2 = "\n---- First round of seat allocations ----\n" +
                            "ratparty awarded 1 seats from 1 votes.\n" +
                            "dogparty awarded 2 seats from 2 votes.\n" +
                            "catparty awarded 2 seats from 2 votes.\n" +
                            "\n" +
                            "---- Second round of seat allocations ----\n";

        ClosedpartyList test2 = new ClosedpartyList(5, ballots, 5, parties, auditFile);
        test2.calculateFirstAlloc();
        assertEquals(testtxt2, test2.getResults());
    }

    @Test
    public void calculateSecondAllocTest() throws IOException {

        List<Ballot> ballots = new ArrayList<Ballot>();
        List<PoliticalParty> parties = new ArrayList<>();
        PoliticalParty party1 = new PoliticalParty("catparty", null);
        PoliticalParty party2 = new PoliticalParty("dogparty", null);
        PoliticalParty party3 = new PoliticalParty("parakeetparty", null);
        PoliticalParty party4 = new PoliticalParty("ratparty", null);
        parties.add(party1);
        parties.add(party2);
        parties.add(party3);

        HashMap<String, Integer> ballot1 = new HashMap<>();
        ballot1.put("catparty", 1);
        HashMap<String, Integer> ballot2 = new HashMap<>();
        ballot2.put("dogparty", 1);
        HashMap<String, Integer> ballot3 = new HashMap<>();
        ballot3.put("catparty", 1);
        HashMap<String, Integer> ballot4 = new HashMap<>();
        ballot3.put("ratparty", 1);
        HashMap<String, Integer> ballot5 = new HashMap<>();
        ballot5.put("dogparty", 1);

        Ballot testBallot1 = new Ballot(ballot1);
        Ballot testBallot2 = new Ballot(ballot2);
        Ballot testBallot3 = new Ballot(ballot3);
        Ballot testBallot4 = new Ballot(ballot4);
        Ballot testBallot5 = new Ballot(ballot5);

        ballots.add(testBallot1);
        ballots.add(testBallot2);
        ballots.add(testBallot3);
        ballots.add(testBallot4);
        ballots.add(testBallot5);


        Audit auditFile = new Audit("testFile.txt");
        String testtxt1 = "\n---- First round of seat allocations ----\n" +
                "catparty awarded 2 seats from 2 votes.\n" +
                "dogparty awarded 2 seats from 2 votes.\n" +
                "parakeetparty awarded 0 seats from 0 votes.\n" +
                "\n" +
                "---- Second round of seat allocations ----\n" +
                "parakeetparty awarded 1 seat from 0 remainder votes.\n";
        ClosedpartyList test1 = new ClosedpartyList(5, ballots, 5, parties, auditFile);
        test1.calculateFirstAlloc();
        assertEquals(testtxt1, test1.getResults());

        HashMap<String, Integer> ballot6 = new HashMap<>();
        ballot6.put("cat", 1);
        HashMap<String, Integer> ballot7 = new HashMap<>();
        ballot7.put("dog", 1);
        HashMap<String, Integer> ballot8 = new HashMap<>();
        ballot8.put("dog", 1);
        HashMap<String, Integer> ballot9= new HashMap<>();
        ballot9.put("rat", 1);
        Ballot testBallot6 = new Ballot(ballot6);
        Ballot testBallot7 = new Ballot(ballot7);
        Ballot testBallot8 = new Ballot(ballot8);
        Ballot testBallot9 = new Ballot(ballot9);
        ballots.add(testBallot6);
        ballots.add(testBallot7);
        ballots.add(testBallot8);
        ballots.add(testBallot9);

        String testtxt2 = "\n---- First round of seat allocations ----\n" +
                "parakeetparty awarded 0 seats from 0 votes.\n" +
                "dogparty awarded 2147483647 seats from 2 votes.\n" +
                "catparty awarded 2147483647 seats from 2 votes.\n" +
                "\n" +
                "---- Second round of seat allocations ----\n" +
                "catparty awarded 1 seat from 2 remainder votes.\n" +
                "dogparty awarded 1 seat from 2 remainder votes.\n" +
                "parakeetparty awarded 1 seat from 0 remainder votes.\n";

        ClosedpartyList test2 = new ClosedpartyList(5, ballots, 7, parties, auditFile);
        test2.calculateFirstAlloc();
        assertEquals(testtxt2, test2.getResults());
    }
}


