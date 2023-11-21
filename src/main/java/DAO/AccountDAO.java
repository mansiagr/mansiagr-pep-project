package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import Util.ConnectionUtil;
import Model.Account;

public class AccountDAO {

    public boolean doesUsernameExist(String username) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            String sql = "SELECT * FROM Account WHERE username = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Account createAccount(Account account) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            String sql = "INSERT INTO Account (username, password) VALUES (?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1, account.getUsername());
            statement.setString(2, account.getPassword());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                return null;
            }
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                account.setAccount_id(generatedKeys.getInt(1));
                return account;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Account authenticateUser(Account account) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            String sql = "SELECT * FROM Account WHERE username = ? AND password = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, account.getUsername());
            statement.setString(2, account.getPassword());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int accountId = resultSet.getInt("account_id");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                return new Account(accountId, username, password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
