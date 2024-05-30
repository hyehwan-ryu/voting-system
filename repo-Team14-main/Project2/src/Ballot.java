import java.util.Map;
import java.lang.Exception;

/**
 * Represents a ballot cast per individual in the csv file
 * It contains a hashmap that maps a candidate's name/political party name to their preferenc ranking
 * Rankings go from 1 to n candidates, where 1 is most preferred
 * @author Bilal Osman
 */
public class Ballot {

    private Map<String, Integer> vote; // Hashmap of candidate to preference rating

    /**
     * Creates a ballot that shows the ranking for each candidate/political party
     *
     * @param   vote    hashmap of candidate name/political party name to ranking from parsed
     *                  information from the file
     */
    public Ballot(Map<String, Integer> vote) {
        this.vote = vote;
    } // ballot constructor

    /**
     * Getter method for the vote attribute
     *
     * @return   vote    hashmap of candidate name/political party name to ranking from parsed
     *                   information from the file
     */
    public Map<String, Integer> getVote() {
        return vote;
    }

    /**
     * Looks for candidate's/political party's ranking in hashmap
     *
     * @param   candidate    candidate/political party name
     * @return  the ranking of the candidate as an integer, if candidate doesn't exist 0 is returned
     */
    public int getVoteForCandidate(String candidate) { // returns rating for candidate
        try {
            return vote.get(candidate);
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Looks for ranking value and returns the candidate/political party associated to it
     *
     * @param   preference  ranking value
     * @return  the candidate's/political party's name or null if candidate was not rated
     */
    public String getCandidateFromPreference(int preference) { // returns candidate from rating preference
        for (Map.Entry<String, Integer> i : vote.entrySet()) {
            if (i.getValue() == preference) {
                return i.getKey();
            }
        }
        return null; // returns null if candidate was not rated
    }
}

