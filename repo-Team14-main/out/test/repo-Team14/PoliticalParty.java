import java.util.Queue;

/**
 * Contains information regarding a political party for ClosedpartyList election. Stores the name of the party and
 * a queue of the names of candidates within that party. Also includes the number of votes received, number of seats,
 * and the remainder of votes to be used for calculating seat allocation.
 *
 * @author Hyehwan Ryu, Soorya Sundravel
 */
public class PoliticalParty {

    private String partyName;
    private Queue<String> candidateNames;
    private int partySeats;
    private int partyVotes;
    private int remainderVotes;

    /**
     * Constructor for the PoliticalParty.
     *
     * @param   partyName    The name of the party.
     * @param   candidateNames    An ordered queue of candidates' names in the election.
     */
    public PoliticalParty(String partyName, Queue<String> candidateNames)
    {
        this.partyName = partyName;
        this.candidateNames = candidateNames;
        partySeats = 0;
        partyVotes = 0;
        remainderVotes = 0;
    }

    /**
     * Gets the name of the party.
     *
     * @return  The name of the party.
     */
    public String getPartyName()
    {
        return partyName;
    }

    /**
     * Gets the names of the candidates in party, in an ordered queue.
     *
     * @return  The names of candidates.
     */
    public Queue<String> getCandidateNames()
    {
        return candidateNames;
    }

    /**
     * Gets the number of the seats of the party.
     *
     * @return  The number of the seats.
     */
    public int getPartySeats()
    {
        return partySeats;
    }

    /**
     * Gets the number of the votes received by the party.
     *
     * @return  The number of the votes.
     */
    public int getPartyVotes()
    {
        return partyVotes;
    }

    /**
     * Sets the number of seats of the party.
     *
     * @param   seats   The number of the seats that we want to set to the party.
     */
    public void setPartySeats(int seats)
    {
        partySeats = seats;
    }

     /**
     * Sets the number of the votes received by the party.
     *
     * @param  votes The number of the votes.
     */
    public void setPartyVotes(int votes)
    {
        partyVotes = votes;
    }

    /**
     * Sets the number of remaining votes of the party.
     *
     * @param   votes The number of remaining votes.
     *
     */
    public void setRemainderVotes(int votes)
    {
        remainderVotes = votes;
    }

    /**
     * Gets the number of remaining votes of the party. Used to allocate seats in ClosedpartyList.
     *
     * @return  The number of remaining votes.
     */
    public int getRemainder()
    {
        return remainderVotes;
    }
}