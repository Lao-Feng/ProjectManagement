package zr.zrpower.dao.mapper;

import tk.mybatis.spring.annotation.MapperScan;
import zr.zrpower.model.BPIP_MENU;

import java.util.List;
import java.util.Map;

/**
 * 用户操作数据层
 * @author lwk
 *
 */
@MapperScan({"zr.zrpower.dao.mapper"})
public interface UntilMapper{
	
	/**
	 * 查询用户所有全查菜单
	 * @param userId,flag,menuNo,len
	 * @return
	 */
	public List<BPIP_MENU> userMenuList(Map<String,Object> map);
	
}