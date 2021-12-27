package com.kh.spring.member.controller;

import java.beans.PropertyEditor;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kh.spring.member.model.service.MemberService;
import com.kh.spring.member.model.vo.Member;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/member")
public class MemberController {

	@Autowired
	private MemberService memberService;
	
	@Autowired
	private BCryptPasswordEncoder bcryptPasswordEncoder;
	
	/**
	 * @RequestMapping("/member")를 선언함으로써 중복 제거
	 * Get/PostMapping은 RequestMapping 자식 어노테이션이므로 위의 RequestMapping의 조건을 포함
	 * method=RequestMethod.GET/POST는 @GetMapping @PostMapping 으로 설정
	 * @return
	 */
//	@RequestMapping("member/memberLogin.do", method=RequestMethod.GET)
	@GetMapping("/memberLogin.do")
	public String memberLogin() {
		return "member/memberLogin";
	}
	
	@PostMapping("/memberLogin.do")
	public String memberLogin(
			@RequestParam String id,
			@RequestParam String password, // 평문(rawPassword)
			RedirectAttributes redirectAttr
	) {
		// 인증
		Member member = memberService.selectOneMember(id); // encodedPassword
		log.info("member = {}", member);
		
		if(member != null && bcryptPasswordEncoder.matches(password, member.getPassword())) {
			// 로그인 성공시
			redirectAttr.addFlashAttribute("msg", "로그인 성공!");
		}
		else {
			// 로그인 실패시
			redirectAttr.addFlashAttribute("msg", "아이디 또는 비밀번호가 일치하기 않습니다.");
		}
		
		return "redirect:/";
	}
	
	
	
	@GetMapping("/memberEnroll.do")
	public String memberEnroll() {
		return "member/memberEnroll";
	}
	
	@PostMapping("/memberEnroll.do")
	public String memberEnroll(Member member, RedirectAttributes redirectAttr) {
		log.info("member = {}", member);
		
		// 비밀번호 암호화 처리
		String rawPassword = member.getPassword(); // 평문
		// 랜덤 salt값을 이용한 hashing처리 : 같은 값이라고 해도 다른 해시값을 갖는다. 보안성↑
		// 		ㄴsha512는 같은 값이면 같은 해시값을 갖는다.
		String encodePassword = bcryptPasswordEncoder.encode(rawPassword); // 암호화 처리
		member.setPassword(encodePassword);
		
		
		// 업무로직
		int result = memberService.insertMember(member);
		
		
		// 리다이렉트후에 session의 속성을 참조할 수 있도록한다.
		redirectAttr.addFlashAttribute("msg", result > 0 ? "회원 가입 성공!" : "회원 가입 실패!");
		return "redirect:/";
	}
	
	/**
	 * 입력받은 생년월일을 문자열에서 데이트포맷으로 변환해서 넘겨줌.
	 * @param binder
	 */
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// boolean allowEmpty - true 빈문자열 ""인 경우 null변환함.
		PropertyEditor editor = new CustomDateEditor(sdf, true);
		// j.u.Date변환시 해당 editor객체 사용
		binder.registerCustomEditor(Date.class, editor);
	}
	
}
