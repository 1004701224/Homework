package com.parking.controller;
//����Ա��¼������
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.parking.pojo.User;
import com.parking.service.UserService;
@Controller
public class UserController {
	//����userservice����ʹ��byname����ע�뷽ʽ�������ݿ��в������Ա��Ϣ��֤���
	@Resource
	private UserService userServiceImpl;
	//����user���󷽱�����ת��
	private User users;
	@RequestMapping("login")
	private String login(String name,String password,String num,HttpServletRequest req) {
		System.out.println(name+" "+password);
		String number = (String)req.getSession().getAttribute("number");

		//�ɵ�¼ҳ�洫���û��������룬��֤����Ϣ�����бȶԣ���֤��ǿղ���
		if(num != "" && num != null) {
			//��֤��ǿգ��ȶ���֤�������Ƿ�����
			if(number.equals(num)) {
				//��֤��������ȷ���ж��û���������ǿ�
				if(name != "" && password != "") {
					//�û�������ǿգ������ݿ��в���û���������Ϣ����user��
					List<User> user = userServiceImpl.userselect(name, password);
					if(!user.isEmpty()) {
						//�û���������ȷ���ɹ������ݿ��в���û���Ϣ��������Ϣ���õ�session�У�������ҳ����ã���ת����ҳ��
						users = user.get(0);
						req.getSession().setAttribute("username", users.getName());
//						���û���Ϣ���õ�session�У����õ�¼״̬����ֹ����������
						req.getSession().setAttribute("users", users);
						return "main";
					}
					else {
						//�û������벻��ȷ�����ص�¼ҳ��
						System.out.println("�û������������");
						return "login.jsp";
					}
				}
				else {
					//�û�������δ��д���������ص�¼ҳ��
					System.out.println("�û���������Ϊ��");
					return "login.jsp";
				}
				
			}
			else {
//				��֤��������󣬷��ص�¼ҳ��
				System.out.println("��֤�����");
				return "login.jsp";
			}
		}
		else {
//			��֤��δ��д���������ص�¼ҳ��
			System.out.println("��֤��Ϊ��");
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
						System.out.println("ע��ɹ�����ӭ����"+name);
						return "login.jsp";
					}else {
						System.out.println("ע��ʧ�ܣ�����ϵ����Ա");
						return "register.jsp";
					}
				}else {
					System.out.println("�û�������Ϊ��");
					return "register.jsp";
				}
			}else {
				System.out.println("��Ȩ������޷�ע��");
				return "register.jsp";
			}
		}else {
			System.out.println("��֤��Ϊ��");
			return "register.jsp";
		}
	}
	@RequestMapping("delete")
	private String delete(String name,HttpServletRequest req) {
		name = (String)req.getSession().getAttribute("username");
		if(userServiceImpl.userdelete(name)>0) {
			System.out.println("�˺�ɾ���ɹ�");
			return "login.jsp";
		}else {
			System.out.println("�˺�ɾ��ʧ�ܣ�������");
			return "delete.jsp";
		}
	}
}
