package com.divide2.core.root.example;

import com.divide2.core.root.query.QueryParam;
import lombok.Data;

/**
 * select * from stu s left join clazz c
 * where s.id = 1 and  s.name like "xx"
 * and c.name like 'xx'
 * 多表和单表问题 todo 先解决单表
 * @author bvvy
 * @date 2018/12/20
 */
@Data
public class ExampleQuery implements QueryParam {

    private String stuOrClazzName;

    private String stuId;
}
