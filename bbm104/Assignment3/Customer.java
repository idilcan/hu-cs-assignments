import java.util.Arrays;

public class Customer implements Comparable<Customer> {

    private int id;

    public String getName() {
        return name;
    }

    private String name;
    private String surname;
    private String phone;
    private String address;

    public Customer(String line) {
        String[] words = line.split(" ");
        if(words.length < 5)
            return;

        id = Integer.parseInt(words[0]);
        name = words[1];
        surname = words[2];
        phone = words[3];
        int from = 4;
        if(words[4].equals("Address:"))
        {
            from = 5;
        }
        String[] addArray = Arrays.copyOfRange(words, from, words.length);
        address = String.join(" ", addArray);
    }

    public String getCustomer()
    {
        return Integer.toString(id) + " " + name + " " + surname + " " + phone + " Address: " + address;
    }

    public String getReportCustomer()
    {
        return Integer.toString(id) + " " + name + " " + surname + " " + phone ;
    }

    public int getId() {
        return id;
    }

    @Override
    public int compareTo(Customer o) {
        return this.id - o.id;
    }
    public int compareToWithName(Customer o) {return (this.name + this.surname).toUpperCase().compareTo((o.name+o.surname).toUpperCase()); }
}
