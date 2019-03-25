import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OrderDAO implements IDataAccessObject {
    private List<Order> orders;

    public OrderDAO() {
        orders = new ArrayList<>();
    }

    @Override
    public Object getByID(int ID) {
        for(Order order : orders)
        {
            if(order.getId() == ID)
                return order;
        }
        return null;
    }

    @Override
    public boolean deleteByID(int ID) {
        for(Order order : orders)
        {
            if(order.getId() == ID) {
                orders.remove(order);
                return true;
            }
        }
        return false;
    }

    @Override
    public void add(Object object) {
        if(!(object instanceof Order))
            return;

        orders.add((Order)object);
        Collections.sort(orders, ((o1, o2) -> o1.compareTo(o2)) );
    }

    @Override
    public void update(Object object) {
        if(!(object instanceof Order))
            return;
        Order other = (Order) object;

        for(Order order : orders)
        {
            if(order.getId() == other.getId()) {
                orders.remove(order);
                orders.add(other);
                Collections.sort(orders, ((o1, o2) -> o1.compareTo(o2)) );
                return ;
            }
        }
    }

    @Override
    public ArrayList getALL() {
        ArrayList out = new ArrayList();
        for(Order order : orders)
        {
            out.add(order);
        }
        return out;
    }
}
