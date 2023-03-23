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

	// �ʿ��� ���� ��ü�� Ÿ�Կ� �ش��ϴ� ���� ã�� ����
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
		// ���ε�� ���Ͽ� �׼����� �� �ֵ��� ���� ��û ������ ��Ƽ��Ʈ �������� ó���ϴ� �߰� ����� ����
		multipartRequest.setCharacterEncoding("utf-8");
		// �� ���� ���ο� �� �߰�
		Map<String, Object> articleMap = new HashMap<String, Object>();
		// �� ������ �����ϱ� ���� articleMap ����
		Enumeration enu = multipartRequest.getParameterNames();

		// �۾��� â���� ���۵� �� ������ Map�� key/value�� ����
		while (enu.hasMoreElements()) {
			String name = (String) enu.nextElement();
			String value = multipartRequest.getParameter(name);
			articleMap.put(name, value);
		}

		// ���ε��� �̹��� ���� �̸��� ������
		String imageFileName = upload(multipartRequest);
		HttpSession session = multipartRequest.getSession();
		MemberVO memberVO = (MemberVO) session.getAttribute("member");

		// ���ǿ� ����� ȸ�� �����κ��� ȸ�� ID ������
		String id = memberVO.getId();

		// ȸ�� id, �̹��� ���� �̸�, �θ� �� ��ȣ�� articleMap�� ����
		articleMap.put("parentNO", 0);
		articleMap.put("id", id);
		articleMap.put("imageFileName", imageFileName);

		// ��Ʈ�ѷ����� �� �� ������ �̹��� ���� ������ ���������� ���� upload()�޼��带 ȣ���ؼ� ÷���� �̹��� ������ �����
		// fileList�� �޾ƿ;� ��
		// ÷���� �̸��� fileList�� ��ȯ
		//List<String> fileList = upload(multipartRequest);
		//List<ImageVO> imageFileList = new ArrayList<ImageVO>();

		String message;
		ResponseEntity resEnt = null;
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("content-Type", "text/html; charset=UTF-8");

		try { // �� ������ ����� articleMap�� Service Ŭ������ addArticle() �޼���� ����
			int articleNO = boardService.addNewArticle(articleMap);

			// �� ������ �߰��� �� ���ε��� �̹��� ������ �� ��ȣ�� ����� ������ �̵�
			if (imageFileName != null && imageFileName.length() != 0) {
				File srcFile = new File(ARTICLE_IMAGE_REPO + "\\" + "temp" + "\\" + imageFileName);
				File destDir = new File(ARTICLE_IMAGE_REPO + "\\" + articleNO);
				FileUtils.moveFileToDirectory(srcFile, destDir, true);
			}
			// �� ���� �߰��� �� �޽����� ����
			message = "<script>";
			message += " alert('�� ���� �߰��߽��ϴ�.');";
			message += " location.href='" + multipartRequest.getContextPath() + "/board/listArticles.do'; ";
			message += " </script>";

			resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);

		} catch (Exception e) {
			File srcFile = new File(ARTICLE_IMAGE_REPO + "\\" + "temp" + "\\" + imageFileName);
			srcFile.delete();

			// ���� �߻� �� ���� �޼��� ����
			message = "<script>";
			message += " alert('������ �߻��߽��ϴ�. �ٽ� �õ��� �ּ���.');";
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

		// ÷���� �̸��� fileList�� ��ȯ
		List<String> fileList = upload(multipartRequest);
		List<ImageVO> imageFileList = new ArrayList<ImageVO>();

		if (fileList != null && fileList.size() != 0) {
			// ���۵� �̹��� ������ ImageVO ��ü�� �Ӽ��� ���ʴ�� ������ �� imageFileList�� �ٽ� ����
			for (String fileName : fileList) {
				ImageVO imageVO = new ImageVO();
				imageVO.setImageFileName(fileName);
				imageFileList.add(imageVO);
			}

			// imageFileList�� �ٽ� articleMap�� ����
			articleMap.put("imageFileList", imageFileList);
		}

		String message;
		ResponseEntity resEnt = null;
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=utf-8");

		try {

			// articleMap�� ���� Ŭ������ ����
			int articleNO = boardService.addNewArticle(articleMap);
			if (imageFileList != null && imageFileList.size() != 0) {

				// ÷���� �̹������� for���� �̿��� ���ε�
				for (ImageVO imageVO : imageFileList) {
					imageFileName = imageVO.getImageFileName();
					File srcFile = new File(ARTICLE_IMAGE_REPO + "\\" + "temp" + "\\" + imageFileName);
					File destDir = new File(ARTICLE_IMAGE_REPO + "\\" + articleNO);
					// destDir.mkdirs();
					FileUtils.moveFileToDirectory(srcFile, destDir, true);

				}
			}
			// �� ���� �߰��� �� �޽����� ����
			message = "<script>";
			message += " alert('�� ���� �߰��߽��ϴ�.');";
			message += " location.href='" + multipartRequest.getContextPath() + "/board/listArticles.do'; ";
			message += " </script>";

			resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);

		} catch (Exception e) {

			if (imageFileList != null && imageFileList.size() != 0) {

				// ���� �߻� �� temp ������ �̹������� ��� ����
				for (ImageVO imageVO : imageFileList) {
					imageFileName = imageVO.getImageFileName();
					File srcFile = new File(ARTICLE_IMAGE_REPO + "\\" + "temp" + "\\" + imageFileName);
					srcFile.delete();
				}
			}
			// ���� �߻� �� ���� �޼��� ����
			message = "<script>";
			message += " alert('������ �߻��߽��ϴ�. �ٽ� �õ��� �ּ���.');";
			message += " location.href='" + multipartRequest.getContextPath() + "/board/articleForm.do'; ";
			message += " </script>";

			resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
			e.printStackTrace();
		}
		return resEnt;

	} */

