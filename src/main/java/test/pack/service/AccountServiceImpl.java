package test.pack.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import test.pack.repository.AccountRepository;
import test.pack.repository.EntryNotFoundException;
import test.pack.repository.RepositoryException;

import java.math.BigDecimal;
import java.util.logging.Logger;

@Service
@Transactional
public class AccountServiceImpl implements AccountService {

    private static final Logger log = Logger.getLogger(AccountServiceImpl.class.getName());
    private static final String MESSAGE_INSUFFICIENT_FUNDS = "Balance can't be changed by %s for account %d. Reason: insufficient funds.";
    private static final String MESSAGE_ACCOUNT_NOT_FOUND = "There is no account with id '%d'";
    private static final String MESSAGE_SQL_FAILED = "Unable to perform update balance action for account with id: %d. Reason: %s";

    private AccountRepository accountRepository;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void updateBalance(long accountId, BigDecimal changeBalanceByAmount)
            throws InsufficientFundsException, AccountNotFoundException {
        try {
            BigDecimal currentBalance = accountRepository.getCurrentBalance(accountId);
            // Note: it is supposed that currentBalance is positive or equal to 0.
            BigDecimal updatedBalance = currentBalance.add(changeBalanceByAmount);
            if (updatedBalance.compareTo(BigDecimal.ZERO) < 0) {
                // the account has insufficient funds.
                throw new InsufficientFundsException(String.format(MESSAGE_INSUFFICIENT_FUNDS, changeBalanceByAmount.toString(), accountId));
            }
            accountRepository.updateBalance(accountId, updatedBalance);
        } catch (EntryNotFoundException e) {
            log.info("There is no account with id: " + accountId);
            throw new AccountNotFoundException(String.format(MESSAGE_ACCOUNT_NOT_FOUND, accountId));
        } catch (RepositoryException e) {
            // I suppose that only logging will do ATM
            log.warning(String.format(MESSAGE_SQL_FAILED, accountId, e.getMessage()));
        }
    }

    @Override
    public BigDecimal getCurrentBalance(long accountId) throws AccountNotFoundException {
        try {
            return accountRepository.getCurrentBalance(accountId);
        } catch (EntryNotFoundException e) {
            throw new AccountNotFoundException(String.format(MESSAGE_ACCOUNT_NOT_FOUND, accountId));
        } catch (RepositoryException e) {
            // null is returned because I assume that existing but empty accounts should have zero balance.
            return null;
        }
    }
}
