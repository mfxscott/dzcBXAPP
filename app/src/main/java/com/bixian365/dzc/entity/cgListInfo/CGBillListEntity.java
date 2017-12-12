package com.bixian365.dzc.entity.cgListInfo;

import java.util.List;

/**
 * Created by mfx-t224 on 2017/9/1.
 * 更新后的采购清单列表数据
 */

public class CGBillListEntity {
    private String hasNextIndex;

    private List<CGListInfoEntity> dataset;

    public String getHasNextIndex() {
        return hasNextIndex;
    }

    public void setHasNextIndex(String hasNextIndex) {
        this.hasNextIndex = hasNextIndex;
    }

    public List<CGListInfoEntity> getDataset() {
        return dataset;
    }

    public void setDataset(List<CGListInfoEntity> dataset) {
        this.dataset = dataset;
    }
}
