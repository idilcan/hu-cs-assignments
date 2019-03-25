import java.awt.*;



public class Command {
    public String getName() {
        return name;
    }

    enum CommandType
    {
        LOCATION, ENDGAME, NAME
    }

    private static boolean isExit = false;
    CommandType type;

    public Point getLocation() {
        return location;
    }

    private Point location;
    private String name;

    public String getLine() {
        return line;
    }

    private String line;

    public Command(String line)
    {
        this.line = line;
        location = new Point(0,0);

        if(isExit == true)
        {
            type = CommandType.NAME;
            name = line;
            return;
        }
        if(line.charAt(0) == 'E')
        {
            isExit = true;
            type = CommandType.ENDGAME;
            return;
        }
        type = CommandType.LOCATION;
        location.x = Integer.parseInt(line.split(" ")[0]);
        location.y = Integer.parseInt(line.split(" ")[1]);
    }

    public void print()
    {
        if(type == CommandType.NAME)
            System.out.println("name : " + name);
        else if(type == CommandType.ENDGAME)
            System.out.println("End Game");
        else
        {
            System.out.print("X: " + Integer.toString(location.x) + " - ");
            System.out.println("Y: " + Integer.toString(location.y));
        }
    }

    public boolean isLocation()
    {
        return type == CommandType.LOCATION;
    }

    public boolean isEndGame()
    {
        return type == CommandType.ENDGAME;
    }

    public boolean isName()
    {
        return type == CommandType.NAME;
    }
}
