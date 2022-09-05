package site.metacoding.red.web.dto.response.boards;

import lombok.Getter;
import lombok.Setter;
import site.metacoding.red.web.dto.request.boards.PagingDto;

@Setter
@Getter
public class MainDto {
	private Integer id;
	private String title;
	private String username;
}
