import java.io.*;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader network = new BufferedReader(new FileReader(args[0]));//args1

        BufferedReader query = new BufferedReader(new FileReader(args[1]));//args2
        BufferedWriter out = new BufferedWriter(new FileWriter(args[2]));//args3

        Map initialMap = new Map();
        initialMap.names(network);
        network.close();
        network = new BufferedReader(new FileReader(args[0]));
        initialMap.importMapFromFile(network);

        String line = query.readLine();
        while (line != null){
            line = line.replaceAll("\t", "");
            String[] input = line.split(" ");
            String command = input[0];
            if(command.equalsIgnoreCase("printgraph")){
                out.write(command);
                out.newLine();
                out.write(initialMap.toString());
                out.newLine();
                line = query.readLine();
                continue;
            }
            String from = input[1];
            String to = input[2];
            out.write(command);
            out.write(", ");
            out.write(from);
            out.write(", ");
            out.write(to);
            out.write(", ");
            if(command.equalsIgnoreCase("q1")){
                out.write(input[3]);
                out.write(", ");
                out.write(input[4]);
                out.newLine();
                initialMap.q1(from,to,Integer.parseInt(input[3]), Character.toLowerCase(input[4].charAt(0)),out);
                line = query.readLine();
                continue;
            }
            if(command.equalsIgnoreCase("q2")){
                out.write(input[3]);
                out.newLine();
                initialMap.q2(from,to,input[3],out);
                line = query.readLine();
                continue;
            }
            if(command.equalsIgnoreCase("q3")){
                out.write(input[3]);
                out.newLine();
                initialMap.q3(from,to,Character.toLowerCase(input[3].charAt(0)),out);
                line = query.readLine();
                continue;
            }
            if (command.equalsIgnoreCase("q4")){
                out.write(input[3]);
                out.write(", ");
                out.write(input[4]);
                out.write(", ");
                out.write(input[5]);
                out.newLine();
                int a = 0, h= 0, r = 0;
                for(int i = 3; i < input.length; i++)
                switch(input[i].charAt(0)){
                    case 'A':
                    case 'a':
                        a = Character.getNumericValue(input[i].charAt(1));
                        break;
                    case 'H':
                    case 'h':
                        h = Character.getNumericValue(input[i].charAt(1));
                        break;
                    case 'R':
                    case 'r':
                        r = Character.getNumericValue(input[i].charAt(1));
                        break;
                }
                initialMap.q4(from,to,a,h,r,out);
            }
            line = query.readLine();
        }
        out.close();
    }
}
