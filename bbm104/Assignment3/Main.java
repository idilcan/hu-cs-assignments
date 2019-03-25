import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.lang.reflect.Constructor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Main {
    private static CustomerDAO customerDAO;
    private static OrderDAO orderDAO;


    private static void readFromFile(List<String> ordList, File orderFile, List<String> monitoring) {
        try (BufferedReader br = new BufferedReader(new FileReader(orderFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                ordList.add(line);
            }
        }catch(Exception e) {
            monitoring.add("ERROR : File " + orderFile.getName() + " threw an exception.");
        }
    }

    private static void readCustomerFile(List<String> monoturing){

        File customerFile = new File("customer.txt");

        List<String> custList = new ArrayList<>();
        readFromFile(custList, customerFile, monoturing);

        for(String custLine : custList) {
            Customer cus = new Customer(custLine);
            customerDAO.add(cus);
            //System.out.println(cus.getCustomer());
        }
    }

    private static void readOrderFile(List<String> monoturing){
        List<String> ordList = new ArrayList<>();

        File orderFile = new File("order.txt");

        readFromFile(ordList, orderFile, monoturing);

        Order latestOrder = null;
        for(String order: ordList){
            String[] ord = order.split(" ");
            if(ord[0].equals("Order:")){
                latestOrder = new Order(Integer.parseInt(ord[1]),Integer.parseInt(ord[2]));
                orderDAO.add(latestOrder);
            } else {
                if(latestOrder != null) {
                    if (ord[0].equals("softdrink")){
                        latestOrder.addDrink(new SoftDrink());
                    } else { //pizza
                        PizzaBase pizza;
                        if (ord[0].equals("Neapolitan")) {
                            pizza = new Neapolitan();
                        } else { //AmericanPan
                            pizza = new AmericanPan();
                        }
                        for(int i = 1; i<ord.length; i++)
                        {
                            String topping = ord[i];
                            pizza = getPizzaTopping(pizza, topping, monoturing, true);
                        }
                        latestOrder.addPizza(pizza);
                    }
                }
            }
        }
    }

    private static PizzaBase getPizzaTopping(PizzaBase pizza, String topping, List<String> monitoring, boolean add) {
        try {
            Class<?> clazz = Class.forName(topping);
            Constructor<?> constructor = clazz.getConstructor(PizzaBase.class);
            pizza = (PizzaBase)constructor.newInstance( pizza);

        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | InstantiationException | ClassNotFoundException e) {
            monitoring.add("ERROR : Pizza cannot be added. Toppings are false(or wrong).");
            add = false;
        }

        return pizza;
    }

    public static void main(String[] args) {
        List<String> monoturing = new ArrayList<>();
        customerDAO = new CustomerDAO();
        orderDAO = new OrderDAO();

        readCustomerFile(monoturing);
        readOrderFile(monoturing);

        List<String> inputList = new ArrayList<>();
        File inputFile = new File(args[0]);

        readFromFile(inputList, inputFile, monoturing);

        for(String input : inputList){
            String[] inputPortion = input.split(" ");
            String firstElement = inputPortion[0].toLowerCase();
            if(firstElement.equals("addcustomer")){
                if(customerDAO.getByID(Integer.parseInt(inputPortion[1])) != null){
                    monoturing.add("ERROR : Customer " + inputPortion[1] + " cannot be added. It exist.");
                    continue;
                }
                String[] customerToBeAdded = Arrays.copyOfRange(inputPortion, 1, inputPortion.length);
                String addCustomer = String.join(" ", customerToBeAdded);
                Customer customer = new Customer(addCustomer);
                customerDAO.add(customer);
                monoturing.add("Customer " + inputPortion[1] + " " + inputPortion[2] + " " + inputPortion[3] + " " + "added");
            }else if(firstElement.equals("removecustomer")){
                if(!Objects.equals(customerDAO.getByID(Integer.parseInt(inputPortion[1])), null)) {
                    Customer customer = (Customer) customerDAO.getByID(Integer.parseInt(inputPortion[1]));
                    monoturing.add("Customer " + inputPortion[1] + " " + customer.getName() + " removed");
                    customerDAO.deleteByID(Integer.parseInt(inputPortion[1]));
                }else {
                    monoturing.add("ERROR : Customer " + inputPortion[1] + " cannot be deleted. It does not exist.");

                }

            }else if(firstElement.equals("createorder")){
                int orderID = Integer.parseInt(inputPortion[1]);
                if(Objects.equals(orderDAO.getByID(orderID), null)) {
                    if(!Objects.equals(customerDAO.getByID(Integer.parseInt(inputPortion[2])), null)) {
                        Order currentOrder;
                        currentOrder = new Order(orderID, Integer.parseInt(inputPortion[2]));
                        orderDAO.add(currentOrder);
                        monoturing.add("Order " + inputPortion[1] + " created");
                    }else{
                        monoturing.add("ERROR : Order " + orderID + " cannot be created . ID " + inputPortion[2] + " does not exist.");
                    }
                }else{
                    monoturing.add("ERROR : Order " + orderID + " cannot be created . It does exist.");
                }

            }else if(firstElement.equals("addpizza")){
                int orderID = Integer.parseInt(inputPortion[1]);
                if(!Objects.equals(orderDAO.getByID(orderID), null)) {
                    Order currentOrder = (Order) orderDAO.getByID(orderID);
                    PizzaBase pizza = null;
                    if (inputPortion[2].toLowerCase().equals("neapolitan")) {
                        pizza = new Neapolitan();
                        monoturing.add("Neapolitan pizza added to order " + orderID);
                    } else if (inputPortion[2].toLowerCase().equals("americanpan")){
                        pizza = new AmericanPan();
                        monoturing.add("AmericanPan pizza added to order " + orderID);
                    }else {
                        monoturing.add("ERROR : " + inputPortion[2] + "cannot be created. It does not exist.");
                    }
                    if (inputPortion.length < 7) {
                        boolean add = true;
                        for (int i = 3; i < inputPortion.length; i++) {
                            String topping = inputPortion[i];
                            if(pizza != null)
                                pizza = getPizzaTopping(pizza, topping, monoturing, add);
                        }
                        if (add) {
                            currentOrder.addPizza(pizza);
                            orderDAO.update(currentOrder);
                        }
                    } else {
                        monoturing.add("ERROR : Pizza cannot be added to " + orderID + ". Pizza has too much topping");
                    }
                } else {
                    monoturing.add("ERROR : Pizza cannot be added to order " + orderID + " It does not exist.");
                }
            }else if(firstElement.equals("adddrink")){
                int orderID = Integer.parseInt(inputPortion[1]);
                if(!Objects.equals(orderDAO.getByID(orderID), null)) {
                    Order currentOrder = (Order)orderDAO.getByID(orderID);
                    SoftDrink softdrink = new SoftDrink();
                    currentOrder.addDrink(softdrink);
                    orderDAO.update(currentOrder);
                    monoturing.add("Drink added to order " + orderID);
                } else {
                    monoturing.add("ERROR : Drink cannot be added to order " + orderID + ". It does not been created.");
                }
            } else if (firstElement.equals("paycheck")){
                int orderID = Integer.parseInt(inputPortion[1]);
                if(!Objects.equals(orderDAO.getByID(orderID), null)) {
                    Order currentOrder = (Order)orderDAO.getByID(orderID);
                    List<String> currentReport = currentOrder.getReport();
                    monoturing.add("Paycheck to order " + orderID);
                    monoturing.addAll(currentReport);
                } else {
                    monoturing.add("ERROR : Order " + orderID + " cannot be paid. It does not exist.");
                }
            } else if(firstElement.equals("list")){
                monoturing.add("List Customers:");
                for(Object obj : customerDAO.getALLWithNamesSorted()) {
                    Customer cus = (Customer) obj;
                    monoturing.add(cus.getReportCustomer());
                }
            } else if (firstElement.equals("removeorder")){
                int orderID = Integer.parseInt(inputPortion[1]);
                if(!Objects.equals(orderDAO.getByID(orderID), null)) {
                    orderDAO.deleteByID(orderID);
                } else {
                    monoturing.add("ERROR : Order " + Integer.toString(orderID) + " cannot be removed.It does not exist.");
                }
            } else {
                monoturing.add("ERROR : " + firstElement + " cannot be executed. It does not exist.");
            }
        }
        try {
            Path output = Paths.get("output.txt");
            Files.write(output, monoturing, Charset.forName("UTF-8"));

            List<String> customers = new ArrayList<>();
            for(Object objectcustomer : customerDAO.getALL()){
                Customer customer = (Customer) objectcustomer;
                customers.add(customer.getCustomer());
            }
            Path customerPath = Paths.get("customer.txt");
            Files.write(customerPath, customers, Charset.forName("UTF-8"));

            List<String> orders = new ArrayList<>();
            for(Object objectorder : orderDAO.getALL()){
                Order order = (Order) objectorder;
                orders.add(order.toString());
            }
            Path orderPath = Paths.get("order.txt");
            Files.write(orderPath, orders, Charset.forName("UTF-8"));

        }catch (Exception e){
            System.out.println("error");
        }
    }
}
