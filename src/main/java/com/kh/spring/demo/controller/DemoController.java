package com.kh.spring.demo.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.kh.spring.demo.model.service.DemoService;
import com.kh.spring.demo.model.vo.Dev;

import lombok.extern.slf4j.Slf4j;

/**
* <h1>컨트롤러클래스의 핸들러 메소드가 가질수 있는 파라미터</h1>
* 
* <pre>
* HttpServletRequest
* HttpServletResponse
* HttpSession

* java.util.Locale : 요청에 대한 Locale
* InputStream/Reader : 요청에 대한 입력스트림
* OutputStream/Writer : 응답에 대한 출력스트림. ServletOutputStream, PrintWriter

사용자입력값처리
* Command객체 : http요청 파라미터를 커맨드객체에 저장한 VO객체
* CommandMap :  HandlerMethodArgumentResolver에 의해 처리된 사용자입력값을 가진 Map객체
* @Valid : 커맨드객체 유효성 검사객체
* Error, BindingResult : Command객체에 저장결과(Command객체 바로 다음위치시킬것.)
* @PathVariable : 요청url중 일부를 매개변수로 취할 수 있다.
* @RequestParam : 사용자입력값을 자바변수에 대입처리(필수여부 설정)
* @RequestHeader : 헤더값
* @CookieValue : 쿠키값
* @RequestBody : http message body에 작성된 json을 vo객체로 변환처리

뷰에 전달할 모델 데이터 설정
* ModelAndView
* ModelMap 
* Model

* @ModelAttribute : model속성에 대한 getter
* @SessionAttribute : session속성에 대한 getter(required여부 선택가능)
* @SessionAttributes : session에서 관리될 속성명을 class-level에 작성
* SessionStatus: @SessionAttributes로 등록된 속성에 대하여 사용완료(complete)처리. 세션을 폐기하지 않고 재사용한다.


기타
* MultipartFile : 업로드파일 처리 인터페이스. CommonsMultipartFile
* RedirectAttributes : DML처리후 요청주소 변경을 위한 redirect를 지원
* </pre>
*/

@Controller
@Slf4j
public class DemoController {

	/**
	 * logging framework : slf4j(서비스) -> Log4j(구현체)
	 */
//	private static Logger log = LoggerFactory.getLogger(DemoController.class);
	
	
	/**
	 * 의존주입 (DI) 처리
	 * 1. 필드
	 * 2. 생성자
	 * 3. setter
	 */
	@Autowired
	private DemoService demoService;
	
	/**
	 * GET /spring/demo/devForm.do
	 * @return
	 */
	@RequestMapping("/demo/devForm.do")
	public String devForm() {
		log.info("/demo/devForm.do가 호출되었습니다.");
		return "demo/devForm";
	}
	
	/**
	 * handler method
	 * 
	 * @RequestMappig method속성
	 * - 지정하지 않으면, 모든 메소드 허용
	 * - 지정하면, 지정한 메소드만 허용
	 * @return
	 */
	@RequestMapping(value={"/demo/dev1.do"}, method={RequestMethod.POST})
	public String dev1(HttpServletRequest request, HttpServletResponse response) {
		
		String name = request.getParameter("name");
		int career = Integer.parseInt(request.getParameter("career"));
		String email = request.getParameter("email");
		String gender = request.getParameter("gender");
		String[] lang = request.getParameterValues("lang");
		
//		log.info("name = {}", name);
//		log.info("career = {}", career);
//		log.info("email = {}", email);
//		log.info("gender = {}", gender);
//		log.info("lang = {}", Arrays.toString(lang));
		
		Dev dev = new Dev(0, name, career, email, gender, lang);
		log.info("dev1 = {}", dev);
		
		request.setAttribute("dev", dev);
		
		
		return "demo/devResult";
	}
	
	
	@RequestMapping(value="/demo/dev2.do", method=RequestMethod.POST)
	public String dev2(
			@RequestParam(name = "name", required = true, defaultValue = "홍길동") String name, // jsp의 name값. 
			@RequestParam(name = "career", defaultValue = "100") int careeeeeeeer,
			@RequestParam("email") String email,
			@RequestParam("gender") String gender,
			@RequestParam("lang") String[] lang,
			Model model
	) {
		Dev dev = new Dev(0, name, careeeeeeeer, email, gender, lang);
		log.info("dev2 = {}", dev);
		
		// model : view단에 데이터를 전달하기 위한 Map객체
		model.addAttribute("dev", dev);
		
		return "demo/devResult";
	}
	
	
	/**
	 * 커맨드객체의 모든 필드는 상응하는 사용자입려값으로 채워져 있다.
	 * 커맨드객체는 자동으로 Model객체의 속성으로 추가된다.
	 * 
	 * 위에서 model에 등록한 dev가 jsp에서 잘 출력된다면 그냥 가져오면 된다. (dev1처럼 하나하나 설정할 필요 없음!!)
	 * 
	 * @param dev
	 * @return
	 */
	@RequestMapping(value="/demo/dev3.do", method=RequestMethod.POST)
//	public String dev3(Dev dev) {
	public String dev3(@ModelAttribute Dev dev, @RequestParam String token) {
		log.info("dev3 = {}", dev);
		log.info("token = {}", token); // name값으로 가져오기 ??
		return "demo/devResult";
	}
	
	
	
	@RequestMapping(value="/demo/insertDev.do", method=RequestMethod.POST)
	public String insertDev(Dev dev) {
		log.info("insertdev = {}", dev);
		
		int result = demoService.insertDev(dev);
		String msg = result > 0 ? "Dev등록 성공" : "Dev등록 실패";
		log.info("msg = {}", msg);
		
		return "redirect:/demo/devList.do";
	}
	
	
	/**
	 * select * from dev order by no desc
	 * -> vo
	 * -> map
	 */
	@RequestMapping("/demo/devList.do")
	public String devList(Model model) {
		
		List<Dev> list = demoService.selectAllDev();
		log.info("devList = {}", list);
		
		// 맵으로 할경우
//		List<Map<String, Object>> mapList = demoService.selectDevMapList();
//		log.info("mapList = {}", mapList);
//		model.addAttribute("mapList", mapList);
		
		model.addAttribute("list", list);
		
		return "demo/devList";
		
		
	}
}















