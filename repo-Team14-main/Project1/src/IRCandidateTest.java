import static org.junit.Assert.assertEquals;


import org.junit.Test;

public class IRCandidateTest {
    // constructor test
    @Test
    public void testIRCandidate(){
        final double DELTA = 1e-15;
        String name = "biden";
        IRCandidate demo = new IRCandidate("biden");
        assertEquals(name, demo.getName());
        assertEquals(0, demo.getVotes());
        assertEquals(0.0, demo.getPercent(), DELTA);

    }

    @Test
    public void getNameTest(){
        String name = "biden";
        IRCandidate demo = new IRCandidate(name);
        assertEquals(name, demo.getName());
    }

    @Test
    public void getVotesTest(){
        int votes = 2;
        IRCandidate demo = new IRCandidate("biden");
        demo.setVotes(votes);
        assertEquals(votes, demo.getVotes());
    }

    @Test
    public void getPercentTest() {
        final double DELTA = 1e-15;
        double percent = 25.5;
        IRCandidate demo = new IRCandidate("biden");
        demo.setPercent(percent);
        assertEquals(percent, demo.getPercent(), DELTA);
    }

    @Test
    public void setVotesTest() {
        int votes = 2;
        IRCandidate demo = new IRCandidate("biden");
        demo.setVotes(votes);
        assertEquals(votes, demo.getVotes());
    }

    @Test
    public void setPercentTest() {
        final double DELTA = 1e-15;
        double percent = 25.5;
        IRCandidate demo = new IRCandidate("biden");
        demo.setPercent(percent);
        assertEquals(percent, demo.getPercent(), DELTA);
    }
}