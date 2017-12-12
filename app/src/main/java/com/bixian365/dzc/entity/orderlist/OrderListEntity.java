package com.bixian365.dzc.entity.orderlist;

/**
 * 普通订单摊主订单列表
 * Created by NN on 2017/9/26.
 */

public class OrderListEntity {
    private String hasNextIndex;

    private OrderDatasetEntity dataset;

    public String getHasNextIndex() {
        return hasNextIndex;
    }

    public void setHasNextIndex(String hasNextIndex) {
        this.hasNextIndex = hasNextIndex;
    }

    public OrderDatasetEntity getDataset() {
        return dataset;
    }

    public void setDataset(OrderDatasetEntity dataset) {
        this.dataset = dataset;
    }
}
