package com.divide2.team.service.impl;

import com.divide2.auth.dto.UserSearchDTO;
import com.divide2.auth.vo.UserVO;
import com.divide2.core.basic.service.impl.SimpleBasicServiceImpl;
import com.divide2.core.er.Asserter;
import com.divide2.core.er.Loginer;
import com.divide2.core.root.converter.Converter;
import com.divide2.friend.dto.TeamMenuChatDTO;
import com.divide2.mine.vo.WarehouseVO;
import com.divide2.order.query.OrderQueryParam;
import com.divide2.order.vo.OrderVO;
import com.divide2.product.query.StockQuery;
import com.divide2.product.vo.ProductVO;
import com.divide2.product.vo.StockStreamVO;
import com.divide2.product.vo.StockVO;
import com.divide2.team.dto.*;
import com.divide2.team.listener.TeamListener;
import com.divide2.team.model.*;
import com.divide2.team.repo.*;
import com.divide2.team.service.TeamInnerConnectService;
import com.divide2.team.service.TeamService;
import com.divide2.team.vo.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author bvvy
 * @date 2019/2/13
 */
@Service
public class TeamServiceImpl extends SimpleBasicServiceImpl<Team, String, TeamRepository> implements TeamService {
    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;
    private final TeamInnerConnectService teamInnerConnectService;
    private final TeamApplicationRepository teamApplicationRepository;
    private final TeamCooperationApplicationRepository teamCooperationApplicationRepository;
    private final TeamCooperationRepository teamCooperationRepository;
    private final TeamListener teamListener;

    public TeamServiceImpl(MemberRepository memberRepository,
                           TeamRepository teamRepository,
                           TeamInnerConnectService teamInnerConnectService,
                           TeamApplicationRepository teamApplicationRepository,
                           TeamCooperationApplicationRepository teamCooperationApplicationRepository,
                           TeamCooperationRepository teamCooperationRepository,
                           TeamListener teamListener) {
        this.memberRepository = memberRepository;
        this.teamRepository = teamRepository;
        this.teamInnerConnectService = teamInnerConnectService;
        this.teamApplicationRepository = teamApplicationRepository;
        this.teamCooperationApplicationRepository = teamCooperationApplicationRepository;
        this.teamCooperationRepository = teamCooperationRepository;
        this.teamListener = teamListener;
    }

    @Override
    public TeamVO getVO(String teamId) {
        Team team = this.get(teamId);
        return toTeamVO(team);
    }

    private TeamVO toTeamVO(Team team) {
        return new TeamVO(
                team.getId(),
                team.getName(),
                team.getImage()
        );
    }

    @Override
    @Transactional
    public void add(TeamAddDTO dto) {
        this.checkGroupExists(dto.getName());
        Team team = new Team();
        team.setName(dto.getName());
        this.add(team);
        teamListener.onCreate(team);
    }

    private void checkGroupExists(String name) {
        Team team = teamRepository.getByName(name);
        Asserter.isNull(team);
    }


    @Override
    public TeamVO search(UserSearchDTO search) {
        Team team = teamRepository.getByName(search.getQuery());
        if (team == null) {
            return null;
        }
        return toTeamVO(team);
    }

    @Override
    public List<MemberVO> listMembers(String teamId) {
        this.getVO(teamId);
        List<Member> teamUsers = memberRepository.findByTeamId(teamId);
        List<SquadVO> squads = teamInnerConnectService.listSquadByTeam(teamId);
        List<MemberVO> vos = new ArrayList<>();
        squads.forEach(squad -> {
            List<UserVO> users = teamUsers.stream()
                    .filter(member -> member.getSquadId().equals(squad.getId()))
                    .map(member -> teamInnerConnectService.getUser(member.getUserId()))
                    .collect(Collectors.toList());

            MemberVO vo = new MemberVO(squad, users);
            vos.add(vo);
        });
        return vos;
    }

    @Override
    public Set<String> listTeamUserMenus(String teamId, String type, String principleId) {
        return teamInnerConnectService.listTeamUserChildrenMenuIds(teamId, type, principleId);
    }


    @Override
    public Set<String> listMemberAuthorities(String teamId) {
        return teamInnerConnectService.listMemberAuthorities(teamId);
    }

