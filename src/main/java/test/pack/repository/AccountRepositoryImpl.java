package test.pack.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

@Repository
public class AccountRepositoryImpl implements AccountRepository {

    private static final Logger log = Logger.getLogger(AccountRepositoryImpl.class.getName());
    private static final String SQL_SELECT_BALANCE = "SELECT money FROM account a WHERE a.id = ?";
    // update query to increment/decrement current balance depending on whether given parameter is positive or negative.
    private static final String SQL_UPDATE_BALANCE = "UPDATE account a SET a.money = a.money + ? WHERE a.id = ?";

    @Autowired
    private DataSource dataSource;

    @Override
    public BigDecimal getCurrentBalance(long accountId) throws RepositoryException {
        PreparedStatement statement = null;
        try {
            Connection con = dataSource.getConnection();
            statement = con.prepareStatement(SQL_SELECT_BALANCE);
            statement.setLong(1, accountId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                // the value represents current balance
                return rs.getBigDecimal(1);
            } else {
                throw new EntryNotFoundException("Unable to find account with id: " + accountId);
            }
        } catch (SQLException e) {
            // Oops! Query failed.
            String errorMessage = String.format("SQL query failed: %s", e.getMessage());
            log.warning(errorMessage);
            throw new RepositoryException(errorMessage);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    // I suppose throwing an Exception here is redundant
                    log.warning("Failed to close prepared statement: " + e.getMessage());
                }
            }
        }
    }

    @Override
    public boolean updateBalance(long accountId, BigDecimal changeByAmount) throws RepositoryException {
        PreparedStatement statement = null;
        try {
            Connection con = dataSource.getConnection();
            statement = con.prepareStatement(SQL_UPDATE_BALANCE);
            statement.setBigDecimal(1, changeByAmount);
            statement.setLong(2, accountId);
            return statement.executeUpdate() == 1;
        } catch (SQLException e) {
            // Oops! Query failed.
            String errorMessage = String.format("SQL query failed: %s", e.getMessage());
            log.warning(errorMessage);
            throw new RepositoryException(errorMessage);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    // I suppose throwing an Exception here is redundant
                    log.warning("Failed to close prepared statement: " + e.getMessage());
                }
            }
        }
    }
}
