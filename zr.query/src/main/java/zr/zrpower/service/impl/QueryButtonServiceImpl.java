package zr.zrpower.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zr.zrpower.common.db.DBEngine;
import zr.zrpower.common.db.DBRow;
import zr.zrpower.common.db.DBSet;
import zr.zrpower.common.util.FunctionMessage;
import zr.zrpower.common.util.ReflectionUtil;
import zr.zrpower.common.util.SysPreperty;
import zr.zrpower.dao.UserMapper;
import zr.zrpower.queryengine.mode.QUERY_CONFIG_BUTTON;
import zr.zrpower.service.QueryButtonService;

import java.util.List;
import java.util.Map;

@Service
public class QueryButtonServiceImpl implements QueryButtonService {
	/** The QueryButtonServiceImpl Logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(QueryButtonServiceImpl.class);
	QueryControlImpl queryControl = new QueryControlImpl();
	private DBEngine dbEngine;
	private static int clients = 0;
	/** 用户操作数据层. */
	@Autowired
	private UserMapper userMapper;
//	/** 查询配置的DAO层. */
//	@Autowired
//	private QueryConfigMapper queryConfigMapper;

	/**
	 * 构造方法
	 */
	public QueryButtonServiceImpl() {
		dbEngine = new DBEngine(SysPreperty.getProperty().MainDataSource, 
				SysPreperty.getProperty().IsConvert);
		dbEngine.initialize();
		if (clients < 1) {
		}
		clients = 1;
	}

	@Override
	public QUERY_CONFIG_BUTTON[] getButtonList(String FID) throws Exception {
		StringBuffer strSql = new StringBuffer();
		QUERY_CONFIG_BUTTON bgs[] = null;
		strSql.append("Select * from QUERY_CONFIG_BUTTON ");
		try {
			if (FID.equals("0")) {
				strSql.append(" Where FID='0' or FID is null");
			} else {
				if (FID.length() == 8) {
					strSql.append(" Where FID='" + FID + "'");
				}
			}
			strSql.append(" Order By BCODE,ID");
			List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(strSql.toString());
			int length = retlist != null ? retlist.size() : 0;
			if (length == 0) {
				bgs = null;
			} else {// length > 0
				bgs = new QUERY_CONFIG_BUTTON[length];
				for (int i = 0; i < length; i++) {
					Map<String, Object> retmap = retlist.get(i);
					bgs[i] = (QUERY_CONFIG_BUTTON) ReflectionUtil.convertMapToBean(retmap, QUERY_CONFIG_BUTTON.class);
				}
			}
		} catch (Exception e) {
			bgs = null;
			LOGGER.error("getButtonList方法出现异常：" + e.toString());
		}
		return bgs;
	}

	@Override
	public QUERY_CONFIG_BUTTON[] getShowButton(String FID) throws Exception {
		StringBuffer strSql = new StringBuffer();
		QUERY_CONFIG_BUTTON bgs[] = null;
		strSql.append("Select * from QUERY_CONFIG_BUTTON  where FID='0' or FID is null or FID='" 
					 + FID + "' Order By FID,BCODE,ID");
		try {
			List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(strSql.toString());
			int length = retlist != null ? retlist.size() : 0;
			if (length == 0) {
				bgs = null;
			} else {// length > 0
				bgs = new QUERY_CONFIG_BUTTON[length];
				for (int i = 0; i < length; i++) {
					Map<String, Object> retmap = retlist.get(i);
					bgs[i] = (QUERY_CONFIG_BUTTON) ReflectionUtil.convertMapToBean(retmap, QUERY_CONFIG_BUTTON.class);
				}
			}
		} catch (Exception e) {
			bgs = null;
			LOGGER.error("getShowButton方法出现异常：" + e.toString());
		}
		return bgs;
	}

	@Override
	public QUERY_CONFIG_BUTTON[] getButtonByFID(String FID) throws Exception {
		StringBuffer strSql = new StringBuffer();
		QUERY_CONFIG_BUTTON bgs[] = null;
		strSql.append("Select a.* from QUERY_CONFIG_BUTTON a, QUERY_CONFIG_BRELATION b "
					+ "Where a.ID=b.BID and b.FID='" + FID + "' order by b.ID ");
		try {
			List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(strSql.toString());
			int length = retlist != null ? retlist.size() : 0;
			if (length == 0) {
				bgs = null;
			} else {// length > 0
				bgs = new QUERY_CONFIG_BUTTON[length];
				for (int i = 0; i < length; i++) {
					Map<String, Object> retmap = retlist.get(i);
					bgs[i] = (QUERY_CONFIG_BUTTON) ReflectionUtil.convertMapToBean(retmap, QUERY_CONFIG_BUTTON.class);
				}
			}
		} catch (Exception e) {
			bgs = null;
			LOGGER.error("getButtonByFID方法出现异常：" + e.toString());
		}
		return bgs;
	}