    @Override
    public void delete(String id) {
        teamListener.onDelete(this.get(id));
    }

    @Override
    public void apply(TeamApplyDTO dto) {

        Member existMember = memberRepository.getByTeamIdAndUserId(dto.getToTeamId(), Loginer.userId());
        Asserter.isNull(existMember);
        TeamJoinApplication teamJoinApplication = teamApplicationRepository.getByFromIdAndToTeamId(Loginer.userId(), dto.getToTeamId());
        if (teamJoinApplication == null) {
            teamJoinApplication = new TeamJoinApplication();
            teamJoinApplication.setFromId(Loginer.userId());
            teamJoinApplication.setToTeamId(dto.getToTeamId());
            teamJoinApplication.setMemoName(dto.getMemoName());
            teamJoinApplication.setCreateTime(LocalDateTime.now());
            teamJoinApplication.setUpdateTime(LocalDateTime.now());
            teamJoinApplication.setStatus("waiting_confirm");
        } else {
            teamJoinApplication.setStatus("waiting_confirm");
            teamJoinApplication.setUpdateTime(LocalDateTime.now());
        }
        teamApplicationRepository.save(teamJoinApplication);

        teamInnerConnectService.addChat(new TeamMenuChatDTO(dto.getToTeamId(), "team_join_apply"));


    }

    @Override
    public void confirm(TeamConfirmDTO dto) {

        TeamJoinApplication teamJoinApplication = teamApplicationRepository.getOne(dto.getTeamApplicationId());
        Member existsUser = memberRepository.getByTeamIdAndUserId(teamJoinApplication.getToTeamId(), teamJoinApplication.getFromId());
        Asserter.isNull(existsUser);
        teamJoinApplication.setStatus("finish");
        teamJoinApplication.setUpdateTime(LocalDateTime.now());
        teamApplicationRepository.save(teamJoinApplication);
        Member member = new Member();
        member.setMemoName(dto.getMemoName());
        member.setSquadId(dto.getSquadId());
        member.setTeamId(teamJoinApplication.getToTeamId());
        member.setUserId(teamJoinApplication.getFromId());
        memberRepository.save(member);

    }

    @Override
    public List<TeamApplicationVO> listTeamApplications(String teamId) {
        List<TeamJoinApplication> teamJoinApplications = teamApplicationRepository.findByToTeamIdOrderByUpdateTimeDesc(teamId);
        return Converter.of(teamJoinApplications).convert(this::toTeamApplicationVO);
    }

    private TeamApplicationVO toTeamApplicationVO(TeamJoinApplication teamJoinApplication) {

        UserVO user = teamInnerConnectService.getUser(teamJoinApplication.getFromId());
        return new TeamApplicationVO(
                teamJoinApplication.getId(),
                teamJoinApplication.getMemoName(),
                teamJoinApplication.getStatus(),
                user
        );
    }


    @Override
    public void confirmCooperation(TeamCooperationConfirmDTO dto) {
        TeamCooperationApplication application = teamCooperationApplicationRepository.getOne(dto.getId());
        application.setStatus("finish");
        teamCooperationApplicationRepository.save(application);
        checkExistCooperation(application.getFromTeamId(), application.getToTeamId());
        TeamCooperation teamCooperation = new TeamCooperation();
        teamCooperation.setCreateTime(LocalDateTime.now());
        teamCooperation.setFromTeamId(application.getFromTeamId());
        teamCooperation.setToTeamId(application.getToTeamId());
        teamCooperationRepository.save(teamCooperation);
        teamCooperation = new TeamCooperation();
        teamCooperation.setCreateTime(LocalDateTime.now());
        teamCooperation.setFromTeamId(application.getToTeamId());
        teamCooperation.setToTeamId(application.getFromTeamId());
        teamCooperationRepository.save(teamCooperation);

    }

    private void checkExistCooperation(String fromTeamId, String toTeamId) {
        TeamCooperation teamCooperation = teamCooperationRepository.getByFromTeamIdAndToTeamId(fromTeamId, toTeamId);
        Asserter.isNull(teamCooperation);
        teamCooperation = teamCooperationRepository.getByFromTeamIdAndToTeamId(toTeamId, fromTeamId);
        Asserter.isNull(teamCooperation);
    }

