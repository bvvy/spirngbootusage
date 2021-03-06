package com.divide2.basis.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * todo not hear
 * @author bvvy
 * @date 2018/9/9
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "basis_star")
public class Star {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    /**
     * 点赞的人
     */
    private Integer fromId;
    private String fromName;
    private String fromAvatar;
    /**
     * 被点赞的对象
     */
    private Integer toId;

    /**
     * 被点赞的对象类型
     */
    private String type;

    /**
     * 被点赞的对象的名称
     */
    private String toName;
    /**
     * 创建时间
     */
    private LocalDateTime cdt;
}
