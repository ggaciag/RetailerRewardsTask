package pl.gitgg.retailerrewardstask.domain.transaction;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public List<Transaction> getAll() {
        return transactionRepository.findAll();
    }

    public Transaction createNew(Transaction transaction) {
        return transactionRepository.insert(transaction);
    }

    public void delete(String id) {
        transactionRepository.deleteById(id);
    }
}
