<jsp:directive.page contentType="text/html; charset=UTF-8" />
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-br">
<head>
	<%@include file="base-head.jsp"%>
	<title>CRUD Manager - Login</title>
</head>
<body>
	<%-- Sem o menu de navegação: a tela de login é pública --%>
	<div class="container">
		<div class="login-card panel panel-default">
			<div class="panel-heading text-center">
				<img height="30px" width="30px" alt="CRUD Manager" 
					 src="${pageContext.request.contextPath}/images/logo.png">
				<strong>&nbspCRUD Manager</strong>
			</div>
			
			<div class="panel-body">
				<c:if test="${not empty error}">
					<div class="alert alert-danger"><c:out value="${error}" /></div>
				</c:if>
				
				<c:if test="${param.logout == 'true'}">
					<div class="alert alert-success">Você saiu do sistema com sucesso.</div>
				</c:if>
				
				<form action="${pageContext.request.contextPath}/login" method="POST">
					<div class="form-group">
						<label for="email">E-mail</label>
						<input type="email" class="form-control" id="email" name="email" 
							   autofocus="autofocus" placeholder="seu@email.com" required 
							   value="<c:out value='${email}' />" />
					</div>
					
					<div class="form-group">
						<label for="password">Senha</label>
						<input type="password" class="form-control" id="password" name="password" 
							   placeholder="Sua senha" required />
					</div>
					
					<button type="submit" class="btn btn-danger btn-block">Entrar</button>
				</form>
			</div>
		</div>
	</div>
</body>
</html>
