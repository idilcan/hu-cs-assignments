import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class W_Searcher extends BaseSearcher {
    private String tString = "T";
    private String sString = "S";
    private String dString = "D";
    private String minusString = "-";
    private String plusString = "+";
    private String orString = "|";
    private String slashString = "/";
    private String backslashString = "\\";
    private String wString = "W";

    public W_Searcher() {
        accepted.put("W", 10);
        accepted.put(new String("T"), new Integer(15));
        accepted.put("D", 30);
        accepted.put(new String("T"), new Integer(15));
        accepted.put(new String("/"), new Integer(20));
        accepted.put(new String("-"), new Integer(20));
        accepted.put(new String("+"), new Integer(20));
        accepted.put(new String("|"), new Integer(20));
        accepted.put(new String("\\"), new Integer(20));
    }

    private int removePoints(Point location, Point point2, Point point3) {
        int score = accepted.get(board.getData(location));
        score += accepted.get(board.getData(point2));
        score += accepted.get(board.getData(point3));

        board.removeCells(location, point2, point3);

        return score;
    }

    private int searchUpsideDown(Point location, int side)
    {
        int score = 0;
        List<String> point2AcceptedList = new ArrayList<>();
        List<String> point3AcceptedList = new ArrayList<>();
        point2AcceptedList.add(tString);
        point2AcceptedList.add(plusString);
        point2AcceptedList.add(orString);

        Point point2 = nextPoint(location, side);
        Point point3 = nextPoint(point2, side);

        String s2 = board.getData(point2);
        String s3 = board.getData(point3);
        if(s2.equals(wString))
        {
            point3AcceptedList.add(tString);
            point3AcceptedList.add(wString);
            if(point3AcceptedList.contains(s3) || isArithmetic(s3))
            {
                score = removePoints(location, point2, point3);
            }
        }
        else if(s2.equals(tString) && s3.equals(tString))
        {
            score = removePoints(location, point2, point3);
        }
        else if((s2.equals(plusString) || s2.equals(orString)) && isArithmetic(s3))
        {
            score = removePoints(location, point2, point3);
        }
        return score;
    }

    private int searchLeftRight(Point location, int side)
    {
        int score = 0;
        List<String> point2AcceptedList = new ArrayList<>();
        point2AcceptedList.add(sString);
        point2AcceptedList.add(minusString);
        point2AcceptedList.add(plusString);

        Point point2 = nextPoint(location, side);
        Point point3 = nextPoint(point2, side);

        String s2 = board.getData(point2);
        String s3 = board.getData(point3);
        if(s2.equals(wString))
        {

            List<String> point3AcceptedList = new ArrayList<>();
            point3AcceptedList.add(sString);
            point3AcceptedList.add(wString);
            if(point3AcceptedList.contains(s3) || isArithmetic(s3))
            {
                score = removePoints(location, point2, point3);
            }
        }
        else if(s2.equals(sString) && s3.equals(sString))
        {
            score = removePoints(location, point2, point3);
        }
        else if((s2.equals(plusString) || s2.equals(minusString)) && isArithmetic(s3))
        {
            score = removePoints(location, point2, point3);
        }
        return score;
    }

    private int searchDiagonal(Point location, int side, String searchString)
    {
        List<String> point2AcceptedList = new ArrayList<>();
        int score = 0;
        point2AcceptedList.add(dString);
        point2AcceptedList.add(searchString);

        Point point2 = nextPoint(location, side);
        Point point3 = nextPoint(point2, side);

        String s2 = board.getData(point2);
        String s3 = board.getData(point3);
        if(s2.equals(wString))
        {
            List<String> point3AcceptedList = new ArrayList<>();
            point3AcceptedList.add(dString);
            point3AcceptedList.add(wString);

            if(point3AcceptedList.contains(s3) || isArithmetic(s3))
            {
                score = removePoints(location, point2, point3);
            }
        }
        else if(s2.equals(dString) && s3.equals(dString))
        {
            score = removePoints(location, point2, point3);
        }
        else if(s2.equals(searchString) && isArithmetic(s3))
        {
            score = removePoints(location, point2, point3);
        }
        return score;
    }



    private boolean isArithmetic(String s)
    {
        return s.equals("-") || s.equals("+") || s.equals("|") || s.equals("\\") || s.equals("/");
    }


    @Override
    public int search(Point location) {
        int score = 0;
        score = searchUpsideDown(location, 2);
        if(score == 0)
            score = searchUpsideDown(location, 8);

        if(score == 0)
            score = searchLeftRight(location, 4);
        if(score == 0)
            score = searchLeftRight(location, 6);

        if(score == 0)
            score = searchDiagonal(location, 1, backslashString);
        if(score == 0)
            score = searchDiagonal(location, 9, backslashString);

        if(score == 0)
            score = searchDiagonal(location, 3, slashString);
        if(score == 0)
            score = searchDiagonal(location, 7, slashString);


        return score;

    }
}
