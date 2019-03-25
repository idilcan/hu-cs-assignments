import java.awt.*;

public class S_Searcher extends BaseSearcher {
    public S_Searcher() {
        accepted.put("S", 15);
    }

    @Override
    public int search(Point location) {
        int score = performSearch(location, 4);

        if(score == 0)
            score = performSearch(location, 6);

        return score;
    }
}
