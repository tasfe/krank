package org.crank.controller;

import java.util.ArrayList;
import java.util.List;

public class DataPaginatorImpl implements DataPaginator {

    protected int currentPage=0;
    protected int itemsPerPage=10;
    protected int startItemIndexCurrentPage=0;
    protected int numberOfPages;
    protected PaginatableDataSource dataSource;
    protected int count;
    
    public DataPaginatorImpl () {
    }
    
    public DataPaginatorImpl (PaginatableDataSource dataSource) {
        this.dataSource = dataSource;
        this.count = dataSource.getCount();
        this.numberOfPages = count / itemsPerPage;
        
    }
    
    public int getNumberOfPages() {
        return numberOfPages;
    }

    public int getStartItemIndexCurrentPage() {
        return startItemIndexCurrentPage;
    }

    public void setDataSource( PaginatableDataSource dataSource ) {
        this.dataSource = dataSource;
    }

    public void setItemsPerPage( int itemsPerPage ) {
        this.itemsPerPage = itemsPerPage;
    }

    public void fastForwardPages() {
        currentPage += 5;
        if (currentPage > numberOfPages) {
            currentPage = numberOfPages;
        }
    }

    public void rewindPages() {
        currentPage -= 5;
        if (currentPage < 0) {
            currentPage = 0;
        }
    }

    public int getCurrentPageNumber() {
        return currentPage;
    }

    public int getPageCount() {
        return numberOfPages;
    }

    public void moveToEndPage() {
        currentPage = numberOfPages-1;
    }

    public void moveToStartPage() {
        currentPage = 0;
    }

    public void moveToNextPage() {
        currentPage += 1;
        if (currentPage > numberOfPages) {
            currentPage = numberOfPages;
        }
    }

    public void moveToPreviousPage() {
        currentPage -= 1;
        if (currentPage < 0) {
            currentPage = 0;
        }
    }

    public boolean isFastForwardPagesEnabled() {
        return (currentPage + 5) < numberOfPages;
    }

    public boolean isRewindPagesEnabled() {
        return (currentPage - 5) >= 0;
    }

    public boolean isMoveToEndPageEnabled() {
        return true;
    }

    public boolean isMoveToStartPageEnabled() {
        return true;
    }

    public boolean isMoveToNextPageEnabled() {
        return (currentPage + 1) < numberOfPages;
    }

    public boolean isMoveToPreviousPageEnabled() {
        return (currentPage - 1) >= 0;
    }


    public void reset() {
        this.numberOfPages = dataSource.getCount();
        this.currentPage = 0;
    }

    public List getPage() {
        return dataSource.list( currentPage * itemsPerPage, itemsPerPage );
    }

    public void moveToPage( int pageNumber ) {
        currentPage = pageNumber;
        if (currentPage > numberOfPages) {
            currentPage = numberOfPages;
        } else if (currentPage < 0) {
            currentPage = 0;
        }
    }

    public List<Integer> getPageNumberList() {
        int start = currentPage - 5;
        if (start < 0) {
            start = 0;
        }
        List<Integer> list = new ArrayList<Integer>(10);
        for (int index = 0; index < numberOfPages && index < 10; index++) {
            list.add((start + index + 1));
            if ((start + index + 2) > numberOfPages) {
                break;
            }
        }
        return list;
    }

}
