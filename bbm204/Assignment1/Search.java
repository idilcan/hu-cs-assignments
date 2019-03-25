import java.util.List;

public class Search {
    public static int binary(List<Integer> list, int item) {

        if (list.size() == 1){
            if(list.get(0) != item){
                return -1;
            }
            else{
                return 0;
            }
        }
        int middle = list.size()/2;
        if(list.get(middle) < item)
            return binary(list.subList(list.size()/2, list.size()), item) + middle;
        else if(list.get(middle) > item)
            return binary(list.subList(0, list.size()/2), item);
        else
            return middle;
    }
}
