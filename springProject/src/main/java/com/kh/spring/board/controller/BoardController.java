package com.kh.spring.board.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.kh.spring.board.model.service.BoardService;
import com.kh.spring.board.model.vo.Board;
import com.kh.spring.board.model.vo.Reply;
import com.kh.spring.common.model.vo.PageInfo;
import com.kh.spring.common.template.Pagination;

@Controller
public class BoardController {
	
	@Autowired
	private BoardService bService;
	
	/*
	@RequestMapping("list.bo")
	public String selectBoardList(@RequestParam(value="currentPage", defaultValue="1") int currentPage, Model model) {
		
		int listCount = bService.selectListCount();
		PageInfo pi = Pagination.getPageInfo(listCount, currentPage, 10, 5);
		
		ArrayList<Board> list = bService.selectList(pi);
		
		model.addAttribute("pi", pi);
		model.addAttribute("list", list);
		
		return "board/boardListView";
	}
	*/
	
	@RequestMapping("list.bo")
	public ModelAndView selectBoardList(@RequestParam(value="currentPage", defaultValue="1") int currentPage, ModelAndView mv) {
		
		int listCount = bService.selectListCount();
		PageInfo pi = Pagination.getPageInfo(listCount, currentPage, 10, 5);
		
		ArrayList<Board> list = bService.selectList(pi);
		
		/*
		mv.addObject("pi", pi);
		mv.addObject("list", list);
		mv.setViewName("board/boardListView");
		*/
		mv.addObject("pi", pi)
		  .addObject("list", list)
		  .setViewName("board/boardListView");
		
		return mv;
	}
	
	@RequestMapping("enrollForm.bo")
	public String enrollForm() {
		return "board/boardEnrollForm";
	}
	
	@RequestMapping("insert.bo")
	public String insertBoard(Board b, MultipartFile upfile, HttpSession session, Model model) {
		
		/*
		 * 만약 다중 첨부파일 업로드 기능일 겨웅?
		 * <input type="file"> 요소들 다 동일한 키값으로 부여
		 * Controller쪽에서 매개변수로 MultipartFile[] 키값 으로 받아오면됨
		 */
		
		
		
		//System.out.println(b);
		//System.out.println(upfile.getOriginalFilename());
		// 곧바로 받아내지지 않음 => 파일업로드 관련 라이브러리 추가 + 파일업로드 관련 클래스 빈등록
		
		// 전달된 파일이 있을 경우 => 파일명 수정 작업 후 서버에 업로드 => 파일원본명,실제서버에업로드된경로를 b 추가로 담기
		if(!upfile.getOriginalFilename().equals("")) {
			
			/*
			String savePath = session.getServletContext().getRealPath("/resources/uploadFiles/");
			
			String originName = upfile.getOriginalFilename();  // 원본명 ("aaa.jpg")
			
			String currentTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
			int ranNum = (int)(Math.random() * 900000 + 10000);
			String ext = originName.substring(originName.lastIndexOf("."));
			
			String changeName = currentTime + ranNum + ext;
			
			try {
				upfile.transferTo(new File(savePath + changeName));
			} catch (IllegalStateException | IOException e) {
				e.printStackTrace();
			}
			*/
			String changeName = saveFile(session, upfile);
			
			b.setOriginName(upfile.getOriginalFilename());
			b.setChangeName("resources/uploadFiles/" + changeName); // 업로드된파일명+파일명
			
		}
		
		int result = bService.insertBoard(b); // servieImpl, dao, sql 작성
		
		if(result>0) {
			// 성공했을 경우
			session.setAttribute("alertMsg", "성공적으로 게시글 작성되었습니다.");
			return "redirect:list.bo";
			
		}else {
			// 실패했을 경우 
			model.addAttribute("errorMsg", "게시글 작성 실패");
			return "common/errorPage";
		}
		
	}
	
	@RequestMapping("detail.bo")
	public String selectBoard(int bno, Model model) {
		int result= bService.increaseCount(bno);
		
		if(result>0) {
			Board b = bService.selectBoard(bno);
			model.addAttribute("b", b);
			return "board/boardDetailView";
			
		}else {
			model.addAttribute("errorMsg", "게시글 조회 실패");
			return "common/errorPage";
		}
	}
	
