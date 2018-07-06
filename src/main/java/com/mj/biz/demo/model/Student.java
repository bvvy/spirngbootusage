package com.mj.biz.demo.model;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * @author bvvy
 * 学生
 */
@Data
@Builder
@Entity
@Table(name = "t_student")
public class Student {
    /**
     * id
     */
    @Id
    @GeneratedValue
    private Integer id;
    /**
     * 姓名
     */
    private String name;
    /**
     * 性别
     */
    private String gender;

    /**
     * 班级id
     */
    private Integer clazzId;
}