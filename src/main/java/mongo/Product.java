package mongo;

import org.bson.Document;

import java.util.Locale;

public class Product {
    private final int id;
    private final String name;
    private double price;


    public Product(Document doc) {
        this(doc.getInteger("id"), doc.getString("name"),
                doc.getDouble("price"));
    }

    public Product(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Document document() {
        return new Document("id", id)
                .append("name", name)
                .append("price", price);
    }

    private Double convertPrice(String currency) {
        switch (currency) {
            case "USD":
                return price * 0.014;
            case "EUR":
                return price * 0.011;
            default:
                return price;
        }
    }

    public String toString(String currency) {
        price = convertPrice(currency);
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price='" + String.format(Locale.US, "%.2f", price) + currency + '\'' +
                "}";
    }
}
