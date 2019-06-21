package cn.hxz.webapp.content.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import cn.hxz.webapp.content.entity.Article;
import net.chenke.playweb.support.mybatis.DynaMapper;
import net.chenke.playweb.support.mybatis.PageRequest;

/**
 * ArticleMapper.java Create on 2017-01-19 14:53:55
 * <p>
 *  Mapper接口
 * </p>
 *
 * @author cn.feeboo
 * @version 1.0
 */
public interface ArticleMapper extends DynaMapper<Article> {
	
	List<Map<String,Object>> selectJoinTest(RowBounds rowBounds);

	Article  selectProviousOne(@Param(value = "id")Long id, @Param(value="channelId")Long  channelId);

	Article selectNextOne(@Param(value = "id")Long id, @Param(value="channelId")Long  channelId);
	
	List<Map<String,Object>> findArticleBycanteenId(@Param(value="channelId")Long channelId, @Param(value="filters")Map<String,Object> filters);
}