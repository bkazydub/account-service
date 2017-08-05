package test.pack.service;

import java.math.BigDecimal;

public interface AccountService {
    void updateBalance(long accountId, BigDecimal changeBalanceByAmount) throws InsufficientFundsException, AccountNotFoundException;
    BigDecimal getCurrentBalance(long accountId) throws AccountNotFoundException;
}
