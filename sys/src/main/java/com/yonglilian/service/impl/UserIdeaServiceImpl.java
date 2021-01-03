package com.yonglilian.service.impl;

import com.yonglilian.dao.mapper.UserIdeaMapper;
import com.yonglilian.model.BPIP_USER_IDEA;
import com.yonglilian.service.UserIdeaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zr.zrpower.common.util.FunctionMessage;
import zr.zrpower.common.util.ReflectionUtil;
import zr.zrpower.common.util.StringUtils;
import zr.zrpower.dao.UserMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 意见设置服务层实现
 * @author lwk
 *
 */
@Service
public class UserIdeaServiceImpl implements UserIdeaService {
	/** The UserIdeaServiceImpl Logger. */
	private static final Logger LOGGER = LoggerFactory.getLogger(UserIdeaServiceImpl.class);
	/** 用户操作数据层. */
	@Autowired
	private UserMapper userMapper;
	/** 意见设置数据层. */
	@Autowired
	private UserIdeaMapper userIdeaMapper;

	@Override
	public FunctionMessage addUserIdea(BPIP_USER_IDEA userIdea) throws Exception {
		  String strSql= "";
		  FunctionMessage fm = new FunctionMessage(1);
		  try {
		    strSql = "Select ID From BPIP_USER_IDEA Where ID='"+userIdea.getID()+ "'";
		    String retID = userMapper.selectStrExecSQL(strSql);
		    if (StringUtils.isNotBlank(retID)) {
		      fm.setResult(false);
		      fm.setMessage("意见【" + userIdea.getID() + "】已经存在");
		    } else {
		      String strMaxNo = getMaxFieldNo("BPIP_USER_IDEA", "ID", 8);
		      userIdea.setID(strMaxNo); //设置ID
		      Map<String, Object> paraMap = new HashMap<String, Object>();
		      paraMap.put("ID", userIdea.getID());
		      paraMap.put("USER_NO", userIdea.getUSER_NO());
		      paraMap.put("CONTENT", userIdea.getCONTENT());
		      
		      Integer retVal = userIdeaMapper.addUserIdea(paraMap);
		      if (retVal != null && retVal > 0) {
		    	  fm.setMessage("意见录入成功");
		    	  fm.setResult(true);
		      }
		    }
		  } catch (Exception ex) {
			  LOGGER.error("UserIdeaServiceImpl.addUserIdea", ex);
			  fm.setResult(false);
			  fm.setMessage("调用方法AddUserIdea出现异常" + ex.toString());
		  }
		  return fm;
	}

	@Override
	public FunctionMessage delUserIdea(String ID) throws Exception {
	     FunctionMessage fm = new FunctionMessage(1);
	     try {
	    	 String execSQL = "Delete From BPIP_USER_IDEA Where ID=" + ID;
	    	 Integer retVal = userMapper.deleteExecSQL(execSQL);
	    	 if (retVal != null && retVal > 0) {
	    		 fm.setResult(true);
	    	 } else {
	    		 fm.setMessage("删除不成功");
	    		 fm.setResult(false);
	    	 }
	     } catch (Exception ex) {
			  LOGGER.error("UserIdeaServiceImpl.delUserIdea", ex);
	    	 fm.setResult(false);
	    	 fm.setMessage("调用方法delUserIdea出现异常" + ex.toString());
	     }
	     return fm;
	}

	@Override
	public FunctionMessage editUserIdea(BPIP_USER_IDEA userIdea) throws Exception {
	    FunctionMessage fm = new FunctionMessage(1);
	    try {
	    	Map<String, Object> paraMap = new HashMap<String, Object>();
		    paraMap.put("ID", userIdea.getID());
		    paraMap.put("USER_NO", userIdea.getUSER_NO());
		    paraMap.put("CONTENT", userIdea.getCONTENT());
		    
		    Integer retVal = userIdeaMapper.editUserIdea(paraMap);
		    if (retVal != null && retVal > 0) {
		    	fm.setResult(true);
		    	fm.setMessage("意见修改成功");
		    } else {
		    	fm.setResult(false);
		    	fm.setMessage("意见【" + userIdea.getCONTENT() + "】不存在");
		    }
	    } catch (Exception ex) {
	    	LOGGER.error("UserIdeaServiceImpl.editUserIdea", ex);
			fm.setResult(false);
			fm.setMessage("调用方法EditUserIdea出现异常" + ex.toString());
	    }
	    return fm;
	}

	@Override
	public BPIP_USER_IDEA[] getIdeaList(String strWhere) throws Exception {
	      BPIP_USER_IDEA ideas[] = null;
	      String strSql = null;
	      try {
	    	  strSql = "Select ID,USER_NO,CONTENT From BPIP_USER_IDEA ";
	    	  if (StringUtils.isNotBlank(strWhere)) {
	    		  strSql+="  where "+ strWhere;
	    	  }
	    	  List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(strSql);
	    	  int length = retlist != null ? retlist.size() : 0;
	    	  if (length > 0) {
	    		  return null;
	    	  } else {
	    		  ideas = new BPIP_USER_IDEA[length];
	    		  for (int i = 0; i < length; i++) {
	    			  Map<String, Object> retmap = retlist.get(i);
	    			  ideas[i] = (BPIP_USER_IDEA) ReflectionUtil.convertMapToBean(retmap, BPIP_USER_IDEA.class);
	    		  }
	    	  }
	      } catch (Exception ex) {
	    	  LOGGER.error("UserIdeaServiceImpl.getIdeaList", ex);
	      }
	      return ideas;
	}

	@Override
	public BPIP_USER_IDEA[] getIdeaList1(String userID) throws Exception {
		BPIP_USER_IDEA ideas[] = null;
		String strSql= "";
		try {
          strSql = "Select ID,USER_NO,CONTENT From BPIP_USER_IDEA where USER_NO='"+userID+"'";
          List<Map<String, Object>> retlist = userMapper.selectListMapExecSQL(strSql);
    	  int length = retlist != null ? retlist.size() : 0;
          if (length == 0) {
            return null;
          } else {
        	ideas = new  BPIP_USER_IDEA[length];
            for (int i = 0; i < length; i++) {
  			  Map<String, Object> retmap = retlist.get(i);
  			  ideas[i] = (BPIP_USER_IDEA) ReflectionUtil.convertMapToBean(retmap, BPIP_USER_IDEA.class);
            }
          }
        } catch (Exception ex) {
        	LOGGER.error("UserIdeaServiceImpl.getIdeaList1", ex);
        }
        return ideas;
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
      String strSQL= "";
      int LenMaxNo = 0;
      
      strSQL = "SELECT MAX("+FieldName+") AS MaxNo FROM "+TableName;
      try {
    	  String retMaxNo = userMapper.selectStrExecSQL(strSQL);
    	  if (retMaxNo == null) {
    		  retMaxNo = "";
    	  }
		  MaxNo = retMaxNo;// 最大编号
		  if (MaxNo.length() > 0) {
			  MaxNo = String.valueOf(Integer.parseInt(MaxNo) + 1);
			  LenMaxNo = MaxNo.length();
			  MaxNo = "0000000000000000000000000" + MaxNo;
		  } else {
			  MaxNo = "00000000000000000000000001";
			  LenMaxNo = 1;
		  }
    	  MaxNo = MaxNo.substring(25 - FieldLen + LenMaxNo);
      } catch (Exception ex) {
    	  ex.printStackTrace();
      }
      return MaxNo;
    }
}