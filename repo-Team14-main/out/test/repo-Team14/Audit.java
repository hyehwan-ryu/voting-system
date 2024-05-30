import java.io.File;
import java.io.IOException;
import java.io.FileWriter;

/**
 * Represents an audit file. Audit objects are used to append
 * election progress information to an audit file throughout
 * the election process.
 * @author Liam O'Neil
 */
public class Audit{

    private String filepath;
    private File file;

    /**
     * Creates an audit file to be used to track election information.
     *
     * @param   filepath    The file path and name of the .txt audit file to be created.
     * @throws  IOException Signals that the file cannot be created.
     */
    public Audit(String filepath) throws IOException
    {
        this.filepath = filepath;
        file = new File(filepath);
        file.delete();
        file.createNewFile();
    }

    /**
     * Writes to the audit file represented by this Audit object.
     *
     * @param   text  A String to write to the file.
     * @throws  IOException  Signals that the file cannot be opened,
     *                      or the text cannot be written to the file.
     */
    public void appendString(String text) throws IOException
    {
        FileWriter writer = new FileWriter(filepath, true);
        writer.write(text);
        writer.write("\n");
        writer.close();
    }

}