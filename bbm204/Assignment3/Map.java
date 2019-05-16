import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Map {
    private static ArrayList<Node> nodeList;
    private static int size;

    public static ArrayList<String> getNames() {
        return names;
    }

    private static ArrayList<String> names;

    public Map(){
        nodeList = new ArrayList<>();
        size = 0;
    }

    public void importMapFromFile(BufferedReader transportationFile) throws IOException {
        String line = transportationFile.readLine();
        char currentWay = 'b';
        int index = 0;
        while (line != null) {

            line = line.replaceAll("\t", "");
            //line = line.replaceAll("\n", " ");
            String[] input = line.split(" ");
            if (input[0].equalsIgnoreCase("airway")) {
                currentWay = 'a';
                index = 0;
                line = transportationFile.readLine();
                line = line.replaceAll("\t", " ");
                continue;
            }
            if (input[0].equalsIgnoreCase("highway")) {
                currentWay = 'h';
                index = 0;
                line = transportationFile.readLine();
                line = line.replaceAll("\t", " ");
                continue;
            }
            if (input[0].equalsIgnoreCase("railway")) {
                currentWay = 'r';
                index = 0;
                line = transportationFile.readLine();
                line = line.replaceAll("\t", " ");
                continue;
            }

            if (index < names.size()) {

                Node city = new Node(names.get(index));
                if (!hasElement(names.get(index))) {
                    addLink(currentWay, input, city);
                    nodeList.add(city);
                    index++;
                } else {
                    Node node = nodeList.get(index);
                    nodeList.remove(index);
                    addLink(currentWay, input, node);
                    nodeList.add(index, node);
                    index++;
                }
                line = transportationFile.readLine();
            }else{
                index = 0;
                line = transportationFile.readLine();
            }
        }



    }

    private void addLink(char currentWay, String[] input, Node node) {
        for (int i = 0; i < input[1].length(); i++) {
            char c = input[1].charAt(i);
            if (c == '1') {
                Link newLink = new Link(i, currentWay);
                node.addLink(newLink);
            }
        }
    }

    private boolean hasElement(String elementName){
        for(Node i: nodeList){
            if(i.getCityName().equalsIgnoreCase(elementName)){
                return true;
            }
        }
        return false;
    }

    public void names(BufferedReader transportationFile) throws IOException{
        names = new ArrayList<>();
        String line = transportationFile.readLine();
        line = line.replaceAll("\t", "");
        while (line != null){
            String[] input = line.split(" ");
            if(input[0].equalsIgnoreCase("airway") ||
                    input[0].equalsIgnoreCase("highway") || input[0].equalsIgnoreCase("railway")){
                if(names.size() > 0) break;
                line = transportationFile.readLine();
                line = line.replaceAll("\t", " ");
                continue;
            }
            if(line.equalsIgnoreCase("")) break;
            names.add(input[0]);
            line = transportationFile.readLine();
            line = line.replaceAll("\t", " ");
        }
    }

    public String toString(){
        StringBuilder str = new StringBuilder();
        for(Node i: nodeList){
            str.append(i.getCityName());
            str.append(" --> ");
            ArrayList<Integer> added = new ArrayList<>();
            for(Link j: i.getLinks()){
                if(added.contains(j.getIndex())){
                    continue;
                }
                str.append(nodeList.get(j.getIndex()).getCityName());
                str.append(" ");
                added.add(j.getIndex());
            }
            str.append("\n");
        }
        return str.toString();
    }

    public void q1(String from, String to, int times, char type, BufferedWriter out) throws IOException {
        ArrayList<Path> paths = getPathsFromTo(names.indexOf(from),names.indexOf(to));
        ArrayList<Path> pathsArray = new ArrayList<>();
        for (Path i: paths){
            if(type == 'a'){
                if(i.countCheck(-1,times,-1)){
                    pathsArray.add(i);
                }
                if(pathsArray.size() >= 5) break;
            }
            if(type == 'r'){
                if(i.countCheck(-1,-1,times)){
                    pathsArray.add(i);
                }
                if(pathsArray.size() >= 5) break;
            }
            if(type == 'h'){
                if(i.countCheck(times,-1,-1)){
                    pathsArray.add(i);
                }
                if(pathsArray.size() >= 5) break;
            }
        }
        if(paths.size() == 0 || pathsArray.size() == 0){
            out.write("There is no way!");
            out.newLine();
            return;
        }
        for(Path i : pathsArray){
            out.write(i.toString());
            out.newLine();
        }
    }

    public void q2(String from, String to, String through, BufferedWriter out) throws IOException {
        ArrayList<Path> paths = getPathsFromTo(names.indexOf(from),names.indexOf(to));
        ArrayList<Path> pathsArray = new ArrayList<>();
        for (Path i: paths){
            if(i.has(through, names)){
                pathsArray.add(i);

                if(pathsArray.size() >= 5) break;
            }
        }
        if(paths.size() == 0 || pathsArray.size() == 0){
            out.write("There is no way!");
            out.newLine();
            return;
        }
        for(Path i : pathsArray){
            out.write(i.toString());
            out.newLine();
        }
    }
    public void q3(String from, String to, char type,BufferedWriter out) throws IOException {
        ArrayList<Path> paths = getPathsFromTo(names.indexOf(from),names.indexOf(to));
        ArrayList<Path> pathsArray = new ArrayList<>();
        for (Path i: paths){
            if(type == 'a'){
                if(i.countCheck(0,-1,0)){
                    pathsArray.add(i);
                }
                if(pathsArray.size() >= 5) break;
            }
            if(type == 'r'){
                if(i.countCheck(0,0,-1)){
                    pathsArray.add(i);
                }
                if(pathsArray.size() >= 5) break;
            }
            if(type == 'h'){
                if(i.countCheck(-1,0,0)){
                    pathsArray.add(i);
                }
                if(pathsArray.size() >= 5) break;
            }
        }
        if(paths.size() == 0 || pathsArray.size() == 0){
            out.write("There is no way!");
            out.newLine();
            return;
        }
        for(Path i : pathsArray){
            out.write(i.toString());
            out.newLine();
        }
    }

    public void q4(String from, String to, int a, int h, int r, BufferedWriter out) throws IOException {
        ArrayList<Path> paths = getPathsFromTo(names.indexOf(from),names.indexOf(to));
        ArrayList<Path> pathsArray = new ArrayList<>();
        for (Path i: paths){
            if(i.countCheck(h,a,r)){
                pathsArray.add(i);
                if(pathsArray.size() >= 5) break;
            }
        }
        if(paths.size() == 0 || pathsArray.size() == 0){
            out.write("There is no way!");
            out.newLine();
            return;
        }
        for(Path i : pathsArray){
            out.write(i.toString());
            out.newLine();
        }
    }

    public ArrayList<Path> getPathsFromTo(int city1Index, int city2Index){
        ArrayList<Path> pathsArray = new ArrayList<>();
        dfs(city1Index,city2Index,pathsArray, new Path(names.get(city1Index)));

        return pathsArray;
    }

    private void dfs(int fromIndex, int toIndex, ArrayList<Path> paths, Path path){
        if(fromIndex == toIndex){
            Path pathtobeAdded = new Path(path);
            paths.add(pathtobeAdded);
            path.removeLastLink();
            return;
        }
        if(nodeList.get(fromIndex).getLinks() == null){
            path.removeLastLink();
            return;
        }
        for(Link i: nodeList.get(fromIndex).getLinks()){
            path.addLink(i);
            path.increase(i.getTransportWay());
            dfs(i.getIndex(),toIndex,paths,path);
            path.decrease(i.getTransportWay());
        }
        path.removeLastLink();

    }
}
