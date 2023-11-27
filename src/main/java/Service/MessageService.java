package Service;
import java.util.ArrayList;
import DAO.MessageDAO;
import Model.Message;

public class MessageService {
    private MessageDAO messageDAO;

    public MessageService() {
        this.messageDAO = new MessageDAO(); 
    }

    public boolean doesIdExist(int messageId) {
        return messageDAO.doesIdExist(messageId);
    }

    public ArrayList<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public Message createMessage(Message message) {
        return messageDAO.createMessage(message);
    }

    public Message getMessageById(int messageId) {
        return messageDAO.getMessageById(messageId);
    }

    public boolean deleteMessageById(int messageId) {
        return messageDAO.deleteMessageById(messageId);
    }

    public boolean updateMessageById(int messageId, Message updatedMessage) {
        return messageDAO.updateMessageById(messageId, updatedMessage);
    }

    public ArrayList<Message> getMessagesByUserId(int userId) {
        return messageDAO.getMessagesByUserId(userId);
    }

}
