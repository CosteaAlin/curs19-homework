package ro.fasttrackit.model;

public record Transaction(int id, String product, Type type, double amount) {
}
