package it.polito.SE2.P12.SPG.entity;


import it.polito.SE2.P12.SPG.interfaceEntity.WalletUserType;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "wallet_operation")
@Data
public class WalletOperation {
    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO
    )
    private Long walletOperationId;
    @ManyToOne
    private User cust;
    @Column(name = "operation_type")
    private String operationType;
    @Column(name = "time")
    private Long time;
    @Column(name = "amount")
    private Double amount;

    public WalletOperation(WalletUserType user, String operationType, Long time, Double amount) {
        this.cust = (User) user;
        this.operationType = operationType;
        this.time = time;
        this.amount = amount;
    }

}
