package Controller;
import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;

import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {

    private MessageService messageService;
    private AccountService accountService;

    public SocialMediaController(){
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }
     


    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create().start(8080);

       // app.get("example-endpoint", this::exampleHandler);
        app.post("/register", this::register);

        app.post("/login", this::login);
        app.post("/messages", this::createMessage);
        app.get("/messages", this::getAllMessages);
        app.get("/messages/{message_id}", this::getMessageById);
        app.delete("/messages/{message_id}", this::deleteMessageById);
        app.patch("/messages/{message_id}", this::updateMessageById);
        app.get("/accounts/{account_id}/messages", this::getMessagesByUserId);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    // private void exampleHandler(Context context) {
    //     context.json("sample text");
    // }

    // Endpoints for user registration
    private void register(Context ctx) {
        Account newAccount = ctx.bodyAsClass(Account.class);

        //check if username is null or pass charac 
        if (newAccount.getUsername().isBlank() || newAccount.getPassword().length() >= 4) {
            ctx.status(400).json("Invalid username or password");
            return;
        }

        //check if username exists, else give error. then made new account 
        if (accountService.doesUsernameExist(newAccount.getUsername())) {
            ctx.status(400).json("Username already exists");
            return;
        }

        Account accountMade = accountService.register(newAccount);

        if (accountMade != null) {
            ctx.json(accountMade).status(200);
          } else {
            ctx.result("Failed to register user").status(400);
          }
    }

    // Endpoint for user login
    private void login(Context ctx) {

    }

    // Endpoint for creating a new message
    private void createMessage(Context ctx) {

    }

    // Endpoint for retrieving all messages
    private void getAllMessages(Context ctx) {

    }

    // Endpoint for retrieving a message by its ID
    private void getMessageById(Context ctx) {

    }

    // Endpoint for deleting a message by its ID
    private void deleteMessageById(Context ctx) {

    }

    // Endpoint for updating a message by its ID
    private void updateMessageById(Context ctx) {
        
    }

    // Endpoint for retrieving all messages by a particular user
    private void getMessagesByUserId(Context ctx) {

    }


}