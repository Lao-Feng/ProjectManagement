package zr.zrpower.dao.mapper;

import tk.mybatis.spring.annotation.MapperScan;
import zr.zrpower.model.dbmanage.BPIP_TABLESPACE;

import java.util.Map;

/**
 * 表分类管理数据层
 * @author lwk
 *
 */
@MapperScan({"zr.zrpower.dao.mapper"})
public interface TableSpaceMapper {
	/**
	 * 增加表空间信息
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Integer insertSP(Map<String, Object> map) throws Exception;

	/**
	 * 编辑表空间
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Integer editSP(Map<String, Object> map) throws Exception;

	/**
	 * 删除表空间
	 * @param ID
	 * @return
	 * @throws Exception
	 */
	public Integer deleteSP(String ID) throws Exception;

	/**
	 *  获取表分类列表
	 * @param condition
	 * @return
	 * @throws Exception
	 */
	public BPIP_TABLESPACE[] getBpipTableSpaceList(String condition) throws Exception;

	/**
	 * 获取表分类
	 * @param ID
	 * @return
	 * @throws Exception
	 */
	public BPIP_TABLESPACE getSPACEID(String ID) throws Exception;

	/**
	 * 得到表分类数
	 * @return
	 * @throws Exception
	 */
	public String sumTableSpace() throws Exception;

	/**
	 * 根据表空间编号获取表空间中文名称
	 * @param ID 表空间编号
	 * @return
	 * @throws Exception
	 */
	public String getSpaceCHINESENAME(String ID) throws Exception;

	/**
	 * 根据表编号获取表空间
	 * @param ID 表编号
	 * @return
	 * @throws Exception
	 */
	public String getSpaceNo(String ID) throws Exception;

	/**
	 * 功能或作用：取出最大ID
	 * @return 执行后返回一个最大ID
	 * @throws Exception
	 */
	public String getSpaceId() throws Exception;

}
