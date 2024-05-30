import static org.junit.Assert.assertEquals;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class POTest {

    @Test
    public void testPO() throws IOException {
        int totalVotes = 10;
        List<Ballot> ballots = new ArrayList<Ballot>();
        HashMap<String, Integer> ballot1 = new HashMap<>();
        Ballot testBallot = new Ballot(ballot1);
        ballots.add(testBallot);
        List<POCandidate> candidates = new ArrayList<>();
        Audit auditFile = new Audit("testFile.txt");

        PopularityOnly demo = new PopularityOnly(totalVotes, ballots, candidates, auditFile);
        assertEquals(totalVotes, demo.getTotalVotes());
        assertEquals( ballots, demo.getBallots());
        assertEquals(auditFile, demo.getAudit());
    }


    @Test
    public void MajorityWinnerTest() throws IOException {

        List<Ballot> ballots = new ArrayList<Ballot>();

        HashMap<String, Integer> ballot1 = new HashMap<>();
        ballot1.put("cat", 5);
        ballot1.put("dog", 1);
        ballot1.put("rat", 4);
        HashMap<String, Integer> ballot2 = new HashMap<>();
        ballot2.put("cat", 6);
        ballot2.put("dog", 2);
        ballot2.put("rat", 2);
        HashMap<String, Integer> ballot3 = new HashMap<>();
        ballot3.put("cat", 4);
        ballot3.put("dog", 5);
        ballot3.put("rat", 1);

        Ballot testBallot1 = new Ballot(ballot1);
        Ballot testBallot2 = new Ballot(ballot2);
        Ballot testBallot3 = new Ballot(ballot3);

        ballots.add(testBallot1);
        ballots.add(testBallot2);
        ballots.add(testBallot3);

        List<POCandidate> candidates = new ArrayList<>();
        POCandidate candidate1 = new POCandidate("cat");
        POCandidate candidate2 = new POCandidate("dog");
        POCandidate candidate3 = new POCandidate("rat");
        candidates.add(candidate1);
        candidates.add(candidate2);
        candidates.add(candidate3);

        Audit auditFile = new Audit("testFile.txt");

        PopularityOnly test1 = new PopularityOnly(3, ballots, candidates, auditFile);
        test1.calculateWinner();
        String winnerName = test1.getWinningCandidate();
        assertEquals("cat", winnerName);
    }

    @Test
    public void tiedWinnerTest() throws IOException {

        List<Ballot> ballots = new ArrayList<Ballot>();

        HashMap<String, Integer> ballot1 = new HashMap<>();
        ballot1.put("cat", 2);
        ballot1.put("dog", 2);

        HashMap<String, Integer> ballot2 = new HashMap<>();
        ballot2.put("cat", 3);
        ballot2.put("dog", 3);


        Ballot testBallot1 = new Ballot(ballot1);
        Ballot testBallot2 = new Ballot(ballot2);

        ballots.add(testBallot1);
        ballots.add(testBallot2);

        List<POCandidate> candidates = new ArrayList<>();
        POCandidate candidate1 = new POCandidate("cat");
        POCandidate candidate2 = new POCandidate("dog");
        candidates.add(candidate1);
        candidates.add(candidate2);

        Audit auditFile = new Audit("testFile.txt");

        PopularityOnly test1 = new PopularityOnly(2, ballots, candidates, auditFile);
        test1.calculateWinner();
        boolean result = false;
        if (test1.getWinningCandidate().compareTo("") != 0){
            result = true;
        }
        assertEquals(true, result);

    }

}
