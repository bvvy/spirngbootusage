package com.ying.auth.service;

import com.ying.auth.dto.GroupAddDTO;
import com.ying.auth.dto.GroupApplyDTO;
import com.ying.auth.dto.GroupConfirmDTO;
import com.ying.auth.model.Group;
import com.ying.auth.vo.GroupApplicationVO;
import com.ying.auth.vo.GroupUserVO;
import com.ying.auth.vo.GroupVO;
import com.ying.core.basic.service.BasicService;

import java.util.List;

/**
 * @author bvvy
 * @date 2019/2/13
 */
public interface GroupService extends BasicService<Group, String> {

    void add(GroupAddDTO dto);

    /**
     * 获取团队下的成员
     *
     * @param groupId 团队
     * @return 团队成员
     */
    List<GroupUserVO> listGroupUsers(String groupId);

    GroupVO getVO(String groupId);

    /**
     * todo 通知
     *
     * @param dto
     */
    void apply(GroupApplyDTO dto);

    /**
     * todo 通知
     *
     * @param dto
     */
    void confirm(GroupConfirmDTO dto);

    List<GroupApplicationVO> listGroupApplications(String groupId);

}