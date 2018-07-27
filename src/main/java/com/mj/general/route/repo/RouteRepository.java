package com.mj.general.route.repo;

import com.mj.general.route.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zejun
 * @date 2018/7/10 11:28
 */
@Repository
public interface RouteRepository extends JpaRepository<Route,Integer>, QuerydslPredicateExecutor<Route> {

    /**
     * 根据航线代码查询数据
     * @param routeCode 航线代码
     * @return Route
     */
    Route getByRouteCodeIgnoreCase(String routeCode);

    /**
     * 根据船id查询数据
     * @param carrierId 船id
     * @return Route
     */
    List<Route> findByCarrierId(Integer carrierId);

}
