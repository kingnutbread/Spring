package com.githrd.www.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.githrd.www.dao.GBoardDao;
import com.githrd.www.dao.ReBoardDao;
import com.githrd.www.util.PageUtil;
import com.githrd.www.vo.BoardVO;

/**
 * 이 클래스는 댓글 게시판 관련 요청을 처리할 클래스
 * 
 * @author 박소연
 * @since 2022.06.08
 * @version v.1.0
 * 
 * 		작업 이력 ]
 * 			2022.06.08 - 담당자 : 박소연
 * 							클래스 제작,
 *  						리스트 보기 요청 처리
 */
@Controller
@RequestMapping("/reBoard")
public class ReBoard {
	@Autowired
	ReBoardDao rDao;
	@Autowired
	GBoardDao gDao;
	
	
	//댓글 게시판 리스트 보기 요청
	@RequestMapping("/reBoardList.blp")
	public ModelAndView reBoardList(ModelAndView mv, PageUtil page, String msg) {
		//할일
		//데이터베이스에서 데이터 가져오기
		
		//PageUtil이 먼저 만들어져야 하기 때문에
		
		//총 게시글 수부터 가져온다.
		int total = rDao.getTotal();
		
		//PageUtil을 셋팅
		page.setPage(total);
		
		// 댓글 리스트 조회
		List<BoardVO> list = rDao.getList(page);
		
		if(msg != null) {
			mv.addObject("MSG", msg);
		}
		
		mv.addObject("LIST", list);
		mv.addObject("PAGE", page);
		mv.setViewName("reBoard/reBoardList");
		
		return mv;
	}
	
	//새 댓글 작성 폼보기 요청
	@RequestMapping(path="/reBoardWrite.blp", method=RequestMethod.POST, params={"id", "nowPage"})
	public ModelAndView reBoardWrite(ModelAndView mv, String id, String nowPage) {
		BoardVO bVO = rDao.getWriterInfo(id);
		
		//데이터 넘겨주고
		mv.addObject("DATA", bVO);
		mv.setViewName("reBoard/reBoardWrite");
		
		return mv;
	}
	
	//댓글 등록 처리 요청
	@RequestMapping("/writeProc.blp")
	public ModelAndView writeProc(ModelAndView mv, BoardVO bVO, String nowPage) {
		int cnt = rDao.addReBoard(bVO);
		
		String view = "/www/reBoard/reBoardList.blp";
		if(cnt == 0) {
			//실패
			view = "/www/reBoard/reBoardWrite.blp";
			mv.addObject("MSG", "게시글 등록에 실패했습니다.");			
		} else {
			//성공
			nowPage = "1";
			mv.addObject("MSG", "게시글 등록에 성공했습니다.");			
		}
		
		mv.addObject("VIEW", view);
		mv.addObject("NOWPAGE", nowPage);
		
		mv.setViewName("reBoard/redirect");
		return mv;
	}
	
	//대댓글 작성 폼 보기 함수
	@RequestMapping("/commentWrite.blp")
	public ModelAndView commentWrite(ModelAndView mv, BoardVO bVO, String nowPage, String vw) {
		bVO = rDao.getCommentData(bVO);
		
		//데이터 셋팅
		mv.addObject("DATA", bVO);
		//뷰 부르고
		mv.setViewName("reBoard/reBoardComment");
		return mv;
	}
	
	//대댓글 등록 처리 함수
	@RequestMapping("/commentProc.blp")
	public ModelAndView writeProc(ModelAndView mv, BoardVO bVO, String nowPage, String vw) {
		
		int result = rDao.addReBoard(bVO);
		
		if(result == 1) {
			//댓글 등록 성공
			mv.addObject("VIEW", "/www/reBoard/reBoardList.blp");
			
		} else {
			//댓글 등록 실패
			mv.addObject("VIEW", "/www/reBoard/commentWrite.blp");
		}
		mv.addObject("NOWPAGE", nowPage);		

		//뷰 부르고
		mv.setViewName("reBoard/redirect");
		return mv;
	}
	
	//댓글 수정 폼보기 요청 함수
	@RequestMapping("/reBoardEdit.blp")
	public ModelAndView reBoardEdit(ModelAndView mv, BoardVO bVO, String nowPage, String vw) {
		bVO = rDao.getEditData(bVO);
		mv.addObject("DATA", bVO);
		mv.setViewName("reBoard/reBoardEdit");
		return mv;
	}
	
	//게시글 수정 처리 함수
	@RequestMapping("/editProc.blp")
	public ModelAndView editProc(ModelAndView mv, BoardVO bVO, String nowPage, String vw) {
		
		int result = rDao.editProc(bVO);
		
		if(result == 1) {
			// 수정 성공
			mv.addObject("VIEW", "/www/reBoard/reBoardList.blp");
		} else {
			// 수정 실패
			mv.addObject("VIEW", "/www/reBoard/reBoardEdit.blp");
		}
		
		//데이터 심고
		mv.addObject("NOWPAGE", nowPage);
		
		//뷰 부르고
		mv.setViewName("reBoard/redirect");
		return mv;
	}
	
	//게시글 삭제 처리 함수
	@RequestMapping("/delReBoard.blp")
	public ModelAndView delReBoard(ModelAndView mv, BoardVO bVO, String nowPage, String vw) {
		rDao.delReBoard(bVO);
		//데이터 심고
		mv.addObject("VIEW", vw);
		mv.addObject("NOWPAGE", nowPage);
		//뷰
		mv.setViewName("reBoard/redirect");
		return mv;
	}
}
