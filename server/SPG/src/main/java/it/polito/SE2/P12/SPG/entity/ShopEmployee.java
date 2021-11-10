package it.polito.SE2.P12.SPG.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="shop_employee")
@Data
@NoArgsConstructor
public class ShopEmployee extends User{
}
