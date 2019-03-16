package com.ying.order.service;

import com.ying.auth.vo.UserVO;
import com.ying.core.basic.service.ConnectService;
import com.ying.friend.vo.FriendVO;
import com.ying.order.model.Order;
import com.ying.product.dto.InStockDTO;
import com.ying.product.dto.OutStockDTO;
import com.ying.product.model.ProductSpec;
import com.ying.product.vo.ProductVO;
import com.ying.team.vo.TeamVO;

/**
 * @author bvvy
 * @date 2018/12/15
 */
public interface OrderConnectService extends ConnectService {
    FriendVO getOnlyFriend(Integer userId, Integer toId);

    /**
     * 获取用户
     * @param userId useri d
     * @return user
     */
    UserVO getUser(Integer userId);

    ProductVO getProductById(String productId);

    void sendMessage(Order order);

    ProductSpec getProductSpec(String productSpecId);

    TeamVO getTeam(String teamId);


    void outStock(OutStockDTO outStock);

    void inStock(InStockDTO inStockDTO);
}
