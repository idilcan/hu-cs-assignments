import java.awt.*;

public class T_Searcher extends BaseSearcher {
    public T_Searcher() {
        accepted.put(new String("T"), new Integer(15));
    }

    @Override
    public int search(Point location) {
        int score = performSearch(location, 2);

        if(score == 0)
            score = performSearch(location, 8);

        return score;
    }
}
