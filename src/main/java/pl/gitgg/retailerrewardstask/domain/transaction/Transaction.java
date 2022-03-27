package pl.gitgg.retailerrewardstask.domain.transaction;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document("transactions")
@Builder
@Data
public class Transaction {

    @Id
    private String id;

    private BigDecimal value;


}
