package ro.fasttrackit.controller;

import org.springframework.web.bind.annotation.*;
import org.yaml.snakeyaml.util.EnumUtils;
import ro.fasttrackit.exceptions.InvalidInputException;
import ro.fasttrackit.exceptions.ResourceNotFoundException;
import ro.fasttrackit.model.Transaction;
import ro.fasttrackit.model.Type;
import ro.fasttrackit.service.TransactionService;

import java.util.List;
import java.util.Map;

@RestController
public class TransactionController {
    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }

    @GetMapping("transactions")
    List<Transaction> getTransaction(@RequestParam(required = false) String product,
                                     @RequestParam(required = false) Type type,
                                     @RequestParam(required = false) Double minAmount,
                                     @RequestParam(required = false) Double maxAmount) {
        return service.getTransactions(product, type, minAmount, maxAmount);
    }

    @GetMapping("transactions/{id}")
    Transaction getTransactionById(@PathVariable int id) {
        return service.getTransactionById(id).
                orElseThrow(() -> new ResourceNotFoundException("Can`t find transaction with id " + id));
    }

    @PostMapping("transactions")
    Transaction addTransaction(@RequestBody Transaction transaction) {
        if (transaction.product() == null || transaction.product().isEmpty()) {
            throw new InvalidInputException("Invalid json");
        }
        return service.addTransaction(transaction);
    }

    @PutMapping("transactions/{id}")
    Transaction replaceTransactionById(@PathVariable int id,
                                       @RequestBody Transaction transaction) {
        if (transaction.product() == null || transaction.product().isEmpty()) {
            throw new InvalidInputException("Invalid json");
        } else if (service.getTransactionById(id).isEmpty()) {
            throw new ResourceNotFoundException("Can`t find transaction with id " + id);
        } else {
            return service.replaceTransaction(id, transaction);
        }
    }

    @DeleteMapping("transactions/{id}")
    Transaction deleteTransaction(@PathVariable int id) {
        return service.deleteTransaction(id)
                .orElseThrow(() -> new ResourceNotFoundException("Can`t find transaction with id " + id));
    }

    @GetMapping("transactions/report/type")
    Map<Type, List<Transaction>> groupByType() {
        return service.groupByType();
    }

    @GetMapping("transactions/report/product")
    Map<String, List<Transaction>> groupByProduct() {
        return service.groupByProduct();
    }
}
