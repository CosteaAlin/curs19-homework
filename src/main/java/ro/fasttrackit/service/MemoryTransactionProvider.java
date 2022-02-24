package ro.fasttrackit.service;

import org.springframework.stereotype.Component;
import ro.fasttrackit.model.Transaction;
import ro.fasttrackit.model.Type;

import java.util.List;

import static ro.fasttrackit.model.Type.BUY;
import static ro.fasttrackit.model.Type.SELL;

@Component
public class MemoryTransactionProvider {

    public List<Transaction> getTransactions() {
        return List.of(
                new Transaction(1, "Microwave", BUY, 50.99),
                new Transaction(2, "TV", SELL, 120.99),
                new Transaction(3, "Desk", BUY, 72.99),
                new Transaction(4, "Lamp", SELL, 12.99)
        );
    }
}
