package com.yonglilian.service;

import com.yonglilian.model.BPIP_USER;

/**
 * 用户操作服务层
 * 
 * @author lwk
 *
 */
public interface UserService {
	
	/**
	 * 获取用户的详细信息（除密码以外）
	 * @param userID
	 * @return
	 * @throws Exception
	 */
	public BPIP_USER getUserInfo(String userID) throws Exception;


    /**
     * 获取用户真实姓名
     * @param userID
     * @return
     * @throws Exception
     */
    public String getUserName(String userID) throws Exception;



}
