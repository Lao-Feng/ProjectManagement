package zr.zrpower.dao.mapper;

import org.apache.ibatis.annotations.Param;
import tk.mybatis.spring.annotation.MapperScan;

import java.util.Map;

/**
 * 流程引擎相关接口的DAO层
 * @author lwk
 *
 */
@MapperScan({"zr.zrpower.dao.mapper"})
public interface FlowDAOMapper {
	/**
	 * 增加流程分类实体
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Integer insertPackage(Map<String, Object> map) throws Exception;

	/**
	 * 编辑流程分类实体
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Integer editPackage(Map<String, Object> map) throws Exception;

	/**
	 * 增加流程按钮表数据实体类
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Integer insertButton(Map<String, Object> map) throws Exception;

	/**
	 * 编辑流程按钮表数据实体类
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Integer editButton(Map<String, Object> map) throws Exception;

	/**
	 * 删除流程按钮表数据实体类
	 * @param ID
	 * @return
	 * @throws Exception
	 */
	public Integer deleteButton(@Param("ID") String ID) throws Exception;

	/**
	 * 增加过程表数据实体类
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Integer insertFLOW_CONFIG_PROCESS(Map<String, Object> map) throws Exception;

	/**
	 * 编辑过程表数据实体类
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Integer editFLOW_CONFIG_PROCESS(Map<String, Object> map) throws Exception;

	/**
	 * 删除过程表数据实体类
	 * @param ID
	 * @return
	 * @throws Exception
	 */
	public Integer deleteFLOW_CONFIG_PROCESS(@Param("ID") String ID) throws Exception;

	/**
	 * 增加过程流程组表数据实体类
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Integer insertFLOW_CONFIG_PROSESS_GROUP(Map<String, Object> map) throws Exception;

	/**
	 * 编辑过程流程组表数据实体类
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Integer editFLOW_CONFIG_PROSESS_GROUP(Map<String, Object> map) throws Exception;

	/**
	 * 增加活动依赖转发(关系)表数据实体类
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Integer insertFLOW_CONFIG_ACTIVITY_CONNE(Map<String, Object> map) throws Exception;

	/**
	 * 编辑活动依赖转发(关系)表数据实体类
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Integer editFLOW_CONFIG_ACTIVITY_CONNE(Map<String, Object> map) throws Exception;

	/**
	 * 增加时间限制表数据实体类
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Integer insertFLOW_CONFIG_TIME(Map<String, Object> map) throws Exception;

	/**
	 * 编辑时间限制表数据实体类
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Integer editFLOW_CONFIG_TIME(Map<String, Object> map) throws Exception;

	/**
	 * 增加活动按钮关系表数据实体类
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Integer insertFLOW_CONFIG_ACTIVITY_BUTTON(Map<String, Object> map) throws Exception;

	/**
	 * 编辑活动按钮关系表数据实体类
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Integer editFLOW_CONFIG_ACTIVITY_BUTTON(Map<String, Object> map) throws Exception;

	/**
	 * 增加活动可操作字段表数据实体类
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Integer insertCOLL_CONFIG_OPERATE_FIELD(Map<String, Object> map) throws Exception;

	/**
	 * 编辑活动可操作字段表数据实体类
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Integer editCOLL_CONFIG_OPERATE_FIELD(Map<String, Object> map) throws Exception;

	/**
	 * 增加活动流程组表数据实体类
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Integer insertFLOW_CONFIG_ACTIVITY_GROUP(Map<String, Object> map) throws Exception;

	/**
	 * 编辑活动流程组表数据实体类
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Integer editFLOW_CONFIG_ACTIVITY_GROUP(Map<String, Object> map) throws Exception;

	/**
	 * 增加流程权限委托管理表数据实体类
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Integer insertFLOW_CONFIG_ENTRUST(Map<String, Object> map) throws Exception;

	/**
	 * 编辑流程权限委托管理表数据实体类
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Integer editFLOW_CONFIG_ENTRUST(Map<String, Object> map) throws Exception;
}