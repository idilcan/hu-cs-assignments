import java.awt.*;

public class BackSlash_Searcher extends ArithmeticSearcher {
    public BackSlash_Searcher() {
        secondAccepted.put(new String("\\"), new Integer(20));
    }

    @Override
    public int search(Point location) {
        int score = performSearch(location, 1);

        if(score == 0)
            score = performSearch(location, 9);

        return score;
    }
}

