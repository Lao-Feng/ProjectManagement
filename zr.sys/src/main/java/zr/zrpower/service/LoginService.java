package zr.zrpower.service;

import zr.zrpower.common.util.FunctionMessage;
import zr.zrpower.model.BPIP_USER;

import java.util.List;

/**
 * 用户操作服务层
 * 
 * @author lwk
 *
 */
public interface LoginService {
	/**
	 * 保存添加新用户
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public String addUser(BPIP_USER user) throws Exception;

	/**
	 * 删除用户
	 * @param userID
	 * @return
	 * @throws Exception
	 */
	public boolean deleteUser(String userID) throws Exception;

	/**
	 * 编辑用户
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public FunctionMessage editUser(BPIP_USER user) throws Exception;

	/**
	 * 获取用户的详细信息（除密码以外）
	 * @param userID
	 * @return
	 * @throws Exception
	 */
	public BPIP_USER getUserInfo(String userID) throws Exception;

	/**
	 * 获取用户信息列表
	 * @param DepID
	 * @return
	 * @throws Exception
	 */
	public String[][] getUserArrary(String DepID) throws Exception;

	/**
	 * 获取用户列表
	 * @param unitID 单位编号
	 * @return
	 * @throws Exception
	 */
	public BPIP_USER[] getUserList(String unitID) throws Exception;

	/**
	 * 获取某部门的用户列表
	 * @param unitID 单位编号
	 * @return
	 * @throws Exception
	 */
	public BPIP_USER[] getDeptUserList(String unitID) throws Exception;

	/**
	 * 用户登录
	 * @param loginID
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public FunctionMessage login(String loginID, String password)  throws Exception;

	/**
	 * 用户停用
	 * @param userID
	 * @return
	 * @throws Exception
	 */
	public boolean stopUser(String userID) throws Exception;

	/**
	 * 解除用户停用
	 * @param userID
	 * @return
	 * @throws Exception
	 */
	public boolean unStopUser(String userID) throws Exception;

	/**
	 * 重设密码
	 * @param userID
	 * @param pwd
	 * @return
	 * @throws Exception
	 */
	public boolean resetPassword(String userID, String pwd) throws Exception;

	/**
	 * 重设密码
	 * @param userID
	 * @param pwd
	 * @return
	 * @throws Exception
	 */
    public boolean resetPassword1(String userID, String pwd) throws Exception;

    /**
     * 获取用户登录名
     * @param userID
     * @return
     * @throws Exception
     */
    public String getUserLoginID(String userID) throws Exception;

    /**
     * 获取用户真实姓名
     * @param userID
     * @return
     * @throws Exception
     */
    public String getUserName(String userID) throws Exception;

    /**
     * 获取用户照片
     * @param userID
     * @return
     * @throws Exception
     */
    public byte[] getUserPhoto(String userID) throws Exception;

    /**
     * 获取用户自定义头像
     * @param userID
     * @return
     * @throws Exception
     */
    public byte[] getUserSelfFace(String userID) throws Exception;

    /**
     * 获取用户签名
     * @param userID
     * @return
     * @throws Exception
     */
    public byte[] getUserIdiograph(String userID) throws Exception;

    /**
     * 获取用户所在单位编号
     * @param userID
     * @return
     * @throws Exception
     */
    public String getUserUNITID(String userID) throws Exception;

    /**
     * 获取用户所在单位名称
     * @param userID
     * @return
     * @throws Exception
     */
    public String getUserUNITName(String userID) throws Exception;

    /**
     * 检查用户口令
     * @param userID
     * @param pwd
     * @return
     * @throws Exception
     */
    public boolean checkPw(String userID, String pwd) throws Exception;

    /**
     * 获取用户类型图片
     * @param uType  用户类型代码
     * @param path   图片路径
     * @return String	详细用户类型图片路径
     */
    public String getUserTypeImg(String uType, String path) throws Exception;

    /**
     * 获取用户状态
     * @param uType     用户状态代码
     * @return String	用户状态名称
     */
    public String getUserState(String uType) throws Exception;

    /**
     * 更改应用配置
     * @param userID	  用户ID
     * @param pageSize   表单每页显示的记录数
     * @param winType    界面样式
     * @return boolean   更改是否成功
     */
    public boolean changeAppCon(String userID, int pageSize, String winType) throws Exception;

    /**
     * 功能：根据单位编号生成单位下的用户列表
     * @param strUnitID 单位编号
     * @param strListName 列表名称
     * @param strUserImg  用户图片文件夹
     * @param ico 图标
     * @param strType 类型
     * @return returnValue 返回生成的用户列表
     */
    public String getShowUnitUserList(String strUnitID, String strListName, 
    		 String strUserImg, String ico, String strType) throws Exception;

    /**
     * 获取所有已经停用的用户
     * @param  UnitID  行政区划
     * @return BPIP_USER[]
	 */
	public BPIP_USER[] getRecoveryUserList() throws Exception;

	/**
     * 彻底删除用户
     * @param strMenuNo
     * @return 返回
     */
    public boolean delAllUser(String strMenuNo) throws Exception;

    /**
     * 功能或作用：还原用户
     * @param strMenuNo
     * @return 返回
     */
    public boolean restoreUser(String strMenuNo) throws Exception;

    /**
     * 分析规则字符串，生成数组
     * @param strItems 字符串
     * @param strItemMark 标识符
     * @return 返回数组
     */
    public List<String> getArrayList(String strItems, String strItemMark) throws Exception;

    /**
     * 获取角色列表
     * @param unitID 角色区域编码
     * @param listName  列表名称
     * @param strUserImg 用户图片文件夹
     * @param ico 图标
     * @return returnValue 返回生成的角色列表
     */
    public String getShowUserRoleList(String unitID, String listName, 
    		 String strUserImg, String ico) throws Exception;

    /**
     * 更新当前用户的在线时间
     * @param userID
     * @throws Exception
     */
    public void upUserOnlineDate(String userID) throws Exception;

    /**
     * 得到服务器的总人数
     * @return int
	 */
	public int getSysSaverPNumber() throws Exception;

	/**
     * 得到登录服务器的人员列表
     * @param pageSize 每页显示的记录数
     * @param page 第几页
     */
    public BPIP_USER[] getLoginUserList(int pageSize, int page) throws Exception;

    /**
     * 创建页面导航链接
     * @param pageSize
     * @param page
     * @return
     * @throws Exception
     */
    public String createPage(int pageSize,int page) throws Exception;

}
