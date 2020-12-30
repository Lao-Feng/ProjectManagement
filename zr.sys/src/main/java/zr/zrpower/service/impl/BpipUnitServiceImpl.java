package zr.zrpower.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zr.zrpower.common.db.DBEngine;
import zr.zrpower.common.util.StringUtils;
import zr.zrpower.common.util.SysPreperty;
import zr.zrpower.dao.mapper.BpipUnitMapper;
import zr.zrpower.model.BPIP_UNIT;
import zr.zrpower.service.BpipUnitService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 单位机构/接口
 * @author nfzr
 *
 */
@Service
public class BpipUnitServiceImpl implements BpipUnitService {
	/** The UserServiceImpl Logger. */
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(BpipUnitServiceImpl.class);
	String dataBaseType = SysPreperty.getProperty().DataBaseType;
	/** 数据库引擎. */
	private DBEngine dbEngine;
	/** 用户操作数据层. */
	@Autowired
	private BpipUnitMapper bpipUnitMapper;

	/**
	 * 构造方法
	 */
	public BpipUnitServiceImpl()  throws Exception{
		dbEngine = new DBEngine(SysPreperty.getProperty().MainDataSource, SysPreperty.getProperty().IsConvert);
		dbEngine.initialize();
	}

	@Override
	public BPIP_UNIT queryObject(String unitid)  throws Exception{
		return bpipUnitMapper.queryObject(unitid);
	}

	@Override
	public List<BPIP_UNIT> queryList(Map<String, Object> map)  throws Exception{
		// TODO Auto-generated method stub
		return bpipUnitMapper.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map)  throws Exception{
		// TODO Auto-generated method stub
		return bpipUnitMapper.queryTotal(map);
	}

	@Override
	public void save(BPIP_UNIT model)  throws Exception{
		String strunitid = "";// 得到最大编号
		if (model != null && model.getParentId() != "") {
			strunitid = getMaxNo(model.getParentId(), model.getType());
			model.setUNITID(strunitid);
			if(!"out".equals(strunitid)) {
				bpipUnitMapper.save(model);
			}
		}else {
			bpipUnitMapper.save(model);
		}
	}

	@Override
	public void update(BPIP_UNIT model)  throws Exception{
		// TODO Auto-generated method stub
		bpipUnitMapper.update(model);
	}

	@Override
	public boolean delete(String unitid)  throws Exception{
		boolean reback = true;
		if(bpipUnitMapper.nextCount(unitid.replaceAll("00", ""))>1) {
			reback = false;
		}else {
			if(bpipUnitMapper.nextUsers(unitid)>0) {
				reback = false;
			}else {
				bpipUnitMapper.delete(unitid);
			}
		}
		return reback;
	}

	@Override
	public boolean deleteBatch(String[] unitids)  throws Exception{
		boolean reback = true;
		
		return reback;
	}

	@Override
	public String getMaxNo(String unitId, int type)  throws Exception{
		// TODO Auto-generated method stub
		Map<String,Object> map = new HashMap<String, Object>();
		String nowUnitId = "";
		String st=unitId.replaceAll("00", "");
		String en="000000000000";
		if(type==0) {//同级别
			if(st.length()==0) {
				map.put("stUnitId", st);
				map.put("enUnitId", en.substring(0, 10));
			}else {
				map.put("stUnitId", st.substring(0,st.length()-2));
				map.put("enUnitId", en.substring(0,10-st.length()));
			}
		}else {//下一级
			if(st.length()==0) {
				map.put("stUnitId", st);
				map.put("enUnitId", en.substring(0, 10));
			}else {
				map.put("stUnitId", st.substring(0,st.length()));
				map.put("enUnitId", en.substring(0,10-st.length()));
			}
		}
		nowUnitId = bpipUnitMapper.getMaxNo(map);	
		if (StringUtils.isBlank(nowUnitId)||nowUnitId=="null") {
			nowUnitId = "010000000000";
		} else {
			nowUnitId = nowUnitId.replaceAll("00", "");
			int max=Integer.valueOf(nowUnitId.substring(nowUnitId.length()-2, nowUnitId.length()));
			max=max+1;
			if(max>99) {
				nowUnitId="out";
			}else {
				String maxNo="00"+max;
				maxNo=maxNo.substring(maxNo.length()-2, maxNo.length());
				nowUnitId=nowUnitId.substring(0, nowUnitId.length()-2)+maxNo+"000000000000";
				nowUnitId=nowUnitId.substring(0, 12);
			}
		}
		return nowUnitId;
	}
}