	@Override
	public FunctionMessage addButton(QUERY_CONFIG_BUTTON Bt) throws Exception {
		FunctionMessage funMsg = new FunctionMessage(1);
		StringBuffer strSql = new StringBuffer();
		try {
			strSql.append("Select * From QUERY_CONFIG_BUTTON Where ID='" + Bt.getID() + "'");
			Map<String, Object> retmap = userMapper.selectMapExecSQL(strSql.toString());
			if (retmap != null && retmap.size() > 0) {
				funMsg.setResult(false);
				strSql.delete(0, strSql.length());// 清空
				strSql.append("查询配置按钮【" + Bt.getID() + "】已经存在");
				funMsg.setMessage(strSql.toString());
			} else {
				String strMaxNo = getMaxFieldNo("QUERY_CONFIG_BUTTON", "ID", 8);
				Bt.setID(strMaxNo);
				boolean isOK = dbEngine.ExecuteInsert(Bt.getData());
				if (isOK) {
					funMsg.setMessage("查询配置按钮录入成功");
					funMsg.setResult(true);
				}
			}
		} catch (Exception ex) {
			LOGGER.error("addButton方法出现异常：", ex);
		}
		queryControl.inithashtable();
		
		return funMsg;
	}

	@Override
	public FunctionMessage editButton(QUERY_CONFIG_BUTTON Bt) throws Exception {
		StringBuffer addBuf = new StringBuffer();
		boolean isOk = false;
		FunctionMessage funMsg = new FunctionMessage(1);
		try {
			DBRow row = Bt.getData();
			addBuf.append("ID=" + Bt.getID());
			isOk = dbEngine.ExecuteEdit(row, addBuf.toString());
			if (isOk) {
				funMsg.setResult(true);
				funMsg.setMessage("查询配置按钮修改成功");
			} else {
				funMsg.setResult(false);
				addBuf.delete(0, addBuf.length());// 清空
				addBuf.append("查询配置按钮【" + Bt.getID() + "】不存在");
				funMsg.setMessage(addBuf.toString());
			}
		} catch (Exception e) {
			LOGGER.error("editButton方法出现异常：" + e.toString());
		}
		queryControl.inithashtable();
		
		return funMsg;
	}

	@Override
	public FunctionMessage deleteButton(String ID) throws Exception {
		boolean isOk = false;
		FunctionMessage funMsg = new FunctionMessage(1);
		StringBuffer addBuf = new StringBuffer();
		try {
			addBuf.append("ID=" + ID);
			isOk = dbEngine.ExecuteDelete("QUERY_CONFIG_BUTTON", addBuf.toString());
			if (isOk) {
				funMsg.setResult(true);
				addBuf.delete(0, addBuf.length());// 清空
				addBuf.append("查询配置按钮【" + "】已经删除");
				funMsg.setMessage(addBuf.toString());
			} else {
				funMsg.setMessage("查询配置按钮删除不成功");
				funMsg.setResult(false);
			}
		} catch (Exception e) {
			LOGGER.error("deleteButton方法出现异常：" + e.toString());
		}
		return funMsg;
	}

	@Override
	public QUERY_CONFIG_BUTTON getButtonByID(String id) throws Exception {
		QUERY_CONFIG_BUTTON bp = null;
		StringBuffer strSql = new StringBuffer();
		try {
			strSql.append("Select * From QUERY_CONFIG_BUTTON Where ID='" + id.trim() + "'");
			Map<String, Object> retmap = userMapper.selectMapExecSQL(strSql.toString());
			if (retmap == null || retmap.size() <= 0) {
				return null;
			} else {// retmap != null
				bp = (QUERY_CONFIG_BUTTON) ReflectionUtil.convertMapToBean(retmap, QUERY_CONFIG_BUTTON.class);
			}
		} catch (Exception e) {
			LOGGER.error("getButtonByID方法出现异常：" + e.toString());
		}
		return bp;
	}

	/**
	 * 功能或作用：取出最大的记录流水号
	 * @param TableName 数据库表名
	 * @param FieldName 数据库字段名称
	 * @param FieldLen 数据库字段长度
	 * @Return MaxNo 执行后返回一个MaxNo字符串
	 */
	private String getMaxFieldNo(String TableName, String FieldName, int FieldLen) {
		String MaxNo = "";
		int LenMaxNo = 0;
		StringBuffer strSQL = new StringBuffer();
		strSQL.append("SELECT MAX(" + FieldName + ") AS MaxNo FROM " + TableName);
		try {
			DBSet dbset = dbEngine.QuerySQL(strSQL.toString());
			if (dbset != null && dbset.RowCount() > 0) {
				MaxNo = dbset.Row(0).Column("MaxNo").getString();
				if (MaxNo != null && MaxNo.length() > 0) {
					MaxNo = String.valueOf(Integer.parseInt(MaxNo) + 1);
					LenMaxNo = MaxNo.length();
					MaxNo = "0000000000000000000000000" + MaxNo;
				} else {
					MaxNo = "00000000000000000000000001";
					LenMaxNo = 1;
				}
			}
			MaxNo = MaxNo.substring(25 - FieldLen + LenMaxNo);
		} catch (Exception ex) {
			LOGGER.error("getMaxFieldNo方法出现异常：" + ex.toString());
		}
		return MaxNo;
	}
}