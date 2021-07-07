package com.kh.ajax;

import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;

@Controller
public class AjaxController {

	// * 응답데이터를 돌려줄 때의 방식
	/*
	 * 1. HttpServletResponse 객체를 이용해서 응답하는 방법
	 *    (기존 jsp/servlet때 Stream을 통해서 응답했던 방식)

	@RequestMapping("ajax1.do")
	public void ajaxMethod1(String name, int age, HttpServletResponse response) throws IOException {
		System.out.println(name);
		System.out.println(age);
		
		// 요청 처리 다 했다는 가정하에 현재 그 페이지에 다시 돌려줄 응답데이터가 있을 경우
		String responseData = "응답문자열 : " + name + "는 " + age + "살 입니다.";
		
		response.setContentType("text/html; charset=utf-8");
		response.getWriter().print(responseData);
		
	}
	 */
	
	/*
	 * 2. 응답할 데이터를 문자열로 곧바로 리턴 (@ResponseBody)
	 *    원래 문자열을 리턴하면 포워딩방식이라 해당 뷰를 탐색한다
	 *    따라서 내가 리턴하는 문자열이 응답뷰가 아닌 응답데이터야 라는걸 선언하는 어노테이션 => @ResponseBody 기재
	 *    
	 *    응답데이터에 한글이 있을 경우 @RequestMapping에서 produces속성으로 contentType 기술해야됨
	 */
	@ResponseBody
	@RequestMapping(value="ajax1.do", produces="text/html; charset=utf-8")
	public String ajaxMethod1(String name, int age) {

		String responseData = "응답문자열 : " + name + "은 " + age + "살 입니다.";
		return responseData;

	}
	
	/*
	 * 여러개의 응답데이터를 응답하고자 할 때의 방법
	 * 
	@RequestMapping("ajax2.do")
	public void ajaxMethod2(int userNo, HttpServletResponse response) throws IOException {
		
		// Member m = mService.selectMembr(userNo);
		Member m = new Member("user01", "pass01", "홍길동", 20, "aaa@naver.com", "01000001111");
		
		//response.getWriter().print(m);
		JSONObject jObj = new JSONObject();
		jObj.put("userId", m.getUserId());
		jObj.put("userPwd", m.getUserPwd());
		jObj.put("userName", m.getUserName());
		jObj.put("age", m.getAge());
		jObj.put("email", m.getEmail());
		jObj.put("phone", m.getPhone());
		
		response.setContentType("application/json; charset=utf-8");
		response.getWriter().print(jObj);
		
		int count = 10;
		String name="강보람";
	}
	 */
	/*
	@ResponseBody
	@RequestMapping(value="ajax2.do", produces="application/json; charset=utf-8")
	public String ajaxMethod2(int userNo) {
		
		// Member m = mService.selectMembr(userNo);
		Member m = new Member("user01", "pass01", "홍길동", 20, "aaa@naver.com", "01000001111");
		
		//response.getWriter().print(m);
		JSONObject jObj = new JSONObject();
		jObj.put("userId", m.getUserId());
		jObj.put("userPwd", m.getUserPwd());
		jObj.put("userName", m.getUserName());
		jObj.put("age", m.getAge());
		jObj.put("email", m.getEmail());
		jObj.put("phone", m.getPhone());
		
		return jObj.toJSONString();
	}
	*/
	
	@ResponseBody
	@RequestMapping(value="ajax2.do", produces="application/json; charset=utf-8")
	public String ajaxMethod2(int userNo) {
		
		// Member m = mService.selectMembr(userNo);
		Member m = new Member("user01", "pass01", "홍길동", 20, "aaa@naver.com", "01000001111");
		
		return new Gson().toJson(m);
	}
	
	@ResponseBody
	@RequestMapping(value="ajax3.do", produces="application/json; charset=utf-8")
	public String ajaxMethod3() {
		
		// ArrayList<Member> list = mService.selectMemberList();
		ArrayList<Member> list = new ArrayList<Member>();
		list.add(new Member("user01", "pass01", "홍길동", 20, "aaa@naver.com", "01000001111"));
		list.add(new Member("user02", "pass02", "김말똥", 22, "222@naver.com", "01000002222"));
		list.add(new Member("user03", "pass03", "박말순", 23, "333@naver.com", "01000003333"));
		
		//[{}, {}, {}]
		
		return new Gson().toJson(list);

	}
	
	
	
	
	
	
}
