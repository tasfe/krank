package org.crank.crud.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.crank.core.RequestParameterMapFinder;
import org.crank.crud.controller.datasource.PagingDataSource;

public class Paginator implements Pageable, Serializable {

    protected int currentPage=0;
    protected int itemsPerPage=10;
    protected int startItemIndexCurrentPage=0;
    protected int numberOfPages;
    protected PagingDataSource dataSource;
    protected int count;
    protected List<Integer> pageNumberList;
    protected RequestParameterMapFinder requestParameterMapFinder;
    protected String currentPageParamName = "currentPage";
    
    public void setCurrentPageParamName( String currentPageParamName ) {
        this.currentPageParamName = currentPageParamName;
    }

    public Paginator () {
    }
    
    public Paginator (PagingDataSource dataSource) {
        this.dataSource = dataSource;
        reset();
    }
    
    public int getNumberOfPages() {
        return numberOfPages;
    }

    public int getStartItemIndexCurrentPage() {
        return startItemIndexCurrentPage;
    }

    public void setDataSource( PagingDataSource dataSource ) {
        this.dataSource = dataSource;
    }

    public int getItemsPerPage() {
        return this.itemsPerPage;
    }

    public void setItemsPerPage( int itemsPerPage ) {
        this.itemsPerPage = itemsPerPage;
        firePagination();
    }

    public void fastForwardPages() {
        currentPage += 5;
        if (currentPage > numberOfPages) {
            currentPage = numberOfPages;
        }
        firePagination();
    }

    public void rewindPages() {
        currentPage -= 5;
        if (currentPage < 0) {
            currentPage = 0;
        }
        firePagination();
    }

    public int getCurrentPageNumber() {
        return currentPage;
    }

    public int getPageCount() {
        return numberOfPages;
    }

    public void moveToEndPage() {
        if (currentPage < 0) {
            currentPage = 0;
        }
        currentPage = numberOfPages-1;
        firePagination();
    }

    public void moveToStartPage() {
        currentPage = 0;
        firePagination();
    }

    public void moveToNextPage() {
        currentPage += 1;
        if (currentPage > numberOfPages) {
            currentPage = numberOfPages;
        }
        firePagination();
    }

    public void moveToPreviousPage() {
        currentPage -= 1;
        if (currentPage < 0) {
            currentPage = 0;
        }
        firePagination();
    }

    public boolean isFastForwardPagesEnabled() {
        return (currentPage + 5) < numberOfPages;
    }

    public boolean isRewindPagesEnabled() {
        return (currentPage - 5) >= 0;
    }

    public boolean isMoveToEndPageEnabled() {
        if (numberOfPages == 1) {
            return false;
        } else {
            return (currentPage + 1) != numberOfPages ;
        }
    }

    public boolean isMoveToStartPageEnabled() {
        
        return currentPage != 0;
    }

    public boolean isMoveToNextPageEnabled() {
        return (currentPage + 1) < numberOfPages;
    }

    public boolean isMoveToPreviousPageEnabled() {
        return (currentPage - 1) >= 0;
    }


    public void reset() {
        this.count = dataSource.getCount();
        this.numberOfPages = count / itemsPerPage;
        if ((count % itemsPerPage) != 0) {
            this.numberOfPages++;
        }
        this.currentPage = 0;
    }

    public List getPage() {
        if (currentPage < 0) {
            currentPage = 0;
        }
        return dataSource.list( currentPage * itemsPerPage, itemsPerPage );
    }

    public void moveToPage() {
        String sCurrentPage = "0";
        Object oCurrentPage = requestParameterMapFinder.getMap().get( this.currentPageParamName );
        if (oCurrentPage instanceof String) {
            sCurrentPage = (String) oCurrentPage;
        } else if (oCurrentPage instanceof String[]) {
            sCurrentPage = ((String[])oCurrentPage)[0];
        }
        int currentPage = Integer.parseInt( sCurrentPage );
        moveToPage(currentPage-1);
        firePagination();
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
    	calcPageNumberList();
        return pageNumberList;
    }

    /**
     * Utility function to initialize the page number list
     */
    private void calcPageNumberList(){
        int start = currentPage - 5;
        if (start < 0) {
            start = 0;
        }
        pageNumberList = new ArrayList<Integer>(10);
        for (int index = 0; index < numberOfPages && index < 10; index++) {
        	pageNumberList.add((start + index + 1));
            if ((start + index + 2) > numberOfPages) {
                break;
            }
        }
    	
    }
    
    
    /**
     * Used to evaluate whether or not to display the fancy last page link delimiter
     */
	public boolean isShowLastPageDelimiter() {
		if (pageNumberList != null) {
			if (pageNumberList.size() > 0) {
				if (pageNumberList.get(pageNumberList.size()-1) + 1 < numberOfPages) {
					return true;
				}
			}
		}
		return false;
	}

    /**
     * Used to evaluate whether or not to display the fancy last page link to the last page
     */
	public boolean isShowLastPageLink() {
		if (pageNumberList != null) {
			if (pageNumberList.size() > 0) {
				if (pageNumberList.get(pageNumberList.size()-1) < numberOfPages) {
					return true;
				}
			}
		}
		return false;
	}

    public void setRequestParameterMapFinder( RequestParameterMapFinder requestParameterMapFinder ) {
        this.requestParameterMapFinder = requestParameterMapFinder;
    }

    private List<PaginationListener> listeners = new ArrayList<PaginationListener>();

    public void addPaginationListener(PaginationListener listener) {
        listeners.add( listener );
    }
    public void removePaginationListener(PaginationListener listener) {
        listeners.remove( listener );
    }

    /**
     * Fire and event to the listeners.
     *
     */
    private void firePagination() {
        PaginationEvent pe = new PaginationEvent(this, currentPage);
        for (PaginationListener pl : listeners) {
            pl.pagination( pe );
        }
    }

}
