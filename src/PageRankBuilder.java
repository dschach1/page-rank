import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class PageRankBuilder {

    private final double DAMPING_FACTOR;
    private final double EPSILON;
    private final int MAX_ITERATIONS;
    private static final double DEFAULT_DAMPING_FACTOR = 0.85;
    private static final double DEFAULT_EPSILON = 0.01;
    private static final int DEFAUL_MAX_ITERATIONS = 25;
    private static final List<String> EMPTY_LIST = new ArrayList<String>();

    PageRankBuilder() {
        this(DEFAUL_MAX_ITERATIONS, DEFAULT_DAMPING_FACTOR, DEFAULT_EPSILON);
    }

    PageRankBuilder(int maxIterations, double dampingFactor, double epsilon) {
        this.MAX_ITERATIONS = maxIterations;
        this.DAMPING_FACTOR = dampingFactor;
        this.EPSILON = epsilon;
    }

    List<Map<String, Double>> compute(HashMap<String, List<String>> papers) {
        Map<String, List<String>> citesMe = buildReverseMap(papers);
        List<String> citesNoOne = buildCitesNoOne(papers);
        List<Map<String, Double>> pageRanks = new ArrayList<Map<String, Double>>();
        pageRanks.add(computeInitialPageRanks(papers));
        for (int i = 0; i < MAX_ITERATIONS; i++) {
            pageRanks.add(computeNextPageRank(DAMPING_FACTOR,
                    papers,
                    citesMe,
                    citesNoOne,
                    pageRanks.get(i)
            ));
            double delta = computeDelta(pageRanks.get(i), pageRanks.get(i + 1));
            System.out.println("i = " + i + " delta = " + delta);
            if (delta < EPSILON) {
                break;
            }
        }
        return pageRanks;
    }

    HashMap<String, Double> computeNextPageRank(double d,
                                                Map<String, List<String>> papers,
                                                Map<String, List<String>> citesMe,
                                                List<String> citesNoOne,
                                                Map<String, Double> pageRank
    ) {
        double prFromNoCitations = getPageRankFromNoCitations(papers, citesNoOne, pageRank);

        HashMap<String, Double> newPageRank = new HashMap<>();

        for (Map.Entry<String, List<String>> entry : papers.entrySet()) {
            String paper = entry.getKey();
            double prFromCitations = 0.0;
            List<String> citingPapers = citesMe.get(paper);
            if (citingPapers != null) {
                for (String citingPaper : citingPapers) {
                    prFromCitations += pageRank.get(citingPaper) / papers.get(citingPaper).size();
                }
            }
            newPageRank.put(paper, (1 - d) / papers.size() + d * (prFromNoCitations + prFromCitations));
        }

        double totalPr  = newPageRank.values().stream().mapToDouble(pr->pr).sum();
        totalPr = newPageRank.values().stream().mapToDouble(pr->pr).sum();
        System.out.println("totalPr = " + totalPr);

        return newPageRank;
    }

    private double computeDelta(Map<String, Double> prOld, Map<String, Double> prNew) {
        return prOld.keySet().stream().mapToDouble(k -> Math.abs(prOld.get(k) - prNew.get(k))).sum();
    }

    private double getPageRankFromNoCitations(Map<String, List<String>> papers, List<String> citesNoOne, Map<String, Double> pageRank) {
        // Compute contribution of papers that do not cite any other paper
        return citesNoOne.stream().mapToDouble(paper -> pageRank.get(paper)).sum()/papers.size();
    }

    private Map<String, Double> computeInitialPageRanks(HashMap<String, List<String>> papers) {
        int numberOfPapers = papers.size();
        double initialPageRank = 1.0/numberOfPapers;
        return papers.keySet().stream().collect(Collectors.toMap( Function.identity(), k -> initialPageRank));
    }

    private Map<String, List<String>> buildReverseMap(Map<String, List<String>> papers) {
        HashMap<String, List<String>> reversMap = new HashMap<>();

        for (Map.Entry<String, List<String>> entry : papers.entrySet()) {
            List<String> citedPapers = entry.getValue();
            for (String citedPaper : citedPapers) {
                List<String> papersCitingMe = reversMap.get(citedPaper);
                if (papersCitingMe == null) {
                    papersCitingMe = new ArrayList<>();
                    reversMap.put(citedPaper, papersCitingMe);
                }
                papersCitingMe.add(entry.getKey());
            }
            if (reversMap.get(entry.getKey()) == null) {
                reversMap.put(entry.getKey(), new ArrayList<>());
            }
        }
        return reversMap;
    }

    private List<String> buildCitesNoOne(Map<String, List<String>> papers) {
        List<String> noCitations = new ArrayList<String>();;

        for (Map.Entry<String, List<String>> entry : papers.entrySet()) {
            List<String> citedPapers = entry.getValue();
                if (citedPapers.size() == 0) {
                    noCitations.add(entry.getKey());
            }
        }
        return noCitations;
    }
}
