package it.polito.SE2.P12.SPG.interfaceEntity;

import it.polito.SE2.P12.SPG.entity.Order;

import java.util.List;

//Questo dovrebbe essere implementato da tutte quelle classi che hanno una lista di ordini a proprio carico.
public interface OrderUserType {
    public List<Order> getOrders();
}
