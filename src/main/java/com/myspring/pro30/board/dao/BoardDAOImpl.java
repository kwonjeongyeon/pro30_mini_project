package com.myspring.pro30.board.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.myspring.pro30.board.vo.ArticleVO;
import com.myspring.pro30.board.vo.ImageVO;

@Repository("boardDAO")
public class BoardDAOImpl implements BoardDAO {

	@Autowired
	private SqlSession sqlSession;

	@Override
	public List selectAllArticlesList() throws DataAccessException {

		// id�� selectAllArticlesList�� SQL���� ��û
		List<ArticleVO> articlesList = sqlSession.selectList("mapper.board.selectAllArticlesList");
		return articlesList;
	}

//�� ������ �Խ��� ���̺� �߰��� �� �� ��ȣ�� ��ȯ
	@Override
	public int insertNewArticle(Map articleMap) throws DataAccessException {
		int articleNO = selectNewArticleNO(); // �� �ۿ� ���� �� ��ȣ ������
		articleMap.put("articleNO", articleNO); // �� ��ȣ articleMap�� ����
		sqlSession.insert("mapper.board.insertNewArticle", articleMap);
		// id�� ���� insert���� ȣ���ϸ鼭 articleMap�� ����
		return articleNO;
	} // �� �ۿ� ���� �� ��ȣ�� ��ȸ�� �� ���޵� articleMap�� �� ��ȣ ����

	/* @Override
	public void insertNewImage(Map articleMap) throws DataAccessException {
		List<ImageVO> imageFileList = (ArrayList) articleMap.get("imageFileList");
		// articleMap�� �۹�ȣ ������
		int articleNO = (Integer) articleMap.get("articleNO");
		// �̹��� ��ȣ ������
		int imageFileNO = selectNewImageFileNO();

		// ImageVO ��ü�� ���ʴ�� ������ �̹��� ��ȣ�� �� ��ȣ �Ӽ��� ����
		for (ImageVO imageVO : imageFileList) {
			imageVO.setImageFileNO(++imageFileNO);
			imageVO.setArticleNO(articleNO);
		}
		sqlSession.insert("mapper.board.insertNewImage", imageFileList);
	}

	private int selectNewImageFileNO() throws DataAccessException {
		return sqlSession.selectOne("mapper.board.selectNewImageFileNO");
	} */

	private int selectNewArticleNO() throws DataAccessException {
		return sqlSession.selectOne("mapper.board.selectNewArticleNO");
	}

	@Override
	public ArticleVO selectArticle(int articleNO) throws DataAccessException {
		return sqlSession.selectOne("mapper.board.selectArticle", articleNO);
	}

	@Override
	public void updateArticle(Map articleMap) throws DataAccessException {
		sqlSession.selectOne("mapper.board.updateArticle", articleMap);
	}

	@Override
	public void deleteArticle(int articleNO) throws DataAccessException {
		sqlSession.selectOne("mapper.board.deleteArticle", articleNO);
	}

}
