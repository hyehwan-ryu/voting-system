

/**
 * Contains information regarding a candidate for InstantRunoff election. Stores the name of candidate, the number of
 * votes they have received, and the percentage within the election.
 *
 * @author Hyehwan Ryu, Soorya Sundravel
 */
public class IRCandidate {

    private String name;
    private int votes;
    private double percentVotes;


    /**
     * Constructor or the IRCandidate class. Sets the name of the candidate, and initializes the votes and
     * percentVotes to 0.
     *
     * @param name The name of the candidate.
     *
     */
    public IRCandidate(String name)
    {
        this.name = name;
        votes = 0;
        percentVotes = 0.0;
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
     * Gets the percentage of votes of a candidate within an election.
     * @return  The percentage of votes.
     *
     */
    public double getPercent()
    {
        return percentVotes;
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

}