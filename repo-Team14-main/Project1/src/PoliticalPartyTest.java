import static org.junit.Assert.assertEquals;
import java.util.*;


import org.junit.Test;

public class PoliticalPartyTest {
    // constructor test

    @Test
    public void testPoliticalParty(){
        Queue<String> candidateNames = new LinkedList<String>();
        String partyName = "Democrats";
        candidateNames.add("biden");
        candidateNames.add("kamala");
        PoliticalParty demo = new PoliticalParty(partyName, candidateNames);
        assertEquals(partyName, demo.getPartyName());
        assertEquals(candidateNames, demo.getCandidateNames());
        assertEquals(0, demo.getPartySeats());
        assertEquals(0, demo.getPartyVotes());
        assertEquals(0, demo.getRemainder());

    }

    @Test
    public void getPartyNameTest(){
        Queue<String> names = new LinkedList<String>();
        names.add("biden");
        names.add("kamala");
        String partyName = "Democrats";
        PoliticalParty demo = new PoliticalParty(partyName, names);
        assertEquals(partyName, demo.getPartyName());
    }

    @Test
    public void getCandidateNamesTest(){
        Queue<String> candidateNames = new LinkedList<String>();
        String name1 = "biden";
        String name2 = "kamala";
        candidateNames.add(name1);
        candidateNames.add(name2);
        PoliticalParty demo = new PoliticalParty("Democrats", candidateNames);
        assertEquals(candidateNames, demo.getCandidateNames());

    }
    @Test
    public void getPartySeatsTest(){
        Queue<String> names = new LinkedList<String>();
        names.add("biden");
        names.add("kamala");
        PoliticalParty demo = new PoliticalParty("Democrats", names);
        int partySeats = 2;
        demo.setPartySeats(partySeats);
        assertEquals(partySeats, demo.getPartySeats());
    }
    @Test
    public void getPartyVotesTest(){
        Queue<String> names = new LinkedList<String>();
        names.add("biden");
        names.add("kamala");
        PoliticalParty demo = new PoliticalParty("Democrats", names);
        int partyVotes = 3;
        demo.setPartyVotes(partyVotes);
        assertEquals(partyVotes, demo.getPartyVotes());
    }
    @Test
    public void setPartySeatsTest(){
        Queue<String> names = new LinkedList<String>();
        names.add("biden");
        names.add("kamala");
        PoliticalParty demo = new PoliticalParty("Democrats", names);
        int partySeats = 2;
        demo.setPartySeats(partySeats);
        assertEquals(partySeats, demo.getPartySeats());
    }
    @Test
    public void setPartyVotesTest(){
        Queue<String> names = new LinkedList<String>();
        names.add("biden");
        names.add("kamala");
        PoliticalParty demo = new PoliticalParty("Democrats", names);
        int partyVotes = 3;
        demo.setPartyVotes(partyVotes);
        assertEquals(partyVotes, demo.getPartyVotes());

    }

    @Test
    public void setRemainderVotesTest(){
        Queue<String> names = new LinkedList<String>();
        names.add("biden");
        names.add("kamala");
        PoliticalParty demo = new PoliticalParty("Democrats", names);
        int votes = 4;
        demo.setRemainderVotes(votes);
        assertEquals(votes, demo.getRemainder());
    }

    @Test
    public void getRemainderTest(){
        Queue<String> names = new LinkedList<String>();
        names.add("biden");
        names.add("kamala");
        PoliticalParty demo = new PoliticalParty("Democrats", names);
        int votes = 4;
        demo.setRemainderVotes(votes);
        assertEquals(votes, demo.getRemainder());

    }

}

