package Controller;
import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import java.util.ArrayList;
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
        System.out.println("sss");
        app.post("/register", this::register);
        System.out.println("ffff");
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
        System.out.println("00000");
        Account newAccount = ctx.bodyAsClass(Account.class);
        System.out.println("111111");
        if (newAccount.getUsername().isBlank() || newAccount.getPassword().length() < 4) {
            System.out.print("HERE");
            ctx.status(400);
        }

        //check if username exists, else give error. then made new account 
        if (accountService.doesUsernameExist(newAccount.getUsername())) {
            ctx.status(400);
        }

        Account accountMade = accountService.register(newAccount);

        if (accountMade != null) {
            ctx.json(accountMade).status(200);
            System.out.println(ctx.json(accountMade));
          } else {
            ctx.status(400);
          }
    }

    // Endpoint for user login
    private void login(Context ctx) {
        Account loginAccount = ctx.bodyAsClass(Account.class);

        Account authenticatedAccount = accountService.authenticateUser(loginAccount);
        if (authenticatedAccount != null) {
            ctx.status(200).json(authenticatedAccount);
        } else {
            ctx.status(401).json("Unauthorized");
        }
    }

    // Endpoint for creating a new message
    private void createMessage(Context ctx) {
        Message newMessage = ctx.bodyAsClass(Message.class); // Deserialize JSON body to Message object

        if (newMessage.getMessage_text().isBlank() || newMessage.getMessage_text().length() > 255) {
            ctx.status(400).json("Invalid message text");
            return;
        }

        // Check if the user exists (posted_by field) - You should implement this validation logic
        int userId = newMessage.getPosted_by();
        boolean userExists = messageService.doesIdExist(userId); // You need to implement this method
    
        if (!userExists) {
            ctx.status(400).json("User specified in posted_by field does not exist");
            return;
        }


        Message createdMessage = messageService.createMessage(newMessage);
        if (createdMessage != null) {
            ctx.status(200).json(createdMessage);
        } else {
            ctx.status(400).json("Failed to create message");
        }
    }

    // Endpoint for retrieving all messages
    private void getAllMessages(Context ctx) {
        ArrayList<Message> messagesRetrieved = messageService.getAllMessages();
        ctx.json(messagesRetrieved).status(200);

    }

    // Endpoint for retrieving a message by its ID
    private void getMessageById(Context ctx) {
        int idFromPath = Integer.parseInt(ctx.pathParam("id"));
        Message message = messageService.getMessageById(idFromPath);
        ctx.status(200).json(message);
        // if (message != null) {
        //     ctx.status(200).json(message);
        // } else {
        //     ctx.status(200).json("Message not found");
        // }
    }

    // Endpoint for deleting a message by its ID
    private void deleteMessageById(Context ctx) {
        int idFromPath = Integer.parseInt(ctx.pathParam("id"));
        boolean deleted = messageService.deleteMessageById(idFromPath);
        ctx.status(200).json(deleted);
        // if (deleted) {
        //     ctx.status(200).json("Message deleted successfully");
        // } else { //check if message existed?
        //     ctx.status(200).json("Message not found");
        // }
    }

    // Endpoint for updating a message by its ID
    private void updateMessageById(Context ctx) {
        int idFromPath = Integer.parseInt(ctx.pathParam("id"));
        Message messageFromBody = ctx.bodyAsClass(Message.class);

        if (messageFromBody.getMessage_text().isBlank() || messageFromBody.getMessage_text().length() > 255) {
            ctx.status(400).json("Invalid message text");
            return;
        }

        //check if username exists, else give error. then made new account 
        if (messageService.doesIdExist(messageFromBody.getMessage_id())) {
            ctx.status(400).json("ID does not exist");
            return;
        }

        boolean updatedMessage = messageService.updateMessageById(idFromPath, messageFromBody);

        // send response depending on information received from service layer
        if (updatedMessage) {
          ctx.json(updatedMessage).status(200);
        } else {
          ctx.result("Note was not updated!").status(400);
        }
    }

    // Endpoint for retrieving all messages by a particular user
    private void getMessagesByUserId(Context ctx) {
        int idFromPath = Integer.parseInt(ctx.pathParam("id"));
        ArrayList<Message> messages = messageService.getMessagesByUserId(idFromPath);
        ctx.status(200).json(messages);
    }


}