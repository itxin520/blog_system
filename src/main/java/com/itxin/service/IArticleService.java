package com.itxin.service;

import com.github.pagehelper.PageInfo;
import com.itxin.model.domain.Article;

import java.util.List;

public interface IArticleService {
    //分页查询文章列表
    public PageInfo<Article> selectArticleWithPage(Integer page,Integer count);
    public List<Article> getHeatArticles();

    //根据文章id查询单个文章详情
    public Article selectArticleWithId(Integer id);

    //发布文章
    public void publish(Article article);

    //根据主键更新文章
    public void updateArticleWithId(Article article);

    //根据主键删除文章
    public void deleteArticleWithId(int id);
}
