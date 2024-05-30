import java.util.HashMap;
import java.util.Map;

/**
 * Contains the progress of an individual candidate throughout an IR election.
 * Primarily for use in the results table displayed when running an IR election.
 * @author Liam O'Neil
 */
public class CandidateProgress {
    String name; 
    String party;
    Map<Integer,Integer> results = new HashMap<Integer,Integer>(); // maps round# to votes for that round

    /**
     * Default constructor. Initializes name and party attributes to empty Strings.
     */
    public CandidateProgress(){
        this.name = "";
        this.party = "";
    }

    /**
     * Create an object with a specified name and party.
     * @param name  name of the candidate
     * @param party party of the candidate
     */
    public CandidateProgress(String name, String party){
        this.name = name;
        this.party = party;
    }
    
    /**
     * Getter for name attribute.
     * @return name of the candidate tracked by this progress object.
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for the party attribute.
     * @return party of the candidate tracked by this progress object.
     */
    public String getParty() {
        return party;
    }

    /**
     * Setter for the name attribute.
     * @param name  the name of the candidate to be tracked by this progress object.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Setter for the party attribute.
     * @param party the party of the candidate to be tracked by this progress object.
     */
    public void setParty(String party) {
        this.party = party;
    }

    /**
     * Adds a round and results to the candidate's 'results' map.
     * @param round the round to be added to the candidate's progress.
     * @param votes the votes awarded to the candidate for the specified round.
     */
    public void addRound(int round, int votes) {
        results.put(round, votes);
    }

    /**
     * Getter for the 'results' attribute.
     * @return a Hashmap containing a mapping from round to votes for the candidate.
     */
    public Map<Integer,Integer> getResults() {
        return results;
    }

}
