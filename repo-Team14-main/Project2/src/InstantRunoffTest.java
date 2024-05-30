import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

import static org.junit.Assert.*;

public class InstantRunoffTest {
    // constructor test
    @Test
    public void testInstantRunoff() throws IOException{
        int totalVotes = 10;
        List<Ballot> ballots = new ArrayList<Ballot>();
        HashMap<String, Integer> ballot1 = new HashMap<>();
        Ballot testBallot = new Ballot(ballot1);
        ballots.add(testBallot);
        List<IRCandidate> candidates = new ArrayList<>();
        Audit auditFile = new Audit("testFile.txt");

        InstantRunoff demo = new InstantRunoff(totalVotes, ballots, candidates, null, auditFile);
        assertEquals(ballots.size(), demo.getTotalVotes());
        assertEquals( ballots, demo.getBallots());
        assertEquals(auditFile, demo.getAudit());
    }

    //Calculate the winning candidate when there is a majority vote. (Over 50 percent)
    @Test
    public void MajorityWinnerTest() throws IOException {

        List<Ballot> ballots = new ArrayList<Ballot>();

        HashMap<String, Integer> ballot1 = new HashMap<>();
        ballot1.put("cat", 1);
        ballot1.put("dog", 2);
        ballot1.put("rat", 3);
        HashMap<String, Integer> ballot2 = new HashMap<>();
        ballot2.put("cat", 1);
        ballot2.put("dog", 3);
        ballot2.put("rat", 2);
        HashMap<String, Integer> ballot3 = new HashMap<>();
        ballot3.put("cat", 2);
        ballot3.put("dog", 1);
        ballot3.put("rat", 3);

        Ballot testBallot1 = new Ballot(ballot1);
        Ballot testBallot2 = new Ballot(ballot2);
        Ballot testBallot3 = new Ballot(ballot3);

        ballots.add(testBallot1);
        ballots.add(testBallot2);
        ballots.add(testBallot3);

        List<IRCandidate> candidates = new ArrayList<>();
        IRCandidate candidate1 = new IRCandidate("cat (I)");
        IRCandidate candidate2 = new IRCandidate("dog (G)");
        IRCandidate candidate3 = new IRCandidate("rat (D)");
        candidates.add(candidate1);
        candidates.add(candidate2);
        candidates.add(candidate3);

        Audit auditFile = new Audit("testFile.txt");

        InstantRunoff test1 = new InstantRunoff(3, ballots, candidates, null, auditFile);
        test1.calculateWinner();
        String winnerName = test1.getWinningCandidate();
        assertEquals("cat (I)", winnerName);

    }

    //Calculate the winning candidate when there is a tie.
    @Test
    public void tiedWinnerTest() throws IOException {

        List<Ballot> ballots = new ArrayList<Ballot>();

        HashMap<String, Integer> ballot1 = new HashMap<>();
        ballot1.put("cat", 1);
        ballot1.put("dog", 2);

        HashMap<String, Integer> ballot2 = new HashMap<>();
        ballot2.put("cat", 2);
        ballot2.put("dog", 1);


        Ballot testBallot1 = new Ballot(ballot1);
        Ballot testBallot2 = new Ballot(ballot2);

        ballots.add(testBallot1);
        ballots.add(testBallot2);

        List<IRCandidate> candidates = new ArrayList<>();
        IRCandidate candidate1 = new IRCandidate("cat (I)");
        IRCandidate candidate2 = new IRCandidate("dog (G)");
        candidates.add(candidate1);
        candidates.add(candidate2);

        Audit auditFile = new Audit("testFile.txt");

        InstantRunoff test1 = new InstantRunoff(2, ballots, candidates, null, auditFile);
        test1.calculateWinner();
        boolean result = false;
        if (test1.getWinningCandidate().compareTo("") != 0){
            result = true;
        }
        assertEquals(true, result);

    }

    //Calculate the winning candidate when we need more than one round.
    @Test
    public void redistributeWinnerTest() throws IOException {
//
        List<Ballot> ballots = new ArrayList<Ballot>();

        HashMap<String, Integer> ballot1 = new HashMap<>();
        ballot1.put("cat", 1);
        ballot1.put("dog", 2);
        ballot1.put("rat", 3);
        HashMap<String, Integer> ballot2 = new HashMap<>();
        ballot2.put("cat", 3);
        ballot2.put("dog", 1);
        ballot2.put("rat", 2);
        HashMap<String, Integer> ballot3 = new HashMap<>();
        ballot3.put("cat", 2);
        ballot3.put("dog", 1);
        ballot3.put("rat", 3);
        HashMap<String, Integer> ballot4 = new HashMap<>();
        ballot3.put("cat", 2);
        ballot3.put("dog", 1);
        ballot3.put("rat", 3);

        Ballot testBallot1 = new Ballot(ballot1);
        Ballot testBallot2 = new Ballot(ballot2);
        Ballot testBallot3 = new Ballot(ballot3);
        Ballot testBallot4 = new Ballot(ballot4);

        ballots.add(testBallot1);
        ballots.add(testBallot2);
        ballots.add(testBallot3);
        ballots.add(testBallot4);

        List<IRCandidate> candidates = new ArrayList<>();
        IRCandidate candidate1 = new IRCandidate("cat (I)");
        IRCandidate candidate2 = new IRCandidate("dog (G)");
        IRCandidate candidate3 = new IRCandidate("rat (D)");
        candidates.add(candidate1);
        candidates.add(candidate2);
        candidates.add(candidate3);

        Audit auditFile = new Audit("testFile.txt");

        InstantRunoff test1 = new InstantRunoff(4, ballots, candidates, null, auditFile);
        test1.calculateWinner();
        String winnerName = test1.getWinningCandidate();

        String loser = "rat (D)";
        assertNotEquals(loser, winnerName);

        loser = "cat (I)";
        assertNotEquals(loser, winnerName);

        HashMap<String, Integer> ballot5 = new HashMap<>();
        ballot5.put("cat (I)", 1);
        ballot5.put("dog (G)", 3);
        ballot5.put("rat (D)", 2);
        Ballot testBallot5 = new Ballot(ballot5);

        HashMap<String, Integer> ballot6 = new HashMap<>();
        ballot6.put("cat (I)", 1);
        ballot6.put("dog (G)", 3);
        ballot6.put("rat (D)", 2);
        Ballot testBallot6 = new Ballot(ballot6);

        ballots.add(testBallot5);
        ballots.add(testBallot6);

        test1 = new InstantRunoff(4, ballots, candidates, null, auditFile);
        test1.calculateWinner();

        loser = "rat (D)";
        winnerName = test1.getWinningCandidate();
        assertNotEquals(loser, winnerName);

        loser = "dog (G)";
        assertNotEquals(loser, winnerName);

    }
}
