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

	@Override
	@RequestMapping(value = "/member/login.do", method = RequestMethod.POST)
	// rAttr : 리다이렉트시 매개변수를 전달합니다.
	public ModelAndView login(@ModelAttribute("member") MemberVO member, RedirectAttributes rAttr,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView();
		memberVO = memberService.login(member);
		if (memberVO != null) { // 로그인 성공시 조건문을 수행
			HttpSession session = request.getSession();
			// 두 개 이상의 페이지 요청 또는 웹 사이트 방문에서 사용자를 식별하고 해당 사용자에 대한 정보를 저장하는 방법을 제공
			session.setAttribute("member", memberVO);
			session.setAttribute("isLogOn", true);
			String action = (String) session.getAttribute("action");
			// 로그인 성공시 세션에 저장된 action값을 가져옴
			session.removeAttribute("action");
			if (action != null) { // action값이 null이 아니면 action값을 뷰이름으로 지정해 글쓰기창으로 이동
				mav.setViewName("redirect:" + action);
			} else {
				mav.setViewName("redirect:/member/listMembers.do");
			}
		} else { // 로그인 실패시 다시 로그인창으로 이동
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

	@RequestMapping(value = "/member/*Form.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView form(@RequestParam(value = "result", required = false) String result,
			@RequestParam(value = "action", required = false) String action, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String viewName = (String) request.getAttribute("viewName");
		HttpSession session = request.getSession();
		session.setAttribute("action", action);
		//글쓰기창 요청명을 action속성으로 세션에 저장
		ModelAndView mav = new ModelAndView();
		mav.addObject("result", result);
		mav.setViewName(viewName);
		return mav;
	}

}
