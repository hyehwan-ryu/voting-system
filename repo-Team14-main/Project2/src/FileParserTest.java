import java.io.*;

import org.junit.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class FileParserTest {

    @Test
    public void FileParserTest() {
        FileParser fp_cpl = new FileParser("Project2/testing/CPL.csv");
        FileParser fp_ir = new FileParser("Project2/testing/IRV.csv");

        File CPL = new File("Project2/testing/CPL.csv");
        File IR = new File("Project2/testing/IRV.csv");

        assertEquals(CPL, fp_cpl.getFile());
        assertEquals(IR, fp_ir.getFile());
    }

    @Test
    public void getFileTest() {
        FileParser temp_file1 = new FileParser("temp.csv");
        File temp_file2 = new File("temp.csv");
        assertEquals(temp_file2, temp_file1.getFile());
    }

    @Test
    public void AuditNameTest(){
        FileParser temp_parse = new FileParser("temp.csv");
        temp_parse.setAuditName("temp_audit");
        assertEquals("temp_audit.txt", temp_parse.getAuditName());

        FileParser temp_parse2 = new FileParser("temp.csv");
        String current = "audit_" + LocalDate.now().toString() + ".txt";
        assertEquals(current, temp_parse2.getAuditName());
    }

    @Test
    public void createIRBallotTest(){
        FileParser fp = new FileParser("temp.csv");
        String[] candidates = new String[]{"cand1", "cand2", "cand3"};
        String[] ranks1 = new String[]{"1", "2", "3"};
        String[] ranks2 = new String[]{"", "2", "3"};

        Ballot blt1 = fp.createIRBallot(ranks1, candidates);
        Ballot blt2 = fp.createIRBallot(ranks2, candidates);

        Map<String, Integer> maptest = new HashMap<String, Integer>();
        maptest.put("cand1", 1);
        maptest.put("cand2", 2);
        maptest.put("cand3", 3);
        Ballot blt3 = new Ballot(maptest);

        assertEquals(blt1.getVote(), blt3.getVote());
        maptest.remove("cand1");
        assertEquals(blt2.getVote(), blt3.getVote());
    }

    @Test
    public void invalidIRBallotTest(){
        FileParser fp = new FileParser("temp.csv");
        String[] candidates = new String[]{"cand1", "cand2", "cand3", "cand4", "cand5"};
        String[] invalid_ranks = new String[]{"", "2", "3", "", ""};
        Ballot blt1 = fp.createIRBallot(invalid_ranks, candidates);
        assertNull(blt1);

        Map<String, Integer> maptest = new HashMap<String, Integer>();
        maptest.put("cand2", 2);
        maptest.put("cand3", 3);
        Ballot blt2 = new Ballot(maptest);

        assertEquals(fp.getInvalid_ballots().get(0).getVote(), blt2.getVote());
    }


    @Test
    public void createBallotTest() {
        FileParser fp = new FileParser("temp.csv");
        String[] candidates = new String[]{"cand1", "cand2", "cand3"};
        String[] ranks = new String[]{"1", "2", "3"};

        Ballot blt1 = fp.createBallot(ranks, candidates);

        Map<String, Integer> maptest = new HashMap<String, Integer>();
        maptest.put("cand1", 1);
        maptest.put("cand2", 2);
        maptest.put("cand3", 3);
        Ballot blt2 = new Ballot(maptest);

        assertEquals(blt1.getVote(), blt2.getVote());
    }

    @Test
    public void makeCandidatesTest() {
        IRCandidate candidate1 = new IRCandidate("expected");
        FileParser fp = new FileParser("temp.csv");
        IRCandidate candidate2 = fp.makeCandidates("expected");

        assertEquals(candidate1.getName(), candidate2.getName());
    }

    @Test
    public void makePartiesTest() {
        PoliticalParty party1 = new PoliticalParty("expected", null);
        FileParser fp = new FileParser("temp.csv");
        PoliticalParty party2 = fp.makeParties("expected");

        assertEquals(party1.getPartyName(), party2.getPartyName());
    }

    @Test
    public void simulateIRTest() {
        String IRtxt1 = "Rosen (D)";
        FileParser fp_IR = new FileParser("temp.csv");
        String[] candidates = new String[]{"Rosen (D)", "Kleinberg (R)", "Chou (I)", "Royce (L)"};

        List<Ballot> IR_ballot_list = new ArrayList<Ballot>();
        String[] lineballots_IR = new String[]{"1,3,4,2", "1,,2,", "1,2,3,", "3,2,1,4", ",,1,2", ",,,1"};
        for (int i = 0; i < lineballots_IR.length; i++) {
            String temp = lineballots_IR[i];
            // System.out.println(temp);
            String[] ranks = temp.split(",", 0);
            Ballot line_ballot = fp_IR.createIRBallot(ranks, candidates); // creates a ballot object
            if (line_ballot != null) {
                IR_ballot_list.add(line_ballot); // add to ballot list
            }

        }

        try {
            fp_IR.simulate("IR", candidates, IR_ballot_list, 6, 0);
        } catch (IOException e) {
            System.out.println("An error occured");
            e.printStackTrace();
        }
        assertEquals(IRtxt1, fp_IR.getResults());

        String IRtxt3 = "Smith (D)";
        String IRtxt4 = "Rosen (D)";
        String IRtxt5 = "Klenberg (R)";
        String IRtxt6 = "Royce (L)";
        String IRtxt7 = "George (L)";
        String IRtxt8 = "Kirk (R)";
        FileParser fp_IR2 = new FileParser("temp.csv");
        candidates = new String[]{"Rosen (D)", "Kleinberg (R)", "Chou (I)", "Royce (L)", "Grant (I)", "Smith (D)"};
        IR_ballot_list = new ArrayList<Ballot>();
        lineballots_IR = new String[]{"1,3,,,2,", ",1,,2,3,4", "2,,1,,,", ",,,,1,", ",1,2,3,,", "2,3,1,6,5,4", "6,5,4,3,2,1", ",,1,,,", "3,,2,4,1,", "1,2,3,,,4", "2,4,,1,3,5", "6,4,3,5,1,2"};
        for (int i = 0; i < lineballots_IR.length; i++) {
            String temp = lineballots_IR[i];
            // System.out.println(temp);
            String[] ranks = temp.split(",", 0);
            Ballot line_ballot = fp_IR2.createIRBallot(ranks, candidates); // creates a ballot object
            if (line_ballot != null) {
                IR_ballot_list.add(line_ballot); // add to ballot list
            }
        }
        try {
            fp_IR2.simulate("IR", candidates, IR_ballot_list, 12, 0);
        } catch (IOException e) {
            System.out.println("An error occured");
            e.printStackTrace();
        }
        assertNotEquals(IRtxt3, fp_IR2.getResults());
        assertEquals(IRtxt4, fp_IR2.getResults());
        assertNotEquals(IRtxt5, fp_IR2.getResults());
        assertNotEquals(IRtxt6, fp_IR2.getResults());
        assertNotEquals(IRtxt7, fp_IR2.getResults());
        assertNotEquals(IRtxt8, fp_IR2.getResults());

        FileParser fp_IR3 = new FileParser("temp.csv");
        candidates = new String[]{"Rosen (D)", "Kleinberg (R)", "Chou (I)", "Royce (L)", "Wilt (D)", "Farris (R)", "George (L)", "Kirk (R)"};
        IR_ballot_list = new ArrayList<Ballot>();
        lineballots_IR = new String[]{"4,1,,3,,2,", ",,,1,3,2,,4", ",,,,1,,,", ",2,,,3,1,,4", "2,3,,,,,,1", "6,8,2,1,3,4,5,8", "1,4,,2,,3,,5", "3,,,,2,,,1", ",2,,3,,4,,1", "6,5,4,2,1,3,,", "3,,1,,2,5,,4", "5,4,3,2,6,1,8,7", ",3,5,4,,2,,1", "3,,,,,1,,2", ",1,,4,2,3,5,", "1,2,3,4,5,6,7,8", ",,1,3,2,,,", ",,,2,3,4,,1", ",1,,,,,,", "3,6,4,5,2,,1,", "8,7,6,5,4,3,2,1", "3,4,,2,,1,,", "7,6,1,4,3,2,5,8", ",,,,1,3,2,", "1,3,2,6,4,7,,5"};
        for (int i = 0; i < lineballots_IR.length; i++) {
            String temp = lineballots_IR[i];
            // System.out.println(temp);
            String[] ranks = temp.split(",", 0);
            Ballot line_ballot = fp_IR3.createIRBallot(ranks, candidates); // creates a ballot object
            if (line_ballot != null) {
                IR_ballot_list.add(line_ballot); // add to ballot list
            }
        }
        try {
            fp_IR3.simulate("IR", candidates, IR_ballot_list, 24, 0);
        } catch (IOException e) {
            System.out.println("An error occured");
            e.printStackTrace();
        }
        assertNotEquals(IRtxt4, fp_IR3.getResults());
        assertNotEquals(IRtxt5, fp_IR3.getResults());
        assertNotEquals(IRtxt6, fp_IR3.getResults());
        assertNotEquals(IRtxt7, fp_IR3.getResults());
        assertNotEquals(IRtxt8, fp_IR3.getResults());

    }

    @Test
    public void simulateCPLTest() {
        FileParser fp_CPL1 = new FileParser("temp.csv");
        String[] parties = new String[]{"Democratic", "Republican", "New Wave", "Reform", "Green", "Independent"};

        List<Ballot> CPL_ballot_list = new ArrayList<Ballot>();
        String[] lineballots_CPL = new String[]{"1,,,,,", "1,,,,,", ",1,,,,", ",,,,1,", ",,,,,1", ",,,1,,", ",,,1,,", "1,,,,,", ",1,,,,"};
        for (int i = 0; i < lineballots_CPL.length; i++) {
            String temp = lineballots_CPL[i];
            // System.out.println(temp);
            String[] ranks = temp.split(",", 0);
            Ballot line_ballot = fp_CPL1.createBallot(ranks, parties); // creates a ballot object
            CPL_ballot_list.add(line_ballot); // add to ballot list
        }

        String CPLtxt1 = "\n---- First round of seat allocations ----\n" +
                "Democratic awarded 1 seats from 3 votes.\n" +
                "Republican awarded 0 seats from 2 votes.\n" +
                "New Wave awarded 0 seats from 0 votes.\n" +
                "Reform awarded 0 seats from 2 votes.\n" +
                "Green awarded 0 seats from 1 votes.\n" +
                "Independent awarded 0 seats from 1 votes.\n" +
                "\n" +
                "---- Second round of seat allocations ----\n" +
                "Reform awarded 1 seat from 2 remainder votes.\n" +
                "Republican awarded 1 seat from 2 remainder votes.\n";

        try {
            fp_CPL1.simulate("CPL", parties, CPL_ballot_list, 9, 3);
        } catch (IOException e) {
            System.out.println("An error occured");
            e.printStackTrace();
        }
        assertEquals(CPLtxt1, fp_CPL1.getResults());

        FileParser fp_CPL2 = new FileParser("temp.csv");
        parties = new String[]{"Democratic", "Republican", "New Wave", "Reform", "Green", "Independent"};

        CPL_ballot_list = new ArrayList<Ballot>();
        lineballots_CPL = new String[]{"1,,,,,", ",,1,,,", ",,,1,,", ",,1,,,", "1,,,,,", "1,,,,,", ",1,,,,", "1,,,,,", ",,,1,,", ",,,,,1", ",,,,1,", ",,,1,,", "1,,,,,", ",1,,,,", ",1,,,,"};
        for (int i = 0; i < lineballots_CPL.length; i++) {
            String temp = lineballots_CPL[i];
            // System.out.println(temp);
            String[] ranks = temp.split(",", 0);
            Ballot line_ballot = fp_CPL2.createBallot(ranks, parties); // creates a ballot object
            CPL_ballot_list.add(line_ballot); // add to ballot list
        }

        String CPLtxt2 = "\n---- First round of seat allocations ----\n" +
                "Democratic awarded 1 seats from 5 votes.\n" +
                "Republican awarded 0 seats from 3 votes.\n" +
                "New Wave awarded 0 seats from 2 votes.\n" +
                "Reform awarded 0 seats from 3 votes.\n" +
                "Green awarded 0 seats from 1 votes.\n" +
                "Independent awarded 0 seats from 1 votes.\n" +
                "\n" +
                "---- Second round of seat allocations ----\n" +
                "Reform awarded 1 seat from 3 remainder votes.\n" +
                "Republican awarded 1 seat from 3 remainder votes.\n" +
                "New Wave awarded 1 seat from 2 remainder votes.\n" +
                "Independent awarded 1 seat from 1 remainder votes.\n";

        try {
            fp_CPL2.simulate("CPL", parties, CPL_ballot_list, 16, 5);
        } catch (IOException e) {
            System.out.println("An error occured");
            e.printStackTrace();
        }
        assertEquals(CPLtxt2, fp_CPL2.getResults());

        FileParser fp_CPL3 = new FileParser("temp.csv");
        parties = new String[]{"Democratic", "Republican", "New Wave", "Reform", "Green", "Labour", "Non-Parliamentary political parties", "Plaid Cymru", "Independent"};

        CPL_ballot_list = new ArrayList<Ballot>();
        lineballots_CPL = new String[]{"1,,,,,,,,", ",,,,1,,,", ",,,1,,,,,", "1,,,,,,,,", "1,,,,,,,,", ",,,,1,,,,", ",,,,1,,,", ",1,,,,,,,", ",,,,,1,,,", ",,1,,,,,,", ",,,,,,,1,", "1,,,,,,,,", ",,1,,,,,,", ",1,,,,,,,", ",1,,,,,,,", ",,,,,,1,,", ",,,,,,1,,", ",,1,,,,,,", ",,,,,,1,,", ",,,,,,1,,", ",,,,,,,1,", ",,,,,,,,1", ",,,1,,,,,", ",1,,,,,,,", ",,,,,1,,,", ",,,,,,,1,", ",,,,,,,1,", ",,,,1,,,", ",,1,,,,,,", ",,,1,,,,,"};
        for (int i = 0; i < lineballots_CPL.length; i++) {
            String temp = lineballots_CPL[i];
            // System.out.println(temp);
            String[] ranks = temp.split(",", 0);
            Ballot line_ballot = fp_CPL3.createBallot(ranks, parties); // creates a ballot object
            CPL_ballot_list.add(line_ballot); // add to ballot list
        }

        String CPLtxt3 = "\n---- First round of seat allocations ----\n" +
                "Democratic awarded 1 seats from 4 votes.\n" +
                "Republican awarded 1 seats from 4 votes.\n" +
                "New Wave awarded 1 seats from 4 votes.\n" +
                "Reform awarded 0 seats from 3 votes.\n" +
                "Green awarded 1 seats from 4 votes.\n" +
                "Labour awarded 0 seats from 2 votes.\n" +
                "Non-Parliamentary political parties awarded 1 seats from 4 votes.\n" +
                "Plaid Cymru awarded 1 seats from 4 votes.\n" +
                "Independent awarded 0 seats from 1 votes.\n" +
                "\n" +
                "---- Second round of seat allocations ----\n" +
                "Reform awarded 1 seat from 3 remainder votes.\n" +
                "Labour awarded 1 seat from 2 remainder votes.\n";

        try {
            fp_CPL3.simulate("CPL", parties, CPL_ballot_list, 30, 8);
        } catch (IOException e) {
            System.out.println("An error occured");
            e.printStackTrace();
        }
        assertEquals(CPLtxt3, fp_CPL3.getResults());
    }

    @Test
    public void simulatePOTest(){
        String POtxt1 = "Kleinberg (R)";
        FileParser fp_PO = new FileParser("temp.csv");
        String[] candidates = new String[]{"Rosen (D)", "Kleinberg (R)", "Chou (I)", "Royce (L)"};

        List<Ballot> PO_ballot_list = new ArrayList<Ballot>();
        String[] lineballots_PO = new String[]{",1,,", "1,,,", ",1,,", ",,1,", ",,,1"};
        for (int i = 0; i < lineballots_PO.length; i++) {
            String temp = lineballots_PO[i];
            // System.out.println(temp);
            String[] ranks = temp.split(",", 0);
            Ballot line_ballot = fp_PO.createBallot(ranks, candidates); // creates a ballot object
            PO_ballot_list.add(line_ballot); // add to ballot list
        }
        try {
            fp_PO.simulate("PO", candidates, PO_ballot_list, 6, 0);
        } catch (IOException e) {
            System.out.println("An error occured");
            e.printStackTrace();
        }
        assertEquals(POtxt1, fp_PO.getResults());

        FileParser fp_PO2 = new FileParser("temp.csv");
        String POtxt2 = "Rosen (D)";
        String POtxt3 = "Chou (I)";
        String POtxt4 = "Royce (L)";
        String POtxt5 = "Grant (I)";
        String POtxt6 = "Smith (D)";

        candidates = new String[]{ "Kleinberg (R)", "Rosen (D)", "Chou (I)", "Royce (L)", "Grant (I)", "Smith (D)"};
        PO_ballot_list = new ArrayList<Ballot>();
        lineballots_PO = new String[]{"1,,,,,", ",,,1,,", ",,1,,,", ",,,,1,", ",1,,,,", "1,,,,,", ",,,,,1", ",,1,,,", ",,,,1,", "1,,,,,"};
        for (int i = 0; i < lineballots_PO.length; i++) {
            String temp = lineballots_PO[i];
            // System.out.println(temp);
            String[] ranks = temp.split(",", 0);
            Ballot line_ballot = fp_PO2.createBallot(ranks, candidates); // creates a ballot object
            PO_ballot_list.add(line_ballot); // add to ballot list
        }
        try {
            fp_PO2.simulate("PO", candidates, PO_ballot_list, 10, 0);
        } catch (IOException e) {
            System.out.println("An error occured");
            e.printStackTrace();
        }
        assertNotEquals(POtxt2, fp_PO2.getResults());
        assertNotEquals(POtxt3, fp_PO2.getResults());
        assertNotEquals(POtxt4, fp_PO2.getResults());
        assertNotEquals(POtxt5, fp_PO2.getResults());
        assertNotEquals(POtxt6, fp_PO2.getResults());

        FileParser fp_PO3 = new FileParser("temp.csv");
        String POtxt7 = "Deutsch (R)";
        String POtxt8 = "Borg (R)";

        candidates = new String[]{ "Kleinberg (R)", "Rosen (D)", "Chou (I)", "Royce (L)", "Grant (I)", "Smith (D)", "Deutsch (R)", "Borg (R)"};
        PO_ballot_list = new ArrayList<Ballot>();
        lineballots_PO = new String[]{",,,1,,,,", "1,,,,,,,", ",,,,,,1,", ",,,1,,,,", "1,,,,,,,", ",,,,,,,1", ",,,,,1,,", ",,1,,,,,", ",1,,,,,,", ",,,,,,1,", ",,,,1,,,", ",,,,,,,1", "1,,,,,,,", "1,,,,,,,", ",1,,,,,,", ",,,,,1,,", ",,,,,1,,", ",,,,,,,1"};
        for (int i = 0; i < lineballots_PO.length; i++) {
            String temp = lineballots_PO[i];
            // System.out.println(temp);
            String[] ranks = temp.split(",", 0);
            Ballot line_ballot = fp_PO3.createBallot(ranks, candidates); // creates a ballot object
            PO_ballot_list.add(line_ballot); // add to ballot list
        }
        try {
            fp_PO3.simulate("PO", candidates, PO_ballot_list, 18, 0);
        } catch (IOException e) {
            System.out.println("An error occured");
            e.printStackTrace();
        }
        assertNotEquals(POtxt3, fp_PO3.getResults());
        assertNotEquals(POtxt4, fp_PO3.getResults());
        assertNotEquals(POtxt5, fp_PO3.getResults());
        assertNotEquals(POtxt6, fp_PO3.getResults());
        assertNotEquals(POtxt7, fp_PO3.getResults());
        assertNotEquals(POtxt8, fp_PO3.getResults());
    }


    @Test
    public void BuildIRElectionTest() {
        String[] filename1 = new String[]{"Project2/testing/IRV.csv"};
        FileParser parser1_IRV = new FileParser(filename1);
        String IRtxt1 = "Kleinberg (R)";
        try {
            parser1_IRV.buildElection();
        } catch (IOException e) {
            System.out.println("An error occured");
            e.printStackTrace();
        }
        assertEquals(IRtxt1, parser1_IRV.getResults());

        String[] filename2 = new String[]{"Project2/testing/IRV2.csv"};
        FileParser parser2_IRV = new FileParser(filename2);
        // String IRtxt2 = "Rosen (D)";
        try {
            parser2_IRV.buildElection();
        } catch (IOException e) {
            System.out.println("An error occured");
            e.printStackTrace();
        }
        assertTrue( parser2_IRV.getResults().equals("Rosen (D)") || parser2_IRV.getResults().equals("Grant (I)"));
        // assertEquals(IRtxt2, parser2_IRV.getResults());

        String[] filename3 = new String[]{"Project2/testing/IRV3.csv"};
        FileParser parser3_IRV = new FileParser(filename3);
        String IRtxt3 = "Rosen (D)";
        try {
            parser3_IRV.buildElection();
        } catch (IOException e) {
            System.out.println("An error occured");
            e.printStackTrace();
        }
        assertTrue( parser3_IRV.getResults().equals("Rosen (D)") || parser3_IRV.getResults().equals("Grant (I)") || parser3_IRV.getResults().equals("Kleinberg (R)"));

        String[] filename4 = new String[]{"Project2/testing/IRV4.csv"};
        FileParser parser4_IRV = new FileParser(filename4);

        // String IRtxt3 = "Chou (I)";
        try {
            parser4_IRV.buildElection();
        } catch (IOException e) {
            System.out.println("An error occured");
            e.printStackTrace();
        }
        assertTrue( parser4_IRV.getResults().equals("Royce (L)") || parser4_IRV.getResults().equals("Chou (I)") || parser4_IRV.getResults().equals("Rosen (D)"));

    }

    @Test
    public void buildCPLElectionTest() {
        String[] filename1 = new String[]{"Project2/testing/CPL.csv"};
        FileParser parser1_CPL = new FileParser(filename1);

        String CPLtxt1x0 = "\n---- First round of seat allocations ----\n" +
                "Democratic awarded 1 seats from 3 votes.\n" +
                "Republican awarded 1 seats from 2 votes.\n" +
                "New Wave awarded 0 seats from 0 votes.\n" +
                "Reform awarded 1 seats from 2 votes.\n" +
                "Green awarded 0 seats from 1 votes.\n" +
                "Independent awarded 0 seats from 1 votes.\n" +
                "\n" +
                "---- Second round of seat allocations ----\n" +
                "Independent awarded 1 seat from 1 remainder votes.\n" +
                "Democratic awarded 1 seat from 1 remainder votes.\n";

        String CPLtxt1x1 = "\n---- First round of seat allocations ----\n" +
                "Democratic awarded 1 seats from 3 votes.\n" +
                "Republican awarded 1 seats from 2 votes.\n" +
                "New Wave awarded 0 seats from 0 votes.\n" +
                "Reform awarded 1 seats from 2 votes.\n" +
                "Green awarded 0 seats from 1 votes.\n" +
                "Independent awarded 0 seats from 1 votes.\n" +
                "\n" +
                "---- Second round of seat allocations ----\n" +
                "Independent awarded 1 seat from 1 remainder votes.\n" +
                "Green awarded 1 seat from 1 remainder votes.\n";
        try {
            parser1_CPL.buildElection();
        } catch (IOException e) {
            System.out.println("An error occured");
            e.printStackTrace();
        }
        assertTrue( parser1_CPL.getResults().equals(CPLtxt1x0) || parser1_CPL.getResults().equals(CPLtxt1x1));

        String[] filename2 = new String[]{"Project2/testing/CPL2.csv"};
        FileParser parser2_CPL = new FileParser(filename2);

        String CPLtxt2 = "\n---- First round of seat allocations ----\n" +
                "Democratic awarded 1 seats from 4 votes.\n" +
                "Republican awarded 1 seats from 4 votes.\n" +
                "New Wave awarded 0 seats from 0 votes.\n" +
                "Reform awarded 1 seats from 4 votes.\n" +
                "Green awarded 0 seats from 2 votes.\n" +
                "Independent awarded 0 seats from 2 votes.\n" +
                "\n" +
                "---- Second round of seat allocations ----\n" +
                "Independent awarded 1 seat from 2 remainder votes.\n" +
                "Green awarded 1 seat from 2 remainder votes.\n";
        try {
            parser2_CPL.buildElection();
        } catch (IOException e) {
            System.out.println("An error occured");
            e.printStackTrace();
        }
        assertEquals(CPLtxt2, parser2_CPL.getResults());

        String[] filename3 = new String[]{"Project2/testing/CPL3.csv"};
        FileParser parser3_CPL = new FileParser(filename3);

        String CPLtxt3 = "\n---- First round of seat allocations ----\n" +
                "Democratic awarded 1 seats from 7 votes.\n" +
                "Republican awarded 1 seats from 5 votes.\n" +
                "New Wave awarded 0 seats from 0 votes.\n" +
                "Reform awarded 1 seats from 7 votes.\n" +
                "Green awarded 0 seats from 3 votes.\n" +
                "Independent awarded 0 seats from 3 votes.\n" +
                "\n" +
                "---- Second round of seat allocations ----\n" +
                "Independent awarded 1 seat from 3 remainder votes.\n" +
                "Green awarded 1 seat from 3 remainder votes.\n";
        try {
            parser3_CPL.buildElection();
        } catch (IOException e) {
            System.out.println("An error occured");
            e.printStackTrace();
        }
        assertEquals(CPLtxt3, parser3_CPL.getResults());

        String[] filename4 = new String[]{"Project2/testing/CPL4.csv"};
        FileParser parser4_CPL = new FileParser(filename4);

        String CPLtxt4x0 = "\n---- First round of seat allocations ----\n" +
                "Democratic awarded 1 seats from 4 votes.\n" +
                "Republican awarded 1 seats from 4 votes.\n" +
                "New Wave awarded 1 seats from 4 votes.\n" +
                "Reform awarded 0 seats from 3 votes.\n" +
                "Green awarded 1 seats from 4 votes.\n" +
                "Labour awarded 0 seats from 1 votes.\n" +
                "Non-Parliamentary political parties awarded 1 seats from 4 votes.\n" +
                "Plaid Cymru awarded 1 seats from 5 votes.\n" +
                "Independent awarded 0 seats from 1 votes.\n" +
                "\n" +
                "---- Second round of seat allocations ----\n" +
                "Reform awarded 1 seat from 3 remainder votes.\n" +
                "Plaid Cymru awarded 1 seat from 1 remainder votes.\n" +
                "Labour awarded 1 seat from 1 remainder votes.\n";

        String CPLtxt4x1 = "\n---- First round of seat allocations ----\n" +
                "Democratic awarded 1 seats from 4 votes.\n" +
                "Republican awarded 1 seats from 4 votes.\n" +
                "New Wave awarded 1 seats from 4 votes.\n" +
                "Reform awarded 0 seats from 3 votes.\n" +
                "Green awarded 1 seats from 4 votes.\n" +
                "Labour awarded 0 seats from 1 votes.\n" +
                "Non-Parliamentary political parties awarded 1 seats from 4 votes.\n" +
                "Plaid Cymru awarded 1 seats from 5 votes.\n" +
                "Independent awarded 0 seats from 1 votes.\n" +
                "\n" +
                "---- Second round of seat allocations ----\n" +
                "Reform awarded 1 seat from 3 remainder votes.\n" +
                "Independent awarded 1 seat from 1 remainder votes.\n" +
                "Plaid Cymru awarded 1 seat from 1 remainder votes.\n";

        try {
            parser4_CPL.buildElection();
        } catch (IOException e) {
            System.out.println("An error occured");
            e.printStackTrace();
        }
        // assertTrue( parser3_CPL.getResults().equals("Rosen (D)") || parser3_CPL.getResults().equals("Grant (I)"));
        assertTrue( parser4_CPL.getResults().equals(CPLtxt4x0) || parser4_CPL.getResults().equals(CPLtxt4x1));

    }

    @Test
    public void buildPOElection(){
        String[] filename1 = new String[]{"Project2/testing/PO.csv"};
        FileParser parser1_PO = new FileParser(filename1);
        String POtxt1 = "Pike (D)";
        try {
            parser1_PO.buildElection();
        } catch (IOException e) {
            System.out.println("An error occured");
            e.printStackTrace();
        }
        assertEquals(POtxt1, parser1_PO.getResults());

        String[] filename2 = new String[]{"Project2/testing/PO2.csv"};
        FileParser parser2_PO = new FileParser(filename2);
        String POtxt2 = "Borg (R)";
        try {
            parser2_PO.buildElection();
        } catch (IOException e) {
            System.out.println("An error occured");
            e.printStackTrace();
        }
        assertEquals(POtxt2, parser2_PO.getResults());

        String[] filename3 = new String[]{"Project2/testing/PO3.csv"};
        FileParser parser3_PO = new FileParser(filename3);
        // String POtxt3 = "Foster (D)";
        try {
            parser3_PO.buildElection();
        } catch (IOException e) {
            System.out.println("An error occured");
            e.printStackTrace();
        }
        assertTrue( parser3_PO.getResults().equals("Foster (D)") || parser3_PO.getResults().equals("Pike (D)"));

        String[] filename4 = new String[]{"Project2/testing/PO4.csv"};
        FileParser parser4_PO = new FileParser(filename4);
        String POtxt4 = "Borg (R)";
        try {
            parser4_PO.buildElection();
        } catch (IOException e) {
            System.out.println("An error occured");
            e.printStackTrace();
        }
        assertEquals(POtxt4, parser4_PO.getResults());
    }

    @Test
    public void multipleIRTest(){
        String[] filenames = new String[]{"Project2/testing/IRV.csv", "Project2/testing/IRV2.csv", "Project2/testing/IRV3.csv"};
        FileParser parser_IRV = new FileParser(filenames);
        String IRtxt = "Rosen (D)";
        try {
            parser_IRV.buildElection();
        } catch (IOException e) {
            System.out.println("An error occured");
            e.printStackTrace();
        }
        assertEquals(IRtxt, parser_IRV.getResults());
    }

    @Test
    public void multipleCPLTest(){
        String[] filenames = new String[]{"Project2/testing/CPL.csv", "Project2/testing/CPL2.csv", "Project2/testing/CPL3.csv"};
        FileParser parser_CPL = new FileParser(filenames);
        String CPLtxt = "\n---- First round of seat allocations ----\n" +
                "Democratic awarded 1 seats from 14 votes.\n" +
                "Republican awarded 1 seats from 11 votes.\n" +
                "New Wave awarded 0 seats from 0 votes.\n" +
                "Reform awarded 1 seats from 13 votes.\n" +
                "Green awarded 0 seats from 6 votes.\n" +
                "Independent awarded 0 seats from 6 votes.\n" +
                "\n" +
                "---- Second round of seat allocations ----\n" +
                "Independent awarded 1 seat from 6 remainder votes.\n" +
                "Green awarded 1 seat from 6 remainder votes.\n";
        try {
            parser_CPL.buildElection();
        } catch (IOException e) {
            System.out.println("An error occured");
            e.printStackTrace();
        }
        assertEquals(CPLtxt, parser_CPL.getResults());
    }

    @Test
    public void multiplePOTest(){
        String[] filenames = new String[]{"Project2/testing/PO.csv", "Project2/testing/PO2.csv", "Project2/testing/PO3.csv"};
        FileParser parser_PO = new FileParser(filenames);
        try {
            parser_PO.buildElection();
        } catch (IOException e) {
            System.out.println("An error occured");
            e.printStackTrace();
        }
        assertTrue( parser_PO.getResults().equals("Foster (D)") || parser_PO.getResults().equals("Pike (D)"));
    }

}