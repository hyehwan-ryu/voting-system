import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class POCandidateTest {

    @Test
    public void testPOCandidate(){
        final double DELTA = 1e-15;
        String name = "biden";
        POCandidate demo = new POCandidate("biden");
        assertEquals(name, demo.getName());
        assertEquals(0, demo.getVotes());

    }

    @Test
    public void getNameTest(){
        String name = "biden";
        POCandidate demo = new POCandidate(name);
        assertEquals(name, demo.getName());
    }

    @Test
    public void getVotesTest(){
        int votes = 2;
        POCandidate demo = new POCandidate("biden");
        demo.setVotes(votes);
        assertEquals(votes, demo.getVotes());
    }


    @Test
    public void setVotesTest() {
        int votes = 2;
        POCandidate demo = new POCandidate("biden");
        demo.setVotes(votes);
        assertEquals(votes, demo.getVotes());
    }

}
