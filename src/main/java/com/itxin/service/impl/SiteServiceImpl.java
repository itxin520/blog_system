package com.itxin.service.impl;

import com.github.pagehelper.PageHelper;
import com.itxin.dao.ArticleMapper;
import com.itxin.dao.CommentMapper;
import com.itxin.dao.StatisticMapper;
import com.itxin.model.ResponseData.StaticticsBo;
import com.itxin.model.domain.Article;
import com.itxin.model.domain.Comment;
import com.itxin.model.domain.Statistic;
import com.itxin.service.ISiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SiteServiceImpl implements ISiteService {

    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private StatisticMapper statisticMapper;

    @Override
    public List<Comment> recentComments(int count) {
        PageHelper.startPage(1,count>10 || count<1? 10:count);
        List<Comment> byPage = commentMapper.selectNewComment();
        return byPage;
    }

    @Override
    public List<Article> recentArticles(int count) {
        PageHelper.startPage(1,count>10 || count<1? 10:count);
        List<Article> list = articleMapper.selectArticleWithPage();
        //封装文章统计数据
        for (int i=0;i<list.size();i++){
            Article article = list.get(i);
            Statistic statistic = statisticMapper.selectStatisticWithArticleId(article.getId());
            article.setHits(statistic.getHits());
            article.setCommentsNum(statistic.getCommentsNum());
        }
        return list;
    }

    @Override
    public StaticticsBo getStatistics() {
        StaticticsBo staticticsBo = new StaticticsBo();
        Integer articles = articleMapper.countArticle();
        Integer comments = commentMapper.countComment();
        staticticsBo.setArticles(articles);
        staticticsBo.setComments(comments);
        return staticticsBo;
    }

    @Override
    public void updateStatistics(Article article) {

        Statistic statistic = statisticMapper.selectStatisticWithArticleId(article.getId());
        statistic.setHits(statistic.getHits()+1);
        statisticMapper.updateArticleHitsWithId(statistic);
    }
}
