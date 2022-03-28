package pl.gitgg.retailerrewardstask.domain.transaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.LocalDate;

@Document("transactions")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {

    @Id
    private String id;

    @Indexed
    @NotBlank(message = "User id is mandatory")
    private String userId;

    private LocalDate date;

    @PositiveOrZero(message = "Value must be zero or greater")
    private BigDecimal value;

    public static TransactionDto from(Transaction t) {
        return TransactionDto.builder()
                .userId(t.getUserId())
                .date(t.getDate())
                .id(t.getId())
                .value(t.getValue())
                .build();
    }

    public Transaction toTransaction() {
        return Transaction.builder()
                .userId(this.userId)
                .date(this.date)
                .id(this.id)
                .value(this.value)
                .build();
    }
}
