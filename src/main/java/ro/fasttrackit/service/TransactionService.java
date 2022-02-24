package ro.fasttrackit.service;

import org.springframework.stereotype.Service;
import ro.fasttrackit.model.Transaction;
import ro.fasttrackit.model.Type;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.*;

@Service
public class TransactionService {
    private final List<Transaction> transactions = new ArrayList<>();

    public TransactionService(MemoryTransactionProvider memoryTransactionProvider) {
        this.transactions.addAll(memoryTransactionProvider.getTransactions());
    }

    public List<Transaction> getTransactions(String product, Type type, Double minAmount, Double maxAmount) {
        return transactions.stream()
                .filter(transaction -> product == null || transaction.product().equalsIgnoreCase(product))
                .filter(transaction -> type == null || transaction.type() == type)
                .filter(transaction -> minAmount == null || transaction.amount() > minAmount)
                .filter(transaction -> maxAmount == null || transaction.amount() < maxAmount)
                .toList();
    }

    public Optional<Transaction> getTransactionById(int id) {
        return transactions.stream()
                .filter(transaction -> transaction.id() == id)
                .findFirst();
    }

    public Transaction addTransaction(Transaction transaction) {
        Transaction newTransaction = cloneWithId(transactions.size() + 1, transaction);
        transactions.add(newTransaction);
        return newTransaction;
    }

    public Transaction replaceTransaction(int id, Transaction transaction) {
        Transaction existing = getTransactionById(id).get();
        return replaceExistingTransaction(id, existing, transaction);
    }

    public Optional<Transaction> deleteTransaction(int id) {
        Optional<Transaction> transactionToDelete = getTransactionById(id);
        transactionToDelete.ifPresent(this.transactions::remove);
        return transactionToDelete;
    }


    public Map<Type, List<Transaction>> groupByType() {
        return this.transactions
                .stream().collect(groupingBy(Transaction::type));
    }

    public Map<String, List<Transaction>> groupByProduct() {
        return this.transactions
                .stream().collect(groupingBy(Transaction::product));
    }

    private Transaction cloneWithId(int id, Transaction transaction) {
        return new Transaction(
                id,
                transaction.product(),
                transaction.type(),
                transaction.amount()
        );
    }

    private Transaction replaceExistingTransaction(int id, Transaction existing, Transaction transaction) {
        this.transactions.remove(existing);
        Transaction newTransaction = cloneWithId(id, transaction);
        this.transactions.add(newTransaction);
        return newTransaction;
    }
}
