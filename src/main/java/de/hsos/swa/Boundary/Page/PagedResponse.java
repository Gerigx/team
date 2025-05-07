package de.hsos.swa.Boundary.Page;

import java.util.List;

public class PagedResponse<T> {
    
    private List<T> data;
    private long totalCount;
    private int page;
    private int pageSize;
    private int totalPages;
    
    public PagedResponse(List<T> data, long totalCount, int page, int pageSize) {
        this.data = data;
        this.totalCount = totalCount;
        this.page = page;
        this.pageSize = pageSize;
        this.totalPages = pageSize > 0 ? (int) Math.ceil((double) totalCount / pageSize) : 0;
    }
    
    // Getter und Setter
    public List<T> getData() {
        return data;
    }
    
    public long getTotalCount() {
        return totalCount;
    }
    
    public int getPage() {
        return page;
    }
    
    public int getPageSize() {
        return pageSize;
    }
    
    public int getTotalPages() {
        return totalPages;
    }

}
