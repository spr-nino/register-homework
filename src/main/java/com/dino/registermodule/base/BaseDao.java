package com.dino.registermodule.base;

import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 基础数据访问接口，定义针对实体 {@link BaseEntity} 的通用增删改查与分页查询操作。
 * <p>具体 Mapper 继承本接口后即可获得统一的 CRUD 与分页查询契约。
 *
 * @param <T> 实体对象类型，需继承 {@link BaseEntity}
 */
public interface BaseDao<T extends BaseEntity> {

    /**
     * 根据主键删除记录。
     *
     * @param id 待删除记录的主键
     * @return 受影响的记录行数
     */
    int deleteByPrimaryKey(String id);

    /**
     * 插入完整实体记录（包含全部字段）。
     *
     * @param entity 待插入的实体
     * @return 受影响的记录行数
     */
    int insert(T entity);

    /**
     * 选择性插入实体记录，仅插入非空字段。
     *
     * @param entity 待插入的实体
     * @return 受影响的记录行数
     */
    int insertSelective(T entity);

    /**
     * 根据主键查询单条记录。
     *
     * @param id 查询所用的主键
     * @return 匹配的实体，未找到时返回 null
     */
    T selectByPrimaryKey(String id);

    /**
     * 根据主键选择性更新记录，仅更新实体中的非空字段。
     *
     * @param entity 包含待更新字段与主键的实体
     * @return 受影响的记录行数
     */
    int updateByPrimaryKeySelective(T entity);

    /**
     * 根据主键完整更新记录（包含全部字段）。
     *
     * @param entity 包含主键与完整数据的实体
     * @return 受影响的记录行数
     */
    int updateByPrimaryKey(T entity);

    /**
     * 获得分页数据
     * @param param 分页参数
     * @return 分页数据集合
     */
    List<T> selectPageList(BaseParam param);

    /**
     * 按查询条件、分页信息和排序规则查询分页数据，并返回自定义结果类型。
     *
     * @param param              查询条件参数
     * @param pageParam          分页参数（含起始行号与每页大小）
     * @param orderFieldParamList 排序字段列表，决定结果的排序规则
     * @param <R>                结果元素类型，需继承 {@link BaseResult}
     * @return 当前页的结果集合
     */
    <R extends BaseResult> List<R> selectPageList(@Param("param") BaseParam param, @Param("pageParam") PageParam pageParam,
                                                  @Param("orderFieldParamList") List<OrderFieldParam> orderFieldParamList);

    /**
     * 统计符合查询条件的记录总数，用于分页计算总页数。
     *
     * @param param 查询条件参数
     * @return 符合条件的记录总数
     */
    int selectPageCount(BaseParam param);
}
