package com.yonglilian.dao.mapper;

import com.yonglilian.model.BPIP_USER_REMOVE;
import tk.mybatis.spring.annotation.MapperScan;

import java.util.Map;

/**
 * 调动用户数据层
 * @author lwk
 *
 */
@MapperScan({"com.yonglilian.dao.mapper"})
public interface UserRemoveMapper {
	/**
	 * 增加新调动用户信息
	 * @param userRemove 用户实体
	 * @return
	 * @throws Exception
	 */
	public Integer addID(Map<String, Object> map) throws Exception;

	/**
	 * 获取调动用户列表
	 * @param userID
	 * @return
	 * @throws Exception
	 */
	public BPIP_USER_REMOVE[] getUserRemoveList(String userID) throws Exception;

	/**
	 * 获取调动用户编号
	 * @param userID
	 * @return
	 * @throws Exception
	 */
	public BPIP_USER_REMOVE getUserInfo(String userID) throws Exception;

	/**
	 * 根据单位编号获取单位名称
	 * @param beforeUnitID 单位编号
	 * @return
	 * @throws Exception
	 */
	public String getBEFOREUNITName(String beforeUnitID) throws Exception;

}
