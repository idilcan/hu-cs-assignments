import java.io.*;
import java.util.*;

public class Leaderboard {
    private class Player implements Comparable<Player>
    {
        public void setScore(int score) {
            this.score = score;
        }

        private String name;
        private int score;

        public Player(String name, int point) {
            this.name = name;
            this.score = point;
        }
        public Player(String line) {
            this.name = line.split(" ")[0];
            this.score = Integer.parseInt(line.split(" ")[1]);
        }

        @Override
        public int compareTo(Player o) {
            return o.score - this.score;
        }

        public String getName() {
            return name;
        }

        public int getScore() {
            return score;
        }
    }

    private List<Player> playerList = new ArrayList<>();

    public Leaderboard(){
        File leaderboardFile = new File("leaderboard.txt");
        try {
            Scanner reader = new Scanner(leaderboardFile);
            while (reader.hasNext()){
                Player player = new Player(reader.nextLine());
                playerList.add(player);
            }
            playerList.sort(Player::compareTo);

        } catch (FileNotFoundException e) {
        }
    }

    public void add(String name, int score){
        boolean found = false;
        for(Player player : playerList){
            if(player.getName().equals(name)){
                player.setScore(score);
                found = true;
            }
        }
        if(!found) {
            playerList.add(new Player(name, score));
            playerList.sort(Player::compareTo);
        }
    }

    public void printPlayer(String name){
        for(int i= 0; i< playerList.size(); i++){
            Player player = playerList.get(i);

            if(player.getName().equals(name))
            {
                System.out.print( "Your rank is " + Integer.toString(i+1) + "/" + Integer.toString(playerList.size()));
                //if not first player
                if(i<(playerList.size()-1))
                {
                    Player postPlayer = playerList.get(i+1);
                    int postPlayerScore = postPlayer.getScore();
                    String postPlayerName = postPlayer.getName();
                    if(postPlayerScore == player.getScore())
                    {
                        System.out.print(", your score is equal to " + postPlayerName);
                    }
                    else
                    {
                        System.out.print(", your score is " + Integer.toString(player.getScore() - postPlayerScore ) + " points higher than " + postPlayerName);
                    }
                }
                if(i>0 && i<(playerList.size()-1))
                    System.out.print(" and ");
                if(i>0)
                {
                    Player prePlayer = playerList.get(i-1);
                    int prePlayerScore = prePlayer.getScore();
                    String prePlayerName = prePlayer.getName();
                    if(prePlayerScore == player.getScore())
                    {
                        System.out.print(", your score is equal to " + prePlayerName);
                    }
                    else
                    {
                        System.out.print(", your score is " + Integer.toString(prePlayerScore - player.getScore()) + " points lower than " + prePlayerName);
                    }
                }
                break;
            }
        }

    }

    public void close(){
        try {
            PrintWriter writer = new PrintWriter("leaderboard.txt", "UTF-8");
            for(Player player : playerList){
                writer.println(player.getName() + " " + Integer.toString(player.getScore()));
            }
            writer.close();

        } catch (UnsupportedEncodingException e1) {

        } catch (FileNotFoundException e) {

        }
    }
}
