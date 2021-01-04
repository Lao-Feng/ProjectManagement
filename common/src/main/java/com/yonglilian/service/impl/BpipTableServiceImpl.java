package com.yonglilian.service.impl;

import com.yonglilian.common.util.StringUtils;
import com.yonglilian.common.util.SysPreperty;
import com.yonglilian.dao.mapper.BpipTableMapper;
import com.yonglilian.model.dbmanage.BPIP_FIELD;
import com.yonglilian.model.dbmanage.BPIP_TABLE;
import com.yonglilian.service.BaseDbaDaoService;
import com.yonglilian.service.BpipTableFieldService;
import com.yonglilian.service.BpipTableService;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zr.zrpower.common.db.DBEngine;

import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * 接口实现
 * 
 * @author nfzr
 *
 */
@Service
public class BpipTableServiceImpl implements BpipTableService {
	/** The UserServiceImpl Logger. */
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(BpipTableServiceImpl.class);
	/**
	 * 数据库类型，1：Oracle，2：MSsql，3：MySQL
	 */
	String dataBaseType = SysPreperty.getProperty().DataBaseType;// 数据库类型
	/** 数据库引擎. */
	private DBEngine dbEngine;
	private static int clients = 0;
	/** 用户操作数据层. */
	@Autowired
	private BpipTableMapper bpipTableMapper;
	@Autowired
	private BpipTableFieldService bpipTableFieldService;
	/** 数据库数据层. */
	@Autowired
	private BaseDbaDaoService baseDbaDaoService;

	/**
	 * 构造方法
	 */
	public BpipTableServiceImpl() {
		dbEngine = new DBEngine(SysPreperty.getProperty().MainDataSource, SysPreperty.getProperty().IsConvert);
		dbEngine.initialize();
		if (clients < 1) {
		}
		clients++;
	}

	@Override
	public BPIP_TABLE queryObject(String tableId) throws Exception {
		// TODO Auto-generated method stub
		BPIP_TABLE model = bpipTableMapper.queryObject(tableId);
		model.setTABLENAMEOLD(model.getTABLENAME());//表重命名前的名称
		model.setPRIMARYKEYOLD(model.getPRIMARYKEY());//表重命名前的主键
		model.setCHINESENAMEOLD(model.getCHINESENAME());//表汉字名称被重命名
		return model;
	}

	@Override
	public List<BPIP_TABLE> queryList(Map<String, Object> map) throws Exception {
		// TODO Auto-generated method stub
		return bpipTableMapper.queryList(map);
	}

