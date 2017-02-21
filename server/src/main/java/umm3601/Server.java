package umm3601;

import umm3601.mongotest.Mongotest;
import umm3601.user.UserController;

import java.io.IOException;

import static spark.Spark.*;


public class Server {
    public static void main(String[] args) throws IOException {

        /*
         * You should probably delete this and the whole `mongotest` package after you've
         * gotten what you need out of those examples.
         */
        testDatabaseConnection();

        UserController userController = new UserController();

        options("/*", (request, response) -> {

            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }

            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }
 
            return "OK";
        });

        before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));

        // Simple example route
        get("/hello", (req, res) -> "Hello World");

        // Redirects for the "home" page
        redirect.get("", "/");
        redirect.get("/", "http://localhost:9000");

        // List users
        get("api/users", (req, res) -> {
            res.type("application/json");
            return userController.listUsers(req.queryMap().toMap());
        });

        // See specific user
        get("api/users/:id", (req, res) -> {
            res.type("application/json");
            String id = req.params("id");
            return userController.getUser(id);
        });

        // Handle "404" file not found requests:
        notFound((req, res) -> {
            res.type("text");
            res.status(404);
            return "Sorry, we couldn't find that!";
        });

    }

    /**
     * You should probably delete this and the whole `mongotest` package after you've
     * gotten what you need out of those examples.
     */
    private static void testDatabaseConnection() {
        // Connect to default mongo Database:
        Mongotest mongotest = new Mongotest();
        mongotest.connectToMongo();
    }
}
