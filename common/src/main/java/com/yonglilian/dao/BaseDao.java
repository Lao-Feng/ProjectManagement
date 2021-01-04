package com.yonglilian.dao;

import java.util.List;
import java.util.Map;

/**
 * @author   ftl
 *  
 */
public interface BaseDao<T> {
    /**
     * 插入数据<通过实体>
     * @param t
     */
	void save( T t );
    /**
     * 插入数据 Map<String, Object>
     * @param map
     */
	void save( Map<String, Object> map );
    /**
     * 批量保存数据
     * @param list
     */
	int saveBatch( List<T> list );
    /**
     * 修改数据
     * @param t
     * @return
     */
	int update( T t );
    /**
     * 修改数据
     * @param map
     * @return
     */
	int update( Map<String, Object> map );
    /**
     * 删除数据
     * @param id
     * @return
     */
	int delete( Object id );
    /**
     * 删除数据
     * @param map
     * @return
     */
	int delete( Map<String, Object> map );
    /**
     * 批量删除数据
     * @param id
     * @return
     */
	int deleteBatch( Object[] id );
	/**
	 * 批量删除数据
	 * @param id
	 * @return
	 */
	int deleteBatchList(@SuppressWarnings("rawtypes") List id );
    /**
     * 查询数据详情
     * @param id
     * @return
     */
	T queryObject( Object id );
    /**
     * 查询数据
     * @param map
     * @return
     */
	List<T> queryList( Map<String, Object> map );

    /**
     * 批量修改返回
     * @param map
     * @return
     */
	List<T> updateList( Map<String, Object> map );
	/**
	 * 查询数据
	 * @param param
	 * @return
	 */
	List<T> queryListParam(String param);
    /**
     * 查询数据
     * @param id
     * @return
     */
	List<T> queryList( Object id );
	/**
	 * 查询数据
	 * @return
	 */
	List<T> queryListAll();
    /**
     * 查询数据记录数
     * @param map
     * @return
     */
	int queryTotal( Map<String, Object> map );
    /**
     * 查询数据记录数
     * @return
     */
	int queryTotal();

}
