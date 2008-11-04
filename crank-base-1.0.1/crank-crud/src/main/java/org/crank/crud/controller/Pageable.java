package org.crank.crud.controller;

import java.util.List;

/** Defines an interface for a pagination controller.
 *  This interface is not tied to JSF, Spring MVC, or Swing but could be used with any GUI framework.
 *  @author Rick Hightower
 */
public interface Pageable {
    /** Advance to the next page. */
    void moveToNextPage();
    /** Rewind by one page. */
    void moveToPreviousPage();
    /** Fast forward a few pages. */
    void fastForwardPages();
    /** Rewind a few pages. */
    void rewindPages();
    /** moveTo to the start page. */
    void moveToStartPage();
    /** moveTo to the end page. */
    void moveToEndPage();
    
    /** Is the next page control enabled? */
    boolean isMoveToNextPageEnabled();
    /** Is the previous page control enabled? */
    boolean isMoveToPreviousPageEnabled();
    /** Is the fast forward page control enabled? */
    boolean isFastForwardPagesEnabled();
    /** Is the rewind page control enabled? */
    boolean isRewindPagesEnabled();
    /** Is the moveTo page control enabled? */
    boolean isMoveToStartPageEnabled();
    /** Is the moveTo end page control enabled? */
    boolean isMoveToEndPageEnabled();
    /** Is the show last page delimiter allowed? */
    boolean isShowLastPageDelimiter();
    /** Is the show last page link allowed? */
    boolean isShowLastPageLink();
    /** Returns the current page number. */
    int getCurrentPageNumber();
    /** Return the page count. */
    int getPageCount();
    /** Reset the controller. Usually becuase underlying model was updated. */
    void reset();
    /** Get the current page of data. */
    @SuppressWarnings("unchecked")
	List getPage();
    /** Get the page number list. This is used to allow to display page numbers
     * to end users so they can jump to a page quickly. It will only show X pages at a time. */
    List<Integer> getPageNumberList();
    /** Move to the the current page. */
    void moveToPage(int pageNumber);
    /** Move to the the current page. Can only be used with CrankListener. */
    void moveToPage();

    /* Register for notifcation of page changes (pagination events). */
    void addPaginationListener(PaginationListener listener);
    /* Unregister for notifcation of page changes. */
    void removePaginationListener(PaginationListener listener);
    
    boolean isInitialized();

    void setAssumedCount(int assumedCount);


}
