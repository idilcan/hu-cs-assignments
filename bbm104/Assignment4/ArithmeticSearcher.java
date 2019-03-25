import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public abstract class ArithmeticSearcher extends BaseSearcher {
    protected Map<String, Integer> secondAccepted;
    public ArithmeticSearcher()
    {
        accepted.put(new String("/"), new Integer(20));
        accepted.put(new String("-"), new Integer(20));
        accepted.put(new String("+"), new Integer(20));
        accepted.put(new String("|"), new Integer(20));
        accepted.put(new String("\\"), new Integer(20));

        secondAccepted = new HashMap<String, Integer>();
    }

    @Override
    protected int performSearch(Point location, int side) {
        Point point1 = nextPoint(location, side);
        Point point2 = nextPoint(point1, side);

        if (secondAccepted.containsKey(board.getData(point1)) &&
                accepted.containsKey(board.getData(point2))) {

            int score = 0;
            score += accepted.get(board.getData(location));
            score += secondAccepted.get(board.getData(point1));
            score += accepted.get(board.getData(point2));

            board.removeCells(location, point1, point2);
            return score;
        }
        return 0;
    }
}
