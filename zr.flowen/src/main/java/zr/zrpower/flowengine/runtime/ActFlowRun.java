package zr.zrpower.flowengine.runtime;

import zr.zrpower.common.db.DBEntity;
import zr.zrpower.common.db.DBType;

/**
 * <p>
 * 流程引擎流程运行类
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright (c)
 * </p>
 */

public class ActFlowRun extends DBEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 构造函数
	 */
	public ActFlowRun() {
		super();
		initialize();
	}

	/**
	 * 初始化函数
	 */
	protected void initialize() {
		this.dbrow.setTableName("FLOWRUN");
		this.dbrow.addColumn("OptCmd", null, DBType.STRING); // 记录属性按钮命令值
		this.dbrow.addColumn("Identification", null, DBType.STRING); // 流程标识
		this.dbrow.addColumn("OptCmd_Name", null, DBType.STRING); // 记录属性按钮命令值中文
		this.dbrow.addColumn("Workflow_No", null, DBType.STRING); // 流程编号
		this.dbrow.addColumn("Node_No_S", null, DBType.STRING); // 当前步骤编号
		this.dbrow.addColumn("M_Node_No_S_E", null, DBType.STRING); // 下一分支步骤编号
		this.dbrow.addColumn("S_Node_No_S_E", null, DBType.STRING); // 上一分支步骤编号
		this.dbrow.addColumn("UserNo", null, DBType.STRING); // 当前用户编号
		this.dbrow.addColumn("Execute_No", null, DBType.STRING); // 流程流转编号
		this.dbrow.addColumn("Do_User_Nos", null, DBType.STRING); // 选择的处理用户编号
		this.dbrow.addColumn("FormID", null, DBType.STRING); // 业务的ID值
		this.dbrow.addColumn("TitleName", null, DBType.STRING); // 业务的标题名称
		this.dbrow.addColumn("OtherID", null, DBType.STRING); // 关联字段的ID值
		this.dbrow.addColumn("Path", null, DBType.STRING); // 流程调用文件的路径
		this.dbrow.addColumn("DoIdea", null, DBType.STRING); // 处理意见
		this.dbrow.addColumn("Unit", null, DBType.STRING); // 单位编号
		this.dbrow.addColumn("ParentID", null, DBType.STRING); // 父流程ID值或父表单记录值
		this.dbrow.addColumn("ParentID1", null, DBType.STRING); // 父流程ID1值或父表单记录值
		this.dbrow.addColumn("RID", null, DBType.STRING); // 关联流程的流转ID
		this.dbrow.addColumn("MsgType", null, DBType.STRING); // 短信提示类别
		this.dbrow.addColumn("IsFlow", null, DBType.STRING); // 关联的可新建的流程
		this.dbrow.addColumn("RECORDID", null, DBType.STRING); // 附件ID
		this.dbrow.addColumn("ISBRANCH", null, DBType.STRING); // 多路分支是否表现在按钮上

	}

	// 记录属性按钮命令值
	public void setOptCmd(String OptCmd) {
		this.dbrow.Column("OptCmd").setValue(OptCmd);
	}

	public String getOptCmd() {
		return this.dbrow.Column("OptCmd").getString();
	}

	// 流程标识
	public void setIdentification(String Identification) {
		this.dbrow.Column("Identification").setValue(Identification);
	}

	public String getIdentification() {
		return this.dbrow.Column("Identification").getString();
	}

	// 记录属性按钮命令值中文
	public void setOptCmd_Name(String OptCmd_Name) {
		this.dbrow.Column("OptCmd_Name").setValue(OptCmd_Name);
	}

	public String getOptCmd_Name() {
		return this.dbrow.Column("OptCmd_Name").getString();
	}

	// 流程编号
	public void setWorkflow_No(String Workflow_No) {
		this.dbrow.Column("Workflow_No").setValue(Workflow_No);
	}

	public String getWorkflow_No() {
		return this.dbrow.Column("Workflow_No").getString();
	}

	// 当前步骤编号
	public void setNode_No_S(String Node_No_S) {
		this.dbrow.Column("Node_No_S").setValue(Node_No_S);
	}

	public String getNode_No_S() {
		return this.dbrow.Column("Node_No_S").getString();
	}

	// 下一分支步骤编号
	public void setM_Node_No_S_E(String M_Node_No_S_E) {
		this.dbrow.Column("M_Node_No_S_E").setValue(M_Node_No_S_E);
	}

	public String getM_Node_No_S_E() {
		return this.dbrow.Column("M_Node_No_S_E").getString();
	}

	// 上一分支步骤编号
	public void setS_Node_No_S_E(String S_Node_No_S_E) {
		this.dbrow.Column("S_Node_No_S_E").setValue(S_Node_No_S_E);
	}

	public String getS_Node_No_S_E() {
		return this.dbrow.Column("S_Node_No_S_E").getString();
	}

	// 当前用户编号
	public void setUserNo(String UserNo) {
		this.dbrow.Column("UserNo").setValue(UserNo);
	}

	public String getUserNo() {
		return this.dbrow.Column("UserNo").getString();
	}

	// 流程流转编号
	public void setExecute_No(String Execute_No) {
		this.dbrow.Column("Execute_No").setValue(Execute_No);
	}

	public String getExecute_No() {
		return this.dbrow.Column("Execute_No").getString();
	}

	// 选择的处理用户编号
	public void setDo_User_Nos(String Do_User_Nos) {
		this.dbrow.Column("Do_User_Nos").setValue(Do_User_Nos);
	}

	public String getDo_User_Nos() {
		return this.dbrow.Column("Do_User_Nos").getString();
	}

	// 业务的ID值
	public void setFormID(String FormID) {
		this.dbrow.Column("FormID").setValue(FormID);
	}

	public String getFormID() {
		return this.dbrow.Column("FormID").getString();
	}

	// 业务的标题名称
	public void setTitleName(String TitleName) {
		this.dbrow.Column("TitleName").setValue(TitleName);
	}

	public String getTitleName() {
		return this.dbrow.Column("TitleName").getString();
	}

	// 关联字段的ID值
	public void setOtherID(String OtherID) {
		this.dbrow.Column("OtherID").setValue(OtherID);
	}

	public String getOtherID() {
		return this.dbrow.Column("OtherID").getString();
	}

	// 流程调用文件的路径
	public void setPath(String Path) {
		this.dbrow.Column("Path").setValue(Path);
	}

	public String getPath() {
		return this.dbrow.Column("Path").getString();
	}

	// 处理意见
	public void setDoIdea(String DoIdea) {
		this.dbrow.Column("DoIdea").setValue(DoIdea);
	}

	public String getDoIdea() {
		return this.dbrow.Column("DoIdea").getString();
	}

	// 单位编号
	public void setUnit(String Unit) {
		this.dbrow.Column("Unit").setValue(Unit);
	}

	public String getUnit() {
		return this.dbrow.Column("Unit").getString();
	}

	// 父流程ID值或父表单记录值
	public void setParentID(String ParentID) {
		this.dbrow.Column("ParentID").setValue(ParentID);
	}

	public String getParentID() {
		return this.dbrow.Column("ParentID").getString();
	}

	// 父流程ID1值或父表单记录值
	public void setParentID1(String ParentID1) {
		this.dbrow.Column("ParentID1").setValue(ParentID1);
	}

	public String getParentID1() {
		return this.dbrow.Column("ParentID1").getString();
	}

	// 关联流程的流转ID
	public void setRID(String RID) {
		this.dbrow.Column("RID").setValue(RID);
	}

	public String getRID() {
		return this.dbrow.Column("RID").getString();
	}

	// 短信提示类别
	public void setMsgType(String MsgType) {
		this.dbrow.Column("MsgType").setValue(MsgType);
	}

	public String getMsgType() {
		return this.dbrow.Column("MsgType").getString();
	}

	// 关联的可新建的流程
	public void setIsFlow(String IsFlow) {
		this.dbrow.Column("IsFlow").setValue(IsFlow);
	}

	public String getIsFlow() {
		return this.dbrow.Column("IsFlow").getString();
	}

	// 附件ID
	public void setRECORDID(String RECORDID) {
		this.dbrow.Column("RECORDID").setValue(RECORDID);
	}

	public String getRECORDID() {
		return this.dbrow.Column("RECORDID").getString();
	}

	// 多路分支是否表现在按钮上
	public void setISBRANCH(String ISBRANCH) {
		this.dbrow.Column("ISBRANCH").setValue(ISBRANCH);
	}

	public String getISBRANCH() {
		return this.dbrow.Column("ISBRANCH").getString();
	}

	// ----------------------------------
	// 初始化流程运转参数
	// OptCmd; //记录属性按钮命令值
	// Identification; //流程标识
	// OptCmd_Name; //记录属性按钮命令值中文
	// Workflow_No; //流程编号
	// Node_No_S; //当前步骤编号
	// M_Node_No_S_E; //下一分支步骤编号
	// S_Node_No_S_E; //上一分支步骤编号
	// UserNo; //当前用户编号
	// Execute_No; //流程流转编号
	// Do_User_Nos; //选择的处理用户编号
	// FormID; //业务的ID值
	// TitleName; //业务的标题名称
	// OtherID; //关联字段的ID值
	// Path; //流程调用文件的路径
	// DoIdea; //处理意见
	// Unit; //单位编号
	// ParentID; //父流程ID值或父表单记录值
	// ParentID1; //父流程ID1值或父表单记录值
	// RID; //关联流程的流转ID
	// MsgType //短信提示类别
	// user; //用户Session对象
	// ISBRANCH //多路分支是否表现在按钮上
	public void FlowInit(String strOptCmd, String strIdentification, String strOptCmd_Name, String strWorkflow_No,
			String strNode_No_S, String strM_Node_No_S_E, String strS_Node_No_S_E, String strUserNo,
			String strExecute_No, String strDo_User_Nos, String strFormID, String strTitleName, String strOtherID,
			String strPath, String strDoIdea, String strUnit, String strParentID, String strParentID1, String strRID,
			String strMsgType, String strRECORDID, String strISBRANCH) {
		// 初始化为空-----------
		FlowInitNull();
		if (strOptCmd != null) {
			setOptCmd(strOptCmd);
		}
		if (strIdentification != null) {
			setIdentification(strIdentification);
		}
		if (strOptCmd_Name != null) {
			setOptCmd_Name(strOptCmd_Name);
		}
		if (strWorkflow_No != null) {
			setWorkflow_No(strWorkflow_No);
		}
		if (strNode_No_S != null) {
			setNode_No_S(strNode_No_S);
		}
		if (strM_Node_No_S_E != null) {
			setM_Node_No_S_E(strM_Node_No_S_E);
		}
		if (strS_Node_No_S_E != null) {
			setS_Node_No_S_E(strS_Node_No_S_E);
		}
		if (strUserNo != null) {
			setUserNo(strUserNo);
		}
		if (strExecute_No != null) {
			setExecute_No(strExecute_No);
		}
		if (strDo_User_Nos != null) {
			setDo_User_Nos(strDo_User_Nos);
		}
		if (strFormID != null) {
			setFormID(strFormID);
		}
		if (strTitleName != null) {
			setTitleName(strTitleName);
		}
		if (strOtherID != null) {
			setOtherID(strOtherID);
		}
		if (strPath != null) {
			setPath(strPath);
		}
		if (strDoIdea != null) {
			setDoIdea(strDoIdea);
		}
		if (strUnit != null) {
			setUnit(strUnit);
		}
		if (strParentID != null) {
			setParentID(strParentID);
		}
		if (strParentID1 != null) {
			setParentID1(strParentID1);
		}
		if (strRID != null) {
			setRID(strRID);
		}
		if (strMsgType != null) {
			setMsgType(strMsgType);
		}
		if (strRECORDID != null) {
			setRECORDID(strRECORDID);
		}
		if (strISBRANCH != null) {
			setISBRANCH(strISBRANCH);
		}
	}

	// 初始化流程运转参数(退回时)
	public void FlowInitUntread(String strS_Node_No_S_E, String strM_Node_No_S_E) {
		// 退回时上一步和下一步互换
		if (strS_Node_No_S_E == null) {
			setM_Node_No_S_E("");
		} else {
			setM_Node_No_S_E(strS_Node_No_S_E);
		}
		if (strM_Node_No_S_E == null) {
			setS_Node_No_S_E("");
		} else {
			setS_Node_No_S_E(strM_Node_No_S_E);
		}
	}

	// 初始化流程运转默认参数
	public void FlowInitDefault(String strExecute_No, String strIdentification, String strOtherID, String strParentID,
			String strParentID1, String strRID, String strRECORDID, String strISBRANCH) {
		// 初始化为空-----------
		FlowInitNull();
		if (strExecute_No != null) {
			setExecute_No(strExecute_No);
		}
		if (strIdentification != null) {
			setIdentification(strIdentification);
		}
		if (strOtherID != null) {
			setOtherID(strOtherID);
		}
		if (strParentID != null) {
			setParentID(strParentID);
		}
		if (strParentID1 != null) {
			setParentID1(strParentID1);
		}
		if (strRID != null) {
			setRID(strRID);
		}
		if (strRECORDID != null) {
			setRECORDID(strRECORDID);
		}
		if (strISBRANCH != null) {
			setISBRANCH(strISBRANCH);
		}
	}

	// 初始化流程运转默认参数
	public void FlowInitDefault(String strIdentification, String strOtherID, String strParentID, String strParentID1,
			String strRID, String strRECORDID, String strISBRANCH) {
		// 初始化为空-----------
		FlowInitNull();
		if (strIdentification != null) {
			setIdentification(strIdentification);
		}
		if (strOtherID != null) {
			setOtherID(strOtherID);
		}
		if (strParentID != null) {
			setParentID(strParentID);
		}
		if (strParentID1 != null) {
			setParentID1(strParentID1);
		}
		if (strRID != null) {
			setRID(strRID);
		}
		if (strRECORDID != null) {
			setRECORDID(strRECORDID);
		}
		if (strISBRANCH != null) {
			setISBRANCH(strISBRANCH);
		}
	}

	// 初始化流程运转参数为空
	private void FlowInitNull() {
		setOptCmd("");
		setIdentification("");
		setOptCmd_Name("");
		setWorkflow_No("");
		setNode_No_S("");
		setM_Node_No_S_E("");
		setS_Node_No_S_E("");
		setUserNo("");
		setExecute_No("");
		setDo_User_Nos("");
		setFormID("");
		setTitleName("");
		setOtherID("");
		setPath("");
		setDoIdea("");
		setUnit("");
		setParentID("");
		setParentID1("");
		setRID("");
		setMsgType("");
		setIsFlow("");
		setRECORDID("");
		setISBRANCH("");
	}
	// ----------------------------------
}