	@Override
	public boolean save(BPIP_TABLE model) throws Exception {
		// 判断数据库是否存在改表
		if (bpipTableMapper.isTable(model.getTABLENAME().trim()) == 0) {
			//1、创建数据库表
			if(baseDbaDaoService.CreatTable(model)) {
				//查询数据库表最大编号
				String tableId= getMaxNo();
				model.setTABLEID(tableId);
				//2、插入bpip_field表记录
				if(model.getTABLETYPE().equals("1")) {
					//表示字典表，还要插入code,name,spell,isshow三个字段的表字典记录
					getCodeBpipField(model.getTABLEID());
					model.setPRIMARYKEY("CODE");
				}else {//其它类型的表类型
					getKeyBpipField(model);
				}
				
				//3、插入表记录
				bpipTableMapper.save(model);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean update(BPIP_TABLE model) throws Exception {
		String sql="";
		if(!model.getTABLENAME().equals(model.getTABLENAMEOLD())&&model.getTABLENAME().trim().length()>0) {//表英文是否被重命名
			//判断修改后的表是否已经存在
			if(bpipTableMapper.isTable(model.getTABLENAME().trim()) == 0) {
				sql="alter table "+model.getTABLENAMEOLD()+" rename "+model.getTABLENAME()+"";
				baseDbaDaoService.dbaSql(sql);
			}else {
				return false;
			}
		}
		if(!model.getCHINESENAME().equals(model.getCHINESENAMEOLD())&&model.getCHINESENAME().trim().length()>0) {//表汉字名称是否被重命名
			sql="alter table `"+model.getTABLENAME()+"` COMMENT='"+model.getCHINESENAME()+"'";
			baseDbaDaoService.dbaSql(sql);
		}
//		if(!model.getPRIMARYKEY().equals(model.getPRIMARYKEYOLD())&&model.getPRIMARYKEY()!=null&&model.getPRIMARYKEY().trim().length()>0) {//表主键是否被改变
//			//执行新的主键
//			baseDbaDaoService.AlterTableKey(model);
//		}
		bpipTableMapper.update(model);
		return true;
	}

	@Override
	public boolean delete(String tableId,String tableName) throws Exception {
		boolean reback=false;
		bpipTableMapper.dropTable(tableName);
		bpipTableMapper.delete(tableId);
		bpipTableMapper.deleteFields(tableId);
		reback=true;
		return reback;
	}
	
	/**
	 * 查询数据表bpip_table最大id
	 * @return
	 * @throws Exception
	 */
	public String getMaxNo() throws Exception {
		String maxNo=bpipTableMapper.getMaxNo();
		if(StringUtils.isBlank(maxNo)||maxNo=="null") {
			maxNo="0001";
		}else {
			maxNo="0000"+(Integer.valueOf(maxNo)+1);
			maxNo=maxNo.substring(maxNo.length()-4, maxNo.length());
		}
		return maxNo;
	}
	
	/**
	 * 插入创建表时其它表类型的主键字段bpip_field
	 * @param model
	 * @return
	 * @throws Exception 
	 */
	public void getKeyBpipField(BPIP_TABLE model) throws Exception {
		BPIP_FIELD field=new BPIP_FIELD();
		field.setTABLEID(model.getTABLEID());// 表编号
		field.setFIELDNAME(model.getPRIMARYKEY());// 字段名
		field.setFIELDTAG("1");// 特殊属性
		field.setCHINESENAME("主键key");// 中文名称
		field.setFIELDTYPE(model.getKEYTYPEID());// 字段类型ID
		field.setFIELDLENGTH(model.getKEYLEN());// 字段长度
		field.setISNULL("0");// 是否可空
		field.setISKEY("1");// 是否主键
		field.setTABLENAME(model.getTABLENAME());
		field.setFIELDTYPENAME(model.getKEYTYPE());
		bpipTableFieldService.save(field);
		field=null;
	}
	
	/**
	 * 执行字典表bpip_field数据记录插入
	 * @param tableId
	 * @return
	 * @throws Exception 
	 */
	public void getCodeBpipField(String tableId) throws Exception {
		BPIP_FIELD field=new BPIP_FIELD();
		//1、code主键
		field.setTABLEID(tableId);// 表编号
		field.setFIELDNAME("CODE");// 字段名
		field.setFIELDTAG("1");// 特殊属性
		field.setCHINESENAME("编号id");// 中文名称
		field.setFIELDTYPE("1");// 字段类型
		field.setFIELDLENGTH(6);// 字段长度
		field.setISNULL("0");// 是否可空
		field.setISKEY("1");// 是否主键
		
		bpipTableFieldService.save(field);
		
		//2、name字段
		field.setTABLEID(tableId);// 表编号
		field.setFIELDNAME("NAME");// 字段名
		field.setFIELDTAG("1");// 特殊属性
		field.setCHINESENAME("名称");// 中文名称
		field.setFIELDTYPE("1");// 字段类型
		field.setFIELDLENGTH(100);// 字段长度
		field.setISNULL("1");// 是否可空
		field.setISKEY("0");// 是否主键
		bpipTableFieldService.save(field);
		
		//3、spell字段
		field.setTABLEID(tableId);// 表编号
		field.setFIELDNAME("SPELL");// 字段名
		field.setFIELDTAG("1");// 特殊属性
		field.setCHINESENAME("拼音首字母");// 中文名称
		field.setFIELDTYPE("1");// 字段类型
		field.setFIELDLENGTH(50);// 字段长度
		field.setISNULL("1");// 是否可空
		field.setISKEY("0");// 是否主键
		bpipTableFieldService.save(field);
		
		//4、isshow
		field.setTABLEID(tableId);// 表编号
		field.setFIELDNAME("ISSHOW");// 字段名
		field.setFIELDTAG("1");// 特殊属性
		field.setCHINESENAME("是否禁用");// 中文名称
		field.setFIELDTYPE("1");// 字段类型
		field.setFIELDLENGTH(1);// 字段长度
		field.setISNULL("1");// 是否可空
		field.setISKEY("0");// 是否主键
		bpipTableFieldService.save(field);
		
		field=null;
	}

	@Override
	public void entity(String tableId) throws Exception {
		// TODO Auto-generated method stub
		String tableName = "";
		try {
	        // 得到表的名称即英文名
	        BPIP_TABLE model = bpipTableMapper.queryObject(tableId);
	        tableName = model.getTABLENAME();
	        String objectName = "";// Java驼峰命名法
	        String[] array = tableName.split("_");
	        for (String str : array) {
	        	str = str.toLowerCase();
	        	objectName += (str.substring(0, 1).toUpperCase() + str.substring(1));
	        }
	        
	        // 获取当前项目所在的磁盘目录(Windows/Linux)
	        String execPath = System.getProperty("user.dir") + "/mybatis-generator";
	        String disk = execPath.substring(0, 2);//项目所在磁盘名称
	        execPath = execPath.replaceAll("\\\\", "/");
	        
	        /** 使用dom4j修改MyBatis的配置文件mybatisConfig.xml */
	        // 创建File对象获取Document和根节点
	        SAXReader sax = new SAXReader();//创建一个SAXReader对象
	        // 根据指定的路径创建file对象
	        File xmlFile = new File(execPath + "/mybatisConfig.xml");
	        
	        // 获取document对象,如果文档无节点，则会抛出Exception提前结束
	        Document document = sax.read(xmlFile);
	        
	        // 获取根节点，context节点和table节点
	        Element root = document.getRootElement();
	        Element context = root.element("context");
	        Element table = context.element("table");
	        
	        // 获取此节点的tableName属性
	        Attribute tableAttr = table.attribute("tableName");
	        tableAttr.setValue(tableName);//更改此属性值为数据表名称
	        
	        // 获取此节点的domainObjectName属性
	        Attribute domainObjectAttr = table.attribute("domainObjectName");
	        domainObjectAttr.setValue(objectName);//更改此属性值为实体类名称
			
	        // 把改变的domcument对象保存到指定的xml文件中
	        operateXmlConfig(document, xmlFile);
	        
	        // 生成windows脚本generator_java.bat文件
	        File batfile = new File(execPath + "/generator_java.bat");
	        File shfile  = new File(execPath + "/generator_java.sh");
	        if (!batfile.exists()) {
	        	batfile.createNewFile();//如果bat脚本文件不存在则创建
	        }
	        if (!shfile.exists()) {
	        	shfile.createNewFile();//如果shell脚本文件不存在则创建
	        }
	        String osname = System.getProperty("os.name");
	        if (osname.toLowerCase().startsWith("win")) {// Windows系统
	        	FileWriter output = new FileWriter(batfile);
	            BufferedWriter bufWriter = new BufferedWriter(output);
	            bufWriter.write("@echo off");
	            bufWriter.newLine();
	            bufWriter.write(disk);
	            bufWriter.newLine();
	            bufWriter.write("cd " + execPath);
	            bufWriter.newLine();
	            bufWriter.write("java -jar mybatis-generator-core-1.3.5.jar -configfile ./mybatisConfig.xml -overwrite");
	            bufWriter.newLine();
	            bufWriter.write("exit");
	            bufWriter.close();
	            
	            // java调用generator_java.bat生成Model.java，Mapper.xml和Mapper.java
	            String locationCmd = execPath + "/generator_java.bat";
	            // java调用Windows的bat脚本并运行
	            callWinbat(locationCmd);
	            
	        } else {// Unix/Linux系统
	        	FileWriter output = new FileWriter(shfile);
	            BufferedWriter bufWriter = new BufferedWriter(output);
	            bufWriter.write("#!/bin/sh");
	            bufWriter.newLine();
	            bufWriter.write("cd " + execPath);
	            bufWriter.newLine();
	            bufWriter.write("java -jar mybatis-generator-core-1.3.5.jar -configfile ./mybatisConfig.xml -overwrite");
	            bufWriter.newLine();
	            bufWriter.close();
	            
	            String[] commands = new String[] {
            		"/system/bin/sh", "-c", 
            		"chmod -R 777 "+execPath+"/generator_java.sh"
	            };
				Process process = null;
				DataOutputStream outputStream = null;
				try {
					process = Runtime.getRuntime().exec("su");
					outputStream = new DataOutputStream(process.getOutputStream());
					int length = commands.length;
					for (int i = 0; i < length; i++) {
						outputStream.writeBytes(commands[i] + "\n");
					}
					outputStream.writeBytes("exit\n");
					outputStream.flush();
					process.waitFor();// 调用shell脚本执行
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						if (outputStream != null) {
							outputStream.close();
						}
						process.destroy();
					} catch (Exception e1) {
						e1.printStackTrace();
					}
		        }
	        }
		} catch (Exception ex) {
			LOGGER.error("TableServiceImpl.createEntity Exception:\n", ex);
		}
	}
	/**
	 * 把改变的domcument对象保存到指定的xml文件中
	 * @param document
	 * @param xmlFile
	 * @return
	 * @throws IOException
	 */
	private void operateXmlConfig(Document document, File xmlFile) throws IOException {
		Writer osWrite = new OutputStreamWriter(new FileOutputStream(xmlFile));//创建输出流
		OutputFormat format = OutputFormat.createPrettyPrint();//获取输出的指定格式
		//设置编码，确保解析的xml为UTF-8格式
		format.setEncoding("UTF-8");
		//XMLWriter指定输出文件以及格式
		XMLWriter writer = new XMLWriter(osWrite, format);
		//把document写入xmlFile指定的文件[可以为被解析的文件或者新创建的文件]
		writer.write(document);
		writer.close();
	}

	/**
	 * Java调用Windows的bat脚本并运行
	 * @param locationCmd
	 * @return
	 * @throws Exception
	 */
	private boolean callWinbat(String locationCmd) throws Exception {
		boolean result = false;
		try {
			Process process = Runtime.getRuntime().exec(locationCmd);
			InputStream input = process.getInputStream();
	        int c;
	        while ((c = input.read()) != -1) {
	              System.out.print((char)c);
	        }
	        input.close();
	        try {
	        	process.waitFor();
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
	        System.out.println(">>>>>>>>>>>>Java callWinbat Done.");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
	}

	@Override
	public List<BPIP_TABLE> selectList(Map<String, Object> map) throws Exception {
		return bpipTableMapper.selectList(map);
	}

}