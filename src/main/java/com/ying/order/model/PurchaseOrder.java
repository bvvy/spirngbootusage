package com.ying.order.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 采购单
 * @author bvvy
 * @date 2018/12/2
 */
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "o_purchase_order")
@Data
public class PurchaseOrder extends OrderImpl implements IOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer orderId;

    private LocalDateTime createTime;

    private Integer toId;

    private String toName;

    private Integer fromId;

    private String fromName;


}