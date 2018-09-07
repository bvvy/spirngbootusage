package com.ying.basis.comment.service.impl;

import com.ying.basis.comment.model.Comment;
import com.ying.basis.comment.repo.CommentRepository;
import com.ying.basis.comment.service.CommentService;
import com.ying.core.basic.service.impl.SimpleBasicServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author bvvy
 * @date 2018/7/19
 */
@Service
public class CommentServiceImpl extends SimpleBasicServiceImpl<Comment, Integer, CommentRepository> implements CommentService {

    private final CommentRepository commentRepository;

    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }


}