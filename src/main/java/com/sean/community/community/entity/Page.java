package com.sean.community.community.entity;

/**
 * 封装分页相关的信息。
 */
public class Page {
    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        if (current >= 1)
            this.current = current;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        if (limit >= 1 && limit <= 100)
            this.limit = limit;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        if (rows >=0)
            this.rows = rows;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    private int current = 1;
    private int limit = 10;
    private int rows;
    //查询路径 复用分页链接
    private String path;

    /**
     * 获取当前页的起始行
     * @return
     */
    public int getOffset() {
        // current * limit - limit
        return (current - 1) * limit;
    }

    public int getTotal() {
        return rows%limit == 0 ? rows / limit: rows/limit + 1;
    }
    /**
     * 获取起始页码
     * @return
     */
    public int getFrom() {
        int from = current - 2;
        int total = getTotal();
        if (current == total) from = current - 4;
        if (current == total - 1)  from = current - 3;
        return from < 1 ? 1 : from;
    }

    /**
     * 获取结束页码
     * @return
     */
    public  int getTo() {
        int to = current + 2;
        int total = getTotal();
        if (current == 1) to = current + 4;
        if (current == 2) to = current + 3;
        return to > total ? total : to;
    }
}
