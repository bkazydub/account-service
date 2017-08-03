package test.pack.test;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;
import test.pack.AppConfig;
import test.pack.service.AccountNotFoundException;
import test.pack.service.AccountService;
import test.pack.service.InsufficientFundsException;

import java.math.BigDecimal;

import static org.springframework.test.annotation.DirtiesContext.MethodMode.AFTER_METHOD;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
@TestExecutionListeners(value = {
        DirtiesContextTestExecutionListener.class,
        DependencyInjectionTestExecutionListener.class
})
@Transactional
public class AccountServiceTest {

    @Autowired
    private AccountService accountService;

    @Test
    public void getAccountBalance() {
        try {
            BigDecimal currentBalance = accountService.getCurrentBalance(2);
            Assert.assertEquals(BigDecimal.valueOf(4000.00).setScale(2, BigDecimal.ROUND_HALF_UP), currentBalance.setScale(2, BigDecimal.ROUND_HALF_UP));
        } catch (AccountNotFoundException e) {
            // this shouldn't be the case
            Assert.assertTrue(false);
        }
    }

    @Test
    @DirtiesContext(methodMode = AFTER_METHOD)
    public void changeByPositiveAmount() {
        try {
            final int accountId = 1;
            BigDecimal balance = accountService.getCurrentBalance(accountId);
            BigDecimal updateByAmount = BigDecimal.valueOf(30.00);
            accountService.updateBalance(accountId, updateByAmount);
            BigDecimal expected = balance.add(updateByAmount);
            Assert.assertEquals(expected, accountService.getCurrentBalance(1));
        } catch (InsufficientFundsException | AccountNotFoundException e) {
            // this shouldn't be the case
            Assert.assertTrue(false);
        }
    }

    @Test
    @DirtiesContext(methodMode = AFTER_METHOD)
    public void changeByNegativeAmountBalanceIsSufficient() {
        try {
            final int accountId = 2;
            BigDecimal balance = accountService.getCurrentBalance(accountId);
            BigDecimal updateByAmount = BigDecimal.valueOf(-1000.00);
            accountService.updateBalance(2, updateByAmount);
            BigDecimal updatedBalance = accountService.getCurrentBalance(accountId);
            Assert.assertEquals(balance.add(updateByAmount), updatedBalance);
        } catch (InsufficientFundsException | AccountNotFoundException e) {
            // this shouldn't be the case
            Assert.assertTrue(false);
        }
    }

    @Test(expected = InsufficientFundsException.class)
    public void changeByNegativeAmountInsufficientBalance() throws InsufficientFundsException, AccountNotFoundException {
        accountService.updateBalance(3, BigDecimal.valueOf(-150.20));
    }

    @Test(expected = AccountNotFoundException.class)
    public void getNonExistingAccountsBalance() throws AccountNotFoundException {
        accountService.getCurrentBalance(7);
    }

    @Test(expected = AccountNotFoundException.class)
    public void updateNonExistingAccountsBalance() throws AccountNotFoundException {
        try {
            accountService.updateBalance(8, BigDecimal.valueOf(-2682));
        } catch (InsufficientFundsException e) {
            // this shouldn't be the case
            Assert.assertTrue(false);
        }
    }
}
