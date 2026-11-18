<jsp:directive.page contentType="text/html; charset=UTF-8" />
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="pt_BR" />
<!DOCTYPE html>
<html lang="pt-br">
<head>
	<%@include file="base-head.jsp"%>
	<title>CRUD Manager - Vagas</title>
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
				<h3>Vagas</h3>
			</div>
			
			<div class="col-md-3">
				<a href="${pageContext.request.contextPath}/job/form" class="btn btn-danger pull-right h2"><span class="glyphicon glyphicon-plus"></span>&nbspAdicionar Vaga</a>
			</div>
		</div>
		
		<hr />
		
		<div id="list" class="row">
			<div class="table-responsive col-md-12">
				<table class="table table-striped table-hover" cellspacing="0" cellpadding="0">
					<thead>
						<tr>
							<th>Título</th>
							<th>Empresa</th>
							<th>Salário</th>
							<th>Encerramento</th>
							<th>100% remota?</th>
							<th>Editar</th>
							<th>Excluir</th>
						</tr>
					</thead>
					<tbody>
						<c:if test="${empty jobs}">
							<tr>
								<td colspan="7" class="text-center text-muted">Nenhuma vaga cadastrada.</td>
							</tr>
						</c:if>
						
						<c:forEach var="job" items="${jobs}">
							<tr>
								<td><c:out value="${job.title}" /></td>
								<td><c:out value="${job.company.name}" /></td>
								<td><fmt:formatNumber value="${job.salary}" type="currency" /></td>
								<td><fmt:formatDate value="${job.closingDate}" pattern="dd/MM/yyyy" /></td>
								<td>
									<c:choose>
										<c:when test="${job.remote}"><span class="label label-success">Sim</span></c:when>
										<c:otherwise><span class="label label-default">Não</span></c:otherwise>
									</c:choose>
								</td>
								
								<td class="actions">
									<a class="btn btn-info btn-xs" 
									   href="${pageContext.request.contextPath}/job/update?jobId=${job.id}">
									   <span class="glyphicon glyphicon-edit"></span>
									</a>
								</td>
								
								<td class="actions">
									<a class="btn btn-danger btn-xs modal-remove"
									   job-id="${job.id}" 
									   job-title="<c:out value='${job.title}' />" data-toggle="modal" 
									   data-target="#delete-modal" href="#"><span 
									   class="glyphicon glyphicon-trash"></span></a>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
		
		<c:set var="paginationUrl" value="${pageContext.request.contextPath}/jobs" />
		<%@include file="pagination.jsp"%>
	</div>
	
	<script src="${pageContext.request.contextPath}/js/jquery.min.js"></script>
	<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			setTimeout(function() {
				$("#alert").slideUp(500);
			}, 3000);
			
			$(".modal-remove").click(function () {
				var jobTitle = $(this).attr('job-title');
				var jobId = $(this).attr('job-id');
				$(".modal-body #hiddenValue").text("a vaga '" + jobTitle + "'");
				$("#id").attr("value", jobId);
				$("#form").attr("action", "${pageContext.request.contextPath}/job/delete");
			})
		});
	</script>
</body>
</html>
