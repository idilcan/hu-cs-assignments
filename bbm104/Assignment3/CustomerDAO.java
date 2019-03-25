import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CustomerDAO implements IDataAccessObject {
    private List<Customer> customers;

    public CustomerDAO() {
        customers = new ArrayList<>();
    }

    @Override
    public Object getByID(int ID) {
        for(Customer customer : customers)
        {
            if(customer.getId() == ID)
                return customer;
        }

        return null;
    }

    @Override
    public boolean deleteByID(int ID) {
        for(Customer customer : customers)
        {
            if(customer.getId() == ID) {
                customers.remove(customer);
                return true;
            }
        }

        return false;
    }

    @Override
    public void add(Object object) {
        if(!(object instanceof Customer))
            return;

        customers.add((Customer)object);
        customers.sort((Comparator.naturalOrder()));

    }

    @Override
    public void update(Object object) {
        if(!(object instanceof Customer))
            return;
        Customer other = (Customer)object;

        for(Customer customer : customers)
        {
            if(customer.getId() == other.getId()) {
                customers.remove(customer);
                customers.add(other);
                Collections.sort(customers, ((o1, o2) -> o1.compareTo(o2)) );
                return ;
            }
        }
    }

    @Override
    public ArrayList getALL() {
        ArrayList out = new ArrayList();
        out.addAll(customers);
        return out;
    }

    public ArrayList getALLWithNamesSorted() {
        ArrayList out = new ArrayList();
        Collections.sort(customers, ((o1, o2) -> o1.compareToWithName(o2)) );
        out.addAll(customers);
        Collections.sort(customers, ((o1, o2) -> o1.compareTo(o2)) );
        return out;
    }
}
