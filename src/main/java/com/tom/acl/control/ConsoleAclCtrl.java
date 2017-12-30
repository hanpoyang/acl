package com.tom.acl.control;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Base64;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/util")
public class ConsoleAclCtrl{
	private static final String BASE = "/root/www";
	private static String SCRIPT_FILE_TEXT = "";
	private static String TERMINAL_CSS_TEXT = "";
	private static String HEADER_TEXT = "";
	
	public ConsoleAclCtrl() {
		// TODO Auto-generated constructor stub
		readFile();
	}
	
	@RequestMapping(value="/check", produces={"text/html;charset=UTF-8"})
	@ResponseBody
	public String check(@RequestBody Map<String, String> param, HttpServletRequest request, HttpServletResponse response){
		//phone
		String phoneNumber = param.get("x500");
		phoneNumber = new String(Base64.getDecoder().decode(phoneNumber.getBytes()));
		System.out.println("phone number2 is:"+phoneNumber);
		
		Object sessionObject = request.getSession().getAttribute("_STATUS_");
		String status = "";
		//Session为空，没有 验证返回空
		if (null != sessionObject) {
			status = (String)sessionObject;
		}
		
		//if checked then show scripts otherwise nothing.
		if("checked".equals(status) ){
			return "{\"flag\":\"0\"}";
		}
		
		if(",13751132206,13316824270,18681435001,075586200609,".indexOf(","+phoneNumber+",") > -1) {
			request.getSession().setAttribute("_STATUS_", "checked");
			return "{\"flag\":\"0\"}";
		}
		return "{\"flag\": \"1\"}";
		
	}
	
	/**
	 * 提供io.socket js 脚本引用
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/scripts.shtml")
	@ResponseBody
	public String showScripts(HttpServletRequest request, HttpServletResponse response){
		Object sessionObject = request.getSession().getAttribute("_STATUS_");
		String status = "";
		//Session为空，没有 验证返回空
		if (null != sessionObject) {
			status = (String)sessionObject;
		}
		if("checked".equals(status)) {
			return SCRIPT_FILE_TEXT;
		}
		return "<!--nothing here.-->";
	}
	
	/**
	 * 提供功能菜单
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/header.shtml")
	@ResponseBody
	public String showHeader(HttpServletRequest request, HttpServletResponse response){
		Object sessionObject = request.getSession().getAttribute("_STATUS_");
		String status = "";
		//Session为空，没有 验证返回空
		if (null != sessionObject) {
			status = (String)sessionObject;
		}
		if("checked".equals(status)) {
			return HEADER_TEXT;
		}
		return "<!--nothing here.-->";
	}
	
	/**
	 * 提供io.socket js 脚本引用
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/terminalcss.shtml")
	@ResponseBody
	public String showTerminalCss(HttpServletRequest request, HttpServletResponse response){
		Object sessionObject = request.getSession().getAttribute("_STATUS_");
		String status = "";
		//Session为空，没有 验证返回空
		if (null != sessionObject) {
			status = (String)sessionObject;
		}
		if("checked".equals(status)) {
			return TERMINAL_CSS_TEXT;
		}
		return "/**end here**/";
	}
	
	/**
	 * clean session
	 */
	@RequestMapping("/clear")
	@ResponseBody
	public String clearSession(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		session.invalidate();
		return "{\"flag\":\"0\"}";
	}
	
	/**
	 * 读取文件
	 */
	private static void readFile(){
		//获取脚本
		if(null == SCRIPT_FILE_TEXT || "".equals(SCRIPT_FILE_TEXT)) {
			try(InputStream in = new FileInputStream(BASE+"/script_file.shtml");
				BufferedReader reader = new BufferedReader(new InputStreamReader(in))){
				String line = null;
				StringBuilder sb = new StringBuilder();
				while((line = reader.readLine()) != null) {
					sb.append(line);
				}
				SCRIPT_FILE_TEXT = sb.toString();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		//获取样式
		if(null == TERMINAL_CSS_TEXT || "".equals(TERMINAL_CSS_TEXT)) {
			try(InputStream in = new FileInputStream(BASE+"/terminal");
				BufferedReader reader = new BufferedReader(new InputStreamReader(in))){
				String line = null;
				StringBuilder sb = new StringBuilder();
				while((line = reader.readLine()) != null) {
					sb.append(line);
				}
				TERMINAL_CSS_TEXT = sb.toString();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		//读取菜单内容
		if(null == HEADER_TEXT || "".equals(HEADER_TEXT)) {
			try(InputStream in = new FileInputStream(BASE+"/header");
				BufferedReader reader = new BufferedReader(new InputStreamReader(in))){
				String line = null;
				StringBuilder sb = new StringBuilder();
				while((line = reader.readLine()) != null) {
					sb.append(line);
				}
				HEADER_TEXT = sb.toString();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		
	}
}