package Service;
import java.util.ArrayList;
import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    private AccountDAO accountDAO;

    public AccountService(){
        this.accountDAO = new AccountDAO();    
    }

    public boolean doesUsernameExist(String username) {
        return accountDAO.doesUsernameExist(username);
    }

    public Account register(Account account) {
        return accountDAO.createAccount(account);
    }

    public Account authenticateUser(Account account) {
        return accountDAO.authenticateUser(account);
    }
    
    //doesUsernameExist
    //register
    //authenticateUser

}
