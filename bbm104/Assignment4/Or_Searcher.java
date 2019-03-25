import com.sun.org.apache.xpath.internal.operations.Or;

import java.awt.*;

public class Or_Searcher extends ArithmeticSearcher {
    public Or_Searcher() {
        secondAccepted.put(new String("|"), new Integer(20));
        secondAccepted.put(new String("+"), new Integer(20));

    }

    @Override
    public int search(Point location) {
        int score = performSearch(location, 2);

        if(score == 0)
            score = performSearch(location, 8);

        return score;
    }
}
