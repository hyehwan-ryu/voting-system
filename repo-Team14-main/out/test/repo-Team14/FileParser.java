import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.List;

/**
 * Purpose of this class is to take in filename, open file, scrape all necessary information from the file,
 * assuming file is formatted correctly, and creates the necessary data structures and classes from information.
 * After that the whole election simulation will commence, by calling the appropriate methods depending
 * on the type of voting system which are either Instant Runoff or Closed Party List voting.
 * @author Bilal Osman
 */
public class FileParser {
    private File file;
    private String results;

    /**
     * Creates a FileParser object and imports the filenmae for parsing
     *
     * @param   filename    name of the file
     */
    public FileParser(String filename) { // constructor
        this.file = new File(filename); // stores passed in csv file
    }

    /**
     * Getter method for the file attribute
     *
     * @return  the file attribute
     */
    public File getFile(){
        return this.file;
    }
    /**
     * Uses scanner to open and read file. Gathers necessary information for election such as:
     * type of election, number of voters, number of seats(CPL only), and list of ballots
     * It then calls simulate() to get results of election based on information gathered
     *
     * @throws  IOException Signals that the file cannot be created.
     */
    public void buildElection() throws IOException { // simulates whole election
        try {
            Scanner scan = new Scanner(this.file);
            String type = scan.nextLine();
            int numvoters; // number of voters - first line
            int numseats = 0; // number of seats - 4th line for CPL
            String[] candidates; // list of candidate names - will convert to candidate objects

            List<Ballot> ballot_list = new ArrayList<Ballot>(); // list of ballot objects for each ballot cast in file

            if(type.equals("IR")){ // only for Instant Runoff
                int numCand = Integer.parseInt(scan.nextLine()); // number of candidates
                String temp = scan.nextLine();
                candidates = temp.split(", ", 0); // list of candidates names
                numvoters = Integer.parseInt(scan.nextLine()); // get number of votes
            }
            else if(type.equals("CPL")){ // only for Closed Party List
                int numCand = Integer.parseInt(scan.nextLine()); //number of candidates
                String temp = scan.nextLine();
                candidates = temp.split(", ", 0); // list of candidates names
                numseats = Integer.parseInt(scan.nextLine()); //get number of seats
                numvoters = Integer.parseInt(scan.nextLine()); // get number of votes
            } else { // needed to compile when there is a case for invalid file
                System.out.println("Error: Need a valid voting system\n");
                return;
            }

            while(scan.hasNextLine()) { // loop through each ballot and create object
                String temp = scan.nextLine();
                String[] ranks = temp.split(",", 0);
                Ballot line_ballot = createBallot(ranks, candidates); // creates a ballot object
                ballot_list.add(line_ballot); // add to ballot list

            }
            scan.close(); // close file
            simulate(type, candidates, ballot_list, numvoters, numseats); //call a private function to simulate election type

        } catch (FileNotFoundException e) { //file error handling
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    /**
     * Makes a ballot object based on the ballot string from file and list of candidate/political party names
     *
     * @param   line    ballot string list of rankings -- will be converted to int
     * @param   cds     list of candidate/political party names
     * @return  ballot object containing a Hashmap that maps a ranking to a candidate/political party
     */
    public Ballot createBallot(String[] line, String[] cds){
        // each ballot contains a hashmap mapping a candidate name/political party to their ranking per ballot
        HashMap<String, Integer> vote = new HashMap<String, Integer>();
        for(int i = 0; i < line.length; i++){ // string parsing
            if(!(line[i].equals(""))) {
                int r = Integer.parseInt(line[i]);
                vote.put(cds[i], r);
            }
        }
        // System.out.println("");

        Ballot ballot = new Ballot(vote); // creates ballot and returns
        return ballot;
    }

    /**
     * Makes a IRCandidate object from candidate name
     *
     * @param   name    name of candidate
     * @return  IRCandidate object for the candidate
     */
    public IRCandidate makeCandidates(String name){
        IRCandidate candidate = new IRCandidate(name); // makes an IRCandidate object from candidate name
        return candidate;
    }

    /**
     * Makes a PoliticalParty object from political party name
     *
     * @param   name    name of political party
     * @return  PoliticalParty object for the political party
     */
    public PoliticalParty makeParties(String name){
        PoliticalParty party = new PoliticalParty(name, null); // makes an PoliticalParty object from name
        return party;
    }

    /**
     * Getter for the election results that would be shown in the audit file
     *
     * @return election information
     */
    public String getResults(){
        return this.results;
    }

    /**
     * Runs either an Instant Runoff Election or a Closed Party List ELection based on type of election form file
     * Creates an Audit file that will have all election information from running respective voting system
     *
     * @param   type    the type of election being simulated
     * @param   candidates  list of names for candidates or political parties
     * @param   ballot_list ArrayList of Ballot objects containing rankings for each
     *                      candidate per ballot
     * @param   numvoters   number of voters who cast ballots
     * @param   numseats    number of seats available to political party runners -- 0 if
     *                      type of voting system is Instant Runoff
     * @throws  IOException Signals that the file cannot be created.
     */
    public void simulate(String type, String[] candidates, List<Ballot> ballot_list, int numvoters, int numseats) throws IOException {
        Audit audit = new Audit("audit.txt"); // create audit file

        if(type.equals("IR")) { // only for Instant Runoff
            List<IRCandidate> clist = new ArrayList<IRCandidate>();
            for(int i=0; i < candidates.length; i++){ // create a list of IRCandidate objects per candidate
                clist.add(makeCandidates(candidates[i]));
            }
            InstantRunoff newElec = new InstantRunoff(numvoters, ballot_list, clist, audit); // simulate IR election
            newElec.calculateWinner();
            this.results = newElec.getWinningCandidate();
        } else if(type.equals("CPL")) { // only for Closed Party List
            List<PoliticalParty> plist = new ArrayList<PoliticalParty>();
            for(int i=0; i < candidates.length; i++){ // create a list of PoliticalParty objects per political party candidate
                plist.add(makeParties(candidates[i]));
            }
            ClosedpartyList newElec = new ClosedpartyList(numvoters, ballot_list, numseats, plist, audit); //simulate CPL election
            newElec.calculateFirstAlloc();
            // newElec.displayResults();
            this.results = newElec.getResults();
        }
        System.out.println(results);
        System.out.println("\nElection results are now in 'audit.txt'");

    }

//    private void getManualData(String input){}

    /**
     * Our main method. This is how simulation is started.
     * The file name should be included as the first command line argument
     *
     * @param   args  The command line arguments.
     * @throws  IOException  Signals that the file cannot be created.
     **/
    public static void main(String[] args) {

        // file name will be given as an argument when running the function
        // assume file exists in directory
        FileParser parse = new FileParser(args[0]);

        // simulates election based on data from file
        try {
            parse.buildElection();
        } catch (IOException e) { //file error handling
            System.out.println("An error occurred. Unable to continue.");
        }
        // results in audit file

    }
}