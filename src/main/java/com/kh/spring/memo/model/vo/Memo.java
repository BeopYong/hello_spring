package com.kh.spring.memo.model.vo;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//@Getter
//@Setter
//@ToString
@Data // Equivalent to @Getter @Setter @RequiredArgsConstructor @ToString @EqualsAndHashCode. 다 포함하고 있음. 
@NoArgsConstructor
@AllArgsConstructor // @Data에 포함되어 있지만 따로 선언해주는게 좋음
public class Memo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int no;
	private String memo;
	private String password;
	private Date regDate;
}
