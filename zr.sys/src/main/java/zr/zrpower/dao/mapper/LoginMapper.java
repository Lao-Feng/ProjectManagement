package zr.zrpower.dao.mapper;

import tk.mybatis.spring.annotation.MapperScan;
import zr.zrpower.dao.BaseDao;
import zr.zrpower.model.BPIP_USER;

import java.util.Map;

/**
 * 用户操作数据层
 * @author lwk
 *
 */
@MapperScan({"zr.zrpower.dao.mapper"})
public interface LoginMapper extends BaseDao<BPIP_USER>{
	
	/**
	 * 用户登录
	 * @param LOGINID，LOGINID
	 * @return
	 * @throws Exception
	 */
	public BPIP_USER login(Map<String, Object> map);
	

	int updateonline(String userId);
	/**
	 * 获取用户的详细信息
	 * @param userID
	 * @return
	 * @throws Exception
	 */
	public BPIP_USER getUserInfo(Map<String, Object> map) throws Exception;

	
}