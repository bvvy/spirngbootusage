package com.mj.ocean.quotation.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author zejun
 * @date 2018/7/17 18:16
 */
@Data
@Entity
@Table(name = "ocean_fc_quotation_cost_service")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuotationCost {

    /**
     * id
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 报价主表id
     */
    @Column(name = "quotation_id")
    private Integer quotationId;

    /**
     * 成本服务，成本类别,比如商务成本，业务成本，客户成本
     */
    @Column(name = "cost_service")
    private String costService;

    /**
     * 报价类别，标准报价，和特殊报价
     */
    @Column(name = "cost_service_code")
    private String costServiceCode;

    /**
     * 柜型，存json值  20GP，40GP，40HC，45HC
     */
    @Column(name = "cabinet_type")
    private String cabinetType;

}
