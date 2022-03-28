package pl.gitgg.retailerrewardstask.domain.transaction;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import pl.gitgg.retailerrewardstask.domain.exception.BadRequestException;

import javax.validation.Valid;
import java.util.List;

import static java.util.Objects.nonNull;

@RestController
@RequestMapping("/api/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping
    @ApiOperation(value = "Find all transactions")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful operation", response = List.class)
    })
    public List<TransactionDto> getAll(
            @ApiParam("[Optional] User Id, if empty method will return transactions for all users")
            @RequestParam(value = "userId", required = false) String userId) {
        List<Transaction> transactions = StringUtils.hasText(userId)
                ? transactionService.getAllByUser(userId)
                : transactionService.getAll();

        return transactions.stream()
                .map(TransactionDto::from)
                .toList();
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Find transaction by Id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful operation", response = TransactionDto.class),
            @ApiResponse(code = 404, message = "Could not find transaction with given ID")
    })
    public TransactionDto get(
            @ApiParam("Transaction Id") @PathVariable String id) {
        return TransactionDto.from(transactionService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Create new transaction")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Transaction created successfully", response = TransactionDto.class),
            @ApiResponse(code = 400, message = "Wrong parameters provided or missing fields in Transaction body")
    })
    public TransactionDto createNewTransaction(@RequestBody @Valid TransactionDto transaction) {
        return TransactionDto.from(transactionService.createNew(transaction.toTransaction()));
    }


    @PutMapping("/{id}")
    @ApiOperation(value = "Update existing transaction")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Transaction updated successfully", response = TransactionDto.class),
            @ApiResponse(code = 400, message = "Wrong parameters provided or missing fields in Transaction body"),
            @ApiResponse(code = 400, message = "Id in object body doesn't match id provided in url"),
            @ApiResponse(code = 404, message = "Could not find transaction with given ID")
    })
    public TransactionDto updateTransaction(@PathVariable String id, @RequestBody @Valid TransactionDto transaction) {
        if (nonNull(transaction.getId()) && !transaction.getId().equals(id)) {
            throw new BadRequestException("Id in object body doesn't match id provided in url");
        }

        transaction.setId(id);
        return TransactionDto.from(transactionService.update(transaction.toTransaction()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Delete existing transaction")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Operation finished successfully, nothing to return"),
            @ApiResponse(code = 404, message = "Could not find transaction with given ID")
    })
    public void deleteTransaction(@PathVariable String id) {
        transactionService.delete(id);
    }
}
