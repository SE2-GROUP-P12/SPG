package it.polito.SE2.P12.SPG.service;

import it.polito.SE2.P12.SPG.entity.Order;
import it.polito.SE2.P12.SPG.entity.WalletOperation;
import it.polito.SE2.P12.SPG.repository.OrderRepo;
import it.polito.SE2.P12.SPG.repository.UserRepo;
import it.polito.SE2.P12.SPG.repository.WalletOperationRepo;
import it.polito.SE2.P12.SPG.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WalletOperationService {
    private final WalletOperationRepo walletOperationRepo;
    private final OrderRepo orderRepo;
    private final UserRepo userRepo;

    @Autowired
    public WalletOperationService(WalletOperationRepo walletOperationRepo, OrderRepo orderRepo, UserRepo userRepo) {
        this.walletOperationRepo = walletOperationRepo;
        this.orderRepo = orderRepo;
        this.userRepo = userRepo;
    }

    public void recordTopUp(String email, Double value) {
        walletOperationRepo.save(new WalletOperation(userRepo.findUserByEmail(email),
                "TOP-UP",
                Utils.getCurrentTimestamp(),
                value));
    }

    public void recordPayment(Long orderId) {
        Order order = orderRepo.findOrderByOrderId(orderId);
        walletOperationRepo.save(new WalletOperation(order.getCust(),
                "PAYMENT",
                Utils.getCurrentTimestamp(),
                order.getValue()));
    }

    public List<WalletOperation> getWalletOperationsByEmail(String email) {
        return walletOperationRepo.findWalletOperationsByWalletEmail(email);
    }


}
