package com.mj.auth.principal.repo;


import com.mj.auth.principal.model.User;
import com.mj.auth.principal.repo.cutom.UserRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

/**
 * @author bvvy
 * 基础user repo
 */
public interface UserRepository extends UserRepositoryCustom, JpaRepository<User, Integer>,QuerydslPredicateExecutor<User> {

    /**
     * 通过用户名获取用户
     * @param username 用户名
     * @return principal
     */
    User getByUsername(String username);
}
