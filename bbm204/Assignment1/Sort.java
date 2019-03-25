import java.util.ArrayList;
import java.util.List;

public class Sort {
    public static void selection(List<Integer> list){

        int n = list.size();
        for(int i= 0; i < n-1; i++) {
            int min = i;
            for (int j = i + 1; j < n; j++) {
                if (list.get(j) < list.get(min)) {
                    min = j;
                }
            }
            if (min != i) {
                int temp = list.get(i);
                list.set(i, list.get(min));
                list.set(min, temp);
            }

        }
    }

    public static List<Integer> merge(List<Integer> list)
    {
        if(list.size() == 1) return list;

        List<Integer> list1 = list.subList(0,list.size()/2);
        List<Integer> list2 = list.subList(list.size()/2, list.size());
        list1 = merge(list1);
        list2 = merge(list2);
        return mergeList(list1, list2);

    }

    public static ArrayList<Integer> mergeList(List<Integer> temp1, List<Integer> temp2){

        List<Integer> list1 = new ArrayList<>(temp1);
        List<Integer> list2 = new ArrayList<>(temp2);


        ArrayList<Integer> list3 = new ArrayList<>();
        while(!list1.isEmpty() && !list2.isEmpty()){
            if(list1.get(0) > list2.get(0)){
                list3.add(list2.get(0));
                list2.remove(0);
            }else{
                list3.add(list1.get(0));
                list1.remove(0);
            }
        }
        while(!list1.isEmpty()){
            list3.add(list1.get(0));
            list1.remove(0);
        }
        while(!list2.isEmpty()){
            list3.add(list2.get(0));
            list2.remove(0);
        }
        return list3;

    }

    public static void radix(List<Integer> list) {
        List<Integer>[] buckets = new ArrayList[10];
        for (int i = 0; i < buckets.length; i++) {
            buckets[i] = new ArrayList<Integer>();
        }

        // sort
        boolean flag = false;
        int tmp = -1, divisor = 1;
        while (!flag) {
            flag = true;
            // split input between lists
            for (Integer i : list) {
                tmp = i / divisor;
                buckets[tmp % 10].add(i);
                if (flag && tmp > 0) {
                    flag = false;
                }
            }
            // empty lists into input array
            int a = 0;
            for (int b = 0; b < 10; b++) {
                for (Integer i : buckets[b]) {
                    list.set(a++, i);
                }
                buckets[b].clear();
            }
            // move to next digit
            divisor *= 10;
        }
    }

    public static void insertion(List<Integer> list) {
        for(int index = 1; index < list.size(); index++){
            int element = list.get(index);
            int compare = index-1;
            while((compare >= 0) && (list.get(compare) > element)){
                list.set(compare+1, list.get(compare));
                compare--;
            }
            list.set(compare+1, element);
        }
    }
}
