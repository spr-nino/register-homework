package com.dino.registermodule.base;

import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 基础服务接口，基于 {@link BaseDao} 为实体提供默认的增删改查与分页查询实现。
 * <p>实现类只需实现 {@link #getBaseDao()} 提供具体的数据访问对象，其余方法均可直接使用默认实现。
 *
 * @param <T> 实体类型，需继承 {@link BaseEntity}
 */
public interface BaseServices<T extends BaseEntity>{

    /**
     * 保存实体：先生成主键并写入创建时间，再执行插入，整个过程处于事务中。
     *
     * @param entity 待保存的实体
     */
    @Transactional
    default void save(T entity){
        entity.setId(this.getPrimaryKey());
        entity.setCreateTime(new Date());
        this.getBaseDao().insert(entity);
    }

    /**
     * 根据主键查询单条实体记录。
     *
     * @param primaryKey 主键
     * @return 匹配的实体，未找到时返回 null
     */
    default T findOne(String primaryKey){
        return this.getBaseDao().selectByPrimaryKey(primaryKey);
    }

    /**
     * 根据主键查询并返回自定义结果类型，默认返回 null，需由实现类按需覆盖。
     *
     * @param primaryKey 主键
     * @param <R>        结果类型
     * @return 自定义结果对象，默认实现返回 null
     */
    default <R> R findOneResult(String primaryKey){
        return null;
    }


    /**
     * 根据主键删除实体记录。
     *
     * @param primaryKey 待删除记录的主键
     */
    default void remove(String primaryKey){
        this.getBaseDao().deleteByPrimaryKey(primaryKey);
    }

    /**
     * 根据主键选择性更新实体，仅更新非空字段。
     *
     * @param entity 包含主键与待更新字段的实体
     */
    default void updateByPrimaryKeySelective(T entity){
        this.getBaseDao().updateByPrimaryKeySelective(entity);
    }

    /**
     * 生成主键，返回去除连字符后的 UUID 字符串。
     *
     * @return 32 位无连字符的 UUID 字符串
     */
    default String getPrimaryKey(){
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        return uuid;
    }

    /**
     * 分页查询并封装为 {@link PageResult}，自动统计总记录数并计算总页数。
     *
     * @param param              查询条件参数
     * @param pageParam          分页参数（当前页码、每页大小等）
     * @param orderFieldParamList 排序字段列表
     * @param <P>                查询参数类型，需继承 {@link BaseParam}
     * @param <R>                结果元素类型，需继承 {@link BaseResult}
     * @return 封装好的分页结果
     */
    default <P extends BaseParam,R extends BaseResult> PageResult<R> findPage(P param, PageParam pageParam, List<OrderFieldParam> orderFieldParamList){
        // 复制分页参数，同时换算出数据库查询的起始行号
        pageParam = new PageParam(pageParam);
        int totalCount = this.getPageCount(param);
        int pageSize = pageParam.getPageSize();
        List<R> resultList = this.findPageList(param,pageParam,orderFieldParamList);
        PageResult<R> result = new PageResult<>();
        result.setItems(resultList);
        result.setTotalCount(totalCount);
        result.setCurrentPage(pageParam.getCurrentPage());
        result.setPageSize(pageSize);
        // 总页数 = 总记录数 / 每页大小，整除时即为商的整数部分
        int totalPage = totalCount/pageSize;
        // 不能整除说明还有不足一页的剩余记录，需要再加一页
        if(totalCount%pageSize!=0){
            totalPage += 1;
        }
        result.setTotalPage(totalPage);
        return result;
    }

    /**
     * 按查询条件、分页参数与排序规则查询当前页的数据列表。
     *
     * @param param              查询条件参数
     * @param pageParam          分页参数
     * @param orderFieldParamList 排序字段列表
     * @param <P>                查询参数类型，需继承 {@link BaseParam}
     * @param <R>                结果元素类型，需继承 {@link BaseResult}
     * @return 当前页的数据列表
     */
    default <P extends BaseParam,R extends BaseResult> List<R> findPageList(P param, PageParam pageParam, List<OrderFieldParam> orderFieldParamList){
        return this.getBaseDao().selectPageList(param,pageParam,orderFieldParamList);
    }

    /**
     * 统计符合查询条件的记录总数。
     *
     * @param param 查询条件参数
     * @param <P>   查询参数类型，需继承 {@link BaseParam}
     * @return 符合条件的记录总数
     */
    default <P extends BaseParam> int getPageCount(P param){
        return this.getBaseDao().selectPageCount(param);
    }

    /**
     * 获取服务所使用的数据访问对象，由具体实现类提供。
     *
     * @return 当前服务对应的 {@link BaseDao} 实例
     */
    BaseDao<T> getBaseDao();

}
