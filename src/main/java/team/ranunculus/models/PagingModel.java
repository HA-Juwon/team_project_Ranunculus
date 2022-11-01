package team.ranunculus.models;

public class PagingModel {
    public static final int DEFAULT_PAGINATION_COUNT = 10;
    public static final int DEFAULT_ROW_COUNT_PER_PAGE = 10;
    public final int paginationCount; // 페이지 하단에 표시할 페이지 버튼 개수 기본값10 DEFAULT_PAGINATION_COUNT
    public final int rowCountPerPage; // 한 페이지에 표시할 게시글의 개수 기본값 10 DEFAULT_ROW_COUNT_PER_PAGE
    public final int totalRowCount; //진짜 전체 게시글의 개수

    public final int requestPage; // 현재 클라이언트가 보겠다고 요청한 페이지 번호

    public final int maxPage; // 이동할 수 있는 최대 페이지 번호 총게시글 개수에 따라 달라짐
    public final int minPage = 1; // 이동 할 수 있는 최소 페이지 번호 항상1
    public final int boundStartPage; // 페이지 하단에 표시할 페이지 버튼 중 가장 작은 값
    public final int boundEndPage; // 페이지 하단에 표시할 페이지 버튼 중 가장 큰 값

    public PagingModel(int totalRowCount, int requestPage) {
        this(PagingModel.DEFAULT_PAGINATION_COUNT, PagingModel.DEFAULT_ROW_COUNT_PER_PAGE, totalRowCount, requestPage);
    }

    public PagingModel(int paginationCount, int rowCountPerPage, int totalRowCount, int requestPage) {
        this.paginationCount = paginationCount;
        this.rowCountPerPage = rowCountPerPage;
        this.totalRowCount = totalRowCount;
        this.maxPage = this.totalRowCount / this.rowCountPerPage + (this.totalRowCount % this.rowCountPerPage == 0 ? 0 : 1);
        if (requestPage < this.minPage) {
            requestPage = this.minPage;
        }
        if (requestPage > this.maxPage) {
            requestPage = this.maxPage;
        }
        this.requestPage = requestPage;

        this.boundStartPage = (this.requestPage / this.paginationCount) * this.paginationCount + 1;
        this.boundEndPage = Math.min(this.maxPage, (this.requestPage / this.paginationCount) * this.paginationCount + this.paginationCount);
    }

}
//외우기 영원히