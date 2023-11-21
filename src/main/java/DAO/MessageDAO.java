package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import Model.Message;
import Util.ConnectionUtil;

public class MessageDAO {

    public boolean doesIdExist(int messageId) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            String sql = "SELECT * FROM Message WHERE message_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, messageId);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<Message> getAllMessages() {
        ArrayList<Message> messages = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection()) {
            String sql = "SELECT * FROM Message";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int messageId = resultSet.getInt("message_id");
                int postedBy = resultSet.getInt("posted_by");
                String messageText = resultSet.getString("message_text");
                long timePostedEpoch = resultSet.getLong("time_posted_epoch");
                messages.add(new Message(messageId, postedBy, messageText, timePostedEpoch));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    public Message createMessage(Message message) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            String sql = "INSERT INTO Message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setInt(1, message.getPosted_by());
            statement.setString(2, message.getMessage_text());
            statement.setLong(3, message.getTime_posted_epoch());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                return null;
            }
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                message.setMessage_id(generatedKeys.getInt(1));
                return message;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Message getMessageById(int messageId) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            String sql = "SELECT * FROM Message WHERE message_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, messageId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int postedBy = resultSet.getInt("posted_by");
                String messageText = resultSet.getString("message_text");
                long timePostedEpoch = resultSet.getLong("time_posted_epoch");
                return new Message(messageId, postedBy, messageText, timePostedEpoch);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean deleteMessageById(int messageId) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            String sql = "DELETE FROM Message WHERE message_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, messageId);
            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateMessageById(int messageId, Message updatedMessage) {
        try (Connection connection = ConnectionUtil.getConnection()) {
            String sql = "UPDATE Message SET message_text = ? WHERE message_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, updatedMessage.getMessage_text());
            statement.setInt(2, messageId);
            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList<Message> getMessagesByUserId(int userId) {
        ArrayList<Message> messages = new ArrayList<>();
        try (Connection connection = ConnectionUtil.getConnection()) {
            String sql = "SELECT * FROM Message WHERE posted_by = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int messageId = resultSet.getInt("message_id");
                int postedBy = resultSet.getInt("posted_by");
                String messageText = resultSet.getString("message_text");
                long timePostedEpoch = resultSet.getLong("time_posted_epoch");
                messages.add(new Message(messageId, postedBy, messageText, timePostedEpoch));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

}

