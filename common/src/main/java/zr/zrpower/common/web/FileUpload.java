package zr.zrpower.common.web;

import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;

/**
 * 文件上传工具类
 * @author lwk
 *
 */
public class FileUpload {
	private long size;// 文件大小
	private Map<String, Object> rtFields;// 文件属性
	private String objectPath = "C:/";// 文件存放路径
	protected String TEMPLAET;// 上传文件名称

	/**
	 * 文件上传工具类-构造方法
	 */
	public FileUpload() {
		size = 10 * 1024 * 1024;// 默认文件大小为10MB
		rtFields = new HashMap<String, Object>();
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public Map<String, Object> getRtFields() {
		return rtFields;
	}

	public void setRtFields(Map<String, Object> rtFields) {
		this.rtFields = rtFields;
	}

	public String getObjectPath() {
		return objectPath;
	}

	public void setObjectPath(String objectPath) {
		this.objectPath = objectPath;
	}

	public String getTEMPLAET() {
		return TEMPLAET;
	}

	public Object getFieldValue(String fieldName) {
		if (rtFields == null || fieldName == null) {
			return null;
		} else {
			return rtFields.get(fieldName);
		}
	}

	/**
	 * 设置上传文件
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public boolean setSourceFile(HttpServletRequest request) throws IOException {
		boolean retval = false;// true：上传文件成功，false：上传文件失败
		Enumeration<String> enums = request.getParameterNames();
		while (enums.hasMoreElements()) {
			String paraName = (String) enums.nextElement();
			String paraVal = request.getParameter(paraName);
			rtFields.put(paraName, paraVal);
		}
		try {
			// 转换request，解析出request中的文件
	        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
	        MultiValueMap<String, MultipartFile> multiFileMap = multipartRequest.getMultiFileMap();
	        
	        List<MultipartFile> uploadFileList = new LinkedList<MultipartFile>();
            for (Entry<String, List<MultipartFile>> entry : multiFileMap.entrySet()) {
            	uploadFileList = entry.getValue();
            }
            for (MultipartFile uploadFile : uploadFileList) {
            	// 获取文件名全称，包含文件后缀名
    			String originFileName = uploadFile.getOriginalFilename();
    			if (uploadFile.getSize() > size) {
    				retval = false;//上传文件过大，请重新上传！
    				continue;
    			}
    			this.TEMPLAET = originFileName;
                retval = true;
                if (uploadFile.getSize() > 0) {
                	rtFields.put("PHOTO", uploadFile.getBytes());
                }
            }
		} catch(Exception ex) {
			ex.printStackTrace();
			retval = false;
		}
		return retval;
	
	}

	/**
	 * Spring支持的文件上传接口，支持多文件上传
	 * @param request		文件上传请求对象
	 * @return
	 * @throws IOException
	 */
	public boolean uploadFile(HttpServletRequest request) throws IOException {
		boolean retval = false;// true：上传文件成功，false：上传文件失败
		// 上传文件的保存路径，可以根据业务需求自行更改
		String rootPath = objectPath + "/";
		rootPath = rootPath.replaceAll("\\\\", "/");
		File fileDir = new File(rootPath);
		if (!fileDir.exists()) {// 如果文件夹不存在，则创建
			fileDir.mkdirs();
		}
		Enumeration<String> enums = request.getParameterNames();
		while (enums.hasMoreElements()) {
			String paraName = (String) enums.nextElement();
			String paraVal = request.getParameter(paraName);
			rtFields.put(paraName, paraVal);
		}
		try {
			// 转换request，解析出request中的文件
	        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
	        MultiValueMap<String, MultipartFile> multiFileMap = multipartRequest.getMultiFileMap();
	        
	        List<MultipartFile> uploadFileList = new LinkedList<MultipartFile>();
            for (Entry<String, List<MultipartFile>> entry : multiFileMap.entrySet()) {
            	uploadFileList = entry.getValue();
            }
            for (MultipartFile uploadFile : uploadFileList) {
            	// 获取文件名全称，包含文件后缀名
    			String originFileName = uploadFile.getOriginalFilename();
    			File destFile = new File(rootPath + originFileName);
    			if (!destFile.exists()) {
    				destFile.mkdirs();
    			}
    			if (uploadFile.getSize() > size) {
    				retval = false;//上传文件过大，请重新上传！
    				continue;
    			}
    			uploadFile.transferTo(destFile);// 保存文件
    			this.TEMPLAET = originFileName;
                retval = true;
            }
		} catch(Exception ex) {
			ex.printStackTrace();
			retval = false;
		}
		return retval;
	}
}