package com.myspring.pro30.board.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.myspring.pro30.board.service.BoardService;
import com.myspring.pro30.board.vo.ArticleVO;
import com.myspring.pro30.board.vo.ImageVO;
import com.myspring.pro30.member.vo.MemberVO;

@Controller("boardController")
public class BoardControllerImpl implements BoardController {

	private static final String ARTICLE_IMAGE_REPO = "c:\\board\\article_image";

	// 필요한 의존 객체의 타입에 해당하는 빈을 찾아 주입
	@Autowired
	private BoardService boardService;
	@Autowired
	private ArticleVO articleVO;

	@Override
	@RequestMapping(value = "/board/listArticles.do", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView listArticles(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String viewName = (String) request.getAttribute("viewName");
		List articlesList = boardService.listArticles();
		ModelAndView mav = new ModelAndView(viewName);
		mav.addObject("articlesList", articlesList);
		return mav;
	}

	@Override
	@RequestMapping(value = "/board/addNewArticle.do", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity addNewArticle(MultipartHttpServletRequest multipartRequest, HttpServletResponse response)
			throws Exception {
		// 업로드된 파일에 액세스할 수 있도록 서블릿 요청 내에서 멀티파트 콘텐츠를 처리하는 추가 방법을 제공
		multipartRequest.setCharacterEncoding("utf-8");
		// 한 개의 새로운 글 추가
		Map<String, Object> articleMap = new HashMap<String, Object>();
		// 글 정보를 저장하기 위한 articleMap 생성
		Enumeration enu = multipartRequest.getParameterNames();

		// 글쓰기 창에서 전송된 글 정보를 Map에 key/value로 저장
		while (enu.hasMoreElements()) {
			String name = (String) enu.nextElement();
			String value = multipartRequest.getParameter(name);
			articleMap.put(name, value);
		}

		// 업로드한 이미지 파일 이름을 가져옴
		String imageFileName = upload(multipartRequest);
		HttpSession session = multipartRequest.getSession();
		MemberVO memberVO = (MemberVO) session.getAttribute("member");

		// 세션에 저장된 회원 정보로부터 회원 ID 가져옴
		String id = memberVO.getId();

		// 회원 id, 이미지 파일 이름, 부모 글 번호를 articleMap에 저장
		articleMap.put("parentNO", 0);
		articleMap.put("id", id);
		articleMap.put("imageFileName", imageFileName);

		// 컨트롤러에서 새 글 정보와 이미지 파일 정보를 가져오려면 먼저 upload()메서드를 호출해서 첨부한 이미지 파일이 저장된
		// fileList를 받아와야 함
		// 첨부한 이름을 fileList로 반환
		//List<String> fileList = upload(multipartRequest);
		//List<ImageVO> imageFileList = new ArrayList<ImageVO>();

		String message;
		ResponseEntity resEnt = null;
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("content-Type", "text/html; charset=UTF-8");

		try { // 글 정보가 저장된 articleMap을 Service 클래스의 addArticle() 메서드로 전달
			int articleNO = boardService.addNewArticle(articleMap);

			// 글 정보를 추가한 후 업로드한 이미지 파일을 글 번호로 명명한 폴더로 이동
			if (imageFileName != null && imageFileName.length() != 0) {
				File srcFile = new File(ARTICLE_IMAGE_REPO + "\\" + "temp" + "\\" + imageFileName);
				File destDir = new File(ARTICLE_IMAGE_REPO + "\\" + articleNO);
				FileUtils.moveFileToDirectory(srcFile, destDir, true);
			}
			// 새 글을 추가한 후 메시지를 전달
			message = "<script>";
			message += " alert('새 글을 추가했습니다.');";
			message += " location.href='" + multipartRequest.getContextPath() + "/board/listArticles.do'; ";
			message += " </script>";

			resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);

		} catch (Exception e) {
			File srcFile = new File(ARTICLE_IMAGE_REPO + "\\" + "temp" + "\\" + imageFileName);
			srcFile.delete();

			// 오류 발생 시 오류 메세지 전달
			message = "<script>";
			message += " alert('오류가 발생했습니다. 다시 시도해 주세요.');";
			message += " location.href='" + multipartRequest.getContextPath() + "/board/articleForm.do'; ";
			message += " </script>";

			resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
			e.printStackTrace();
		}
		return resEnt;
	}

	/*
	@Override
	@RequestMapping(value = "/board/addNewArticle.do", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity addNewArticle(MultipartHttpServletRequest multipartRequest, HttpServletResponse response)
			throws Exception {
		multipartRequest.setCharacterEncoding("utf-8");
		String imageFileName = null;

		Map articleMap = new HashMap();
		Enumeration enu = multipartRequest.getParameterNames();
		while (enu.hasMoreElements()) {
			String name = (String) enu.nextElement();
			String value = multipartRequest.getParameter(name);
			articleMap.put(name, value);
		}

		// HttpSession session = multipartRequest.getSession();

		// 첨부한 이름을 fileList로 반환
		List<String> fileList = upload(multipartRequest);
		List<ImageVO> imageFileList = new ArrayList<ImageVO>();

		if (fileList != null && fileList.size() != 0) {
			// 전송된 이미지 정보를 ImageVO 객체의 속성에 차례대로 저장한 후 imageFileList에 다시 저장
			for (String fileName : fileList) {
				ImageVO imageVO = new ImageVO();
				imageVO.setImageFileName(fileName);
				imageFileList.add(imageVO);
			}

			// imageFileList를 다시 articleMap에 저장
			articleMap.put("imageFileList", imageFileList);
		}

		String message;
		ResponseEntity resEnt = null;
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=utf-8");

		try {

			// articleMap을 서비스 클래스로 전달
			int articleNO = boardService.addNewArticle(articleMap);
			if (imageFileList != null && imageFileList.size() != 0) {

				// 첨부한 이미지들을 for문을 이용해 업로드
				for (ImageVO imageVO : imageFileList) {
					imageFileName = imageVO.getImageFileName();
					File srcFile = new File(ARTICLE_IMAGE_REPO + "\\" + "temp" + "\\" + imageFileName);
					File destDir = new File(ARTICLE_IMAGE_REPO + "\\" + articleNO);
					// destDir.mkdirs();
					FileUtils.moveFileToDirectory(srcFile, destDir, true);

				}
			}
			// 새 글을 추가한 후 메시지를 전달
			message = "<script>";
			message += " alert('새 글을 추가했습니다.');";
			message += " location.href='" + multipartRequest.getContextPath() + "/board/listArticles.do'; ";
			message += " </script>";

			resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);

		} catch (Exception e) {

			if (imageFileList != null && imageFileList.size() != 0) {

				// 오류 발생 시 temp 폴더의 이미지들을 모두 삭제
				for (ImageVO imageVO : imageFileList) {
					imageFileName = imageVO.getImageFileName();
					File srcFile = new File(ARTICLE_IMAGE_REPO + "\\" + "temp" + "\\" + imageFileName);
					srcFile.delete();
				}
			}
			// 오류 발생 시 오류 메세지 전달
			message = "<script>";
			message += " alert('오류가 발생했습니다. 다시 시도해 주세요.');";
			message += " location.href='" + multipartRequest.getContextPath() + "/board/articleForm.do'; ";
			message += " </script>";

			resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
			e.printStackTrace();
		}
		return resEnt;

	} */

//로그인 폼, 게시판 글 입력 폼
	@RequestMapping(value = "/board/*Form.do", method = RequestMethod.GET)
	private ModelAndView form(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 글쓰기 창을 나타냄
		String viewName = (String) request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView();
		mav.setViewName(viewName);
		return mav;
	}

	// 업로드한 파일 이름을 얻은 후 반환

	private String upload(MultipartHttpServletRequest multipartRequest) throws Exception {
		String imageFileName = null;
		Iterator<String> fileNames = multipartRequest.getFileNames();

		while (fileNames.hasNext()) {
			String fileName = fileNames.next();
			MultipartFile mFile = multipartRequest.getFile(fileName);
			imageFileName = mFile.getOriginalFilename();
			File file = new File(ARTICLE_IMAGE_REPO + "\\" + "temp" + "\\" + fileName);

			if (mFile.getSize() != 0) {
				file.getParentFile().mkdirs();
				mFile.transferTo(new File(ARTICLE_IMAGE_REPO + "\\" + "temp" + "\\" + imageFileName));
			}

		}
		return imageFileName;
	}

	// 이미지 파일 이름이 저장된 List 반환
	/*
	 private List<String> upload(MultipartHttpServletRequest multipartRequest) throws Exception {
	 
	 List<String> fileList = new ArrayList<String>(); 
	 Iterator<String> fileNames = multipartRequest.getFileNames(); 
	 while (fileNames.hasNext()) { 
		 String fileName = fileNames.next(); 
		 MultipartFile mFile = multipartRequest.getFile(fileName); 
		 String originalFileName = mFile.getOriginalFilename(); // 첨부한 이미지 파일의 이름들을 차례대로 저장
		 fileList.add(originalFileName);
	  
	  File file = new File(ARTICLE_IMAGE_REPO + "\\" + "temp" + "\\" + fileName);
	  if (mFile.getSize() != 0) { 
		  if (!file.exists()) {
	 file.getParentFile().mkdirs(); 
	 mFile.transferTo(new File(ARTICLE_IMAGE_REPO + "\\" + "temp" + "\\" + originalFileName)); 
	 } 
		  } 
	  }
	 
	 return fileList; }
	 */

	@RequestMapping(value = "/board/viewArticle.do", method = RequestMethod.GET)
	public ModelAndView viewArticle(@RequestParam("articleNO") int articleNO, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// 메서드 매개 변수가 웹 요청 매개 변수에 바인딩되어야 함을 나타내는 주석
		String viewName = (String) request.getAttribute("viewName");
		articleVO = boardService.viewArticle(articleNO);
		ModelAndView mav = new ModelAndView();
		mav.setViewName(viewName);
		mav.addObject("article", articleVO);
		return mav;
	}

	@RequestMapping(value = "/board/modArticle.do", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity modArticle(MultipartHttpServletRequest multipartRequest, HttpServletResponse response)
			throws Exception {
		multipartRequest.setCharacterEncoding("utf-8");
		Map<String, Object> articleMap = new HashMap<String, Object>();
		Enumeration enu = multipartRequest.getParameterNames();
		// 순환 인터페이스, 스레드에 안전한 구조로 사용, HashTable과 Vector에서 사용 가능
		while (enu.hasMoreElements()) {
			String name = (String) enu.nextElement();
			String value = multipartRequest.getParameter(name);
			articleMap.put(name, value);
		}

		String imageFileName = upload(multipartRequest);
		// List<String> imageFileName = upload(multipartRequest);
		// HttpSession session = multipartRequest.getSession();
//		MemberVO memberVO = (MemberVO) session.getAttribute("member");
//		String id = memberVO.getId();
//		articleMap.put("id", id);
		articleMap.put("imageFileName", imageFileName);

		String articleNO = (String) articleMap.get("articleNO");
		String message;
		ResponseEntity resEnt = null;
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=utf-8");

		try {
			boardService.modArticle(articleMap);
			if (imageFileName != null && imageFileName.length() != 0) {
				// if (imageFileName != null && imageFileName.size() != 0) {
				File srcFile = new File(ARTICLE_IMAGE_REPO + "\\" + "temp" + "\\" + imageFileName);
				File destDir = new File(ARTICLE_IMAGE_REPO + "\\" + articleNO);
				FileUtils.moveFileToDirectory(srcFile, destDir, true);

				String originalFileName = (String) articleMap.get("originalFileName");
				File oldFile = new File(ARTICLE_IMAGE_REPO + "\\" + articleNO + "\\" + originalFileName);
				oldFile.delete();
			}

			message = "<script>";
			message += " alert('글을 수정했습니다.');";
			message += " location.href='" + multipartRequest.getContextPath() + "/board/viewArticle.do?articleNO="
					+ articleNO + "';";
			message += " </script>";

			resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);

		} catch (Exception e) {
			File srcFile = new File(ARTICLE_IMAGE_REPO + "\\" + "temp" + "\\" + imageFileName);
			srcFile.delete();

			message = "<script>";
			message += " alert('오류가 발생했습니다. 다시 시도해 주세요.');";
			message += " location.href='" + multipartRequest.getContextPath() + "/board/viewArticle.do?articleNO="
					+ articleNO + "';";
			message += " </script>";

			resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
			e.printStackTrace();
		}

		return resEnt;
	}

	@Override
	@RequestMapping(value = "/board/removeArticle.do", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity removeArticle(@RequestParam("articleNO") int articleNO, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		response.setContentType("text/html; charset=UTF-8");
		String message;
		ResponseEntity resEnt = null;
		// HTTP 상태코드를 전송하고 싶은 데이터와 함께 전송할 수 있어 세밀한 제어가 필요한 경우 사용
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");

		try {
			boardService.removeArticle(articleNO);
			File destDir = new File(ARTICLE_IMAGE_REPO + "\\" + articleNO);
			FileUtils.deleteDirectory(destDir);

			message = "<script>";
			message += " alert('글을 삭제했습니다.');";
			message += " location.href='" + request.getContextPath() + "/board/listArticles.do';";
			message += " </script>";

			resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);

		} catch (Exception e) {

			message = "<script>";
			message += " alert('오류가 발생했습니다. 다시 시도해 주세요.');";
			message += " location.href='" + request.getContextPath() + "/board/listArticles.do';";
			message += " </script>";

			resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
			e.printStackTrace();

		}

		return resEnt;
	}

}
