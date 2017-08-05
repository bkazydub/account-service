package test.pack.repository;

import java.math.BigDecimal;

// The methods could've been defined to return/take Account (with fields id and balance)
// class but instead defined this way for simplicity's sake
public interface AccountRepository {
    BigDecimal getCurrentBalance(long accountId) throws RepositoryException;
    boolean updateBalance(long accountId, BigDecimal updatedBalance) throws RepositoryException;
    // other methods (find, remove etc) are absent as they appear to be out of scope.
}