	@RequestMapping("delete.bo")
	public String deleteBoard(int bno, String filePath, 
							  HttpSession session, Model model) {
			// filePath : 첨부파일 존재했다면 "resources/uploadFiles/xxxxx.pdf"
			// filePath : 첨부파일 존재하지 않았다면 ""
		
		int result = bService.deleteBoard(bno);
		
		if(result>0) {
			
			 // 첨부파일이 있었을 경우 => 서버에 업로드 된 파일 찾아서 삭제
			if(!filePath.equals("")) {
				new File(session.getServletContext().getRealPath(filePath)).delete();
			}
			
			session.setAttribute("alertMsg", "성공적으로 게시글이 삭제되었습니다.");
			return "redirect:list.bo";				
			
		}else {
			
			model.addAttribute("errorMsg", "게시글 삭제 실패");
			return "common/errorPage";
			
		}
	}
	
	@RequestMapping("updateForm.bo")
	public String updateForm(int bno, Model model) {
		//Board b = bService.selectBoard(bno);
		//model.addAttribute("b", b);
		model.addAttribute("b", bService.selectBoard(bno));
		return "board/boardUpdateForm";
	}
	
	@RequestMapping("update.bo")
	public String updateBoard(Board b, MultipartFile reupfile, HttpSession session, Model model) {
		
		if(!reupfile.getOriginalFilename().equals("")) { // 새로넘어온 첨부파일 있음
			
			// 새로 넘어온 첨부파일있는데 기존의 첨부파일이 있었을 경우 => 서버에 업로드 되어있는 기존의 파일 찾아서 지울거임
			if(b.getChangeName() != null) {
				new File(session.getServletContext().getRealPath(b.getChangeName())).delete();
			}
			
			// 그리고 새로 넘어온 첨부파일 서버에 업로드시킴
			String changeName = saveFile(session, reupfile);
			b.setOriginName(reupfile.getOriginalFilename());
			b.setChangeName("resources/uploadFiles/" + changeName);
			
		}
		
		/*
		 * case 1. 새로 첨부된 파일 x, 기존의 첨부파일 x
		 *      => originName : null, changeName: null
		 *      
		 * case 2. 새로 첨부된 파일 x, 기존의 첨부파일 o
		 *      => originName : 기존의 첨부파일 원본명, changeName : 기존의 첨부파일 수정명
		 * 
		 * case 3. 새로 첨부된 파일 o, 기존의 첨부파일 x
		 *      => originName : 새로운 첨부파일 원본명, changeName : 새로운 첨부파일 수정명 
		 * 
		 * case 4. 새로 첨부된 파일 o, 기존의 첨부파일 o
		 *      => originName : 새로운 첨부파일 원본명, changeName : 새로운 첨부파일 수정명
		 */
		int result = bService.updateBoard(b);
		
		if(result>0) { // 성공 => 상세보기 페이지
			session.setAttribute("alertMsg", "게시글이 성공적으로 수정되었습니다");
			return "redirect:detail.bo?bno=" + b.getBoardNo();
		}else { // 실패 => 에러문구 담아서 => 에러페이지 포워딩
			model.addAttribute("errorMsg", "게시글 수정 실패");
			return "common/errorPage";
		}
		
		
		
	}
	
	
	
	
	// 전달받은 첨부파일 수정명 작업해서 서버에 업로드 시킨 후 해당 수정된파일명(서버에 업로된파일명)을 반환하는 메소드
	public String saveFile(HttpSession session, MultipartFile upfile) {
		
		String savePath = session.getServletContext().getRealPath("/resources/uploadFiles/");
		
		String originName = upfile.getOriginalFilename();  // 원본명 ("aaa.jpg")
		
		String currentTime = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		int ranNum = (int)(Math.random() * 900000 + 10000);
		String ext = originName.substring(originName.lastIndexOf("."));
		
		String changeName = currentTime + ranNum + ext;
		
		try {
			upfile.transferTo(new File(savePath + changeName));
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
		}
		
		return changeName;
		
	}
	
	@ResponseBody
	@RequestMapping(value="rlist.bo", produces="application/json; charset=utf-8")
	public String ajaxSelectReplyList(int bno) {
		// ArrayList<Reply> list = bService.selectReplyList(bno);
		//return new Gson().toJson(list);
		
		return new Gson().toJson(bService.selectReplyList(bno));
	}
	
	@ResponseBody
	@RequestMapping("rinsert.bo")
	public String ajaxInsertReply(Reply r) {
		int result = bService.insertReply(r);
		if(result>0) {
			return "success";
		}else {
			return "fail";
		}
	}
	
	
	
	
	
	
	

}
