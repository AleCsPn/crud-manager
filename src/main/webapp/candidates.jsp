<jsp:directive.page contentType="text/html; charset=UTF-8" />
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="pt_BR" />
<!DOCTYPE html>
<html lang="pt-br">
<head>
	<%@include file="base-head.jsp"%>
	<title>CRUD Manager - Candidatos</title>
</head>
<body>
	<%@include file="modal.html"%>
	<%@include file="nav-menu.jsp"%>
	
	<div id="container" class="container-fluid">
		<div id="alert" style="${not empty message ? 'display: block;' : 'display: none;'}" class="alert alert-dismissable ${alertType eq 1 ? 'alert-success' : 'alert-danger'}">
		  <a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a>
		  <c:out value="${message}" />
		</div>
		
		<div id="top" class="row">
			<div class="col-md-9">
				<h3>Candidatos</h3>
			</div>
			
			<div class="col-md-3">
				<a href="${pageContext.request.contextPath}/candidate/form" class="btn btn-danger pull-right h2"><span class="glyphicon glyphicon-plus"></span>&nbspAdicionar Candidato</a>
			</div>
		</div>
		
		<hr />
		
		<div id="list" class="row">
			<div class="table-responsive col-md-12">
				<table class="table table-striped table-hover" cellspacing="0" cellpadding="0">
					<thead>
						<tr>
							<th>Nome</th>
							<th>Nascimento</th>
							<th>Pretensão salarial</th>
							<th>Experiência</th>
							<th>Vaga desejada</th>
							<th>Editar</th>
							<th>Excluir</th>
						</tr>
					</thead>
					<tbody>
						<c:if test="${empty candidates}">
							<tr>
								<td colspan="7" class="text-center text-muted">Nenhum candidato cadastrado.</td>
							</tr>
						</c:if>
						
						<c:forEach var="candidate" items="${candidates}">
							<tr>
								<td><c:out value="${candidate.fullName}" /></td>
								<td><fmt:formatDate value="${candidate.birthDate}" pattern="dd/MM/yyyy" /></td>
								<td><fmt:formatNumber value="${candidate.salaryExpectation}" type="currency" /></td>
								<td>${candidate.experienceLevel.label}</td>
								<td>
									<c:out value="${candidate.desiredJob.title}" />
									<small class="text-muted">(<c:out value="${candidate.desiredJob.company.name}" />)</small>
								</td>
								
								<td class="actions">
									<a class="btn btn-info btn-xs" 
									   href="${pageContext.request.contextPath}/candidate/update?candidateId=${candidate.id}">
									   <span class="glyphicon glyphicon-edit"></span>
									</a>
								</td>
								
								<td class="actions">
									<a class="btn btn-danger btn-xs modal-remove"
									   candidate-id="${candidate.id}" 
									   candidate-name="<c:out value='${candidate.fullName}' />" data-toggle="modal" 
									   data-target="#delete-modal" href="#"><span 
									   class="glyphicon glyphicon-trash"></span></a>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
		
		<c:set var="paginationUrl" value="${pageContext.request.contextPath}/candidates" />
		<%@include file="pagination.jsp"%>
	</div>
	
	<script src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
	<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			// fecha o alert após 3 segundos
			setTimeout(function() {
				$("#alert").slideUp(500);
			}, 3000);
			
			// ao clicar no excluir de algum candidato, pega o nome e o id 
			// e a ação (delete) e envia para o modal 
			$(".modal-remove").click(function () {
				var candidateName = $(this).attr('candidate-name');
				var candidateId = $(this).attr('candidate-id');
				$(".modal-body #hiddenValue").text("o candidato '" + candidateName + "'");
				$("#id").attr("value", candidateId);
				$("#form").attr("action", "${pageContext.request.contextPath}/candidate/delete");
			})
		});
	</script>
</body>
</html>
