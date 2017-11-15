import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        HashMap<String, List<String>> papers = loadPapers("/Users/davidsc/Downloads/paper_rank_data.txt");
        PageRankBuilder builder = new PageRankBuilder();
        builder.compute(papers);
    }

    private static HashMap<String, List<String>> loadPapers(String filename) {
        InputStream input = Main.class.getResourceAsStream("/paper_rank_data.txt");
        PaperReader pr = new PaperReader();
        PapersBuilder pb = new PapersBuilder();
        pr.read(input, pb);
        return pb.papers;
    }
}
