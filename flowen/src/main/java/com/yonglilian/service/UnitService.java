package com.yonglilian.service;

import com.yonglilian.model.BPIP_UNIT;

/**
 * 单位信息服务层
 * 
 * @author lwk
 *
 */
public interface UnitService {
	
	/**
	 * 根据单位编号获取单位信息
	 * @param UnitID
	 * @return
	 * @throws Exception
	 */
	public BPIP_UNIT getUnit(String unitID) throws Exception;

	
}