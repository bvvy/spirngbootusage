package com.ying.core.er;

import com.ying.auth.model.Group;
import com.ying.core.exception.AlreadyExistsException;
import com.ying.core.exception.NotFoundException;

/**
 * @author bvvy
 * @date 2019/2/15
 */
public class Asserter {

    public static void notNull(Object o) {
        if (o == null) {
            throw new NotFoundException();
        }
    }

    public static void isNull(Object o) {
        if (o != null) {
            throw new AlreadyExistsException();
        }
    }
}