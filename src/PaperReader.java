import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class PaperReader {

    void read(InputStream input, PapersBuilder b) {
        read(new InputStreamReader(input), b);
    }

    void read(String filename, PapersBuilder b) {
        try (FileReader fileReader = new FileReader(new File(filename))) {
            read(fileReader, b);
        }
        catch(IOException e) {

        }
    }

    void read(Reader reader, PapersBuilder b) {
        read(new BufferedReader(reader), b);
    }

    void read(BufferedReader bufferedReader, PapersBuilder b) {

        try {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                b.add(line);
            }
        }
        catch(IOException e) {

        }
    }
}
