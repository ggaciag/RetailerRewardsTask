package pl.gitgg.retailerrewardstask.domain.transaction;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping
    public List<Transaction> getAll(@RequestParam(value = "userId", required = false) String userId) {
        return StringUtils.hasText(userId)
                ? transactionService.getAllByUser(userId)
                : transactionService.getAll();
    }

    @GetMapping("/{id}")
    public Transaction get(@PathVariable String id){
        return transactionService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Transaction createNewTransaction(@RequestBody Transaction transaction) {
        return transactionService.createNew(transaction);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTransaction(@PathVariable String id) {
        transactionService.delete(id);
    }
}
