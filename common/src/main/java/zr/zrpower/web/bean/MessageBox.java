package zr.zrpower.web.bean;

import zr.zrpower.common.util.SysPreperty;

import javax.servlet.http.HttpServlet;
import java.io.*;

public class MessageBox extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected PrintWriter out = null;
	protected boolean isOk = false;
	protected String Msg = "非法调用，操作不被接受，你的操作已被记录！";
	protected String returnPath = "javascript:history.go(-1)";

	/**
	 * 显示消息对话框
	 * 传入参数：out 输出源
	 * isOk 成功消息还是失败消息
	 * Msg 消息内容
	 * returnPath 返回路径
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void getBox() throws FileNotFoundException, IOException {
		BufferedReader br = null;

		String strPath = SysPreperty.getProperty().AppUrl + "images/blueimg";
		String imgPath = SysPreperty.getProperty().AppUrl + "images/blueimg/Msg_Error.gif";

		if (isOk) {
			imgPath = SysPreperty.getProperty().AppUrl + "images/blueimg/Msg_OK.gif";
		}
		String strServerPath = getServletContext().getRealPath("/");
		if (strServerPath == null) {
			strServerPath = SysPreperty.getProperty().WebPath;
		}
		br = new BufferedReader(new InputStreamReader(new FileInputStream(strServerPath + "/messagebox.htm")));
		String data = null;
		while ((data = br.readLine()) != null) {
			data = data.replaceAll("BPIP_PATH", strPath);
			data = data.replaceAll("BPIP_MESSAGE", Msg);
			data = data.replaceAll("BPIP_IMG", imgPath);
			data = data.replaceAll("BPIP_RETURN", returnPath);
			out.println(data);
		}
		try {
			br.close();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}