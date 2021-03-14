package mongo;

import org.bson.Document;

public class User {
    private final int id;
    private final String name;
    private final String login;
    private final String currency;


    public User(Document doc) {
        this(doc.getInteger("id"), doc.getString("name"),
                doc.getString("login"), doc.getString("currency"));
    }

    public User(int id, String name, String login, String currency) {
        this.id = id;
        this.name = name;
        this.login = login;
        this.currency = currency;
    }

    public Document document() {
        return new Document("id", id)
                .append("name", name)
                .append("login", login)
                .append("currency", currency);
    }

    public String getCurrency() {
        return currency;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", login='" + login + '\'' +
                ", currency='" + currency + '\'' +
                "}";
    }
}
