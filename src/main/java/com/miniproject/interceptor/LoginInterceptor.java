package com.miniproject.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.miniproject.model.LoginDTO;
import com.miniproject.model.Member;
import com.miniproject.service.member.MemberService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LoginInterceptor extends HandlerInterceptorAdapter {
	private static Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);
	private final MemberService service;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		boolean isShowLoginPage = true;
		System.out.println("==========================================");
		if (request.getMethod().toLowerCase().equals("get")) {
			Cookie[] cookie = request.getCookies();
			System.out.println("==========================================");
			System.out.println(cookie.toString());
			System.out.println("==========================================");
			System.out.println("==========================================");
			
			if (false) {
				isShowLoginPage = false;
			}
		
		}
		return isShowLoginPage;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		if (request.getMethod().toLowerCase().equals("post")) {

			HttpSession session = request.getSession();

			String userId = request.getParameter("userId");
			String userPwd = request.getParameter("userPwd");
			boolean remember = request.getParameter("remember") != null;
			LoginDTO loginDTO = new LoginDTO(userId, userPwd, remember);

			System.out.println("인터셉터=" + loginDTO.toString());

			try {
				Member loginMember = service.loginMember(loginDTO);
				if (loginMember != null) {

					session.setAttribute("loginMember", loginMember);

					String url = (String) session.getAttribute("destUrl");

					if (url != null) {
						if (modelAndView != null) {
							modelAndView.setViewName("redirect:" + url);
						}
					} else {
						if (modelAndView != null) {
							modelAndView.setViewName("redirect:../");
						}
					}
					
				} else { // 예외는 발생하지 않았으나 로그인에 실패한경우...
					
					if (modelAndView != null) {
						modelAndView.setViewName("redirect:./showLoginForm?status=fail");
					}
					
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				modelAndView.setViewName("redirect:./showLoginForm?status=fail");
			}

			// 로그인 실패 했다면
			// 로그인 페이지로 이동
		}
		super.postHandle(request, response, handler, modelAndView);
	}

}
