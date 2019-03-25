import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PipedInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Main {
    private static Leaderboard leaderboard = new Leaderboard();
    private static Board board = null;
    private static Board readFromBoardFile(File boardFile) {
        Board board = new Board();
        try (BufferedReader br = new BufferedReader(new FileReader(boardFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                board.readLine(line);
            }
        }catch(Exception e) {
            System.out.println("ERROR : File " + boardFile.getName() + " threw an exception.");
        }
        return board;
    }

    private static List<Command> readFromInputFile(File inputFile) {
        List<Command> out = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                out.add(new Command(line));
            }
        }catch(Exception e) {
            System.out.println("ERROR : File " + inputFile.getName() + " threw exception " + e);
        }
        return out;
    }

    public static void main(String[] args) {

        File file = new File("gameGrid.txt");
        board = readFromBoardFile(file);
        /*this is where code will read inputs from file*/
       // board.print();

        SearcherFactory.board = board;
        List<Command> commands = null;

        if(args.length != 0){
            if(args[0].equals("<"))
            {
                File infile = new File(args[1]);
                commands = readFromInputFile(infile);
            }else{
                File infile = new File(args[0]);
                commands = readFromInputFile(infile);
            }
        }
        
        if(commands == null)
            executeUserInput();
        else
            executeInput(commands);
    }

    private static void executeInput(List<Command> commands) {
        int score = 0;
        boolean first = false;
        boolean gameEnded = false;
        System.out.println("Game grid: ");
        for(Command command : commands)
        {

            if(gameEnded == false) {
                board.print();
                if (command.isLocation()) {
                    if (first) {
                        System.out.println("Score: " + Integer.toString(score) + " points");
                    }
                    score += execute(command);
                    System.out.println("Select coordinate or enter E to end the game: " + command.getLine());
                }
                else if (command.isEndGame()) {
                    gameEnded = true;
                    System.out.println();
                    System.out.println("Select coordinate or enter E to end the game: " + command.getLine());

                }
            }
            else {
                if (command.isName()) {
                    leaderboard.add(command.getName(), score);
                    System.out.println("Total score: " + Integer.toString(score));
                    System.out.println();
                    System.out.println("Enter Name: " + command.getLine());
                    leaderboard.printPlayer(command.getLine());
                    System.out.println();
                    System.out.println("Good bye!");
                    leaderboard.close();
                    break;
                }
            }
            first = true;
        }
    }

    private static void executeUserInput(){
        System.out.println("RUNNING USER MODE");
        Scanner reader = new Scanner(System.in);
        int score = 0;
        boolean first = false;
        boolean cont = true;
        System.out.println("Game grid: ");
        System.out.println();
        boolean gameEnded = false;
        while (cont){
            board.print();
            if (first)
                System.out.println("Score: " + Integer.toString(score) + " points");
            else
                System.out.println();
            System.out.print("Select coordinate or enter E to end the game: ");
            String inputLine = reader.nextLine();
            System.out.println(inputLine);
            System.out.println();
            if(inputLine.equalsIgnoreCase("e")){
                System.out.println("Total score: " + Integer.toString(score));
                System.out.println();
                System.out.print("Enter Name: ");
                String name = reader.next();
                System.out.println(name);
                leaderboard.add(name, score);
                leaderboard.printPlayer(name);
                System.out.println();
                System.out.println();
                System.out.println("Good bye!");
                cont = false;
                leaderboard.close();
                break;
            }
            Command command = new Command(inputLine);

            score += execute(command);
            first = true;
        }

    }

    private static int execute(Command command){
        int score = 0;
        if(command.isLocation())
        {
            Point loc = command.getLocation();
            SearcherInterface searcher = SearcherFactory.getSearcher(board.getData(loc));
            if(searcher != null)
                score += searcher.search(loc);

        }

        return score;
    }
}
