import java.util.*;
import java.util.List;
import java.io.IOException;

/**
 * Provides an abstract class structure to hold information about an election. The election class is inherited by
 * InstantRunoff and ClosedpartyList to create a specific implementation.
 *
 * @author Hyehwan Ryu, Soorya Sundravel
 */
public class Election {

    protected String electionType;
    protected int totalVotes;
    protected List<Ballot> ballots;
    protected Audit auditFile;

    /**
     * Default constructor for the Election class.
     *
     * @throws IOException Signals that the file cannot be opened, or the text cannot be written to the file.
     */
    public Election() throws IOException {
        electionType = "";
        totalVotes = 0;
        ballots = new ArrayList<Ballot>();
        try{
            auditFile = new Audit("");
        }
        catch (Exception e){
            return;
        }
    }
    /**
     * Overloaded constructor for the Election class.
     *
     * @param   electionType    The type of the election that we want to simulate between IR and CPL.
     * @param   totalVotes    The total number of the votes.
     * @param   ballots    A list of ballots containing voting information
     * @param   auditFile   Output file containing the results of the election.
     *
     * @throws IOException Signals that the file cannot be opened, or the text cannot be written to the file.
     */
    public Election(String electionType, int totalVotes, List<Ballot> ballots, Audit auditFile) throws IOException {
        this.electionType = electionType;
        this.totalVotes = totalVotes;
        this.ballots = ballots;
        try{
            this.auditFile = auditFile;
        }
        catch (Exception e){
            return;
        }

    }



    /**
     * Gets the type of election. Can be either IR or CPL style of election.
     *
     * @return  The type of the election.
     */
    public String getElectionType()
    {
        return electionType;
    }

    /**
     * Gets the total number of the votes
     *
     * @return  The number of the total votes of the election.
     */
    public int getTotalVotes()
    {
        return totalVotes;
    }

    /**
     * Gets the ballots containing voting information.
     *
     * @return  The list of ballots
     */
    public List<Ballot> getBallots() { return ballots; }

    /**
     * Gets the audit file containing election results.
     *
     * @return  The audit file
     */
    public Audit getAudit()
    {
        return auditFile;
    }

}
