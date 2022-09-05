package site.metacoding.red.web;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import lombok.RequiredArgsConstructor;
import site.metacoding.red.domain.boards.BoardsDao;
import site.metacoding.red.domain.users.Users;
import site.metacoding.red.web.dto.request.boards.WriteDto;
import site.metacoding.red.web.dto.response.boards.MainDto;

@RequiredArgsConstructor
@Controller
public class BoardsController {

	private final HttpSession session;
	private final BoardsDao boardsDao;
	// @PostMapping("/boards/{id}/delete")
	// @PostMapping("/boards/{id}/update")

	@PostMapping("/boards")
	public String writeBoards(WriteDto writeDto) {
		// 1번 세션에 접근해서 세션값을 확인한다. 그때 Users로 다운캐스팅하고 키값은 principal로 한다.
		Users principal = (Users) session.getAttribute("principal");

		// 2번 pricipal null인지 확인하고 null이면 loginForm 리다이렉션해준다.
		if (principal == null) {
			return "redirect:/loginForm";
			// writeDto.toEntity가 boards가 되서 new 할 필요가 없다
			// Dto에 있는건 그대로 넣으면 되고 UsersId는 세션에서 가져온다
			// 보드스에 인서트 할건데 디티오를 인서트(사용자로부터 받은 값으로)할건데 투엔터티중에 모자란 유저 아이디를 가져온다
		}

		// 3번 BoardsDao에 접근해서 insert 메서드를 호출한다.
		// 조건 : dto를 entity로 변환해서 인수로 담아준다.
		// 조건 : entity에는 세션의 principal에 getId가 필요하다.
		boardsDao.insert(writeDto.toEntity(principal.getId()));

		return "redirect:/"; // 메인페이지 이동
	}

	@GetMapping({ "/", "/boards" })
	public String getBoardList(Model model) {
		// 조건 : xml 파일 들어갔을 때 쿼리문이 존재해야 한다
		// 조건 : join 해야 하는지 구별해야 한다 UI 나올 때 작성자 이름이 없으면 join 할 필요가 없다. 있을 경우에는 다른 테이블에
		// 정보가 있으니 join 해야 한다
		// xml 파일에서 메인이 되는 테이블을 생각해야 한다
		// 개발시에는 테이블에 필요한 데이터를 다 넣어 놓고 최종 완료시에 필요한 데이터들만 남겨놓는게 좋다
		// mapper -> spring과 db, dto -> 클라이언트와 controller의 통신
		List<MainDto> boardsList = boardsDao.findAll();
		model.addAttribute("boardsList", boardsList);
		return "boards/main";
	}

	@GetMapping("/boards/{id}")
	public String getBoardList(@PathVariable Integer id, Model model) {
		model.addAttribute("boards", boardsDao.findById(id));
		return "boards/detail";
	}

	@GetMapping("/boards/writeForm")
	public String writeForm() {// 글쓰기는 항상 이공식 사용
		Users principal = (Users) session.getAttribute("principal");
		if (principal == null) {
			return "redirect:/loginForm";
		}

		return "boards/writeForm";
	}
}
