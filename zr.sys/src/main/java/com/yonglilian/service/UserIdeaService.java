package com.yonglilian.service;

import com.yonglilian.model.BPIP_USER_IDEA;
import zr.zrpower.common.util.FunctionMessage;

/**
 * 意见设置服务层
 * 
 * @author lwk
 *
 */
public interface UserIdeaService {
	/**
	 * 保存新意见信息
	 * @param userIdea 新意见信息实体
	 * @return
	 * @throws Exception
	 */
	public FunctionMessage addUserIdea(BPIP_USER_IDEA userIdea) throws Exception;

	/**
	 * 删除意见信息
	 * @param ID 意见ID
	 * @return
	 * @throws Exception
	 */
	public FunctionMessage delUserIdea(String ID) throws Exception;

	/**
	 * 修改意见信息
	 * @param userIdea 新意见信息实体
	 * @return
	 * @throws Exception
	 */
	public FunctionMessage editUserIdea(BPIP_USER_IDEA userIdea) throws Exception;

	  /**
     * 根据指定条件查询所有的意见信息
     * @param strWhere String  条件
     * @return BPIP_USER_IDEA[]
     */
    public BPIP_USER_IDEA[] getIdeaList(String strWhere) throws Exception;

    /**
     * 根据指定条件查询所有的意见信息
     * @param userID String  条件
     * @return BPIP_USER_IDEA[]
     */
    public BPIP_USER_IDEA[] getIdeaList1(String userID) throws Exception;

}