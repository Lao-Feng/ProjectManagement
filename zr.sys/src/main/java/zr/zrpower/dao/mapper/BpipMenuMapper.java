package zr.zrpower.dao.mapper;


import tk.mybatis.spring.annotation.MapperScan;
import zr.zrpower.dao.BaseDao;
import zr.zrpower.model.BPIP_MENU;

/**
 * 系统菜单dao
 * @author nfzr
 *
 */
@MapperScan({"zr.zrpower.dao.mapper"})
public interface BpipMenuMapper extends BaseDao<BPIP_MENU>{
	
	/**
	 * 判断是否有下一级菜单
	 * @param menuno
	 * @return
	 */
	int nextCount(String menuno);
	
	/**
	 * 删除菜单对应的角色
	 * @param menuno
	 */
	void deleteRoleMenu(String menuno);
	
	/**
	 * 查询最大编号
	 * @param menuno
	 * @return
	 */
	String getMaxNo(String menuno);  
	
}