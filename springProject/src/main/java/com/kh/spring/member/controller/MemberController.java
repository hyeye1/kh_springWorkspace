package com.kh.spring.member.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.kh.spring.member.model.service.MemberService;
import com.kh.spring.member.model.vo.Member;

@Controller // 다음과 같이 Controller타입의 어노테이션을 붙여주면 빈 스캐닝을 통해서 자동으로 빈으로 등록될꺼임 (스프링이 알아서 관리할꺼임)
public class MemberController {
	
	// DI (Dependency Injection)
	// Autowired 타입의 어노테이션을 붙여주면 내가 직접 생성할 필요없이
	// 등록되어 있는 빈들 중에서 해당 타입과 맞는 클래스를 찾아서 자동으로 생성 후 주입해줌!!
	@Autowired
	private MemberService mService;
	@Autowired
	private BCryptPasswordEncoder bcryptPasswordEncoder;
	

	/*
	@RequestMapping("login.me") // @RequestMapping타입의 어노테이션을 붙여줌으로써 HandlerMapping등록
	public String loginMember() {
		System.out.println("실행되나?");
		
		// 응답페이지로 WEB-INF/views/main.jsp
		return "main"; 
		// 여기서 반환되는 문자열로 servlet-context.xml파일에 기술되어있는 ViewResolver 관련한 부분에
		// 기술되어있는 접두어랑 접미어로 인해서
		// 앞에는 /WEB-INF/views/ 붙고 뒤에는 .jsp를 붙여서 만들어진 경로로 응답 jsp를 찾음
		// /WEB-INF/views/main.jsp
	}
	*/
	
	
	// * 파라미터(요청시 전달값)를 전송받는 방법 (요청시 전달되는 값들 처리방법)
	/*
	 * 1. HttpServletRequest를 통해 전송받기 (기존의 jsp/servlet 때의 방식)
	 *    해당 메소드의 매개변수로 HttpServletRequest 작성하면 
	 *    해당 메소드 자동으로 실행시 스프링컨테이너가 자동으로 해당 객체를 인자로 주입함
	 */
	/*
	@RequestMapping("login.me")
	public String loginMember(HttpServletRequest request) {
		
		String userId = request.getParameter("id");
		String userPwd = request.getParameter("pwd");
		
		System.out.println("ID : " + userId);
		System.out.println("PWD : " + userPwd);
		
		return "main";
	}
	*/
	
	
	/*
	 * 2. @RequestParam 어노테이션 방식
	 *    스프링에서 조금 더 간단하게 파라미터를 받아올 수 있는 방법 제공
	 *    HttpServletRequest와 비슷하게 request객체를 이용해서 데이터 전송받는 방법
	 */
	/*
	@RequestMapping("login.me")
	public String loginMember(@RequestParam("id") String userId, 
							  @RequestParam(value="pwd", defaultValue="aaaa") String userPwd) {
		
		System.out.println("ID : " + userId);
		System.out.println("PWD : " + userPwd);
		
		return "main";
	}
	*/
	
	/*
	 * 3. @RequestParam 어노테이션 생략하는 방법
	 *    요청시 전달되는 키값과 받아주고자하는 매개변수명과 일치할 경우
	 *    해당 그 매개변수에 값이 바로 담기게 됨!!
	 * 
	 */
	/*
	@RequestMapping("login.me")
	public String loginMember(String userId, String userPwd) {
		
		System.out.println("ID : " + userId);
		System.out.println("PWD : " + userPwd);
		
		Member m = new Member();
		m.setUserId(userId);
		m.setUserPwd(userPwd);
		
		return "main";
	}
	*/
	
	/*
	 * 4. 곧바로 vo객체의 필드에 담는 방법
	 *    커맨드객체 방식이라고 얘기함
	 *    스프링컨테이너가 해당 매개변수로 있는 객체를 기본생성자로 생성한 후
	 *    setter메소드로 값을 주입해줌!!!!
	 *    
	 *    * 주의사항 
	 *    - 반드시 요청시 전달값의 키값(name속성값)과 내가 담고자하는 객체의 필드명이 일치해야됨
	 *    - 반드시 해당 vo클래스에 기본생성자, setter메소드 무조건 있어야됨
	 */
	/*
	@RequestMapping("login.me")
	public String loginMember(Member m, HttpSession session) {
		
		//System.out.println("ID : " + m.getUserId());
		//System.out.println("PWD : " + m.getUserPwd());
		
		//MemberService mService = new MemberServiceImpl();
		// 직접 객체 생성 (코드로써 new 키워드)을 하게 되면 => 결합도 높음!
		// 결합도가 높을 때 발생할 수 있는 문제
		// 1. 클래스명이 변경됏을 경우 => 컴파일 에러 발생 
		// 2. 메모리영역 매번 새로이 생성됏다가 소멸되는 과정이 반복됨
		//    (즉, 10000건의 요청이 들어오면 10000개의 객체가 생성됏다 소멸된다!!)
		
		// 위의 문제점을 해소하고자 한다면 => 결합도 낮춰주면됨!! (즉, 직접 new 로 객체 생성 안하면됨)
		// => 우선 스프링컨테이너가 해당 MemberServiceImple객체를 관리할 수 있도록 해당 클래스를 "bean으로 등록"
		// => DI(의존성 주입)를 통해서 생성된 객체 받아다 쓰면됨
		
		Member loginUser = mService.loginMember(m);
		
		if(loginUser == null) { // 로그인 실패 => 에러문구request에 담은 후 => 에러페이지
			//System.out.println("로그인 실패");
			
			// 포워딩 방식 (/WEB-INF/views/common/errorPage.jsp)
			return "common/errorPage";    // /WEB-INF/views/common/errorPage.jsp
			
		}else { // 로그인 성공 => loginUser를 session에 담은 후 => 메인페이지
			//System.out.println("로그인 성공");
			session.setAttribute("loginUser", loginUser);
			
			// url 재요청 방식
			return "redirect:/";
		}
		
	}
	*/
	
