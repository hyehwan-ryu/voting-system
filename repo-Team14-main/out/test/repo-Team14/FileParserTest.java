import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;

import java.io.*;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileParserTest {

    @Test
    public void FileParserTest() {
        FileParser fp_cpl = new FileParser("Project1/test/CPL.csv");
        FileParser fp_ir = new FileParser("Project1/test/IRV.csv");

        File CPL = new File("Project1/test/CPL.csv");
        File IR = new File("Project1/test/IRV.csv");

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
        for(int i=0; i < lineballots_IR.length; i++){
            String temp = lineballots_IR[i];
            // System.out.println(temp);
            String[] ranks = temp.split(",", 0);
            Ballot line_ballot = fp_IR.createBallot(ranks, candidates); // creates a ballot object
            IR_ballot_list.add(line_ballot); // add to ballot list
        }

        try {
            fp_IR.simulate("IR", candidates, IR_ballot_list, 6 , 0);
        } catch (IOException e) {
            System.out.println("An error occured");
            e.printStackTrace();
        }
        assertEquals(IRtxt1 ,fp_IR.getResults());

        String IRtxt2 = "Grant (I)";
        String IRtxt3 = "Smith (D)";
        String IRtxt4 = "Rosen (D)";
        String IRtxt5 = "Klenberg (R)";
        String IRtxt6 = "Royce (L)";
        String IRtxt7 = "George (L)";
        String IRtxt8 = "Kirk (R)";
        FileParser fp_IR2 = new FileParser("temp.csv");
        candidates =  new String[]{"Rosen (D)", "Kleinberg (R)", "Chou (I)", "Royce (L)", "Grant (I)", "Smith (D)"};
        IR_ballot_list = new ArrayList<Ballot>();
        lineballots_IR = new String[]{"1,,2,,,", ",1,,2,3,4", "2,,1,,,", ",,,,1,", ",1,2,3,,", "2,3,1,6,5,4", "6,5,4,3,2,1", ",,1,,,", "3,,2,4,1,", "1,2,3,,,", "2,4,,1,3,5", "6,4,3,5,1,2"};
        for(int i=0; i < lineballots_IR.length; i++){
            String temp = lineballots_IR[i];
            // System.out.println(temp);
            String[] ranks = temp.split(",", 0);
            Ballot line_ballot = fp_IR2.createBallot(ranks, candidates); // creates a ballot object
            IR_ballot_list.add(line_ballot); // add to ballot list
        }
        try {
            fp_IR2.simulate("IR", candidates, IR_ballot_list, 12 , 0);
        } catch (IOException e) {
            System.out.println("An error occured");
            e.printStackTrace();
        }
        assertNotEquals(IRtxt3, fp_IR2.getResults());
        assertNotEquals(IRtxt4, fp_IR2.getResults());
        assertNotEquals(IRtxt5, fp_IR2.getResults());
        assertNotEquals(IRtxt6, fp_IR2.getResults());
        assertNotEquals(IRtxt7, fp_IR2.getResults());
        assertNotEquals(IRtxt8, fp_IR2.getResults());

        FileParser fp_IR3 = new FileParser("temp.csv");
        candidates =  new String[]{"Rosen (D)", "Kleinberg (R)", "Chou (I)", "Royce (L)", "Wilt (D)", "Farris (R)", "George (L)", "Kirk (R)"};
        IR_ballot_list = new ArrayList<Ballot>();
        lineballots_IR = new String[]{ "4,1,,3,,2,", ",,,1,3,2,5,4", ",,,,1,,,", ",2,,,,1,,", "2,3,,1,,,,", "6,8,2,1,3,4,5,8", "1,4,,2,,3,,5", ",,,,2,,,1", ",2,,3,,4,,1", "6,5,4,2,1,3,,", "3,,1,,2,5,,4", "5,4,3,2,6,1,8,7", ",3,5,4,,2,1,", "3,,,,,1,,2", ",1,,4,2,3,5,", "1,2,3,4,5,6,7,8", ",,1,3,2,,,", ",,,2,3,4,,1", ",1,,,,,,", "3,6,4,5,2,,1,", "8,7,6,5,4,3,2,1", "3,4,,2,,1,,", "7,6,1,4,3,2,5,8", ",,,,1,3,2,", "1,3,2,6,4,7,,5"};
        for(int i=0; i < lineballots_IR.length; i++){
            String temp = lineballots_IR[i];
            // System.out.println(temp);
            String[] ranks = temp.split(",", 0);
            Ballot line_ballot = fp_IR3.createBallot(ranks, candidates); // creates a ballot object
            IR_ballot_list.add(line_ballot); // add to ballot list
        }
        try {
            fp_IR3.simulate("IR", candidates, IR_ballot_list, 24 , 0);
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
    public void simulateCPLTest(){
        FileParser fp_CPL1 = new FileParser("temp.csv");
        String[] parties = new String[]{"Democratic", "Republican", "New Wave", "Reform", "Green", "Independent"};

        List<Ballot> CPL_ballot_list = new ArrayList<Ballot>();
        String[] lineballots_CPL = new String[]{"1,,,,,", "1,,,,,", ",1,,,,", ",,,,1,", ",,,,,1", ",,,1,,", ",,,1,,", "1,,,,,", ",1,,,,"};
        for(int i=0; i < lineballots_CPL.length; i++){
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
            fp_CPL1.simulate("CPL", parties, CPL_ballot_list, 9 , 3);
        } catch (IOException e) {
            System.out.println("An error occured");
            e.printStackTrace();
        }
        assertEquals(CPLtxt1 , fp_CPL1.getResults());

        FileParser fp_CPL2 = new FileParser("temp.csv");
        parties = new String[]{"Democratic", "Republican", "New Wave", "Reform", "Green", "Independent"};

        CPL_ballot_list = new ArrayList<Ballot>();
        lineballots_CPL = new String[]{"1,,,,,",  ",1,,,,",  ",,,,1,",  ",,1,,,",  ",,,1,,",  "1,,,,,",  ",1,,,,",  "1,,,,,",  ",,,1,,",  ",,,,,1",  ",,,,1,",  ",,,1,,",  "1,,,,,",  ",1,,,,",  ",1,,,,"};
        for(int i=0; i < lineballots_CPL.length; i++){
            String temp = lineballots_CPL[i];
            // System.out.println(temp);
            String[] ranks = temp.split(",", 0);
            Ballot line_ballot = fp_CPL2.createBallot(ranks, parties); // creates a ballot object
            CPL_ballot_list.add(line_ballot); // add to ballot list
        }

        String CPLtxt2 = "\n---- First round of seat allocations ----\n" +
                "Democratic awarded 1 seats from 4 votes.\n" +
                "Republican awarded 1 seats from 4 votes.\n" +
                "New Wave awarded 0 seats from 1 votes.\n" +
                "Reform awarded 1 seats from 3 votes.\n" +
                "Green awarded 0 seats from 2 votes.\n" +
                "Independent awarded 0 seats from 1 votes.\n" +
                "\n" +
                "---- Second round of seat allocations ----\n" +
                "Green awarded 1 seat from 2 remainder votes.\n" +
                "Independent awarded 1 seat from 1 remainder votes.\n";

        try {
            fp_CPL2.simulate("CPL", parties, CPL_ballot_list, 16 , 5);
        } catch (IOException e) {
            System.out.println("An error occured");
            e.printStackTrace();
        }
        assertEquals(CPLtxt2 , fp_CPL2.getResults());

        FileParser fp_CPL3 = new FileParser("temp.csv");
        parties = new String[]{"Democratic", "Republican", "New Wave", "Reform", "Green", "Labour", "Non-Parliamentary political parties", "Plaid Cymru", "Independent"};

        CPL_ballot_list = new ArrayList<Ballot>();
        lineballots_CPL = new String[]{"1,,,,,,,,",  ",,,,1,,,",  ",,,1,,,,,",  "1,,,,,,,,",  "1,,,,,,,,",  ",,,,,,,1,",  ",,,,1,,,",  ",1,,,,,,,",  ",,,,,1,,,",  ",,1,,,,,,",  ",,,,,,,1,",  "1,,,,,,,,",  ",,1,,,,,,",  ",1,,,,,,,",  ",1,,,,,,,",  ",,,,,,1,,",  ",,,,,,1,,",  ",,1,,,,,,",  ",,,,,,1,,",  ",,,,,,1,,",  ",,,,,,,1,",  ",,,,,,,,1",  ",,,1,,,,,",  ",1,,,,,,,",  ",,,,,1,,,",  ",,,,,,,1,",  ",,,,,,,1,",  ",,,,1,,,",  ",,1,,,,,,",  ",,,1,,,,,"};
        for(int i=0; i < lineballots_CPL.length; i++){
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
                "Reform awarded 1 seats from 3 votes.\n" +
                "Green awarded 1 seats from 3 votes.\n" +
                "Labour awarded 0 seats from 2 votes.\n" +
                "Non-Parliamentary political parties awarded 1 seats from 4 votes.\n" +
                "Plaid Cymru awarded 1 seats from 5 votes.\n" +
                "Independent awarded 0 seats from 1 votes.\n" +
                "\n" +
                "---- Second round of seat allocations ----\n" +
                "Plaid Cymru awarded 1 seat from 2 remainder votes.\n";

        try {
            fp_CPL3.simulate("CPL", parties, CPL_ballot_list, 30, 8);
        } catch (IOException e) {
            System.out.println("An error occured");
            e.printStackTrace();
        }
        assertEquals(CPLtxt3 , fp_CPL3.getResults());
    }

    @Test
    public void BuildIRElectionTest() {
        String filename1 = "Project1/testing/IRV.csv";
        FileParser parser1_IRV = new FileParser(filename1);
        String IRtxt1 = "Rosen (D)";
        try {
            parser1_IRV.buildElection();
        } catch (IOException e) {
            System.out.println("An error occured");
            e.printStackTrace();
        }
        assertEquals(IRtxt1, parser1_IRV.getResults());

        String filename2 = "Project1/testing/IRV2.csv";
        FileParser parser2_IRV = new FileParser(filename2);
        String IRtxt2 = "Grant (I)";
        try {
            parser2_IRV.buildElection();
        } catch (IOException e) {
            System.out.println("An error occured");
            e.printStackTrace();
        }
        assertEquals(IRtxt2, parser2_IRV.getResults());

        String filename3 = "Project1/testing/IRV4.csv";
        FileParser parser3_IRV = new FileParser(filename3);

        String IRtxt3 = "Wilt (D)";
        try {
            parser3_IRV.buildElection();
        } catch (IOException e) {
            System.out.println("An error occured");
            e.printStackTrace();
        }
        assertEquals(IRtxt3, parser3_IRV.getResults());
    }

    @Test
    public void buildCPLElectionTest(){
        String filename1 = "Project1/testing/CPL.csv";
        FileParser parser1_CPL = new FileParser(filename1);

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
            parser1_CPL.buildElection();
        } catch (IOException e) {
            System.out.println("An error occured");
            e.printStackTrace();
        }
        assertEquals(CPLtxt1, parser1_CPL.getResults());

        String filename2 = "Project1/testing/CPL2.csv";
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

        String filename3 = "Project1/testing/CPL4.csv";
        FileParser parser3_CPL = new FileParser(filename3);

        String CPLtxt3 = "\n---- First round of seat allocations ----\n" +
                "Democratic awarded 1 seats from 4 votes.\n" +
                "Republican awarded 1 seats from 4 votes.\n" +
                "New Wave awarded 1 seats from 4 votes.\n" +
                "Reform awarded 1 seats from 3 votes.\n" +
                "Green awarded 1 seats from 3 votes.\n" +
                "Labour awarded 0 seats from 2 votes.\n" +
                "Non-Parliamentary political parties awarded 1 seats from 4 votes.\n" +
                "Plaid Cymru awarded 1 seats from 5 votes.\n" +
                "Independent awarded 0 seats from 1 votes.\n" +
                "\n" +
                "---- Second round of seat allocations ----\n" +
                "Plaid Cymru awarded 1 seat from 2 remainder votes.\n";

        try {
            parser3_CPL.buildElection();
        } catch (IOException e) {
            System.out.println("An error occured");
            e.printStackTrace();
        }
        assertEquals(CPLtxt3, parser3_CPL.getResults());

    }


}