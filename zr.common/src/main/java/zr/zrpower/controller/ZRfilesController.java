package zr.zrpower.controller;

import com.alibaba.fastjson.JSON;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import zr.zrpower.common.util.R;
import zr.zrpower.common.util.SerialNum;
import zr.zrpower.common.util.SysPreperty;
import zr.zrpower.model.FileHelper;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;

/**
 * 文件上传、下载控制
 * 
 * @author nfzr
 *
 */
@RestController
@RequestMapping("zrfile")
public class ZRfilesController {

	/***
	 * 文件上传 客户端可自定义文件上传路径下的文件夹，参数：folder，list:boolean,
	 * 
	 * @param request
	 * @param data
	 *            Map<String, Object>{folder:'文件夹名称',list:boolean}
	 * @return List<ImageHelper> 可以多文件一起上传
	 */
	@RequestMapping(value = "/uploadFile", produces = "text/plain;charset=UTF-8")
	public String uploadFile(MultipartRequest request, @RequestParam Map<String, Object> data) {
		// 系统日志路径
		String logPath = SysPreperty.getProperty().LogFilePath;
		// 文件上传路径
		String upload_path = logPath.substring(0, logPath.length() - 3);
		Map<String, MultipartFile> fileMap = request.getFileMap();
		Set<String> keys = fileMap.keySet();
		String realFileName = null, savePath = null, suffix = null, fileUrl = null, fileModifiedName = null;
		@SuppressWarnings("unused")
		Long size = null;
		boolean goon = false;
		for (String key : keys) {
			MultipartFile multipartFile = fileMap.get(key);
			if (multipartFile == null) {
				continue;
			}
			CommonsMultipartFile file = (CommonsMultipartFile) multipartFile;
			realFileName = file.getOriginalFilename();
			size = file.getSize();// 大小
			suffix = realFileName.substring(realFileName.indexOf("."), realFileName.length()).toLowerCase();
			goon = true;
			String newName = realFileName;
			if (data.containsKey("rename")) {// 是否重命名文件名称
				if (Boolean.valueOf(String.valueOf(data.get("rename")))) {
					newName = SerialNum.getInstance().nextId() + suffix;
					fileModifiedName = newName;
				}
			}
			if (data.containsKey("filePath")) {// 上传路径
				if (String.valueOf(data.get("filePath")).trim().length() > 0) {
					upload_path = String.valueOf(data.get("filePath"));
				}
			}
			if (data.containsKey("folder")) {// 自定义文件夹名称及路径
				fileUrl = data.get("folder") + "/" + newName;
				savePath = upload_path + "/" + fileUrl;
			} else {
				fileUrl = newName + suffix;
				savePath = upload_path + "/" + fileUrl;
			}

			File imageFile = new File(savePath);
			if (!imageFile.getParentFile().exists()) {
				imageFile.getParentFile().mkdirs();
			}
			try {
				file.transferTo(imageFile);
			} catch (IOException e) {
				e.printStackTrace();
				R.error("文件上传错误");
			}
		}
		FileHelper imageHelper = new FileHelper();
		imageHelper.setName(realFileName);
		imageHelper.setFileModifiedName(fileModifiedName);
		imageHelper.setUrl(fileUrl);
		imageHelper.setSuffix(suffix.substring(1, suffix.length()));
		imageHelper.setUp(goon);

		return JSON.toJSONString(imageHelper);
	}

	/**
	 * 文件下载，通过上传文件附件表id，或者初始化生成的uid+formId来进行下载
	 *
	 * @param map
	 *            {fileUrl:'文件路径',fileRealName:'文件真实名称'}
	 * @param response
	 */
	@RequestMapping("/downloadFile")
	public void downloadFile(@RequestParam Map<String, Object> map, HttpServletResponse response) {
		// 系统日志路径
		String logPath = SysPreperty.getProperty().LogFilePath;
		// 文件上传路径
		String upload_path = logPath.substring(0, logPath.length() - 3);
		if (map.containsKey("fileUrl")) {
			File file = new File(upload_path + "/" + String.valueOf(map.get("fileUrl")));
			try {
				OutputStream os = response.getOutputStream();
				response.reset();
				response.setContentType("application/force-download");
				response.setHeader("content-disposition",
						"attachment;filename=" + URLEncoder.encode(String.valueOf(map.get("fileRealName")), "UTF-8"));
				os.write(Files.readAllBytes(Paths.get(file.getPath())));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			return;
		}

	}

}