import java.time.LocalDate;
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
    private ArrayList<File> filelist;
    private String results;
    private String auditName;
    private StringBuilder GUIresults = new StringBuilder();
    private List<Ballot> invalid_ballots = new ArrayList<Ballot>();;

    /**
     * Creates a FileParser object and imports the filenmae for parsing
     *
     * @param   filename    name of the file
     */
    public FileParser(String filename) { // constructor
        this.file = new File(filename); // stores passed in csv file
    }

    /**
     * Creates a FileParser object and imports the filenames for parsing
     *
     * @param   filenames    name of multple files for parsing
     */
    public FileParser(String[] filenames){
        this.filelist = new ArrayList<>();
        for(int i=0; i<filenames.length; i++){
            filelist.add(new File(filenames[i]));
            System.out.println(filenames[i]);
        }
    }

    /**
     * Sets the audit file name the user inputted which is the date of when
     * the program is run
     *
     * @param   auditName    name of audit file
     */
    public void setAuditName(String auditName) {
        this.auditName = auditName + ".txt";
    }
    /**
     * Gets the audit file name the user inputted which is the date of when
     * the program is run
     * If audit name doesn't exist it makes one with current date
     *
     * @return   The name of the audit file
     */
    public String getAuditName(){
        if(this.auditName == null) {
            LocalDate currentDate = LocalDate.now();
            this.auditName = "audit_" + currentDate.toString() + ".txt";
        }
        return this.auditName;
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
     * Getter method for the file list
     *
     * @return  the file list attribute
     */
    public ArrayList<File> getFilelist() {
        return filelist;
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
            Scanner scan = new Scanner(this.filelist.get(0));
            String type = scan.nextLine();
            int numvoters; // number of voters - first line
            int numseats = 0; // number of seats - 4th line for CPL
            String[] candidates; // list of candidate names - will convert to candidate objects
            int ballotLine = 0;
            List<Ballot> ballot_list = new ArrayList<Ballot>(); // list of ballot objects for each ballot cast in file

            if(type.equals("IR")){ // only for Instant Runoff
                int numCand = Integer.parseInt(scan.nextLine()); // number of candidates
                String temp = scan.nextLine();
                candidates = temp.split(", ", 0); // list of candidates names
                numvoters = Integer.parseInt(scan.nextLine()); // get number of votes
                ballotLine = 4;
            }
            else if(type.equals("CPL")){ // only for Closed Party List
                int numCand = Integer.parseInt(scan.nextLine()); //number of candidates
                String temp = scan.nextLine();
                candidates = temp.split(", ", 0); // list of candidates names
                numseats = Integer.parseInt(scan.nextLine()); //get number of seats
                numvoters = Integer.parseInt(scan.nextLine()); // get number of votes
                ballotLine = 5;
            }
            else if(type.equals("PO")){
                int numCand = Integer.parseInt(scan.nextLine()); // number of candidates
                String temp = scan.nextLine();
                candidates = temp.split(", ", 0); // list of candidates names
                numvoters = Integer.parseInt(scan.nextLine()); // get number of votes
                ballotLine = 4;
            }else { // needed to compile when there is a case for invalid file
                System.out.println("Error: Need a valid voting system\n");
                return;
            }

            while(scan.hasNextLine()) { // loop through each ballot and create object from first file
                String temp = scan.nextLine();
                String[] ranks = temp.split(",", 0);
                if (type.equals("IR")) {
                    // creates a ballot object for IR while removing invalid ballots
                    Ballot line_ballot = createIRBallot(ranks, candidates);
                    if (line_ballot != null) {
                        ballot_list.add(line_ballot); // add to ballot list
                    }
                } else {
                    Ballot line_ballot = createBallot(ranks, candidates); // creates a ballot object
                    ballot_list.add(line_ballot); // add to ballot list
                }
            }
            scan.close(); // close file

            int line = 1;
            String temp;
            for(int i = 1; i < this.filelist.size(); i++){ //get ballots from the rest of the files
                scan = new Scanner(this.filelist.get(i));
                while(scan.hasNextLine()) { // loop through each ballot and create object
                    // System.out.println(ballotLine + " " + line);
                    temp = scan.nextLine();
                    if(ballotLine == line) {
                        //System.out.println(temp);
                        numvoters += Integer.parseInt(temp);
                    } else if(ballotLine < line) {
                        String[] ranks = temp.split(",", 0);
                        if(type.equals("IR")){
                            // creates a ballot object for IR while removing invalid ballots
                            Ballot line_ballot = createIRBallot(ranks, candidates);
                            if(line_ballot != null) {
                                ballot_list.add(line_ballot); // add to ballot list
                            }
                        } else {
                            Ballot line_ballot = createBallot(ranks, candidates); // creates a ballot object
                            ballot_list.add(line_ballot); // add to ballot list
                        }
                    }
                    line++;
                }
                scan.close();
                line = 1;
            }
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
     * Makes a ballot object based on the ballot string from file and list of candidate/political party names
     * Removes or returns a null ballot if less than half of candidates are ranked
     * Invalid ballots are placed in a private attribute for table information
     *
     * @param   line    ballot string list of rankings -- will be converted to int
     * @param   cds     list of candidate/political party names
     * @return  ballot object containing a Hashmap that maps a ranking to a candidate/political party
     */
    public Ballot createIRBallot(String[] line, String[] cds){
        HashMap<String, Integer> vote = new HashMap<String, Integer>();
        int candlength = (cds.length / 2) + (cds.length % 2);
        int ranked = 0;
        for(int i = 0; i < line.length; i++){ // string parsing
            if(!(line[i].equals(""))) {
                int r = Integer.parseInt(line[i]);
                vote.put(cds[i], r);
                ranked += 1;
            }
        }
        // System.out.println("");
        if(ranked >= candlength){
            // System.out.println(ranked + " " + candlength);
            Ballot ballot = new Ballot(vote); // creates ballot and returns
            return ballot;
        }
        this.invalid_ballots.add(new Ballot(vote));
        return null;
    }

    /**
     * Returns the list of invalid ballots
     *
     * @return  the list of invalid ballots
     */
    public List<Ballot> getInvalid_ballots() {
        return invalid_ballots;
    }

    /**
     * Sets the invalid ballots attribute with a list of ballots inputted
     *
     * @param   invalid_ballots    list of ballot objects
     */
    public void setInvalid_ballots(List<Ballot> invalid_ballots) {
        this.invalid_ballots = invalid_ballots;
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
     * Makes a IRCandidate object from candidate name
     *
     * @param   name    name of candidate
     * @return  POCandidate object for the candidate
     */
    public POCandidate makePOList(String name){
        POCandidate popularitycandidate = new POCandidate(name); // makes an IRCandidate object from candidate name
        return popularitycandidate;
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
     * Getter for the election results that would be shown in the GUI
     *
     * @return gui election results
     */
    public StringBuilder getGUIresults() {
        return GUIresults;
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
        if(this.getAuditName() == null){ //this is for tests
            this.setAuditName("audit");
        }
        Audit audit = new Audit(this.getAuditName()); // create audit file

        if(type.equals("IR")) { // only for Instant Runoff

            List<IRCandidate> clist = new ArrayList<IRCandidate>();
            for(int i=0; i < candidates.length; i++){ // create a list of IRCandidate objects per candidate
                clist.add(makeCandidates(candidates[i]));
            }
            InstantRunoff newElec = new InstantRunoff(numvoters, ballot_list, clist, this.invalid_ballots, audit); // simulate IR election
            newElec.calculateWinner();
            this.results = newElec.getWinningCandidate();
            this.GUIresults = newElec.getIRtable().append(results);
        } else if(type.equals("CPL")) { // only for Closed Party List
            List<PoliticalParty> plist = new ArrayList<PoliticalParty>();
            for(int i=0; i < candidates.length; i++){ // create a list of PoliticalParty objects per political party candidate
                plist.add(makeParties(candidates[i]));
            }
            ClosedpartyList newElec = new ClosedpartyList(numvoters, ballot_list, numseats, plist, audit); //simulate CPL election
            newElec.calculateFirstAlloc();
            // newElec.displayResults();
            this.results = newElec.getResults();
            this.GUIresults.append(this.results);
        }
        else if(type.equals("PO")){
            List<POCandidate> polist = new ArrayList<POCandidate>();
            for(int i=0; i < candidates.length; i++){ // create a list of IRCandidate objects per candidate
                polist.add(makePOList(candidates[i]));
            }
            PopularityOnly newElec = new PopularityOnly(numvoters, ballot_list, polist, audit); // simulate IR election
            newElec.calculateWinner();
            this.results = newElec.getWinningCandidate();
            this.GUIresults.append(newElec.getResults());
        }
        System.out.println(GUIresults);
        if(type.equals("IR")){
            String invalidname = "invalidated_" + getAuditName().split("_")[1];
            Audit invalid_audit = new Audit(invalidname);
            invalid_audit.appendString("\n---------- Invalid Ballots ----------\n");
            if(invalid_ballots.size() == 0){
                invalid_audit.appendString("No invalid Ballots\n");
            } else {
                //String map;
                for(int i = 0; i < invalid_ballots.size(); i++){
                    invalid_audit.appendString(invalid_ballots.get(i).getVote().toString() + "\n");
                }
            }
            System.out.println("invalid ballots are stored in " + invalidname);
        }

    }


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
        // FileParser parse = new FileParser(args[0]);
        Scanner scan = new Scanner(System.in);
        System.out.println("Type in the date for the election");
        String date = scan.nextLine();
        FileParser parse = new FileParser(args);
        parse.setAuditName("audit_"+date);

        // simulates election based on data from file
        try {
            parse.buildElection();
        } catch (IOException e) { //file error handling
            System.out.println("An error occurred. Unable to continue.");
        }
        // results in audit file

    }
}