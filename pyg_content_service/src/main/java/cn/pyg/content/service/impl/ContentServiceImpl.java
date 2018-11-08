package cn.pyg.content.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import cn.pyg.content.service.ContentService;
import cn.pyg.mapper.TbContentMapper;
import cn.pyg.pojo.TbContent;
import cn.pyg.pojo.TbContentExample;
import cn.pyg.pojo.TbContentExample.Criteria;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

/**
 * 服务实现层
 *
 * @author Administrator
 */
@Service
public class ContentServiceImpl implements ContentService {

    @Autowired
    private TbContentMapper contentMapper;

    /**
     * 查询全部
     */
    @Override
    public List<TbContent> findAll() {
        return contentMapper.selectByExample(null);
    }

    /**
     * 按分页查询
     */
    @Override
    public PageResult findPage(int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        Page<TbContent> page = (Page<TbContent>) contentMapper.selectByExample(null);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 增加
     */
    @Override
    public void add(TbContent content) {
        contentMapper.insert(content);
        //清除新增广告分类的缓存数据
        Long categoryId = content.getCategoryId();
        redisTemplate.boundHashOps("content").delete(categoryId);
    }


    /**
     * 修改
     */
    @Override
    public void update(TbContent content) {
        //清除原来的广告分类数据
        TbContent tbContent = contentMapper.selectByPrimaryKey(content.getId());
        Long oldCatetoryId = tbContent.getCategoryId();
        redisTemplate.boundHashOps("content").delete(oldCatetoryId);
        contentMapper.updateByPrimaryKey(content);

        //如果广告分类发生变化，还需要清除修改后的广告分类缓存数据
        if (oldCatetoryId.longValue() != content.getCategoryId().longValue()) {
            redisTemplate.boundHashOps("content").delete(content.getCategoryId());
        }

    }

    /**
     * 根据ID获取实体
     *
     * @param id
     * @return
     */
    @Override
    public TbContent findOne(Long id) {
        return contentMapper.selectByPrimaryKey(id);
    }

    /**
     * 批量删除
     */
    @Override
    public void delete(Long[] ids) {
        for (Long id : ids) {
            //获取广告数据
            TbContent tbContent = contentMapper.selectByPrimaryKey(id);
            Long categoryId = tbContent.getCategoryId();
            redisTemplate.boundHashOps("content").delete(categoryId);
            contentMapper.deleteByPrimaryKey(id);
        }
    }


    @Override
    public PageResult findPage(TbContent content, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        TbContentExample example = new TbContentExample();
        Criteria criteria = example.createCriteria();

        if (content != null) {
            if (content.getTitle() != null && content.getTitle().length() > 0) {
                criteria.andTitleLike("%" + content.getTitle() + "%");
            }
            if (content.getUrl() != null && content.getUrl().length() > 0) {
                criteria.andUrlLike("%" + content.getUrl() + "%");
            }
            if (content.getPic() != null && content.getPic().length() > 0) {
                criteria.andPicLike("%" + content.getPic() + "%");
            }
            if (content.getStatus() != null && content.getStatus().length() > 0) {
                criteria.andStatusLike("%" + content.getStatus() + "%");
            }

        }

        Page<TbContent> page = (Page<TbContent>) contentMapper.selectByExample(example);
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public List<TbContent> findByCategoryId(Long categoryId) {
        //key-value  先从缓存中取值，如果取不到，再从数据库中取
        List<TbContent> contentList = (List<TbContent>) redisTemplate.boundHashOps("content").get(categoryId);
        if (contentList == null) {
            System.out.println("目前获取数据来源:[mysql数据库]");

            //从数据库中取
            TbContentExample example = new TbContentExample();
            Criteria criteria = example.createCriteria();
            criteria.andCategoryIdEqualTo(categoryId);
            contentList = contentMapper.selectByExample(example);
            //将查询结果放入缓存
            redisTemplate.boundHashOps("content").put(categoryId, contentList);
        } else {
            System.out.println("目前获取数据来源:[redis数据库]");
        }

        return contentList;
    }

}
