package com.mj.general.charge.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.*;

/**
 * @author zejun
 * @date 2018/7/10 09:05
 * 费用项目表
 */
@Data
@Entity
@Table(name = "general_charge")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Charge {

    /**
     * 费用项目id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 是否删除
     */
    @Builder.Default
    private Integer deleted = 0;

    /**
     *费用简称
     */
    @Column(name = "charge_item_code")
    private String chargeItemCode;

    /**
     * 费用中文名
     */
    @Column(name = "charge_item_CN")
    private String chargeItemCN;

    /**
     * 费用英文名
     */
    @Column(name = "charge_item_EN")
    private String chargeItemEN;

    /**
     * 禁用状态
     */
    @Builder.Default
    private String status = "0";
}