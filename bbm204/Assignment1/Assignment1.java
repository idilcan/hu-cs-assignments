import java.io.*;
import java.util.*;

public class Assignment1 {

    public static void main(String[] args) {
        int[] maxes = new int [10];
        for(int e = 0; e < 10; e++){
            maxes[e] = (e+1)*1000;
        }
        //int[] maxes = {50};
        ArrayList<Double> arrayMerge = new ArrayList<>();
        ArrayList<Double> arrayInsertion = new ArrayList<>();
        ArrayList<Double> arrayRadix = new ArrayList<>();
        ArrayList<Double> arraySelection = new ArrayList<>();
        ArrayList<Double> arrayBinary = new ArrayList<>();
        for(int j=0; j<3; j++) {

            for (int max : maxes) {
                List<Integer> arrayList = new ArrayList<>();
                for (int i = 0; i < max; i++) {
                    arrayList.add(i);
                }

                int searchedItem =  0;

                switch (j){
                    case 0 : searchedItem = (max -1)/2; Collections.sort(arrayList);break;
                    case 1 : while(searchedItem == (max-1)/2) {searchedItem = getRandomNumberInRange(0, max);} Collections.shuffle(arrayList);break;
                    case 2 : searchedItem = max+1;Collections.reverse(arrayList);break;
                }

                List<Integer> sort1 = arrayList;
                List<Integer> sort2 = arrayList;
                List<Integer> sort3 = arrayList;
                List<Integer> sort4 = arrayList;

                double t1 = System.nanoTime();
                Sort.merge(sort1);
                double t2 = System.nanoTime();
                Sort.insertion(sort2);
                double t3 = System.nanoTime();
                Sort.radix(sort3);
                double t4 = System.nanoTime();
                Sort.selection(sort4);
                double t5 = System.nanoTime();
                Search.binary(sort3, searchedItem);
                double t6 = System.nanoTime();

                arrayMerge.add((t2 - t1)/1000);
                arrayInsertion.add((t3 - t2)/1000);
                arrayRadix.add((t4 - t3)/1000);
                arraySelection.add((t5 - t4)/1000);
                arrayBinary.add((t6 - t5)/1000);

            }

            FileWriter out = null;

            try{
                out = new FileWriter("output.txt",true);

                String str = null;
                switch (j){
                    case 0 : str = "start best ";break;
                    case 1 : str = "start avr ";break;
                    case 2 : str = "start worst ";break;
                }
                out.append(str);
                out.append("merge\n");
                addToFile(arrayMerge, out, maxes);
                out.append("end\n");

                out.append(str);
                out.append("selection\n");
                addToFile(arraySelection, out, maxes);
                out.append("end\n");

                out.append(str);
                out.append("radix\n");
                addToFile(arrayRadix, out, maxes);
                out.append("end\n");

                out.append(str);
                out.append("insertion\n");
                addToFile(arrayInsertion, out,maxes);
                out.append("end\n");

                out.append(str);
                out.append("binary\n");
                addToFile(arrayBinary, out,maxes);
                out.append("end\n");

                out.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

            arrayBinary.clear();
            arrayInsertion.clear();
            arrayMerge.clear();
            arrayRadix.clear();
            arraySelection.clear();

        }
    }

    private static void addToFile(ArrayList<Double> array, FileWriter out, int[] maxes) throws IOException {
        for(int k = 0; k< array.size(); k++){
            out.append(Integer.toString(maxes[k]));
            out.append(",");
            out.append(Double.toString(array.get(k)));
            out.append("\n");
        }
    }


    private static int getRandomNumberInRange(int min, int max) {
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    private static void printArray(List<Integer> arrayList){
        for(int i : arrayList){
            System.out.print(i);
            System.out.print(" ");
        }
        System.out.print("\n");
    }

}
