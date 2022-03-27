package pl.gitgg.retailerrewardstask.domain.transaction;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.gitgg.retailerrewardstask.domain.exceptions.NotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final static String TRANSACTION_NAME = "transaction";

    public List<Transaction> getAll() {
        return transactionRepository.findAll();
    }

    public Transaction createNew(Transaction transaction) {
        return transactionRepository.insert(transaction);
    }

    public void delete(String id) {
        if(!transactionRepository.existsById(id)){
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
}
