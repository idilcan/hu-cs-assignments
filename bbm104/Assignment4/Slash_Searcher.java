import java.awt.*;

public class Slash_Searcher extends ArithmeticSearcher {
    public Slash_Searcher() {
        secondAccepted.put(new String("/"), new Integer(20));
    }

    @Override
    public int search(Point location) {
        int score = performSearch(location, 3);

        if(score == 0)
            score = performSearch(location, 7);

        return score;
    }
}
