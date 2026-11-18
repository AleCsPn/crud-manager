package util;

/**
 * Representa o estado da paginação de uma listagem.
 * 
 * O controller conta os registros (COUNT), cria um objeto Pagination e usa 
 * getCurrentPage()/getPageSize() para pedir ao DAO somente os registros da 
 * página atual. A JSP (pagination.jsp) usa os getters para desenhar os botões.
 * 
 * Os valores são sempre "corrigidos" (clamp): página menor que 1 vira 1 e 
 * página maior que a última vira a última. Assim, ?page=-5 ou ?page=999 
 * nunca quebram a tela.
 */
public class Pagination {

	// Quantidade de registros por página (usada por todas as listagens)
	public static final int DEFAULT_PAGE_SIZE = 5;

	// Quantos botões numerados aparecem de uma vez (ex.: 3 4 [5] 6 7)
	private static final int VISIBLE_PAGES = 5;

	private final int currentPage;
	private final int pageSize;
	private final int totalItems;
	private final int totalPages;

	public Pagination(int requestedPage, int pageSize, int totalItems) {
		this.pageSize = pageSize < 1 ? DEFAULT_PAGE_SIZE : pageSize;
		this.totalItems = Math.max(totalItems, 0);

		// divisão inteira "para cima": 11 registros / 5 por página = 3 páginas
		// (sempre existe pelo menos 1 página, mesmo sem registros)
		this.totalPages = Math.max(1, (this.totalItems + this.pageSize - 1) / this.pageSize);

		this.currentPage = Math.min(Math.max(requestedPage, 1), this.totalPages);
	}

	/**
	 * Converte o parâmetro "page" da URL em número. 
	 * Se estiver ausente ou inválido (ex.: ?page=abc), devolve 1.
	 */
	public static int parsePage(String value) {
		if (value == null)
			return 1;

		try {
			return Integer.parseInt(value.trim());
		} catch (NumberFormatException e) {
			return 1;
		}
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public int getTotalItems() {
		return totalItems;
	}

	public int getTotalPages() {
		return totalPages;
	}

	// Quantos registros devem ser "pulados" no SQL (LIMIT pageSize OFFSET offset)
	public int getOffset() {
		return (currentPage - 1) * pageSize;
	}

	public boolean isFirstPage() {
		return currentPage == 1;
	}

	public boolean isLastPage() {
		return currentPage == totalPages;
	}

	public int getPreviousPage() {
		return Math.max(currentPage - 1, 1);
	}

	public int getNextPage() {
		return Math.min(currentPage + 1, totalPages);
	}

	// Primeiro número de página que aparece na barra (janela deslizante)
	public int getFirstVisiblePage() {
		int first = Math.max(1, currentPage - VISIBLE_PAGES / 2);
		int last = Math.min(totalPages, first + VISIBLE_PAGES - 1);
		return Math.max(1, last - VISIBLE_PAGES + 1);
	}

	// Último número de página que aparece na barra
	public int getLastVisiblePage() {
		return Math.min(totalPages, getFirstVisiblePage() + VISIBLE_PAGES - 1);
	}

	// Posição (1-based) do primeiro registro exibido; 0 quando não há registros
	public int getFirstItem() {
		return totalItems == 0 ? 0 : getOffset() + 1;
	}

	// Posição (1-based) do último registro exibido
	public int getLastItem() {
		return Math.min(getOffset() + pageSize, totalItems);
	}
}
