package pl.gitgg.retailerrewardstask.domain.transaction;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.gitgg.retailerrewardstask.domain.common.Interval;
import pl.gitgg.retailerrewardstask.domain.exception.NotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final static String TRANSACTION_NAME = "transaction";

    public List<Transaction> getAll() {
        return transactionRepository.findAll();
    }

    public Transaction createNew(Transaction transaction) {
        transaction.setId(UUID.randomUUID().toString());

        if(isNull(transaction.getDate())){
            transaction.setDate(LocalDate.now());
        }

        return transactionRepository.insert(transaction);
    }

    public void delete(String id) {
        if (!transactionRepository.existsById(id)) {
            throw new NotFoundException(TRANSACTION_NAME, id);
        }

        transactionRepository.deleteById(id);
    }

    public List<Transaction> getAllByUser(String userId) {
        return transactionRepository.findAllByUserId(userId);
    }

    public Transaction findById(String id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(TRANSACTION_NAME, id));
    }

    public List<Transaction> getForUserForInterval(String userId, Interval interval) {
        LocalDate from = isNull(interval.getFrom())
                ? LocalDate.EPOCH
                : interval.getFrom();
        LocalDate to = isNull(interval.getTo())
                ? LocalDate.now().plusDays(2)
                : interval.getTo();
        return transactionRepository.findInTheForUserInThePeriod(userId, from, to);
    }

    public Transaction update(Transaction transaction) {
        if (!transactionRepository.existsById(transaction.getId())) {
            throw new NotFoundException(TRANSACTION_NAME, transaction.getId());
        }

        if(isNull(transaction.getDate())) {
            transaction.setDate(LocalDate.now());
        }

        return transactionRepository.save(transaction);
    }
}
