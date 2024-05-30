import java.util.*;


/**
 * Represents a candidate in a Popularity Only election.
 * @author Liam O'Neil
 */
public class POCandidate {

    private String name;
    private int votes;

    private double percentVotes;

    /**
     * Constructor for POCandidate. Initializes votes attribute to 0.
     * 
     * @param name  the name of the candidate.
     */
    public POCandidate(String name)
    {
        this.name = name;
        votes = 0;
    }

    /**
     * Gets the name of the candidate
     * @return  The name of the candidate.
     */
    public String getName()
    {
        return name;
    }


    /**
     * Gets the number of votes for the given candidate.
     * @return  The number of votes.
     */
    public int getVotes()
    {
        return votes;
    }


    /**
     * Sets the number of votes of a candidate.
     *
     * @param votes The number of votes to set.
     */

    public void setVotes(int votes)
    {
        this.votes = votes;
    }

    /**
     * Sets the percentage of votes of a candidate within an election.
     *
     * @param percent  The percentage of votes to set.
     */
    public void setPercent(double percent)
    {
        this.percentVotes = percent;
    }

    /**
     * Gets the percentage of votes of a candidate within an election.
     * @return  The percentage of votes.
     *
     */
    public double getPercent()
    {
        return percentVotes;
    }
}


