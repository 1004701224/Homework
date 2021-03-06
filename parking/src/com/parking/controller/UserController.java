package com.parking.controller;
//管理员登录控制器
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.parking.pojo.User;
import com.parking.service.UserService;
@Controller
public class UserController {
	//建立userservice对象，使用byname依赖注入方式，从数据库中查出管理员信息验证身份
	@Resource
	private UserService userServiceImpl;
	//建立user对象方便数据转发
	private User users;
	@RequestMapping("login")
	private String login(String name,String password,String num,HttpServletRequest req) {
		System.out.println(name+" "+password);
		String number = (String)req.getSession().getAttribute("number");

		//由登录页面传入用户名，密码，验证码信息，进行比对，验证码非空测试
		if(num != "" && num != null) {
			//验证码非空，比对验证码输入是否有误
			if(number.equals(num)) {
				//验证码输入正确，判断用户名、密码非空
				if(name != "" && password != "") {
					//用户名密码非空，从数据库中查出用户名密码信息存入user中
					List<User> user = userServiceImpl.userselect(name, password);
					if(!user.isEmpty()) {
						//用户名密码正确，成功从数据库中查出用户信息，将此信息设置到session中，方便主页面调用，跳转到主页面
						users = user.get(0);
						req.getSession().setAttribute("username", users.getName());
//						将用户信息设置到session中，设置登录状态，防止拦截器拦截
						req.getSession().setAttribute("users", users);
						return "main";
					}
					else {
						//用户名密码不正确，返回登录页面
						System.out.println("用户名或密码错误");
						return "login.jsp";
					}
				}
				else {
					//用户名密码未填写完整，返回登录页面
					System.out.println("用户名或密码为空");
					return "login.jsp";
				}
				
			}
			else {
//				验证码输入错误，返回登录页面
				System.out.println("验证码错误");
				return "login.jsp";
			}
		}
		else {
//			验证码未填写完整，返回登录页面
			System.out.println("验证码为空");
			return "login.jsp";
		}
	}
	@RequestMapping("register")
	private String register(String name,String password,String key,String num,HttpServletRequest req) {
		String number = (String) req.getSession().getAttribute("number");
		if(num != "") {
			if(key.equals("0000")) {
				if(name != "" && password != "") {
					if(userServiceImpl.userregister(name, password)>0) {
						System.out.println("注册成功！欢迎您，"+name);
						return "login.jsp";
					}else {
						System.out.println("注册失败！请联系管理员");
						return "register.jsp";
					}
				}else {
					System.out.println("用户名密码为空");
					return "register.jsp";
				}
			}else {
				System.out.println("授权码错误！无法注册");
				return "register.jsp";
			}
		}else {
			System.out.println("验证码为空");
			return "register.jsp";
		}
	}
	@RequestMapping("delete")
	private String delete(String name,HttpServletRequest req) {
		name = (String)req.getSession().getAttribute("username");
		if(userServiceImpl.userdelete(name)>0) {
			System.out.println("账号删除成功");
			return "login.jsp";
		}else {
			System.out.println("账号删除失败，请重试");
			return "delete.jsp";
		}
	}
}
