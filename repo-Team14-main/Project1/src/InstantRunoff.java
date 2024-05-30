import java.util.*;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
/**
 * Implements functionality to simulate InstantRunoff election. Selects a candidate based on majority of votes.
 * If no clear winner is present, the program removes the candidate with the lowest votes and redistributes them.
 * In case of a tie, the program randomly selects a winner.
 *
 * @author Hyehwan Ryu, Soorya Sundravel, Bilal Osman
 */
public class InstantRunoff extends Election
{
    private String winningCandidate;
    private List<IRCandidate> candidates;
    private Map<IRCandidate,Integer> losers = new HashMap<IRCandidate,Integer>();
    private String electionInfo = "";

    /**
     * Constructor for the InstantRunoff.
     *
     * @param   totalVotes    The number of total votes.
     * @param   ballots    A list of ballots containing voting information.
     * @param   candidates    A list of candidates' names in the election.
     * @param invalid_ballots
     * @param   auditFile   Output file contaning the results of the election.
     *
     * @throws IOException Signals that the file cannot be opened, or the text cannot be written to the file.
     */
    public InstantRunoff(int totalVotes, List<Ballot> ballots, List<IRCandidate> candidates, List<Ballot> invalid_ballots, Audit auditFile) throws IOException {
        super("IR", totalVotes, ballots, auditFile);
        this.candidates = candidates;
        initialInfo();
    }

    private void initialInfo() throws IOException{
        electionInfo += "\nInstant runoff election\n";
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
     * Calculates the winning candidate of an election based on majority vote percentage.
     *
     * @throws IOException Signals that the file cannot be opened, or the text cannot be written to the file.
     */
    public void calculateWinner() throws IOException{
        try {
            int round = 0;

            while (true) {
                assignBallots(); // sets votes and percents for each candidate.
                round++;
                electionInfo += "\n---- Count " + round + " ----\n";
                electionInfo += "Percent of votes:\n";
                boolean haveWinner = false;
                for (int i = 0; i < candidates.size(); i++) { // record percent of votes for each candidate, check for majority
                    double percent = candidates.get(i).getPercent();
                    electionInfo += candidates.get(i).getName() + ": " + percent + "\n";
                    
                    if (percent > 50) {
                        winningCandidate = candidates.get(i).getName();
                        haveWinner = true; // used to ensure loop continues so all candidate percents are recorded
                    }
                }
                if (haveWinner) {
                    electionInfo += "\n---- Winner of election: ----\n";
                    electionInfo += winningCandidate + "\n";
                    auditFile.appendString(electionInfo);
                    return;
                }
                if (candidates.size() == 2) { // only 2 remaining candidates
                    Comparator<IRCandidate> byVotes = Comparator.comparing(IRCandidate::getVotes); // sort by number of votes 
                    Collections.sort(candidates, byVotes);
                    Collections.reverse(candidates); // number of votes, descending order
                    if (candidates.get(0).getVotes() == candidates.get(1).getVotes()) { // 2 remaining candidates are tied
                        List<IRCandidate> topTwoCandidates = new ArrayList<IRCandidate>() {
                            {
                                add(candidates.get(0));
                                add(candidates.get(1));
                            }
                        };
                        winningCandidate = tieBreaker(topTwoCandidates).getName();
                        
                    }
                    else { // 2 remaining candidates are not tied
                        winningCandidate = candidates.get(0).getName();
                    }
                    electionInfo += "\n---- Winner of election: ----\n";
                    electionInfo += winningCandidate + "\n";
                    auditFile.appendString(electionInfo);
                    return;
                }

                calculateRound(round);
            }
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
        return winningCandidate;
    }


    /**
     * If there is no clear winner with majority, the calculateRound sorts by vote to select the candidate with the
     * lowest number of votes. The losing candidate is placed in a separate map, and calls another function to
     * redistribute the votes.
     *
     * @param   round  The round of election that we want to calculate.
     *
     */
    private void calculateRound(int round) {
        
        // sort candidates by votes, low to high
        Comparator<IRCandidate> byVotes = Comparator.comparing(IRCandidate::getVotes);
        Collections.sort(candidates, byVotes); // sort high to low
        //Collections.reverse(candidates); // sort low to high

        // get lowest candidates
        List<IRCandidate> tiedCandidates = new ArrayList<IRCandidate>();
        int lowest = candidates.get(0).getVotes(); // Pick a candidate who has the lowest votes to compare with other candidates.
        tiedCandidates.add(candidates.get(0));
        for (int i = 0; i < candidates.size(); i++) {
            if (candidates.get(i).getVotes() == lowest) { //Check if there is a candidate who received the same number of votes with the lowest.
                tiedCandidates.add(candidates.get(i)); // add that candidate to tied list.
            }
        }

        // add losing candidate to losers map
        IRCandidate losingCandidate;
        if (tiedCandidates.size() > 1) { //If there is more than one candidate who have the same number of votes.
            losingCandidate = tieBreaker(tiedCandidates);
        }
        else { //If there is only one candidate who got the lowest number of votes.
            losingCandidate = tiedCandidates.get(0);
        }
        losers.put(losingCandidate, round); //put the loser into the 'loser.'
        candidates.remove(losingCandidate);
        electionInfo += "\n---- Losing Candidate: " + losingCandidate.getName() + " ----\n";
        redistributeVotes(losingCandidate.getName());
    }


    /**
     * Redistributes the votes of the losing candidate to remaining ones by the voter's next preference.
     *
     * @param   candidateName   The name of the candidate who lost.
     *
     */

    private void redistributeVotes(String candidateName) {
        // decrement each vote on ballots with candidate as first preference
        // using ballot.getVoteForCandidate
        for (int i = 0; i < ballots.size(); i++) {
            if (ballots.get(i).getCandidateFromPreference(1) == candidateName) {
                boolean removeVote = true;
                for (int j = 0; j < candidates.size(); j++) {
                    String name = candidates.get(j).getName();
                    int currentPref = ballots.get(i).getVoteForCandidate(name);
                    ballots.get(i).getVote().put(name, currentPref - 1);
                    removeVote = currentPref > 1 ? false : removeVote;
                }
                if (removeVote) {
                    totalVotes -= 1;
                }
            }
        }
    }

    /**
     * Randomly choose a winner when there is a tie.
     *
     * @param   candidates    The list of the candidates who have the same number of the votes.
     * @return  Winning candidate from the tieBreaker.
     */
    private IRCandidate tieBreaker(List<IRCandidate> candidates) {
        // random number generator between 0 and candidates.size()-1
        Random random = new Random();
        int index = random.nextInt(candidates.size());
        return candidates.get(index);
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
     * From the list of ballots, assign each candidate's attributes with their corresponding votes and percentage of
     * votes.
     */
    private void assignBallots() {
        for (int cIndex = 0; cIndex < candidates.size(); cIndex++) {
            int cVotes = 0;
            String cName = candidates.get(cIndex).getName();

            for (int bIndex = 0; bIndex < ballots.size(); bIndex++) {
                int vote = ballots.get(bIndex).getVoteForCandidate(cName);
                if (vote == 1){
                    cVotes += vote;
                }
            }
            candidates.get(cIndex).setVotes(cVotes);
            double percent = (((double) cVotes) / totalVotes) * 100;
            candidates.get(cIndex).setPercent(percent);
        }
    }

}