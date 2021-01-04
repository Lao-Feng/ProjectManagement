package com.yonglilian.dao.mapper;

import com.yonglilian.model.BPIP_USER;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.spring.annotation.MapperScan;

import java.util.List;
import java.util.Map;

/**
 * 用户操作数据层
 * @author lwk
 *
 */
@MapperScan({"com.yonglilian.dao.mapper"})
public interface ZRdbcpEngineMapper {
	/**
	 * 执行查询SQL语句，返回Integer
	 * @param execSQL
	 * @return
	 * @throws Exception
	 */
	public Integer selectIntExecSQL(@Param("execSQL") String execSQL) throws Exception;

	/**
	 * 执行查询SQL语句，返回String
	 * @param execSQL
	 * @return
	 * @throws Exception
	 */
	public String selectStrExecSQL(@Param("execSQL") String execSQL) throws Exception;

	/**
	 * 执行查询SQL语句，返回Map<String, Object>数据
	 * @param execSQL
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> selectMapExecSQL(@Param("execSQL") String execSQL) throws Exception;

	/**
	 * 执行查询SQL语句，返回List<Map<String, Object>>数据
	 * @param execSQL
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectListMapExecSQL(@Param("execSQL") String execSQL) throws Exception;

	/**
	 * 执行增加SQL语句 
	 * @param execSQL
	 * @return
	 * @throws Exception
	 */
	public Integer insertExecSQL(@Param("execSQL") String execSQL) throws Exception;

	/**
	 * 执行修改SQL语句
	 * @param execSQL
	 * @return
	 * @throws Exception
	 */
	public Integer updateExecSQL(@Param("execSQL") String execSQL) throws Exception;

	/**
	 * 执行删除SQL语句
	 * @param execSQL
	 * @return
	 * @throws Exception
	 */
	public Integer deleteExecSQL(@Param("execSQL") String execSQL) throws Exception;

	/**
	 * 根据loginID查询用户信息
	 * @param LoginID
	 * @return
	 * @throws Exception
	 */
	public BPIP_USER getUserInfoByLID(@Param("loginID") String loginID) throws Exception;

	/**
	 * 获取用户的最大userID
	 * @param unitID
	 * @return
	 * @throws Exception
	 */
	public String getMaxUserID(@Param("unitID") String unitID) throws Exception;

	/**
	 * 获取用户的详细信息
	 * @param userID
	 * @return
	 * @throws Exception
	 */
	public BPIP_USER getUserInfo(@Param("userID") String userID) throws Exception;

	/**
	 * 查询用户信息列表
	 * @param unitID 单位编号
	 * @return
	 * @throws Exception
	 */
	public List<BPIP_USER> getUserInfoList(@Param("unitID") String unitID) throws Exception;

	/**
	 * 获取某部门的用户列表
	 * @param unitID 单位编号
	 * @return
	 * @throws Exception
	 */
	public List<BPIP_USER> getRecoveryUserList(@Param("unitID") String unitID) throws Exception;
}