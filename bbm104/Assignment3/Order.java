import com.sun.org.apache.xerces.internal.xs.StringList;

import java.util.ArrayList;
import java.util.List;

public class Order implements Comparable<Order> {

    private int id;
    private int customerId;
    private List<PizzaBase> pizzaList;
    private List<DrinkBase> drinkList;

    public Order(int id, int customerId) {
        this.id = id;
        this.customerId = customerId;
        pizzaList = new ArrayList<>();
        drinkList = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void addPizza(PizzaBase pizza) {
        pizzaList.add(pizza);
    }

    public void addDrink(DrinkBase drink) {
        drinkList.add(drink);
    }

    public List<String> getReport() {
        int totalCost = 0;
        List<String> report = new ArrayList<>();
        for (PizzaBase pizza : pizzaList) {
            report.add("\t" + pizza.getDescription() + " " + Integer.toString(pizza.getCost()) + "$");
            totalCost += pizza.getCost();
        }
        for (DrinkBase drink : drinkList) {
            report.add("\t" + drink.getDescription() + " " + Integer.toString(drink.getCost()) + "$");
            totalCost += drink.getCost();
        }
        report.add("\tTotal: " + Integer.toString(totalCost) + "$");


        return report;
    }

    @Override
    public int compareTo(Order o) {
        return this.id - o.id;
    }

    public String toString(){
        StringBuilder report;
        report = new StringBuilder("Order: " + id + " " + customerId + "\n");
        for(PizzaBase pizza : pizzaList){
            report.append(pizza.getDescription());
            report.append("\n");
        }
        for(DrinkBase drink : drinkList){
            report.append(drink.getDescription());
            report.append("\n");
        }
        String repo = report.substring(0,report.length()-1);
        return repo;
    }
}


