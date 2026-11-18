<jsp:directive.page contentType="text/html; charset=UTF-8" />
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%-- Fixa o locale: sem ele, o fmt:formatDate ignora o pattern quando o navegador não envia Accept-Language --%>
<fmt:setLocale value="pt_BR" />

<%-- "today" é usado para impedir (no HTML) uma data de nascimento no futuro --%>
<jsp:useBean id="today" class="java.util.Date" />

<!DOCTYPE html>
<html lang="pt-br">
<head>
	<%@include file="base-head.jsp"%>
	<title>CRUD Manager - ${not empty candidate ? 'Atualizar' : 'Adicionar'} Candidato</title>
</head>
<body>
	<%@include file="nav-menu.jsp"%>
	
	<div id="container" class="container-fluid">
		<h3 class="page-header">${not empty candidate ? 'Atualizar' : 'Adicionar'} Candidato</h3>
		
		<%-- action = "insert" ou "update" (definido pelo CandidatesController) --%>
		<form action="${pageContext.request.contextPath}/candidate/${action}" method="POST">
			
			<input type="hidden" value="${candidate.id}" name="candidateId">
			
			<div class="row">
				<%-- 1) input type="text" --%>
				<div class="form-group col-md-6">
					<label for="fullName">Nome completo</label>
					<input type="text" class="form-control" id="fullName" name="fullName" 
						   autofocus="autofocus" placeholder="Nome completo do candidato" 
						   maxlength="150" required 
						   oninvalid="this.setCustomValidity('Por favor, informe o nome completo.')"
						   oninput="setCustomValidity('')"
						   value="<c:out value='${candidate.fullName}' />" />
				</div>
				
				<%-- 2) input type="number" --%>
				<div class="form-group col-md-3">
					<label for="salaryExpectation">Pretensão salarial (R$)</label>
					<input type="number" class="form-control" id="salaryExpectation" name="salaryExpectation" 
						   placeholder="0,00" min="0" max="99999999.99" step="0.01" required 
						   oninvalid="this.setCustomValidity('Por favor, informe a pretensão salarial.')"
						   oninput="setCustomValidity('')"
						   value="${candidate.salaryExpectation}" />
				</div>
				
				<%-- 3) input type="date" --%>
				<div class="form-group col-md-3">
					<label for="birthDate">Data de nascimento</label>
					<input type="date" class="form-control" id="birthDate" name="birthDate" 
						   max="<fmt:formatDate value='${today}' pattern='yyyy-MM-dd' />" required 
						   oninvalid="this.setCustomValidity('Por favor, informe uma data de nascimento válida.')"
						   oninput="setCustomValidity('')"
						   value="<fmt:formatDate value='${candidate.birthDate}' pattern='yyyy-MM-dd' />" />
				</div>
			</div>
			
			<div class="row">
				<%-- 4) input type="radio" (uma opção para cada constante do enum ExperienceLevel) --%>
				<div class="form-group col-md-5">
					<label>Nível de experiência</label>
					<div>
						<c:forEach var="lvl" items="${levels}">
							<label class="radio-inline">
								<input type="radio" name="level" value="${lvl}" required 
									   ${candidate.experienceLevel == lvl ? 'checked' : ''} />
								${lvl.label}
							</label>
						</c:forEach>
					</div>
				</div>
				
				<%-- 5) select: chave estrangeira para Job (lista vinda do banco) --%>
				<div class="form-group col-md-7">
					<label for="job">Vaga desejada</label>
					<select id="job" class="form-control" name="job" required 
							oninvalid="this.setCustomValidity('Por favor, selecione a vaga desejada.')"
							oninput="setCustomValidity('')">
						
						<option value="" disabled ${not empty candidate ? '' : 'selected'}>
							Selecione uma vaga
						</option>
						
						<c:forEach var="j" items="${jobs}">
							<option value="${j.id}" 
								${candidate.desiredJob.id == j.id ? 'selected' : ''}>
								<c:out value="${j.title}" /> - <c:out value="${j.company.name}" />
							</option>
						</c:forEach>
					</select>
					
					<c:if test="${empty jobs}">
						<p class="help-block text-danger">
							Nenhuma vaga cadastrada. 
							<a href="${pageContext.request.contextPath}/job/form">Cadastre uma vaga</a> primeiro.
						</p>
					</c:if>
				</div>
			</div>
			
			<hr />
			<div id="actions" class="row pull-right">
				<div class="col-md-12">
					<a href="${pageContext.request.contextPath}/candidates" 
					   class="btn btn-default">Cancelar</a>
					
					<button type="submit" 
							class="btn btn-primary">${not empty candidate ? 'Atualizar' : 'Cadastrar'} Candidato</button>
				</div>
			</div>
		</form>
	</div>
	
	<script src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
	<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
</body>
</html>
