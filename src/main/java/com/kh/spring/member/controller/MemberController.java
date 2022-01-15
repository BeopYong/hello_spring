package com.kh.spring.member.controller;

import java.beans.PropertyEditor;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.kh.spring.member.model.service.MemberService;
import com.kh.spring.member.model.vo.Member;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/member")
@SessionAttributes({"loginMember", "next"})
public class MemberController {

	@Autowired
	private MemberService memberService;
	
	@Autowired
	private BCryptPasswordEncoder bcryptPasswordEncoder;
	
	
	
	/**
	 * Spring 비동기통신1 (bean)
	 *  - jsonView빈을 통해 model에 담긴 속성을 json문자열로 변환후, 응답메시지에 작성
	 *  	(빈으로 등록하지 않으면 /WEB-INF/views/jsonView.jsp를 찾게됨.)
	 */
	@GetMapping("/checkIdDuplicate1.do")
	public String checkIdDuplicate1(@RequestParam String id, Model model) {
		// 아이디중복검사
		Member member = memberService.selectOneMember(id);
		boolean available = (member == null);
		
		model.addAttribute("available", available);
		model.addAttribute("id", id);
		
		
		return "jsonView";
	}
	
	/**
	 * Spring 비동기통신2 (@ResponseBody)
	 * @ResponseBody : 리턴된 자바객체를 그대로 응답메시지에 json문자열로 변환해서 출력.
	 *  - 1. jackson 의존을 통해서
	 *  - 2. RequestMappingHandlerAdapter빈의 MessageConverters List객체에 jacksonMessageConverter빈이 자동 등록
	 *  <annotation-driven/>에 의해 자동처리
	 */
	@GetMapping("/checkIdDuplicate2.do")
	@ResponseBody
	public Map checkIdDuplicate2(@RequestParam String id) {
		// 아이디중복검사
		Member member = memberService.selectOneMember(id);
		boolean available = (member == null);
		
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		map.put("available", available);
		map.put("serverTime", System.currentTimeMillis());
		
		return map;
	}
	
	/**
	 * Spring 비동기통신3 (ResponseEntity<?>)
	 * ResponseEntity
	 *  - 응답메시지를 직접 작성. 리턴객체를 json변환 기능 -> @ResponseBody
	 *  - 헤더값과 상태코드를 직접 제어
	 *  - 404 / 500 오류 등 직접 전달
	 *  - 생성자를 통해 만들거나, builder패턴으로 생성할 수 있다.
	 *  
	 * 1. status code
	 * 2. 응답헤더
	 * 3. 응답메시지 body에 작성할 java객체
	 * 
	 */
	@GetMapping("/checkIdDuplicate3.do")
	@ResponseBody
	public ResponseEntity<?> checkIdDuplicate3(@RequestParam String id) {
		
		try {
			Member member = memberService.selectOneMember(id);
			boolean available = (member == null);

			Map<String, Object> map = new HashMap<>();
			map.put("id", id);
			map.put("available", available);
			map.put("serverTime", System.currentTimeMillis());
			
			return ResponseEntity.ok(map); // ok = 200을 의미.(정상처리)
			
		} catch (Exception e) {
			return ResponseEntity.notFound().build();
		}
	}
	
	
	
	
	
	
	/**
	 * @RequestMapping("/member")를 선언함으로써 중복 제거
	 * Get/PostMapping은 RequestMapping 자식 어노테이션이므로 위의 RequestMapping의 조건을 포함
	 * method=RequestMethod.GET/POST는 @GetMapping @PostMapping 으로 설정
	 * @return
	 */
//	@RequestMapping("member/memberLogin.do", method=RequestMethod.GET)
	@GetMapping("/memberLogin.do")
	// Referer이라는 명칭이 없을 경우를 대비해 required=false 설정
	public String memberLogin(
			@RequestHeader(name="Referer", required=false) String referer,
			@SessionAttribute(required=false) String next,
			Model model
	) {
		log.info("referer = {}", referer);
		
		if(next == null) {
			model.addAttribute("next", referer);			
		}
		
		return "member/memberLogin";
	}
	
	@PostMapping("/memberLogin.do")
	public String memberLogin(
			@RequestParam String id,
			@RequestParam String password, // 평문(rawPassword)
			@SessionAttribute(required=false) String next, // 위 선언된 어노테이션과 다름. butes / bute
			Model model,
			RedirectAttributes redirectAttr
	) {
		// 인증
		Member member = memberService.selectOneMember(id); // encodedPassword
		log.info("member = {}", member);
		log.info("encodedPassword = {}", bcryptPasswordEncoder.encode(password));
		
		String location = "/";
		if(member != null && bcryptPasswordEncoder.matches(password, member.getPassword())) {
			// 로그인 성공시
			// 기본적으로 저장된 속성을 request의 속성으로 저장.
			// session속성으로 저장하려면 class level에 @SessionAttribute에 키값을 등록해야 한다.
//			redirectAttr.addFlashAttribute("msg", "로그인 성공!");
			model.addAttribute("loginMember", member);
			
			// next값을 location으로 등록
			log.info("next = {}", next);
			location = next;
//			model.addAttribute("next", null); // 일회용으로. 제대로 작동 안함
			
		}
		else {
			// 로그인 실패시
			redirectAttr.addFlashAttribute("msg", "아이디 또는 비밀번호가 일치하기 않습니다.");
		}
		
		return "redirect:" + location;
	}
	
	
	
	@GetMapping("/memberLogout.do")
	public String memberLogout(SessionStatus sessionStatus, ModelMap model) {
		
		model.clear(); // 관리되는 model속성 완전 제거
		
		// 현재 세션객체의 사용완료 설정 - 세션속성들 내부 초기화, 세션객체는 재사용
		if(!sessionStatus.isComplete()) {
			sessionStatus.setComplete(); 
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
