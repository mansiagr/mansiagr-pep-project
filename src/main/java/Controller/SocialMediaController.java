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
        Javalin app = Javalin.create();
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
    private void register(Context ctx) {
        Account newAccount = ctx.bodyAsClass(Account.class);
        System.out.print(newAccount.getPassword().length());
        if (newAccount.getUsername().isBlank()) {
            ctx.status(400);
            return;
        }

        if (newAccount.getPassword().length() < 4) {
            ctx.status(400);
            return;
        }

        if (accountService.doesUsernameExist(newAccount.getUsername())) {
            ctx.status(400);
            return;
        }

        Account accountMade = accountService.register(newAccount);

        if (accountMade != null) {
            ctx.json(accountMade).status(200);
            System.out.println(ctx.json(accountMade));
          } else {
            ctx.status(400);
          }
    }

    private void login(Context ctx) {
        Account loginAccount = ctx.bodyAsClass(Account.class);

        Account authenticatedAccount = accountService.authenticateUser(loginAccount);
        if (authenticatedAccount != null) {
            ctx.status(200).json(authenticatedAccount);
        } else {
            ctx.status(401);
        }
    }

    private void createMessage(Context ctx) {
        Message newMessage = ctx.bodyAsClass(Message.class); 

        if (newMessage.getMessage_text().isBlank() || newMessage.getMessage_text().length() > 255) {
            ctx.status(400);
            return;
        }

        int userId = newMessage.getPosted_by();
        boolean userExists = messageService.doesIdExist(userId); 
    
        if (!userExists) {
            ctx.status(400);
            return;
        }


        Message createdMessage = messageService.createMessage(newMessage);
        if (createdMessage != null) {
            ctx.status(200).json(createdMessage);
        } else {
            ctx.status(400);
        }
    }

    private void getAllMessages(Context ctx) {
        ArrayList<Message> messagesRetrieved = messageService.getAllMessages();
        ctx.json(messagesRetrieved).status(200);

    }

    private void getMessageById(Context ctx) {
        int idFromPath = Integer.parseInt(ctx.pathParam("message_id"));
        System.out.println(idFromPath);
        Message message = messageService.getMessageById(idFromPath);
        System.out.println(message);
        if (message != null) {
            ctx.status(200).json(message);
        } else {
            ctx.status(200);
        }
    }

    private void deleteMessageById(Context ctx) {
        int idFromPath = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.getMessageById(idFromPath);
        boolean deleted = messageService.deleteMessageById(idFromPath);
        if (deleted) {
            ctx.status(200).json(message);
        } else { 
            ctx.status(200);
        }
    }

    private void updateMessageById(Context ctx) {
        int idFromPath = Integer.parseInt(ctx.pathParam("message_id"));
        Message messageFromBody = ctx.bodyAsClass(Message.class);

        if (messageFromBody.getMessage_text().isBlank() || messageFromBody.getMessage_text().length() > 255) {
            ctx.status(400);
            return;
        }

        if (messageService.doesIdExist(messageFromBody.getMessage_id())) {
            ctx.status(400);
            return;
        }

        boolean updatedMessage = messageService.updateMessageById(idFromPath, messageFromBody);
        Message message = messageService.getMessageById(idFromPath);
        if (updatedMessage) {
          ctx.json(message).status(200);
        } else {
          ctx.status(400);
        }
    }

    private void getMessagesByUserId(Context ctx) {
        int idFromPath = Integer.parseInt(ctx.pathParam("account_id"));
        ArrayList<Message> messages = messageService.getMessagesByUserId(idFromPath);
        ctx.status(200).json(messages);
    }


}