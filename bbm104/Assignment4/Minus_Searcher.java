import java.awt.*;

public class Minus_Searcher extends ArithmeticSearcher {
    public Minus_Searcher() {
        secondAccepted.put(new String("-"), new Integer(20));
        secondAccepted.put(new String("+"), new Integer(20));
    }

    @Override
    public int search(Point location) {
        int score = performSearch(location, 4);

        if(score == 0)
            score = performSearch(location, 6);

        return score;
    }
}
