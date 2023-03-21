package com.myspring.pro30.member.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.myspring.pro30.member.service.MemberService;
import com.myspring.pro30.member.vo.MemberVO;

@Controller(value = "memberController")
public class MemberControllerImpl implements MemberController {

	private static final Logger logger = LoggerFactory.getLogger(MemberControllerImpl.class);

	/*
	 * Marks a constructor, field, setter method, or config method as to be
	 * autowired by Spring's dependency injection facilities. 생성자, 필드, 셋터 메서드, 메서드
	 * 설정에 의존성 주입함 // 생성자, 필드, 세터 메서드 또는 구성 메서드를 Spring의 종속성 주입 기능에서 자동으로 연결되도록
	 * 표시합니다.
	 */

	@Autowired
	private MemberService memberService;

	@Autowired
	private MemberVO memberVO;

	/*
	 * public void setMemberService(MemberServiceImpl memberService) {
	 * this.memberService = memberService; }
	 */

	@RequestMapping(value = { "/", "/main.do" }, method = RequestMethod.GET)
	private ModelAndView main(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String viewName = (String) request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView();
		mav.setViewName(viewName);
		return mav;
	}

	@Override
	@RequestMapping(value = "/member/listMembers.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView listMembers(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// String viewName = getViewName(request);
		String viewName = (String) request.getAttribute("viewName");
		// System.out.println(viewName);
		// logger.info("viewName: " + viewName);
		logger.debug("viewName: " + viewName);
		List<MemberVO> membersList = memberService.listMembers();
		ModelAndView mav = new ModelAndView(viewName);
		mav.addObject("membersList", membersList);
		return mav;
	}

//	private String getViewName(HttpServletRequest request) {
//		String contextPath = request.getContextPath();
//		System.out.println(contextPath);
//		String uri = (String) request.getAttribute("javax.servlet.include.request_uri");
//		if (uri == null || uri.trim().equals("")) {
//			uri = request.getRequestURI();
//		}
//		System.out.println(uri);
//
//		int begin = 0;
//		if (!((contextPath == null) || ("".equals(contextPath)))) {
//			begin = contextPath.length();
//		}
//		System.out.println(begin);
//		int end;
//		if (uri.indexOf(";") != -1) {
//			end = uri.indexOf(";");
//		} else if (uri.indexOf("?") != -1) {
//			end = uri.indexOf("?");
//		} else {
//			end = uri.length();
//		}
//		System.out.println(end);
//
//		String viewName = uri.substring(begin, end);
//		System.out.println(viewName);
//		if (viewName.indexOf(".") != -1) {
//			viewName = viewName.substring(0, viewName.lastIndexOf("."));
//			System.out.println(viewName);
//		}
//		if (viewName.lastIndexOf("/") != -1) {
//			viewName = viewName.substring(viewName.lastIndexOf("/", 1), viewName.length());
//			System.out.println(viewName);
//		}
//		return viewName;
//	}

	@Override
	@RequestMapping(value = "/member/addMember.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView addMember(@ModelAttribute("member") MemberVO member, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		request.setCharacterEncoding("UTF-8");

		int result = 0;
		result = memberService.addMember(member);
		ModelAndView mav = new ModelAndView("redirect:/member/listMembers.do");
		return mav;
	}

	@Override
	@RequestMapping(value = "/member/deleteMember.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView deleteMember(@RequestParam("id") String id, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		request.setCharacterEncoding("UTF-8");

		memberService.deleteMember(id);
		ModelAndView mav = new ModelAndView("redirect:/member/listMembers.do");
		return mav;
	}

	@RequestMapping(value = "/member/*Form.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView form(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// String viewName = getViewName(request);
		String viewName = (String) request.getAttribute("viewName");
		logger.debug("viewName: " + viewName);
		ModelAndView mav = new ModelAndView();
		mav.setViewName(viewName);
		return mav;
	}

	@Override
	@RequestMapping(value = "/member/login.do", method = RequestMethod.POST)
	public ModelAndView login(@ModelAttribute("member") MemberVO member, RedirectAttributes rAttr,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView();
		memberVO = memberService.login(member);
		if (memberVO != null) {
			HttpSession session = request.getSession();
			session.setAttribute("member", memberVO);
			session.setAttribute("isLogOn", true);
			mav.setViewName("redirect:/member/listMembers.do");
		} else {
			rAttr.addAttribute("result", "loginFailed");
			mav.setViewName("redirect:/member/loginForm.do");
		}
		return mav;
	}

	@Override
	@RequestMapping(value = "/member/logout.do", method = RequestMethod.GET)
	public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpSession session = request.getSession();
		session.removeAttribute("member");
		session.removeAttribute("isLogOn");
		ModelAndView mav = new ModelAndView();
		mav.setViewName("redirect:/member/listMembers.do");
		return mav;
	}

}