//�α��� ��, �Խ��� �� �Է� ��
	@RequestMapping(value = "/board/*Form.do", method = RequestMethod.GET)
	private ModelAndView form(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// �۾��� â�� ��Ÿ��
		String viewName = (String) request.getAttribute("viewName");
		ModelAndView mav = new ModelAndView();
		mav.setViewName(viewName);
		return mav;
	}

	// ���ε��� ���� �̸��� ���� �� ��ȯ

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

	// �̹��� ���� �̸��� ����� List ��ȯ
	/*
	 private List<String> upload(MultipartHttpServletRequest multipartRequest) throws Exception {
	 
	 List<String> fileList = new ArrayList<String>(); 
	 Iterator<String> fileNames = multipartRequest.getFileNames(); 
	 while (fileNames.hasNext()) { 
		 String fileName = fileNames.next(); 
		 MultipartFile mFile = multipartRequest.getFile(fileName); 
		 String originalFileName = mFile.getOriginalFilename(); // ÷���� �̹��� ������ �̸����� ���ʴ�� ����
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
		// �޼��� �Ű� ������ �� ��û �Ű� ������ ���ε��Ǿ�� ���� ��Ÿ���� �ּ�
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
		// ��ȯ �������̽�, �����忡 ������ ������ ���, HashTable�� Vector���� ��� ����
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
			message += " alert('���� �����߽��ϴ�.');";
			message += " location.href='" + multipartRequest.getContextPath() + "/board/viewArticle.do?articleNO="
					+ articleNO + "';";
			message += " </script>";

			resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);

		} catch (Exception e) {
			File srcFile = new File(ARTICLE_IMAGE_REPO + "\\" + "temp" + "\\" + imageFileName);
			srcFile.delete();

			message = "<script>";
			message += " alert('������ �߻��߽��ϴ�. �ٽ� �õ��� �ּ���.');";
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
		// HTTP �����ڵ带 �����ϰ� ���� �����Ϳ� �Բ� ������ �� �־� ������ ��� �ʿ��� ��� ���
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "text/html; charset=UTF-8");

		try {
			boardService.removeArticle(articleNO);
			File destDir = new File(ARTICLE_IMAGE_REPO + "\\" + articleNO);
			FileUtils.deleteDirectory(destDir);

			message = "<script>";
			message += " alert('���� �����߽��ϴ�.');";
			message += " location.href='" + request.getContextPath() + "/board/listArticles.do';";
			message += " </script>";

			resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);

		} catch (Exception e) {

			message = "<script>";
			message += " alert('������ �߻��߽��ϴ�. �ٽ� �õ��� �ּ���.');";
			message += " location.href='" + request.getContextPath() + "/board/listArticles.do';";
			message += " </script>";

			resEnt = new ResponseEntity(message, responseHeaders, HttpStatus.CREATED);
			e.printStackTrace();

		}

		return resEnt;
	}

}
