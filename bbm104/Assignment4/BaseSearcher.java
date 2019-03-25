import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseSearcher implements SearcherInterface{
    protected Board board;
    protected Map<String, Integer> accepted;


    public BaseSearcher()
    {
        board = null;
        accepted = new HashMap<String, Integer>();
    }
    public void setBoard(Board board)
    {
        this.board = board;
    }

    protected int performSearch(Point location, int side)
    {
        Point point1 = nextPoint(location, side);
        Point point2 = nextPoint(point1, side);

        if (accepted.containsKey(board.getData(location)) &&
                accepted.containsKey(board.getData(point1)) &&
                accepted.containsKey(board.getData(point2))) {

            int score = 0;
            score += accepted.get(board.getData(location));
            score += accepted.get(board.getData(point1));
            score += accepted.get(board.getData(point2));

            board.removeCells(location, point1, point2);
            return score;
        }
        return 0;
    }

    protected Point nextPoint(Point input, int side)
    {
        Point nPoint = new Point(input);

        switch(side){
            case 1:
                nPoint.x--;
                nPoint.y--;
                break;
            case 2:
                nPoint.x--;
                break;
            case 3:
                nPoint.x--;
                nPoint.y++;
                break;
            case 4:
                nPoint.y--;
                break;
            case 6:
                nPoint.y++;
                break;
            case 7:
                nPoint.x++;
                nPoint.y--;
                break;
            case 8:
                nPoint.x++;
                break;
            case 9:
                nPoint.x++;
                nPoint.y++;
                break;
        }

        return nPoint;
    }
}
