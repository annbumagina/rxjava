package mongo;

import com.mongodb.rx.client.MongoClient;
import com.mongodb.rx.client.MongoClients;
import com.mongodb.rx.client.Success;
import rx.Observable;
import static com.mongodb.client.model.Filters.*;

public class ReactiveMongoDriver {
    private static MongoClient client = createMongoClient();

    public static Observable<Success> addUser(User user) {
        Observable<Success> result = client.getDatabase("rxjava")
            .getCollection("users")
            .insertOne(user.document());
        result.subscribe();
        return result;
    }

    public static Observable<User> getUsers() {
        return client.getDatabase("rxjava")
                .getCollection("users")
                .find().toObservable().map(User::new);
    }

    public static Observable<Success> addProduct(Product product) {
        Observable<Success> result = client.getDatabase("rxjava")
                .getCollection("products")
                .insertOne(product.document());
        result.subscribe();
        return result;
    }

    public static Observable<Product> getProducts() {
        return client.getDatabase("rxjava")
                .getCollection("products")
                .find().toObservable().map(Product::new);
    }

    public static Observable<User> getUser(int id) {
        return client.getDatabase("rxjava")
                .getCollection("users")
                .find(eq("id", id)).first().map(User::new);
    }

    private static MongoClient createMongoClient() {
        return MongoClients.create("mongodb://localhost:27017");
    }
}
