package com.itxin.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itxin.dao.CommentMapper;
import com.itxin.dao.StatisticMapper;
import com.itxin.model.domain.Comment;
import com.itxin.model.domain.Statistic;
import com.itxin.service.ICommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CommentServiceImpl implements ICommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private StatisticMapper statisticMapper;

    @Override
    public PageInfo<Comment> getComments(Integer aid, int page, int count) {
        PageHelper.startPage(page,count);
        List<Comment> commentList = commentMapper.selectCommentWithPage(aid);
        PageInfo<Comment> commentPageInfo = new PageInfo<>(commentList);
        return commentPageInfo;
    }

    //用户发表评论
    @Override
    public void pushComment(Comment comment) {
        commentMapper.pushComment(comment);
        //更新文章评论数据量
        Statistic statistic = statisticMapper.selectStatisticWithArticleId(comment.getArticleId());
        statistic.setCommentsNum(statistic.getCommentsNum()+1);
        statisticMapper.updateArticleCommentWithId(statistic);
    }
}
