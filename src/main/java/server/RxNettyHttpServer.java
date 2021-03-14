package server;

import io.reactivex.netty.protocol.http.server.HttpServer;
import mongo.Product;
import mongo.ReactiveMongoDriver;
import mongo.User;
import rx.Observable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RxNettyHttpServer {
    public static void main(final String[] args) {
        HttpServer
            .newServer(8080)
            .start((req, resp) -> {

                String name = req.getDecodedPath().substring(1);
                Observable<String> response = Observable
                        .just(name)
                        .map(usd -> "Hello, " + name + "!");

                Map<String, List<String>> params = req.getQueryParameters();
                switch (name) {
                    case "register":
                        return resp.writeString(register(params));
                    case "users":
                        return resp.writeString(users());
                    case "add-product":
                        return resp.writeString(addProduct(params));
                    case "products":
                        return resp.writeString(products(params));
                }

                return resp.writeString(response);
            })
            .awaitShutdown();
    }

    private static Observable<String> register(Map<String, List<String>> params) {
        try {
            int id = Integer.parseInt(params.get("id").get(0));
            String name = params.get("name").get(0);
            String login = params.get("login").get(0);
            String currency = params.get("currency").get(0);
            return ReactiveMongoDriver.addUser(new User(id, name, login, currency))
                    .compose(s -> Observable.just("Registered"));
        } catch (NullPointerException e) {
            return Observable.just("Wrong request parameters");
        }
    }

    private static Observable<String> addProduct(Map<String, List<String>> params) {
        try {
            int id = Integer.parseInt(params.get("id").get(0));
            String name = params.get("name").get(0);
            double price = Double.parseDouble(params.get("price").get(0));
            return ReactiveMongoDriver.addProduct(new Product(id, name, price))
                    .compose(s -> Observable.just("Added"));
        } catch (NullPointerException e) {
            return Observable.just("Wrong request parameters");
        }
    }

    private static Observable<String> users() {
        return ReactiveMongoDriver
                .getUsers()
                .collect(ArrayList::new, ArrayList::add)
                .map(List::toString);
    }

    private static Observable<String> products(Map<String, List<String>> params) {
        try {
            int id = Integer.parseInt(params.get("userId").get(0));
            return ReactiveMongoDriver.getUser(id)
                .flatMap(user -> ReactiveMongoDriver.getProducts()
                    .map(pr -> pr.toString(user.getCurrency()))
                    .collect(ArrayList::new, ArrayList::add)
                    .map(List::toString))
                .switchIfEmpty(Observable.just("User doesn't exist"));
        } catch (NullPointerException e) {
            return Observable.just("Wrong request parameters");
        }
    }
}
