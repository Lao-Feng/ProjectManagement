package zr.zrpower.dao.mapper;

import tk.mybatis.spring.annotation.MapperScan;
import zr.zrpower.dao.BaseDao;
import zr.zrpower.model.BPIP_USER_ROLE;

/**
 * 数据操作dao
 * @author nfzr
 *
 */
@MapperScan({"zr.zrpower.dao.mapper"})
public interface BpipUserRoleMapper extends BaseDao<BPIP_USER_ROLE>{
	
	/**
	 * 通过用户id删除角色绑定
	 * @param userId
	 */
	void delUserRole(String userId);
}