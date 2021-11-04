package com.itxin.web.admin;

import com.github.pagehelper.PageInfo;
import com.itxin.dao.ArticleMapper;
import com.itxin.model.ResponseData.ArticleResponseData;
import com.itxin.model.ResponseData.StaticticsBo;
import com.itxin.model.domain.Article;
import com.itxin.model.domain.Comment;
import com.itxin.service.IArticleService;
import com.itxin.service.ISiteService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.PrinterURI;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
    @Autowired
    private ISiteService siteServiceImpl;
    @Autowired
    private IArticleService articleServiceImpl;

    //管理中心起始页
    @GetMapping(value = {"","/index"})
    public String index(HttpServletRequest request){
        //获取最新的5篇博客、评论以及统计数据
        List<Article> articles = siteServiceImpl.recentArticles(5);
        List<Comment> comments = siteServiceImpl.recentComments(5);
        StaticticsBo statistics = siteServiceImpl.getStatistics();
        //向request域中存储数据
        request.setAttribute("comments",comments);
        request.setAttribute("articles",articles);
        request.setAttribute("statistics",statistics);
        return "back/index";
    }

    //向文章发表页面跳转
    @GetMapping(value = "/article/toEditPage")
    public String newArticle(){
        return "back/article_edit";
    }

    //发表文章
    @PostMapping(value = "/article/publish")
    @ResponseBody
    private ArticleResponseData publishArticle(Article article){
        if (StringUtils.isBlank(article.getCategories())){
            article.setCategories("默认分类");
        }
        try {
            articleServiceImpl.publish(article);
            logger.info("文章发布成功");
            return ArticleResponseData.ok();
        }catch (Exception e){
            logger.error("文章发布失败，错误信息："+e.getMessage());
            return ArticleResponseData.fail();
        }
    }

    //跳转到后台文章列表页面
    @GetMapping(value = "/article")
    public String index(@RequestParam(value = "page",defaultValue = "1") int page,
                        @RequestParam(value = "count",defaultValue = "10") int count,
                        HttpServletRequest request){
        PageInfo<Article> pageInfo = articleServiceImpl.selectArticleWithPage(page, count);
        request.setAttribute("articles",pageInfo);
        return "back/article_list";
    }

    //向文章修改页面跳转
    @GetMapping(value = "/article/{id}")
    public String editArticle(@PathVariable("id") String id,HttpServletRequest request){
        Article article = articleServiceImpl.selectArticleWithId(Integer.parseInt(id));
        request.setAttribute("contents",article);
        request.setAttribute("categories",article.getCategories());
        return "back/article_edit";
    }
    //修改处理文章
    @PostMapping(value = "/article/modify")
    @ResponseBody
    public ArticleResponseData modifyArticle(Article article){
        try {
            articleServiceImpl.updateArticleWithId(article);
            logger.info("文章更新成功");
            return ArticleResponseData.ok();
        }catch (Exception e){
            logger.error("文章更新失败，错误信息:"+e.getMessage());
            return ArticleResponseData.fail();
        }
    }

    //文章删除
    @PostMapping(value = "/article/delete")
    @ResponseBody
    public ArticleResponseData delete(@RequestParam int id){
        try {
            articleServiceImpl.deleteArticleWithId(id);
            logger.info("文章删除成功");
            return ArticleResponseData.ok();
        }catch (Exception e){
            logger.info("文章删除失败，错误信息："+e.getMessage());
            return ArticleResponseData.fail();
        }
    }

}
