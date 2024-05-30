import junit.framework.TestCase;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.*;

public class BallotTest {

    @Test
    public void BallotTest() {
        Map<String, Integer> maptest = new HashMap<String, Integer>();
        maptest.put("cand1", 1);
        maptest.put("cand2", 2);
        maptest.put("cand3", 3);
        Ballot bt = new Ballot(maptest);

        assertEquals(maptest, bt.getVote());
    }

    @Test
    public void GetVoteTest() {
        Map<String, Integer> maptest1 = new HashMap<String, Integer>();
        maptest1.put("cand1", 1);
        maptest1.put("cand2", 2);
        maptest1.put("cand3", 3);
        Ballot bt = new Ballot(maptest1);

        Map<String, Integer> maptest2 = new HashMap<String, Integer>();
        maptest2.put("cand1", 1);
        maptest2.put("cand2", 2);
        maptest2.put("cand3", 3);

        assertEquals(maptest2, bt.getVote());
        maptest2.remove("cand3", 3);
        assertNotEquals(maptest2, bt.getVote());
    }

    @Test
    public void GetVoteForCandidateTest() {
        Map<String, Integer> maptest = new HashMap<String, Integer>();
        maptest.put("cand1", 100);
        maptest.put("cand2", 200);
        Ballot bt = new Ballot(maptest);
        int rating1 = bt.getVoteForCandidate("cand1");
        int rating2 = bt.getVoteForCandidate("cand2");
        int ratingnull = bt.getVoteForCandidate("cand3");

        assertEquals(100, rating1);
        assertEquals(200, rating2);
        assertEquals(0, ratingnull);
    }

    @Test
    public void GetCandidateFromPreferenceTest() {
        Map<String, Integer> maptest = new HashMap<String, Integer>();
        maptest.put("cand1", 100);
        maptest.put("cand2", 200);
        Ballot bt = new Ballot(maptest);
        String cand1 = bt.getCandidateFromPreference(100);
        String cand2 = bt.getCandidateFromPreference(200);
        String candnull = bt.getCandidateFromPreference(300);

        assertEquals("cand1", cand1);
        assertEquals("cand2", cand2);
        assertEquals(null, candnull);
    }
}
