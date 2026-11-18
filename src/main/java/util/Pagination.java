package util;

public class Pagination {

	public static final int DEFAULT_PAGE_SIZE = 5;

	private static final int VISIBLE_PAGES = 5;

	private final int currentPage;
	private final int pageSize;
	private final int totalItems;
	private final int totalPages;

	public Pagination(int requestedPage, int pageSize, int totalItems) {
		this.pageSize = pageSize < 1 ? DEFAULT_PAGE_SIZE : pageSize;
		this.totalItems = Math.max(totalItems, 0);

		this.totalPages = Math.max(1, (this.totalItems + this.pageSize - 1) / this.pageSize);

		this.currentPage = Math.min(Math.max(requestedPage, 1), this.totalPages);
	}

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

	public int getFirstVisiblePage() {
		int first = Math.max(1, currentPage - VISIBLE_PAGES / 2);
		int last = Math.min(totalPages, first + VISIBLE_PAGES - 1);
		return Math.max(1, last - VISIBLE_PAGES + 1);
	}

	public int getLastVisiblePage() {
		return Math.min(totalPages, getFirstVisiblePage() + VISIBLE_PAGES - 1);
	}

	public int getFirstItem() {
		return totalItems == 0 ? 0 : getOffset() + 1;
	}

	public int getLastItem() {
		return Math.min(getOffset() + pageSize, totalItems);
	}
}
