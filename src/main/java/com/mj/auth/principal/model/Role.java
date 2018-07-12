package com.mj.auth.principal.model;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author bvvy
 * <p>
 * 角色
 */
@Data
@Entity
@Table(name = "sys_role")
@Builder
public class Role {

    public static final String PRINCIPAL = "ROLE";

    /**
     * id
     */
    @Id
    @GeneratedValue
    private Integer id;
    /**
     * 角色编码
     */
    private String code;
    /**
     * 角色名称
     */
    private String name;
}