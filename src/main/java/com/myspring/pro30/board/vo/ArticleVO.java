package com.myspring.pro30.board.vo;

import java.net.URLEncoder;
import java.sql.Date;

import org.springframework.stereotype.Component;

//Indicates that an annotated class is a "component". 주석이 달린 클래스가 구성요소임을 나타냄
//Such classes are considered as candidates for auto-detection when using annotation-based configuration and class path scanning. 

@Component("articleVO")
public class ArticleVO {

	private int level;
	private int articleNO;
	private int parentNO;
	private String title;
	private String content;
	private String imageFileName;
	private String id;
	private Date writeDate;

	public ArticleVO() {

	}

	public ArticleVO(int level, int articleNo, int prarentNo, String title, String content, String imageFileName,
			String id, Date writeDate) {
		super();
		this.level = level;
		this.articleNO = articleNO;
		this.parentNO = parentNO;
		this.title = title;
		this.content = content;
		this.imageFileName = imageFileName;
		this.id = id;
		this.writeDate = writeDate;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getArticleNO() {
		return articleNO;
	}

	public void setArticleNO(int articleNO) {
		this.articleNO = articleNO;
	}

	public int getParentNO() {
		return parentNO;
	}

	public void setParentNO(int parentNO) {
		this.parentNO = parentNO;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getImageFileName() {
		return imageFileName;
	}

	public void setImageFileName(String imageFileName) {

		try {
			if (imageFileName != null && imageFileName.length() != 0) {
				this.imageFileName = URLEncoder.encode(imageFileName, "UTF-8");
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("imageFileName 인코딩관련 에러");
		}

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getWriteDate() {
		return writeDate;
	}

	public void setWriteDate(Date writeDate) {
		this.writeDate = writeDate;
	}

}
