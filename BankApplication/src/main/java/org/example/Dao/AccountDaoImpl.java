package org.example.Dao;

import org.example.Exception.AccountNotFoundException;
import org.example.Model.Account;
import org.example.Model.Transaction;
import org.example.descriptionDto.DescriptionDTO;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AccountDaoImpl implements AccountDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public Account FindAccountNumber(Long accountNo) {
        try (Session session = sessionFactory.openSession()) {
            Account account = session.createQuery("FROM Account WHERE accountNo = :accountNo", Account.class)
                    .setParameter("accountNo", accountNo)
                    .uniqueResult();
            if (account == null) {
                throw new AccountNotFoundException("Account not found for account number: " + accountNo);
            }
            return account;
        }
    }


    @Override
    public void updateBalance(Account account) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(account);
            session.getTransaction().commit();
        }
    }

    @Override
    public void saveTransaction(Transaction transaction) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(transaction);
            session.getTransaction().commit();
        }
    }

    /**
     * Retrieves a list of transactions for a specific account within a given date range.
     *
     * @param accountNo The account number for which transactions need to be fetched.
     * @param fromDate  The start date of the date range.
     * @param toDate    The end date of the date range.
     * @return List of transactions for the specified account and date range.
     */
    @Override
    public List<Transaction> getTransactionsByAccountNo(Long accountNo, LocalDate fromDate, LocalDate toDate) {
        try (Session session = sessionFactory.openSession()) {
            List<Transaction> transactions = session.createQuery("FROM Transaction WHERE (sourceAccountNo = :accountNo OR destinationAccountNo = :accountNo) AND Date BETWEEN :fromDate AND :toDate", Transaction.class)
                    .setParameter("accountNo", accountNo)
                    .setParameter("fromDate", fromDate)
                    .setParameter("toDate", toDate)
                    .list();
            return transactions;
        }catch (Exception e) {
           throw e;
        }
    }

    /**
     * Retrieves the total credited amount for a specific account from the transactions.
     *
     * @param accountNo The account number for which credited amount needs to be fetched.
     * @return The total credited amount for the specified account.
     */
    @Override
    public Double getCreditedAmount(Long accountNo) {
        try (Session session = sessionFactory.openSession()) {
            return (Double) session.createQuery(
                            "SELECT SUM(CASE WHEN destinationAccountNo = :accountNo AND status = 'SUCCESSFUL' THEN amount ELSE 0 END) FROM Transaction")
                    .setParameter("accountNo", accountNo)
                    .uniqueResult();
        }
    }
    /**
     * Retrieves the total debited amount for a specific account from the transactions.
     *
     * @param accountNo The account number for which debited amount needs to be fetched.
     * @return The total debited amount for the specified account.
     */
    @Override
    public Double getDebitedAmount(Long accountNo) {
        try (Session session = sessionFactory.openSession()) {
            return (Double) session.createQuery(
                            "SELECT SUM(CASE WHEN sourceAccountNo = :accountNo AND status = 'SUCCESSFUL' THEN amount ELSE 0 END) FROM Transaction")
                    .setParameter("accountNo", accountNo)
                    .uniqueResult();
        }
    }
    /**
     * Retrieves the total amount (credited + debited) for a specific account from the transactions.
     *
     * @param accountNo The account number for which the total amount needs to be fetched.
     * @return The total amount (credited + debited) for the specified account.
     */
    @Override
    public Double getTotalAmount(Long accountNo) {
        try (Session session = sessionFactory.openSession()) {
            Double creditedAmount = getCreditedAmount(accountNo);
            Double debitedAmount = getDebitedAmount(accountNo);

            return creditedAmount + debitedAmount;
        }
    }
    /**
     * Retrieves a list of transactions for a specific account and year from the database.
     *
     * @param accountNo The account number for which transactions need to be fetched.
     * @param year      The year for which transactions need to be retrieved.
     * @return A list of transactions for the specified account and year.
     */
    @Override
    public List<Transaction> getTransactionsByYear(Long accountNo, Year year) {
        try (Session session = sessionFactory.openSession()){
            return session.createQuery(
                            "FROM Transaction WHERE (sourceAccountNo = :accountNo OR destinationAccountNo = :accountNo) AND YEAR(Date) = :year", Transaction.class)
                    .setParameter("accountNo", accountNo)
                    .setParameter("year", year.getValue())
                    .list();
        }
    }
    /**
     * Retrieves total amounts grouped by description for a specified account from the database.
     *
     * @param accountNo The account number for which total amounts are to be fetched by description.
     * @return List of DescriptionDTO containing description and total amount for the specified account.
     * @throws RuntimeException if an error occurs while fetching the data from the database.
     */
    @Override
    public List<DescriptionDTO> getTotalAmountByDescription(Long accountNo) {
        try (Session session = sessionFactory.openSession()) {
            Query<Object[]> query = session.createQuery(
                            "SELECT description, SUM(amount) as totalAmount FROM Transaction " +
                                    "WHERE (sourceAccountNo = :accountNo OR destinationAccountNo = :accountNo) " +
                                    "GROUP BY description", Object[].class)
                    .setParameter("accountNo", accountNo);

            List<Object[]> resultList = query.list();
            List<DescriptionDTO> descriptionDTOS = new ArrayList<>();

            for (Object[] result : resultList) {
                DescriptionDTO descriptionDTO = new DescriptionDTO();
                descriptionDTO.setDescription((String) result[0]);
                descriptionDTO.setTotalAmount((Double) result[1]);
                descriptionDTOS.add(descriptionDTO);
            }

            return descriptionDTOS;
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public List<Long> getAllAccountNumbers() {

            try (Session session = sessionFactory.openSession()) {
                return session.createQuery("SELECT accountNo FROM Account", Long.class).list();
            } catch (Exception e) {
                throw e;
            }
    }
}

