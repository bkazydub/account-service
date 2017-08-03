package test.pack.service;

import java.math.BigDecimal;

public interface AccountService {
    void updateBalance(long accountId, BigDecimal amount) throws InsufficientFundsException;
}
