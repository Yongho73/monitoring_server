package kr.co.bizmarvel.ccdm.vo.common;

public class PaginationVO {
    private int startIndex;
    private int lastIndex;
    private int pagePerSize;
    private int pageIndex;
    private int totalCount;

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getLastIndex() {
        return lastIndex;
    }

    public void setLastIndex(int lastIndex) {
        this.lastIndex = lastIndex;
    }

    public int getPagePerSize() {
        return pagePerSize;
    }

    public void setPagePerSize(int pagePerSize) {
        this.pagePerSize = pagePerSize;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public PaginationVO(int totalCnt, int size , int index) {
        int division = size;
        int start = division * (index - 1);
        int end = 0;

        if(division == 0 ){
            division = 15;
        }
        if(index == 0){
            index = 1;
        }

        this.startIndex = start;
        this.lastIndex = division;
        this.totalCount = totalCnt;
        this.pageIndex = index;
        this.pagePerSize = size;
    }
}
