package com.kh.spring.board.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter @Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Board {
	
	private int boardNo;
	private String boardTitle;
	private String boardWriter;
	private String boardContent;
	private String originName;
	private String changeName; // resources/xxxxxx/xxxxxx.jpg
	private int count;
	private String createDate;
	private String status;

}
