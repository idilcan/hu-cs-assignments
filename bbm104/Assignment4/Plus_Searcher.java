import java.awt.*;

public class Plus_Searcher extends ArithmeticSearcher {
    public Plus_Searcher() {
        secondAccepted.put(new String("+"), new Integer(20));

    }

    @Override
    public int search(Point location) {
        String minusString = new String("-");
        secondAccepted.put(minusString, new Integer(20));

        int score = performSearch(location, 4);

        if(score == 0)
            score = performSearch(location, 6);

        secondAccepted.remove(minusString);
        secondAccepted.put(new String("|"), new Integer(20));
        if(score == 0)
            score = performSearch(location, 2);
        if(score == 0)
            score = performSearch(location, 8);

        return score;
    }
}
