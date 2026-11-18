<jsp:directive.page contentType="text/html; charset=UTF-8" />
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<c:if test="${not empty pagination}">
	<div id="bottom" class="row">
		<div class="col-md-12">
			<ul class="pagination">
				<c:choose>
					<c:when test="${pagination.firstPage}">
						<li class="disabled"><a>&lt; Anterior</a></li>
					</c:when>
					<c:otherwise>
						<li><a href="${paginationUrl}?page=${pagination.previousPage}" rel="prev">&lt; Anterior</a></li>
					</c:otherwise>
				</c:choose>
				
				<c:forEach var="p" begin="${pagination.firstVisiblePage}" end="${pagination.lastVisiblePage}">
					<li class="${p == pagination.currentPage ? 'active' : ''}">
						<a href="${paginationUrl}?page=${p}">${p}</a>
					</li>
				</c:forEach>
				
				<c:choose>
					<c:when test="${pagination.lastPage}">
						<li class="disabled"><a>Próximo &gt;</a></li>
					</c:when>
					<c:otherwise>
						<li class="next"><a href="${paginationUrl}?page=${pagination.nextPage}" rel="next">Próximo &gt;</a></li>
					</c:otherwise>
				</c:choose>
			</ul>
			
			<p class="text-muted">
				Exibindo ${pagination.firstItem} a ${pagination.lastItem} 
				de ${pagination.totalItems} registros 
				(página ${pagination.currentPage} de ${pagination.totalPages})
			</p>
		</div>
	</div>
</c:if>
