package pl.gitgg.retailerrewardstask.domain.transaction;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository extends MongoRepository<Transaction, String> {

    List<Transaction> findAllByUserId(String userId);

    @Query("{'userId' : ?0, 'date' : {$gte : ?1, $lte : ?2}}")
    List<Transaction> findInTheForUserInThePeriod(String userId, LocalDate from, LocalDate to);
}
