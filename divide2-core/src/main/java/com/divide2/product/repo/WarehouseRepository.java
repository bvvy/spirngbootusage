package com.divide2.product.repo;

import com.divide2.product.model.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author bvvy
 * @date 2018/12/9
 */
public interface WarehouseRepository extends JpaRepository<Warehouse,String> {
    List<Warehouse> findByTeamIdAndEnabled(String teamId,Boolean enabled);

}