	// 이제는 요청 처리 후 응답페이지에 응답할 데이터가 있을경우 request scope영역에 담는 방법
	/*
	 * 1. Model이라는 객체를 사용하는 방법
	 *    응답뷰로 전달하고자 하는 데이터를 맵형식(key,value)로 담을 수 있다.
	 *    scope는 request임
	 *    단, setAttribute가 아닌 addAttribute를 이용해서 담음
	 */
	/*
	@RequestMapping("login.me")
	public String loginMember(Member m, HttpSession session, Model model) {
		Member loginUser = mService.loginMember(m);
		if(loginUser == null) { // 로그인 실패
			model.addAttribute("errorMsg", "로그인 실패!");
			return "common/errorPage";
		}else { // 로그인 성공
			session.setAttribute("loginUser", loginUser);
			return "redirect:/";
		}
	}
	*/
	
	/*
	 * 2. ModelAndView 객체를 사용하는 방법
	 *    Model은 전달하고자 하는 데이터를 맵형식으로 담는 공간이라고 한다면
	 *    View는 RequestDispatcher처럼 forward 할 뷰 페이지 정보를 담을 수 있는 공간
	 *    
	 *    ModelAndView는 이 두가지를 합쳐 놓은 객체
	 *    
	 *    addObject를 이용해서 전달하고자하는 데이터를 맵형식으로 담기 (request scope)
	 *    setViewName을 이용해서 응답할 뷰에 대한 정보 담기
	 */
	@RequestMapping("login.me")
	public ModelAndView loginMember(Member m, HttpSession session, ModelAndView mv) {
		
		/* 암호화 작업 전 (아이디와 비밀번호 대조)
		Member loginUser = mService.loginMember(m);
		if(loginUser == null) { // 로그인 실패
			mv.addObject("errorMsg", "로그인실패");
			mv.setViewName("common/errorPage");
		}else { // 로그인 성공
			session.setAttribute("loginUser", loginUser);
			mv.setViewName("redirect:/");
		}
		
		return mv;
		*/
		
		
		// 암호화 작업 후 (단지 아이디 대조만)
		Member loginUser = mService.loginMember(m);
		// 아이디만을 가지고 조회해옴 (실제db에 저장되어있는 비번 암호문)
		
		if(loginUser != null && bcryptPasswordEncoder.matches(m.getUserPwd(), loginUser.getUserPwd())) {
			// 로그인 성공
			session.setAttribute("loginUser", loginUser);
			mv.setViewName("redirect:/");
		}else {
			// 로그인 실패
			mv.addObject("errorMsg", "로그인실패");
			mv.setViewName("common/errorPage");
		}
		
		return mv;
		
		
	}
	
	
	@RequestMapping("logout.me")
	public String logoutMember(HttpSession session) {
		session.invalidate();
		return "redirect:/";
	}
	
	@RequestMapping("enrollForm.me")
	public String enrollForm() {
		// /WEB-INF/views/member/memberEnrollForm.jsp
		return "member/memberEnrollForm";
	}
	
