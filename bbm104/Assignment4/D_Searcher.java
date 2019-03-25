import java.awt.*;

public class D_Searcher extends BaseSearcher {
    public D_Searcher() {
        accepted.put("D", 30);
    }

    @Override
    public int search(Point location) {
        int score = performSearch(location, 1);

        if(score == 0)
            score = performSearch(location, 9);
        if(score == 0)
            score = performSearch(location, 3);
        if(score == 0)
            score = performSearch(location, 7);

        return score;
    }
}
