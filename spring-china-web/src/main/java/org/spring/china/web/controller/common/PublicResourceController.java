package org.spring.china.web.controller.common;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


import org.apache.commons.io.FileUtils;
import org.spring.china.base.common.Res;
import org.spring.china.web.model.Model_Uploader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/public-resource")
@RestController
public class PublicResourceController {

	@Value("${UPLOAD_PATH}")
	private String UPLOAD_PATH;
	
	/**
	 * 获取用户头像
	 * @param userId
	 * @param type
	 * @param ext
	 * @return 用户头像的二进制数据
	 * @throws IOException
	 */
	@RequestMapping(value="/download-user-avatar/{userId}/{type}/{ext}",method=RequestMethod.GET)
	public ResponseEntity<byte[]> DownloadUserAvatar(
			@PathVariable("userId") long userId,
			@PathVariable("type") String type,
			@PathVariable("ext") String ext) throws IOException{		
		String dfileName = new String((userId+"_"+type).getBytes("gb2312"), "iso8859-1"); 
        HttpHeaders headers = new HttpHeaders();    
        headers.setContentDispositionFormData("attachment", dfileName+"."+ext);   
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);   
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(new File(this.UPLOAD_PATH+"/avatar/"+dfileName+"."+ext)),    
                                          headers, HttpStatus.CREATED); 
	}
	
	@ResponseBody
	@RequestMapping(value="upload",method=RequestMethod.POST)
	public Res<Model_Uploader> UploadFile(@RequestParam(value="file",required=false) MultipartFile file){
		Res<Model_Uploader> res=new Res<Model_Uploader>();
        try {  
        	String original_file_name = file.getOriginalFilename();  
        	String ext="";
        	if (original_file_name.lastIndexOf(".") >= 0) {
        		ext = original_file_name.substring(original_file_name.lastIndexOf("."));
            }
        	String guid=UUID.randomUUID().toString();
            File targetFile = new File(UPLOAD_PATH+"/file", guid+ext);  
            file.transferTo(targetFile);  
            Model_Uploader up_res=new Model_Uploader();
            up_res.setOriginalName(original_file_name);
            up_res.setFileName(guid+ext);
            up_res.setUrl("/public-resource/download-file?file="+guid+ext);
            res.setData(up_res);
        } catch (Exception e) {  
            e.printStackTrace();   
            res.setCode(-2);
            res.setMsg("上传失败！");
        }  
        return res;	
	}
	
	@RequestMapping(value="/download-file",method=RequestMethod.GET)
	public ResponseEntity<byte[]> DownloadFile(@RequestParam("file") String filename) throws IOException{		
		String dfileName = new String(filename.getBytes("gb2312"), "iso8859-1"); 
        HttpHeaders headers = new HttpHeaders();    
        //String fileName=new String("你好.xlsx".getBytes("UTF-8"),"iso-8859-1");//为了解决中文名称乱码问题  
        headers.setContentDispositionFormData("attachment", dfileName);   
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);   
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(new File(UPLOAD_PATH+"/file/"+dfileName)),    
                                          headers, HttpStatus.CREATED); 
	}
}
