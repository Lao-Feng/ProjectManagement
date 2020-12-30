package zr.zrpower.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zr.zrpower.bean.QueryConfigTableBean;
import zr.zrpower.dao.QueryConfigTableDao;
import zr.zrpower.domain.QueryConfigTable;
import zr.zrpower.service.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("queryConfigTableService")
public class QueryConfigTableServiceImpl implements QueryConfigTableService {
	@Autowired
	private QueryConfigTableDao queryConfigTableDao;
	// 关联表
	@Autowired
	private QueryConfigConnectionService queryConfigConnectionService;
	// 初始条件表
	@Autowired
	private QueryConfigInitService queryConfigInitService;
	// 查询字段表
	@Autowired
	private QueryConfigQueryfieldService queryConfigQueryfieldService;
	// 结果列表
	@Autowired
	private QueryConfigShowfieldService queryConfigShowfieldService;
	// 按钮列表
	@Autowired
	private QueryConfigParameterService queryConfigParameterService;
	// 排序列表
	@Autowired
	private QueryConfigOrderService queryConfigOrderService;

	@Override
	public QueryConfigTableBean queryObject(String id) throws Exception {
		QueryConfigTableBean queryConfigTableBean = null;
		QueryConfigTable queryConfigTable = queryConfigTableDao.queryObject(id);
		if (queryConfigTable != null) {
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("fid", queryConfigTable.getId());
			//主表
			queryConfigTableBean = new QueryConfigTableBean();
			queryConfigTableBean.setId(queryConfigTable.getId());//用于区别是新增还是修改
			queryConfigTableBean.setTable(queryConfigTable);
			//关联list表
			queryConfigTableBean.setConnectionList(queryConfigConnectionService.queryList(map));
			//初始化list表
			queryConfigTableBean.setQueryInitList(queryConfigInitService.queryList(map));
			//查询列表
			queryConfigTableBean.setQueryFieldList(queryConfigQueryfieldService.fieldList(map));
			//结果列表
			queryConfigTableBean.setQueryShowList(queryConfigShowfieldService.queryList(map,true));
			//按钮列表
			queryConfigTableBean.setQueryParameterList(queryConfigParameterService.queryList(map));
			//排序列表
			queryConfigTableBean.setQueryOrderList(queryConfigOrderService.queryList(map));
		}
		return queryConfigTableBean;
	}

	@Override
	public List<QueryConfigTableBean> queryList(Map<String, Object> map) throws Exception {
		List<QueryConfigTableBean> queryConfigTableBeans = new ArrayList<>();
		List<QueryConfigTable> list = queryConfigTableDao.queryList(map);
		for (QueryConfigTable queryConfigTable : list) {
			QueryConfigTableBean queryConfigTableBean = new QueryConfigTableBean();
			BeanUtils.copyProperties(queryConfigTable, queryConfigTableBean);
			queryConfigTableBeans.add(queryConfigTableBean);
		}
		return queryConfigTableBeans;
	}

	@Override
	public int queryTotal(Map<String, Object> map) throws Exception {
		return queryConfigTableDao.queryTotal(map);
	}

	@Override
	public void save(QueryConfigTableBean queryConfigTableBean) throws Exception {
		QueryConfigTable queryConfigTable = new QueryConfigTable();
		BeanUtils.copyProperties(queryConfigTableBean.getTable(), queryConfigTable);
		// 保存主表
		queryConfigTable.setId(getMaxNo());
		queryConfigTableDao.save(queryConfigTable);
		// 关联表
		queryConfigConnectionService.saveBatch(queryConfigTableBean.getConnectionList(), queryConfigTable.getId());
		// 初始条件
		queryConfigInitService.saveBatch(queryConfigTableBean.getQueryInitList(), queryConfigTable.getId());
		// 查询列表
		queryConfigQueryfieldService.saveBatch(queryConfigTableBean.getQueryFieldList(), queryConfigTable.getId());
		// 结果列表
		queryConfigShowfieldService.saveBatch(queryConfigTableBean.getQueryShowList(), queryConfigTable.getId());
		// 按钮列表
		queryConfigParameterService.saveBatch(queryConfigTableBean.getQueryParameterList(), queryConfigTable.getId());
		// 排序列表
		queryConfigOrderService.saveBatch(queryConfigTableBean.getQueryOrderList(), queryConfigTable.getId());
	}

	@Override
	public void update(QueryConfigTableBean queryConfigTableBean) throws Exception {
		QueryConfigTable queryConfigTable = new QueryConfigTable();
		BeanUtils.copyProperties(queryConfigTableBean.getTable(), queryConfigTable);
		queryConfigTableDao.update(queryConfigTable);
		// 1、先删除查询管理配置所有数据
		queryConfigConnectionService.delete(queryConfigTable.getId());
		queryConfigInitService.delete(queryConfigTable.getId());
		queryConfigQueryfieldService.delete(queryConfigTable.getId());
		queryConfigShowfieldService.delete(queryConfigTable.getId());
		queryConfigParameterService.delete(queryConfigTable.getId());
		queryConfigOrderService.delete(queryConfigTable.getId());
		// 2、再保存查询数据
		// 关联表
		queryConfigConnectionService.saveBatch(queryConfigTableBean.getConnectionList(), queryConfigTable.getId());
		// 初始条件
		queryConfigInitService.saveBatch(queryConfigTableBean.getQueryInitList(), queryConfigTable.getId());
		// 查询列表
		queryConfigQueryfieldService.saveBatch(queryConfigTableBean.getQueryFieldList(), queryConfigTable.getId());
		// 结果列表
		queryConfigShowfieldService.saveBatch(queryConfigTableBean.getQueryShowList(), queryConfigTable.getId());
		// 按钮列表
		queryConfigParameterService.saveBatch(queryConfigTableBean.getQueryParameterList(), queryConfigTable.getId());
		// 排序列表
		queryConfigOrderService.saveBatch(queryConfigTableBean.getQueryOrderList(), queryConfigTable.getId());
	}

	@Override
	public void delete(String id) throws Exception {
		queryConfigTableDao.delete(id);
		queryConfigConnectionService.delete(id);
		queryConfigInitService.delete(id);
		queryConfigQueryfieldService.delete(id);
		queryConfigShowfieldService.delete(id);
		queryConfigParameterService.delete(id);
		queryConfigOrderService.delete(id);
	}

	@Override
	public void deleteBatch(String[] ids) throws Exception {
		queryConfigTableDao.deleteBatch(ids);
	}

	@Override
	public String getMaxNo() throws Exception {
		String maxId = queryConfigTableDao.getMaxNo();
		if (maxId == null || maxId.equals("null")) {
			maxId = "00000001";
		} else {
			maxId = "00000000" + (Integer.valueOf(maxId) + 1);
			maxId = maxId.substring(maxId.length() - 8, maxId.length());
		}
		return maxId;
	}

	@Override
	public boolean copy(String id) throws Exception {
		QueryConfigTableBean table = queryObject(id);
		QueryConfigTable model = table.getTable();
		model.setName("复件_"+model.getName());
		table.setTable(model); 
		save(table);
		return true;
	}

}
