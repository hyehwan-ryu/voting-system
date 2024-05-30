import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.junit.Test;

public class AuditTest {
    // constructor test
    @Test
    public void testAudit() throws IOException{
        // When
        String filepath = "audit.txt";
        new Audit(filepath);

        // Then
        File f = new File("audit.txt");
        assertEquals(true, f.exists());
    }

    // appendString test
    @Test
    public void appendStringTest() throws IOException {

        String text1 = "test text1";
        String text2 = "";
        Audit auditFile = new Audit("auditTestFile.txt");

        auditFile.appendString(text1);
        auditFile.appendString(text2);
        BufferedReader reader = new BufferedReader(new FileReader("auditTestFile.txt"));
        assertEquals(text1, reader.readLine());
        assertEquals(text2, reader.readLine());
        reader.close();
    }
}