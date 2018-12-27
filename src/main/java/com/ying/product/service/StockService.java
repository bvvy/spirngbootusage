package com.ying.product.service;

import com.ying.product.bo.StockBO;
import com.ying.product.dto.InStockDTO;
import com.ying.product.dto.OutStockDTO;
import com.ying.product.query.StockQuery;
import com.ying.product.vo.StockVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * todo 单独的模块 商品 库存 订单 三个模块
 * 所有的对库存的操作都是两个动作 入库(in) 和 出库(out)
 * 例如确认发货 调用 out
 * 例如确认收货  调用 in
 *
 * @author bvvy
 * @date 2018/12/9
 */
public interface StockService {
    /**
     * 入库
     *
     * @param dto dto
     */
    void in(InStockDTO dto);

    /**
     * 出库
     *
     * @param dto dto
     */
    void out(OutStockDTO dto);

    Page<StockVO> findByCompany(StockQuery stockQuery, Pageable pageable);

    Page<StockBO> find(StockQuery stockQuery);
}
