import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
public class Exp2 {
    public static void main(String[] args) throws IOException {
        BufferedReader input = new BufferedReader(new FileReader(args[0]));
        BufferedWriter output = new BufferedWriter(new FileWriter(args[1]));
        BinarySearchTree tree = null;
        String line = input.readLine();
        do{
            String command[] = line.split(" ");
            if(command[0].equals("CreateBST")){
                ArrayList<String> arr = new ArrayList<>(Arrays.asList(command[1].split(",")));
                tree = new BinarySearchTree((ArrayList<Integer>) arr.stream().map(Integer::parseInt).collect(Collectors.toList()),output);
            }
            else if(command[0].equals("CreateBSTH")){
                tree = new BinarySearchTree(Integer.parseInt(command[1]),output);
            }
            else if(command[0].equals("FindHeight")){
                tree.getHeight(output);
            }
            else if(command[0].equals("FindWidth")){
                tree.getWidth(output);
            }
            else if(command[0].equals("Preorder")){
                tree.preorder(output);
            }
            else if(command[0].equals("LeavesAsc")){
                tree.leavesAsc(output);
            }
            else if(command[0].equals("DelRoot")){
                tree.delete(0,output);
            }
            else if(command[0].equals("DelRootLc")){
                tree.delete(-1,output);
            }
            else if(command[0].equals("DelRootRc")){
                tree.delete(1,output);
            }
            line = input.readLine();
        }while (line != null);
        output.close();
    }
}