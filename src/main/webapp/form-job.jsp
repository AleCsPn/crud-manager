<jsp:directive.page contentType="text/html; charset=UTF-8" />
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%-- Fixa o locale: sem ele, o fmt:formatDate ignora o pattern quando o navegador não envia Accept-Language --%>
<fmt:setLocale value="pt_BR" />

<!DOCTYPE html>
<html lang="pt-br">
<head>
	<%@include file="base-head.jsp"%>
	<title>CRUD Manager - ${not empty job ? 'Atualizar' : 'Adicionar'} Vaga</title>
</head>
<body>
	<%@include file="nav-menu.jsp"%>
	
	<div id="container" class="container-fluid">
		<h3 class="page-header">${not empty job ? 'Atualizar' : 'Adicionar'} Vaga</h3>
		
		<%-- action = "insert" ou "update" (definido pelo JobsController) --%>
		<form action="${pageContext.request.contextPath}/job/${action}" method="POST">
			
			<input type="hidden" value="${job.id}" name="jobId">
			
			<div class="row">
				<%-- 1) input type="text" --%>
				<div class="form-group col-md-6">
					<label for="title">Título da vaga</label>
					<input type="text" class="form-control" id="title" name="title" 
						   autofocus="autofocus" placeholder="Ex.: Desenvolvedor Java Pleno" 
						   maxlength="150" required 
						   oninvalid="this.setCustomValidity('Por favor, informe o título da vaga.')"
						   oninput="setCustomValidity('')"
						   value="<c:out value='${job.title}' />" />
				</div>
				
				<%-- 2) input type="number" --%>
				<div class="form-group col-md-3">
					<label for="salary">Salário oferecido (R$)</label>
					<input type="number" class="form-control" id="salary" name="salary" 
						   placeholder="0,00" min="0" max="99999999.99" step="0.01" required 
						   oninvalid="this.setCustomValidity('Por favor, informe um salário válido.')"
						   oninput="setCustomValidity('')"
						   value="${job.salary}" />
				</div>
				
				<%-- 3) input type="date" --%>
				<div class="form-group col-md-3">
					<label for="closingDate">Encerramento das inscrições</label>
					<input type="date" class="form-control" id="closingDate" name="closingDate" 
						   required 
						   oninvalid="this.setCustomValidity('Por favor, informe a data de encerramento.')"
						   oninput="setCustomValidity('')"
						   value="<fmt:formatDate value='${job.closingDate}' pattern='yyyy-MM-dd' />" />
				</div>
			</div>
			
			<div class="row">
				<%-- 4) input type="checkbox" --%>
				<div class="form-group col-md-4">
					<label>Modalidade</label>
					<div class="checkbox">
						<label for="remote">
							<input type="checkbox" id="remote" name="remote" value="true" 
								   ${job.remote ? 'checked' : ''} />
							Vaga 100% remota
						</label>
					</div>
				</div>
				
				<%-- 5) select: chave estrangeira para Company (lista vinda do banco) --%>
				<div class="form-group col-md-8">
					<label for="company">Empresa contratante</label>
					<select id="company" class="form-control" name="company" required 
							oninvalid="this.setCustomValidity('Por favor, selecione a empresa.')"
							oninput="setCustomValidity('')">
						
						<option value="" disabled ${not empty job ? '' : 'selected'}>
							Selecione uma empresa
						</option>
						
						<c:forEach var="comp" items="${companies}">
							<option value="${comp.id}" 
								${job.company.id == comp.id ? 'selected' : ''}>
								<c:out value="${comp.name}" />
							</option>
						</c:forEach>
					</select>
					
					<c:if test="${empty companies}">
						<p class="help-block text-danger">
							Nenhuma empresa cadastrada. 
							<a href="${pageContext.request.contextPath}/company/form">Cadastre uma empresa</a> primeiro.
						</p>
					</c:if>
				</div>
			</div>
			
			<hr />
			<div id="actions" class="row pull-right">
				<div class="col-md-12">
					<a href="${pageContext.request.contextPath}/jobs" 
					   class="btn btn-default">Cancelar</a>
					
					<button type="submit" 
							class="btn btn-primary">${not empty job ? 'Atualizar' : 'Cadastrar'} Vaga</button>
				</div>
			</div>
		</form>
	</div>
	
	<script src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
	<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
</body>
</html>
