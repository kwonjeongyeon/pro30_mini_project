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

		// id가 selectAllAriclesList인 SQL문을 요청
		List<ArticleVO> articlesList = sqlSession.selectList("mapper.board.selectAllArticlesList");
		return articlesList;
	}

	@Override
	public int insertNewArticle(Map articleMap) throws DataAccessException {
		int articleNO = selectNewArticleNO(); // 새 글에 대한 글 번호 가져옴
		articleMap.put("articleNO", articleNO); // 글 번호 articleMap에 저장
		sqlSession.insert("mapper.board.insertNewArticle", articleMap);
		// id에 대한 insert문을 호출하면서 articleMap을 전달
		return articleNO;
	} // 새 글에 대한 글 번호를 조회한 후 전달된 articleMap에 글 번호 설정

	private int selectNewArticleNO() throws DataAccessException {
		return sqlSession.selectOne("mapper.board.selectNewArticleNO");
	}

}
