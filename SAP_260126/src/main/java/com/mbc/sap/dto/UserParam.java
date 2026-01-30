package com.mbc.sap.dto;

public class UserParam {
    
    private String searchKeyword; 
    private int pageNumber;
    private int offset; // 무한 스크롤을 위한 오프셋 추가

    public UserParam() {}

    public String getSearchKeyword() {
        return searchKeyword;
    }

    public void setSearchKeyword(String searchKeyword) {
        this.searchKeyword = searchKeyword;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
        // 페이지 번호가 바뀔 때 offset을 자동으로 계산
        this.offset = (pageNumber - 1) * 5;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    @Override
    public String toString() {
        return "UserParam [searchKeyword=" + searchKeyword + ", pageNumber=" + pageNumber + ", offset=" + offset + "]";
    }
}