import java.io.IOException;
import java.util.*;




/**
 * Implements functionality to simulate Popularity Only election. Selects a candidate based on majority of votes.
 * In case of a tie, the program randomly selects a winner.
 *
 * @author Hyehwan Ryu
 */
public class PopularityOnly extends Election {
    private String winnerName;
    private List<POCandidate> candidates;
    private Map<POCandidate,Double> losers = new HashMap<>();
    private String electionInfo = "";
    private String loserName;
    private int winningIndex;


    /**
     * Constructor for the InstantRunoff.
     *
     * @param   totalVotes    The number of total votes.
     * @param   ballots    A list of ballots containing voting information.
     * @param   candidates    A list of candidates' names in the election.
     * @param   auditFile   Output file containing the results of the election.
     *
     * @throws IOException Signals that the file cannot be opened, or the text cannot be written to the file.
     */
    public PopularityOnly(int totalVotes, List<Ballot> ballots, List<POCandidate> candidates, Audit auditFile) throws IOException {
        super("PO", totalVotes, ballots, auditFile);
        this.candidates = candidates;
        initialInfo();
    }

    private void initialInfo() throws IOException{
        electionInfo += "\nPopularity Only election\n";
        electionInfo += "- - - - - - - - - - - - ";
        System.out.println(electionInfo);
        electionInfo += "\n\nTotal votes: " + totalVotes;
        electionInfo += "\nCandidates: ";
        for (int i = 0; i < candidates.size(); i++) {
            electionInfo += "\n\t" + candidates.get(i).getName();
        }
        electionInfo += "\n";
        auditFile.appendString(electionInfo);
        electionInfo = "";
    }

    /**
     * Calculates the winning candidate of an election based on majority number of votes.
     *
     * @throws IOException Signals that the file cannot be opened, or the text cannot be written to the file.
     */

    public void calculateWinner() throws IOException{
        try {
            assignBallots();
            int curVote = candidates.get(0).getVotes();
            winnerName = candidates.get(0).getName();
            winningIndex = 0;

            List<Integer> tieArray = new ArrayList<>();

            for (int i = 1; i < candidates.size(); i++) { // calculate winner
                if (candidates.get(i).getVotes() > curVote){
                    curVote = candidates.get(i).getVotes();
                    winnerName = candidates.get(i).getName();
                    winningIndex = i;
                }
            }

            tieArray.add(winningIndex);

            for (int i = 0; i < candidates.size(); i++) { // calculate winner
                if ( winningIndex != i && candidates.get(i).getVotes() == candidates.get(winningIndex).getVotes() ){ //check if candidates are different and if they have same number of votes
                    //means, tie
                    tieArray.add(i); // add the index of candidate to the array to call tie breaker later
                }
            }

            if (tieArray.size() >= 2){
                //call tie breaker
                tieBreaker(tieArray);

            }
            electionInfo += "\n---- Winner of election: ----\n";
            electionInfo += winnerName + " : " + candidates.get(winningIndex).getPercent() + "% \n";
            electionInfo += "\n---- Losing candidate of election: ----\n";


            for (int i =  0; i < candidates.size(); i++){ //get losers
                if (!(candidates.get(i).getName()).equals(winnerName)){
                    //add losers here
                    electionInfo += candidates.get(i).getName() + " : " + candidates.get(i).getPercent() + "% \n";
                }
            }
            auditFile.appendString(electionInfo);
        }
        catch (IOException e){
            System.out.println("error");
            return;
        }
    }


    /**
     * Gets the name of winner of the election.
     *
     * @return The name of the candidate who won the election.
     *
     */

    public String getWinningCandidate(){
        return winnerName;
    }



    /**
     * Randomly choose a winner when there is a tie.
     *
     * @param   tieArray    The list of indexes of the candidates who have the same number of the votes.
     */
    private void tieBreaker(List<Integer> tieArray) {
        // random number generator between 0 and candidates.size()-1

        Random random = new Random();
        winningIndex = random.nextInt(tieArray.size());
        winnerName =  candidates.get(winningIndex).getName();

    }

    /**
     * Getter for the election results that would be shown in the audit file
     *
     * @return election information
     */
    public String getResults(){
        return electionInfo;
    }


    /**
     * Find votes for each candidate.
     * From the list of ballots, assign each candidate's attributes with their corresponding votes
     *
     */
    private void assignBallots() {
        for (int poIndex = 0; poIndex < candidates.size(); poIndex++) {
            int poVotes = 0;
            String poName = candidates.get(poIndex).getName();

            for (int bIndex = 0; bIndex < ballots.size(); bIndex++) {
                int vote = ballots.get(bIndex).getVoteForCandidate(poName);
                poVotes += vote;
            }
            candidates.get(poIndex).setVotes(poVotes);
            double percent = (((double) poVotes) / totalVotes) * 100;
            candidates.get(poIndex).setPercent(percent);
        }

    }


}



