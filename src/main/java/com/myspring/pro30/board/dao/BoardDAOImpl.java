package com.myspring.pro30.board.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import com.myspring.pro30.board.vo.ArticleVO;

@Repository("boardDAO")
public class BoardDAOImpl implements BoardDAO {

	@Autowired
	private SqlSession sqlSession;

	@Override
	public List selectAllArticlesList() throws DataAccessException {

		// id�� selectAllAriclesList�� SQL���� ��û
		List<ArticleVO> articlesList = sqlSession.selectList("mapper.board.selectAllArticlesList");
		return articlesList;
	}

	@Override
	public int insertNewArticle(Map articleMap) throws DataAccessException {
		int articleNO = selectNewArticleNO(); // �� �ۿ� ���� �� ��ȣ ������
		articleMap.put("articleNO", articleNO); // �� ��ȣ articleMap�� ����
		sqlSession.insert("mapper.board.insertNewArticle", articleMap);
		// id�� ���� insert���� ȣ���ϸ鼭 articleMap�� ����
		return articleNO;
	} // �� �ۿ� ���� �� ��ȣ�� ��ȸ�� �� ���޵� articleMap�� �� ��ȣ ����

	private int selectNewArticleNO() throws DataAccessException {
		return sqlSession.selectOne("mapper.board.selectNewArticleNO");
	}

}
