<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>

	section {
		width: 2500px;
		margin: auto;
	}
	
	section h2 {
		font-size: 2rem;
		margin: 0;
		padding: 0;
		font-weight: bold;
		margin-top: 20px;
		margin-bottom: 20px;
	}
	
	#backgroundImg {
	    background-image: url('../resource/img/customerRequest${requestFiesta}.png');
        background-size: contain;
        position: relative;
		min-height:20vh;
	}
	
	#blank {
		min-height:15vh;
	}
	
	#inputFrm {
		width: 550px;
		min-height:20vh;
		margin-top: 40px;
		margin-left: auto;
  		margin-right: auto;
	}
	
	#addressItem {
		background: transparent;
		border: 1px solid #009688;
		margin: 3px;
		font-weight: bold;
		cursor: pointer;
		padding: 8px 15px;
		color: #009688;
		border-radius: 5px;
	}
	
	#addressItem:hover {
		color: white;
		background: #009688;
		border: 1px solid #009688;
	}
	
	#inputFrm button {
		background: transparent;
		border: 1px solid #009688;
		margin: 3px;
		font-weight: bold;
		cursor: pointer;
		padding: 8px 15px;
		color: #009688;
		border-radius: 5px;
	}
	
	#inputFrm button:hover {
		color: white;
		background: #009688;
		border: 1px solid #009688;
	}
	
	progress {
		width: 550px;
		margin: auto;
	}
	
	 progress {
  	    appearance: none;
    	-moz-appearance: none;
    	-webkit-appearance: none;
	}
	
	progress::-webkit-progress-value {
    	background: #009688;
	}
	
	progress::-webkit-progress-bar {
    	background: lightgray;
	}
	
	
</style>
<script src="https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script>
	function execDaumPostcode() {
		new daum.Postcode({
			oncomplete: function(data) {
			// 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.
				
			// 각 주소의 노출 규칙에 따라 주소를 조합한다.
			// 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
			var addr = ''; // 주소 변수
			var extraAddr = ''; // 참고항목 변수
				
			//사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
			if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
				addr = data.roadAddress;
			} else { // 사용자가 지번 주소를 선택했을 경우(J)
			    addr = data.jibunAddress;
			}
				
			// 사용자가 선택한 주소가 도로명 타입일때 참고항목을 조합한다.
			if(data.userSelectedType === 'R'){
			  // 법정동명이 있을 경우 추가한다. (법정리는 제외)
			  // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
			  if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
			  	extraAddr += data.bname;
			  }
			  // 건물명이 있고, 공동주택일 경우 추가한다.
			  if(data.buildingName !== '' && data.apartment === 'Y'){
			  	extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
			  }
			  // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
			  if(extraAddr !== ''){
			  	extraAddr = ' (' + extraAddr + ')';
			  }
			  // 조합된 참고항목을 해당 필드에 넣는다.
			  document.getElementById("extraAddress").value = extraAddr;
				               
		     } else {
			 	document.getElementById("extraAddress").value = '';
			 }
			// 우편번호와 주소 정보를 해당 필드에 넣는다.
			document.getElementById('postcode').value = data.zonecode;
			document.getElementById("address").value = addr;
			// 커서를 상세주소 필드로 이동한다.
			document.getElementById("detailAddress").focus();
		}
	}).open();
  }
</script>
</head>
<body>
<!-- header import -->
<jsp:include page = "../header.jsp" />
<!-- 항상 section에서 시작 -->
<section>
	<div id="backgroundImg">
	  <img alt="카테고리 대표사진" src="../resource/img/customerRequest${requestFiesta}.png">
	</div>
	<div id="inputFrm">
	  <progress value="50" max="100"></progress><br>
	  <h2>2. 어느 지역인가요?</h2>
      <form action="customerRequest.do" id="placeFrm">
        <input type="hidden" name="command" value="place">
        <input type="hidden" name="requestFiesta" value="${requestFiesta}">
       	<input type="text" id="postcode" name="postcode" placeholder="우편번호">
		<input id="addressItem" type="button" onclick="execDaumPostcode()" value="주소검색">
		<input name="addr" type="text" id="address" placeholder="주소">
		<input name="addr" type="text" id="detailAddress" placeholder="상세주소">
		<input type="text" id="extraAddress" placeholder="참고항목">
		
	    <button name="lastBtn" id="lastBtn"><a href="1_requestRevdate.jsp">이전</a></button>
	    <button type="submit" name="nextBtn" id="nextBtn">다음</button>
    </form>
    </div>
	<div id="blank">
	
	</div>
</section>
<!-- footer import -->
<c:import url="http://localhost:8888/Fiesta/footer.jsp" charEncoding="UTF-8"></c:import>
</body>
</html>