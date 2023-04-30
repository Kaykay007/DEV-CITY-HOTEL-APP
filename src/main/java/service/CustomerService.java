package service;


import exception.CustomerAlreadyExistException;
import model.Customer;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CustomerService {
    private static final CustomerService instance = new CustomerService();
     Map<String, Customer> customerMap;

    private CustomerService() {
        customerMap = new HashMap<>();
    }

    public static CustomerService getInstance() {
        return instance;
    }


    public void addCust(String email, String firstName, String lastName) throws CustomerAlreadyExistException {
        if (!customerMap.containsKey(email)) {
            customerMap.put(email, new Customer(firstName, lastName, email));
        } else {
            throw new CustomerAlreadyExistException("Customer already exists for " + email);
        }
    }

    public Customer getCust(String customerEmail) {
        return customerMap.get(customerEmail);
    }

    public Collection<Customer> getAllCustomers() {
        return customerMap.values();
    }
}