    @Override
    public void applyCooperation(TeamCooperationApplyDTO dto) {
        TeamCooperationApplication existApplication = teamCooperationApplicationRepository.getByFromTeamIdAndToTeamId(dto.getToTeamId(), dto.getFromTeamId());
        if (existApplication != null) {
            this.confirmCooperation(new TeamCooperationConfirmDTO(existApplication.getId()));
            return;
        }
        checkExistCooperation(dto.getFromTeamId(), dto.getToTeamId());
        TeamCooperationApplication teamCooperationApplication = teamCooperationApplicationRepository.getByFromTeamIdAndToTeamId(dto.getFromTeamId(), dto.getToTeamId());
        if (teamCooperationApplication == null) {
            teamCooperationApplication = new TeamCooperationApplication();
        }
        teamCooperationApplication.setCreateTime(LocalDateTime.now());
        teamCooperationApplication.setFromTeamId(dto.getFromTeamId());
        teamCooperationApplication.setToTeamId(dto.getToTeamId());
        teamCooperationApplication.setRemarks(dto.getRemarks());
        teamCooperationApplication.setStatus("waiting_confirm");
        teamCooperationApplicationRepository.save(teamCooperationApplication);
        //todo add chat after init menus when create Team
//        teamInnerConnectService.addChat(new TeamMenuChatDTO(dto.getToTeamId(),"team_cooperate_apply"));
    }

    @Override
    public List<CooperationApplicationVO> listTeamCooperationApplication(String teamId) {
        List<TeamCooperationApplication> teamCooperationApplications = teamCooperationApplicationRepository.findTeamCooperationApplications(teamId);
        return Converter.of(teamCooperationApplications).convert(app -> {
            CooperationApplicationVO vo = new CooperationApplicationVO();
            vo.setId(app.getId());
            vo.setRemarks(app.getRemarks());
            vo.setStatus(app.getStatus());
            TeamVO team = null;
            if (app.getFromTeamId().equals(teamId)) {
                team = this.getVO(app.getToTeamId());
                vo.setSelfApply(true);
            }
            if (app.getToTeamId().equals(teamId)) {
                team = this.getVO(app.getFromTeamId());
            }
            vo.setTeam(team);
            return vo;
        });
    }

    @Override
    public List<TeamVO> listUserTeams(Integer userId) {
        List<Member> userTeams = memberRepository.findByUserId(userId);
        return Converter.of(userTeams).convert(userTeam -> this.getVO(userTeam.getTeamId()));
    }

    @Override
    public List<TeamVO> listTeamCooperations(String teamId) {
        List<TeamCooperation> vos = teamCooperationRepository.findByFromTeamId(teamId);
        return Converter.of(vos).convert(vo -> this.getVO(vo.getToTeamId()));
    }

    @Override
    public List<WarehouseVO> listWarehouse(String teamId) {
        return teamInnerConnectService.listWarehouse(teamId);
    }

    @Override
    public Page<StockVO> findStock(String teamId, StockQuery stockQuery, Pageable pageable) {
        return teamInnerConnectService.findStock(teamId, stockQuery, pageable);
    }

    @Override
    public Page<StockStreamVO> findStockStream(String teamId, StockQuery stockQuery, Pageable pageable) {
        return teamInnerConnectService.findStockStream(teamId, stockQuery, pageable);
    }

    @Override
    public Page<ProductVO> findProduct(String teamId, Pageable pageable) {
        return teamInnerConnectService.findProduct(teamId, pageable);
    }

    @Override
    public Page<OrderVO> findReceiveOrder(String teamId, OrderQueryParam queryParam, Pageable pageable) {
        Page<OrderVO> receiveOrder = teamInnerConnectService.findReceiveOrder(teamId, queryParam, pageable);
        return receiveOrder.map(order -> {
            order.setTeam(this.getVO(order.getFromTeamId()));
            return order;
        });
    }

    @Override
    public Page<OrderVO> findSendOrder(String teamId, OrderQueryParam queryParam, Pageable pageable) {
        Page<OrderVO> sendOrder = teamInnerConnectService.findSendOrder(teamId, queryParam, pageable);
        return sendOrder.map(order -> {
            order.setTeam(this.getVO(order.getToTeamId()));
            return order;
        });
    }
}
