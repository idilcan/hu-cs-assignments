import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Board {
    private int numberOfRows;
    private int numberOfColumns;

    private List<List<String>> board;

    public Board() {
        numberOfColumns = 0;
        numberOfRows = 0;
        board = new ArrayList<>();
    }

    public boolean readLine(String line)
    {
        String[] tokens = line.split(" ");

        int size = tokens.length;
        if(numberOfColumns == 0)
            numberOfColumns = size;
        else if(size != numberOfColumns)
            return false;

        List<String> row = new ArrayList<>();
        for(String token : tokens)
        {
            row.add(token);
        }
        board.add(row);
        numberOfRows++;
        return true;
    }

    public void print()
    {
        //System.out.println("number of rows:" + Integer.toString(numberOfRows) + " columns:" + Integer.toString(numberOfColumns));
        for(List<String> row : board)
        {
            for(String token : row)
            {
                System.out.print(token + " ");
            }
            System.out.println();
        }
    }

    public int executePoint(Point location)
    {
        if(numberOfRows == 0 || numberOfColumns == 0)
            return 0;

        if(location.x > numberOfRows || location.x < 0 || location.y > numberOfColumns || location.y < 0)
            return 0;

        String data = board.get(location.x).get(location.y);


        return 0;
    }

    public String getData(Point location)
    {
        if(numberOfRows == 0 || numberOfColumns == 0)
            return "";

        if(location.x >= numberOfRows || location.x < 0 || location.y >= numberOfColumns || location.y < 0)
            return "";

        return board.get(location.x).get(location.y);
    }

    public void removeCells(Point p1, Point p2, Point p3)
    {
        board.get(p1.x).set(p1.y, " ");
        board.get(p2.x).set(p2.y, " ");
        board.get(p3.x).set(p3.y, " ");


        //make cells fall
        if(p1.y == p2.y) {
            Point p = null;
            if(p1.x>p2.x)
                p = p1;
            else
                p = p3;

            dropColumn(p);
            dropColumn(p);
            dropColumn(p);

        }
        else
        {
            dropColumn(p1);
            dropColumn(p2);
            dropColumn(p3);
        }
    }

    private void dropColumn(Point p)
    {
        for(int i = p.x; i>0; i--)
        {
            board.get(i).set(p.y, board.get(i-1).get(p.y));
        }
        board.get(0).set(p.y, " ");
    }

}