	@RequestMapping("insert.me")
	public String insertMember(Member m, HttpSession session, Model model) {
		
		//System.out.println(m);
		
		/*
		 * 1. 한글이 깨져있는거 확인됨
		 *    post방식이기 때문에 인코딩 해야됨!!
		 *    => 스프링에서 제공하는 인코딩 필터를 web.xml에 등록만 하면 해결 가능!!
		 *    
		 * 2. 나이를 입력하지 않고 요청시에 age키값으로 "" 빈문자열이 넘어오기 때문에 
		 *    Member 객체의 int형의 age필드에 "" 문자열을 담을 수 없어서 문제 발생!!
		 *    => Member클래스에 age필드를 int형 -> String형으로 변경
		 *    
		 * 3. 비밀번호가 사용자가 입력한 그래로의 값!! (평문)
		 *    우리는 암호화 작업을 거쳐서 db에 저장해야됨
		 *    
		 *    기존에 SHA-512 암호화방식은 단방향 암호화방식 (복호화가 불가능한 방식 => 암호문을 가지고 평문을 알아낼 수 없음)
		 *    복호화가 안전하지 않기 때문에 암호화만 가능한 방식을 했던 것!! => 이마저도 안전하지 않다고 함!
		 *     > 사용자가 111이란 값은 매번 입력할 때 마다 매번 같은 암호문이 나와버림
		 *       똑같은 평문을 입력할때마다 매번 똑같은 암호문으로 고정
		 *       다양한 샘플이 확보가 되고 나서부터 암호문을 통해 평문을 유추할 수 있음
		 *         => 레인보우 테이블라는게 나와버림 
		 *    
		 *    위의 문제들을 해결하기 위해 솔팅(salting) 이라는 기법이 추가됨!!
		 *    평문 + salt값(랜덤값) == 솔팅기법
		 *    => 사용자가 아무리 똑같은 평문을 입력해도 매번 다른 랜덤값이 붙어서 암호화됨 => 매번 다른 암호문
		 *    
		 *    => BCrypt방식이라고 함!! => 스프링 시큐리티 모듈에서 제공하고 있음
		 *    
		 *    => 스프링 시큐리티 관련 라이브러리 다운!! (pom.xml 기술)
		 *       => BCryptPasswordEncoder를 제공
		 */
		//System.out.println("암호화 전 : " + m.getUserPwd());
		
		// 암호화 작업
		String encPwd = bcryptPasswordEncoder.encode(m.getUserPwd());
		//System.out.println("암호화 후 : " + encPwd); // 같은 평문을 입력해도 매번 다른 암호문이 나옴
		
		m.setUserPwd(encPwd); // 암호문으로 변경하기
		
		int result = mService.insertMember(m);
		
		if(result > 0) { // 성공 => 알람창 출력할 문구 담아서 => 메인페이지 (url재요청)
			session.setAttribute("alertMsg", "성공적으로 회원가입되었습니다");
			return "redirect:/";
		}else { // 실패 => 에러문구 담아서 => 에러페이지로 포워딩
			model.addAttribute("errorMsg", "회원가입 실패");
			return "common/errorPage";
		}
		
	}
	
	@RequestMapping("myPage.me")
	public String myPage() {
		return "member/myPage";
	}
	
	@RequestMapping("update.me")
	public String updateMember(Member m, HttpSession session, Model model) {
		
		// 회원정보변경용 서비스 호출 
		int result = mService.updateMember(m);
		
		if(result > 0) {
			// 정보 수정 성공
			//		>> session에 loginUser키값으로 갱신된 회원객체(mService에 loginMember메소드호출) 담기
			//		>> session에 alertMsg 키값으로 알람창띄우고자하는 메세지 문구(성공적으로 수정되었습니다) 담기
			//		>> myPage.me   url 재요청 
			session.setAttribute("loginUser", mService.loginMember(m));
			session.setAttribute("alertMsg", "성공적으로 수정되었습니다.");
			return "redirect:myPage.me";
			
		}else {
			// 정보 수정 실패
			//		>> request에 errorMsg 키값으로 에러페이지에 띄우고자 하는 메세지 문구(정보수정실패) 담기
			//		>> errorPage가 보여지도록 포워딩
			model.addAttribute("errorMsg", "정보 수정 실패");
			return "common/errorPage";
		}
	}
	
	
	@RequestMapping("delete.me")
	public String deleteMember(String userPwd, HttpSession session, Model model) {
		
		Member loginUser = (Member)session.getAttribute("loginUser");
		// 아이디, 비번(암호문), 이메일, 주소, ....
		
		if(bcryptPasswordEncoder.matches(userPwd, loginUser.getUserPwd())) {
			// 비밀번호 일치 => 본인 맞음
			int result = mService.deleteMember(loginUser.getUserId());
			
			if(result > 0) {
				session.removeAttribute("loginUser");
				session.setAttribute("alertMsg", "성공적으로 탈퇴되었습니다. 그동안 이용해주셔서 감사합니다.");
				
				// => 메인페이지
				return "redirect:/";
				
			}else { // => 에러페이지
				model.addAttribute("errorMsg", "회원탈퇴 실패");
				return "common/errorPage";
			}
			
			
		}else {
			// 비밀번호 일치 x
			session.setAttribute("alertMsg", "비밀번호가 일치하지 않습니다.");
			return "redirect:myPage.me";
		}
		
	}
	
	@ResponseBody
	@RequestMapping("idCheck.me")
	public String ajaxIdCheck(String checkId) {
		
		int count = mService.idCheck(checkId);
		
		/*
		if(count > 0) { // 일치하는게 이미 있음 => 사용 불가능
			return "N";
		}else { // 일치하는게 없음 => 사용가능
			return "Y";
		}
		*/
		
		return count>0 ? "N":"Y";
	}
	
	
	
	
	
	
	
	
	
}
