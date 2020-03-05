package com.taotao.controller;

import java.io.File;
import java.io.FileInputStream;

import org.apache.commons.net.ftp.FTPClient;
import org.junit.Test;

public class FTPTest {

	@Test
	public void testFtpClient() throws Exception{
		//创建一个FTPClient对象
		FTPClient ftpClient = new FTPClient();
		//创建ftp连接
		ftpClient.connect("192.168.116.128", 21);
		//登录ftp服务器，使用用户名和密码
		ftpClient.login("ftpuser", "ftpuser");
		//长传文件
		//读取本地文件
		FileInputStream fileInputStream = new FileInputStream(new File("C:\\Users\\DOU\\Desktop\\1.jpg"));
		//设置上传文件的路径
		ftpClient.changeWorkingDirectory("/home/ftpuser/www/images");
		//修改文件上传格式
		ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
		//第一个参数：服务器端文档名
		//第二个参数：上传文档的inputstream
		ftpClient.storeFile("image.jpg", fileInputStream);
		//关闭连接
		ftpClient.logout();
		fileInputStream.close();
	}
}
