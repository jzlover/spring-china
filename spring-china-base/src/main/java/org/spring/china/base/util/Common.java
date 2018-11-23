package org.spring.china.base.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.Date;

import javax.imageio.stream.FileImageOutputStream;

public class Common {
	/**
	 * Nate ——时间比较器
	 * @param date
	 * @return
	 */
	public static String FormatDate(Date date){
		if(null==date){return "";}
		Date datenow=new Date();
		long time1 = datenow.getTime();  
		long time2 = date.getTime();  
		long diff ;  
		if(time1<time2) {  
		    diff = time2 - time1;  
		} else {  
		    diff = time1 - time2;  
		}
		long day = 0;  
        long hour = 0;  
        long min = 0;  
     //   long sec = 0;
        long month = 0;
        long year = 0;     
        long yearnow=Calendar.getInstance().get(Calendar.YEAR);
        long yearcompare=Calendar.getInstance().get(Calendar.YEAR);
        if(yearnow<yearcompare)
        {
        	year=yearcompare-yearnow;
        }else{year=yearnow-yearcompare;}
        
        long monthnow=Calendar.getInstance().get(Calendar.MONTH);
        long monthcompare=Calendar.getInstance().get(Calendar.MONTH);
        if(monthnow<monthcompare)
        {
        	month=monthcompare-monthnow;
        }else{month=monthnow-monthcompare;}
       
		day = diff / (24 * 60 * 60 * 1000);  
		hour = (diff / (60 * 60 * 1000) - day * 24);  
		min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);  
	//	sec = (diff/1000-day*24*60*60-hour*60*60-min*60);
		 if (0!=year) {
             return year+"年前";
         } else if (0!=month) {
             return month+"个月前";
         } else if (0!=day) {
             return day+"天前";
         } else if (0!=min) {
             return min+"分钟前";
         }  else {
             return "刚刚";
         }
	}

	public static String GetPortraitUrl(Boolean portrait,long userId ,String size){
		
		if(null==portrait || false==portrait ){return "/system/images/default_"+size+".jpg";}
		else{return "/public-resource/download-user-avatar/"+userId+"/"+size+"/jpg" ;}
	}
	
	public static Boolean Base64ToImage(String imgStr,String path) {
		if(imgStr==null)
			return false;
		try{
			byte[] b=Base64.decode(imgStr);
			for(int i=0;i<b.length;i++){
				if(b[i]<0){
					b[i]+=256;
				}
			}
			OutputStream out =new FileOutputStream(path);
			out.write(b);
			out.flush();
			out.close();
			return true;
		}catch(Exception e){
			return false;
		}
		
	}
	
	public static void DownloadWebPic(String urlString, String filename,String savePath) throws Exception {  
        // 构造URL  
        URL url = new URL(urlString);  
        // 打开连接  
        URLConnection con = url.openConnection();  
        //设置请求超时为5s  
        con.setConnectTimeout(5*1000);  
        // 输入流  
        InputStream is = con.getInputStream();  
      
        // 1K的数据缓冲  
        byte[] bs = new byte[1024];  
        // 读取到的数据长度  
        int len;  
        // 输出的文件流  
       File sf=new File(savePath);  
       if(!sf.exists()){  
           sf.mkdirs();  
       }  
       OutputStream os = new FileOutputStream(sf.getPath()+"\\"+filename);  
        // 开始读取  
        while ((len = is.read(bs)) != -1) {  
          os.write(bs, 0, len);  
        }  
        // 完毕，关闭所有链接  
        os.close();  
        is.close();  
    }   
}
