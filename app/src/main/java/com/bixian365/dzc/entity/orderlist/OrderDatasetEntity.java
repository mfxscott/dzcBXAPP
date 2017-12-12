package com.bixian365.dzc.entity.orderlist;

import java.util.List;

/**
 */

public class OrderDatasetEntity {
    private String total;

    private List<OrderInfoEntity> rows;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<OrderInfoEntity> getRows() {
        return rows;
    }

    public void setRows(List<OrderInfoEntity> rows) {
        this.rows = rows;
    }
}
