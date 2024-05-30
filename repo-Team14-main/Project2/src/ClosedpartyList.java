import java.lang.Math;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;
import java.io.IOException;

/**
 * Implements functionality to simulate ClosedpartyList election. We bring the information from the ballots,
 * allocate seats to respective parties based on vote count, and display the result of the election.
 *
 * @author Hyehwan Ryu, Soorya Sundravel, Bilal Osman
 */
public class ClosedpartyList extends Election {


    private int seatsAvailable;
    private List<PoliticalParty> parties;
    private double quota;
    private String electionInfo = "";

    /**
     * Constructor for the ClosedpartyList.
     *
     * @param   totalVotes    The number of total votes.
     * @param   ballots    A list of ballots containing voting information.
     * @param   seatsAvailable    The number of available seats for allocation.
     * @param   parties    A list of parties in the election.
     * @param   auditFile   Output file containing the results of the election.
     */
    public ClosedpartyList(int totalVotes, List<Ballot> ballots, int seatsAvailable, List<PoliticalParty> parties, Audit auditFile) throws IOException
    {
        super("CPL", totalVotes, ballots, auditFile);
        this.seatsAvailable = seatsAvailable;
        this.parties = parties;
        this.quota = ((double)totalVotes) / ((double)seatsAvailable);
        initialInfo();
        electionInfo += "\nClosed Party List election\n";
        electionInfo += "- - - - - - - - - - - - - - ";
        electionInfo += "\nSeats available: " + seatsAvailable;
        System.out.println(electionInfo);
        electionInfo = "";
    }
    /**
     * Adds total votes, seats available, and party names to the audit file.
     * 
     * @throws IOException signals that the initial electionInfo was unable to be added to the auditFile attribute.
     */
    private void initialInfo() throws IOException{
        electionInfo += "\nClosed Party List election\n";
        electionInfo += "- - - - - - - - - - - - - - \n";
        electionInfo += "Total votes: " + totalVotes;
        electionInfo += "\nSeats available: " + seatsAvailable;
        electionInfo += "\nParties: ";
        for (int i = 0; i < parties.size(); i++) {
            electionInfo += "\n\t" + parties.get(i).getPartyName();
        }
        electionInfo += "\n\n";
        auditFile.appendString(electionInfo);
        electionInfo = "";
    }

    /**
     * Getter for the seats available for the candidates
     *
     * @return seatsAvailable
     */
    public int getSeatsAvailable() {
        return seatsAvailable;
    }
    /**
     * Calculates the first round of allocation of seats towards the corresponding political parties.
     *
     * @throws IOException Signals that the file cannot be opened, or the text cannot be written to the file.
     */
    public void calculateFirstAlloc() throws IOException
    {
        // for each party, calculate its partySeats allocation and remove these from seatsAvailable
        // assumes partyVotes has been set by party.setVotes
        try {
            electionInfo += "\n---- First round of seat allocations ----\n";
            assignBallots();
            for (int i = 0; i < parties.size(); i++) {
                int votes = parties.get(i).getPartyVotes();
                int allocSeats = (int) Math.floor(votes / quota);
                parties.get(i).setPartySeats(allocSeats);
                seatsAvailable -= allocSeats;
                int remainderVotes = (int) (votes - (allocSeats * quota));
                parties.get(i).setRemainderVotes(remainderVotes);

                electionInfo += parties.get(i).getPartyName() + " awarded "
                        + allocSeats + " seats from " + votes + " votes.\n";
            }
            calculateSecondAlloc();
        }
        catch (Exception e){
            return;
        }
    }

    /**
     * Allocates the remaining seats from first round towards the political parties based on votes.
     *
     @throws IOException Signals that the file cannot be opened, or the text cannot be written to the file.
     */
    public void calculateSecondAlloc() throws IOException
    {

        try {
            electionInfo += "\n---- Second round of seat allocations ----\n";
            // sort list of parties by each party's remainder votes
            Comparator<PoliticalParty> byRemainder = Comparator.comparing(PoliticalParty::getRemainder);
            Collections.sort(parties, byRemainder);
            Collections.reverse(parties);
            
            for (int i = 0; i < seatsAvailable; i++) {
                
                int numTied = 1;
                List<PoliticalParty> tiedParties = new ArrayList<PoliticalParty>();
                tiedParties.add(parties.get(i));

                for (int j = i; j < parties.size()-1; j++) {
                    if (parties.get(j).getRemainder() == parties.get(j+1).getRemainder()) {
                        tiedParties.add(parties.get(j+1));
                        numTied++;
                    }
                    else {
                        break;
                    }
                }

                if (numTied > 1 && numTied > seatsAvailable) {
                    tieBreaker(tiedParties, numTied-seatsAvailable);
                    break;
                }

                int currSeats = parties.get(i).getPartySeats();
                parties.get(i).setPartySeats(currSeats + 1);

                electionInfo += parties.get(i).getPartyName() + " awarded 1 seat from "
                        + parties.get(i).getRemainder() + " remainder votes.\n";
                
            }
            auditFile.appendString(electionInfo);
            String results = "---- Results ----";
            auditFile.appendString(results);
            for (int i = 0; i < parties.size(); i++) {
                results = parties.get(i).getPartyName() + " seats: " + parties.get(i).getPartySeats();
                auditFile.appendString(results);
            }
        }
        catch (Exception e) {
            return;
        }
    }
    /**
     * Removes a specified number of tied parties in order to fairly award seats when
     * the number of seats available is less than the number of tied parties. Adds the 
     * results of the tieBreaker to the electionInfo attribute.
     * 
     * @param tiedParties   a List of parties with a tied remainder vote.
     * @param numToRemove   the number of tied parties to remove.
     */
    private void tieBreaker(List<PoliticalParty> tiedParties, int numToRemove){
        while(numToRemove > 0) {
            Random random = new Random();
            int index = random.nextInt(tiedParties.size());
            
            tiedParties.remove(index);
            numToRemove--;
        }
        for(int i = 0; i < tiedParties.size(); i++) {
            int currSeats = tiedParties.get(i).getPartySeats();
            tiedParties.get(i).setPartySeats(currSeats + 1);

            electionInfo += tiedParties.get(i).getPartyName() + " awarded 1 seat from "
                        + tiedParties.get(i).getRemainder() + " remainder votes.\n";
        }
    }

    /**
     * Display the result of the ClosedpartyList election.
     *
     */
    public void displayResults(){
        System.out.println(electionInfo);
    }

    /**
     * Getter for the election results that would be shown in the audit file
     *
     * @return election information
     */
    public String getResults(){
        return this.electionInfo;
    }

    /**
     * Find votes for each party.
     * The votes are assigned to each party's partyVotes attribute.
     *
     */
    private void assignBallots()
    {
        // votes are assigned to each party's partyVotes attribute.
        // for each party, loop through the ballots to find votes
        for (int pIndex = 0; pIndex < parties.size(); pIndex++)
        {
            int pVotes = 0;
            String pName = parties.get(pIndex).getPartyName();

            for (int bIndex = 0; bIndex < ballots.size(); bIndex++)
            {
                pVotes += ballots.get(bIndex).getVoteForCandidate(pName); // assumes 1 or 0
            }

            parties.get(pIndex).setPartyVotes(pVotes);
        }
    }
}
