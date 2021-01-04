package com.yonglilian.service.impl;

import com.yonglilian.common.util.StringUtils;
import com.yonglilian.common.util.SysPreperty;
import com.yonglilian.dao.mapper.BpipTableFieldMapper;
import com.yonglilian.model.dbmanage.BPIP_FIELD;
import com.yonglilian.service.BaseDbaDaoService;
import com.yonglilian.service.BpipTableFieldService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zr.zrpower.common.db.DBEngine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 接口实现
 * 
 * @author nfzr
 *
 */
@Service
public class BpipTableFieldServiceImpl implements BpipTableFieldService {
	/** The UserServiceImpl Logger. */
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(BpipTableFieldServiceImpl.class);
	/**
	 * 数据库类型，1：Oracle，2：MSsql，3：MySQL
	 */
	String dataBaseType = SysPreperty.getProperty().DataBaseType;// 数据库类型
	/** 数据库引擎. */
	private DBEngine dbEngine;
	private static int clients = 0;
	/** 用户操作数据层. */
	@Autowired
	private BpipTableFieldMapper bpipTableFieldMapper;
	/** 数据库数据层. */
	@Autowired
	private BaseDbaDaoService baseDbaDaoService;

	/**
	 * 构造方法
	 */
	public BpipTableFieldServiceImpl() {
		dbEngine = new DBEngine(SysPreperty.getProperty().MainDataSource, SysPreperty.getProperty().IsConvert);
		dbEngine.initialize();
		if (clients < 1) {
		}
		clients++;
	}

	@Override
	public BPIP_FIELD queryObject(String fieldId) throws Exception {
		BPIP_FIELD model = bpipTableFieldMapper.queryObject(fieldId);
		model.setFIELDNAMEOLD(model.getFIELDNAME());
		return model;
	}

	@Override
	public List<BPIP_FIELD> queryList(Map<String, Object> map) throws Exception {
		// TODO Auto-generated method stub
		return bpipTableFieldMapper.queryList(map);
	}

	@Override
	public boolean save(BPIP_FIELD model) throws Exception {
		//检查是否有同名字段名称
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("fieldName", model.getFIELDNAME().trim());
		map.put("tableId", model.getTABLEID().trim());
//		if(model.getTABLENAME()==null||model.getTABLENAME().length()>0) {//创建表，插入记录
//			//插入数据
//			String maxNo=getMaxNo();
//			model.setFIELDID(maxNo);
//			bpipTableFieldMapper.save(model);
//		}else {
//		}
		//创建字段，新增的时候
		if(bpipTableFieldMapper.isField(map)==0) {
			//增加字段
			if(baseDbaDaoService.CreatField(model)) {
				//插入数据
				String maxNo=getMaxNo();
				model.setFIELDID(maxNo);
				bpipTableFieldMapper.save(model);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean update(BPIP_FIELD model) throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("fieldName", model.getFIELDNAME().trim());
		map.put("tableId", model.getTABLEID().trim());
		if(bpipTableFieldMapper.isField(map)<=1) {
			//判断数据库是否存在字段
			if(baseDbaDaoService.booleanField(model)) {//存在，修改
				if(baseDbaDaoService.AlterField(model)) {
					bpipTableFieldMapper.update(model);
					return true;
				}
			}else {//不存在，创建
				if(baseDbaDaoService.CreatField(model)) {
					bpipTableFieldMapper.update(model);
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean delete(BPIP_FIELD model) throws Exception {
		bpipTableFieldMapper.delete(model.getFIELDID());
		baseDbaDaoService.DropField(model);
		return false;
	}

	@Override
	public int dbaType() throws Exception {
		// TODO Auto-generated method stub
		return Integer.valueOf(dataBaseType);
	}

	/**
	 * 返回最大记录id+1
	 * @return
	 * @throws Exception
	 */
	public String getMaxNo()  throws Exception{
		String maxNo=bpipTableFieldMapper.getMaxNo();
		if(StringUtils.isBlank(maxNo)||maxNo=="null") {
			maxNo="00000001";
		}else {
			maxNo="00000000"+(Integer.valueOf(maxNo)+1);
			maxNo=maxNo.substring(maxNo.length()-8, maxNo.length());
		}
		return maxNo;
	}

	@Override
	public List<BPIP_FIELD> selectList(Map<String, Object> map) throws Exception {
		return bpipTableFieldMapper.selectList(map);
	}
	

}