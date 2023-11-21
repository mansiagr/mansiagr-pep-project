package Service;
import java.util.ArrayList;
import DAO.MessageDAO;
import Model.Message;

public class MessageService {

    private MessageDAO messageDAO;
    
    public MessageService(){
        this.messageDAO = new MessageDAO();    
    }
}
