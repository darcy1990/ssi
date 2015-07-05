<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Employees</title>
<script src="../js/jquery-1.11.2.min.js"></script>
<script src="../js/jquery-easyui-1.4.2/jquery.easyui.min.js" type="text/javascript"></script>
<link href="../js/jquery-easyui-1.4.2/themes/default/easyui.css" rel="stylesheet" type="text/css" />
<link href="../js/jquery-easyui-1.4.2/themes/icon.css" rel="stylesheet" type="text/css" />
<script src="../js/jquery-easyui-1.4.2/plugins/jquery.panel.js" type="text/javascript"></script>
<script src="../js/jquery-easyui-1.4.2/plugins/jquery.resizable.js" type="text/javascript"></script>
<script src="../js/jquery-easyui-1.4.2/plugins/jquery.linkbutton.js" type="text/javascript"></script>
<script src="../js/jquery-easyui-1.4.2/plugins/jquery.pagination.js" type="text/javascript"></script>
<script>
	$(document).ready(function(){
		$("#testAjax").click(function() {
			$.ajax({
				   type: "POST",
				   url: "http://localhost:8080/ssi/service/ajax",
				   data: "name=John&location=Boston",
				   dataType: "text",
				   success: function(msg){
				     alert(msg);
				   }
				});
		});
		$("#testAjaxJson").click(function() {
			$.ajax({
				   type: "POST",
				   url: "http://localhost:8080/ssi/service/ajaxJson",
				   data: "name=John&location=Boston",
				   dataType: "json",
				   success: function(msg){
				     alert(msg.name + ' ' + msg.location);
				   }
				});
		});
		
// 		$.ajax({
// 			   type: "POST",
// 			   url: "http://localhost:8080/ssi/service/loginByCookie",
// 			   dataType: "text",
// 			   success: function(msg){
//				   var pre = "<span class='l-btn-text'>";
//				   var suf = "</span>";
// 				   $("#login").after(pre + msg + suf);
// 			   }
// 			});
		
		$.ajax({
			   type: "POST",
			   url: "http://localhost:8080/ssi/service/loginBySession",
			   dataType: "text",
			   success: function(msg){
				   var pre = "<span class='l-btn-text'>";
				   var suf = "</span>";
				   $("#login").after(pre + msg + suf);
			   }
			});
		
		$("#login").click(function() {
			$.ajax({
				   type: "POST",
				   url: "http://localhost:8080/ssi/service/login",
				   data: "name="+$("#username").val()+"&password="+$("#password").val(),
				   dataType: "text",
				   success: function(msg){
				     alert(msg);
				   }
				});
		});
	});
</script>
</head>
<body>
	<span class="l-btn-text">Test spring mvc:</span><br>
	<table border=1 class="easyui-datagrid">
		<thead>
			<tr>
				<th data-options="field:'id',width:100">ID</th>
				<th data-options="field:'name',width:100">Name</th>
				<th data-options="field:'email',width:100">Password</th>
			</tr>
		</thead>
		<c:forEach var="employee" items="${employees.employees}">
			<tr>
				<td>${employee.id}</td>
				<td>${employee.name}</td>
				<td>${employee.email}</td>
			</tr>
		</c:forEach>
	</table>
	<br>
	<!-- <input id="testAjax" value="test ajax" type="button"></input> -->
	<a id="testAjax" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'">press to test ajax</a><br>
	<!-- <input id="testAjaxJson" value="test ajax by json" type="button"></input> -->
	<a id="testAjaxJson" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'">press to test ajax by json</a><br>
	<br>
	<span class="l-btn-text">username:</span><input id="username"></input><br>
	<span class="l-btn-text">password:</span><input id="password"></input><br>
	<!-- <input id="login" value="login to test session" type="button"></input> -->
	<a id="login" href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'">login to test session</a>
</body>
</html>