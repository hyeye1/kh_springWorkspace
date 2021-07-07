package com.kh.spring.member.model.service;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.spring.member.model.dao.MemberDao;
import com.kh.spring.member.model.vo.Member;

//@Component
@Service // 보다 구체화된 즉, Service의 역할을 가진 클래스라는 걸 보여주기 위한 어노테이션
public class MemberServiceImpl implements MemberService {

	@Autowired
	private SqlSessionTemplate sqlSession;
	
	@Autowired
	private MemberDao mDao;
	
	@Override
	public Member loginMember(Member m) {
		
		//Member loginUser = mDao.loginMember(sqlSession, m);
		//return loginUser;
		return mDao.loginMember(sqlSession, m);
	}

	@Override
	public int insertMember(Member m) {
		//int result = mDao.insertMember(sqlSession, m);
		//return result;
		return mDao.insertMember(sqlSession, m);
	}

	@Override
	public int updateMember(Member m) {
		return mDao.updateMember(sqlSession, m);
	}

	@Override
	public int deleteMember(String userId) {
		return mDao.deleteMember(sqlSession, userId);
	}

	@Override
	public int idCheck(String userId) {
		return mDao.idCheck(sqlSession, userId);
	}

}
