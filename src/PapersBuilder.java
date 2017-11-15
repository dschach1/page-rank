import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PapersBuilder {
    HashMap<String, List<String>> papers = new HashMap<>();

    public void add(String row) {
        String[] paper = row.split("\t");
        List<String> refersTo = papers.get(paper[0]);
        if (refersTo == null) {
            refersTo = new ArrayList<String>();
            papers.put(paper[0], refersTo);
        }
        if (!paper[1].equals("NULL")) {
            refersTo.add(paper[1]);
        }
    }
}


