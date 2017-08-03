package test.pack.repository;

import java.math.BigDecimal;

public interface AccountRepository {
    BigDecimal getCurrentBalance(long accountId) throws RepositoryException;
    boolean updateBalance(long accountId, BigDecimal byAmount) throws RepositoryException;
}
