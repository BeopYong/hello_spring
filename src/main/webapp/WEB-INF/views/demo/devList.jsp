<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<jsp:include page="/WEB-INF/views/common/header.jsp">
	<jsp:param value="Dev 목록" name="title"/>
</jsp:include>
<table class="table w-75 mx-auto">
    <tr>
      <th scope="col">번호</th>
      <th scope="col">이름</th>
      <th scope="col">경력</th>
      <th scope="col">이메일</th>
      <th scope="col">성별</th>
      <th scope="col">개발가능언어</th>
    </tr>
   	<c:forEach items="${list}" var="dev">
	    <tr>
    		<td>${dev.no}</td>
	    	<td>${dev.name}</td>
	    	<td>${dev.career}년</td>
	    	<td>${dev.email}</td>
	    	<td>${dev.gender}</td>
	    	<td>
	    		<c:forEach items="${dev.lang}" var="lang" varStatus="vs">
	    			${fn:replace(lang, ' ', '')}${vs.last ? "" : ","}
	    		</c:forEach>
	    	</td>
	    </tr>
   	</c:forEach>
	<%--
	맵으로 했을 경우. select * 로 불러왔기 때문에 별칭이 따로 없어 대문자로 호출해야함. 기본값이 대문자
	<c:forEach items="${mapList}" var="map">
 		<tr>
 			<td>${map.NO}</td>
 			<td>${map.NAME}</td>
 			<td>${map.CAREER}년</td>
 			<td>${map.EMAIL}</td>
 			<td>${map.GENDER}</td>
 			<td>${map.LANG}</td>
 		</tr>
 	</c:forEach>--%>
   
</table>
<jsp:include page="/WEB-INF/views/common/footer.jsp"></jsp:include>
