package io.github.zhenyed.api.common.dataobject;


import com.baomidou.mybatisplus.annotation.TableLogic;

public class DeletableDO extends BaseDO {

    /**
     * 是否删除
     */
    @TableLogic
    private Integer deleted;

    @Override
    public String toString() {
        return "DeletableDO{" +
                "deleted=" + deleted +
                '}';
    }

    public Integer getDeleted() {
        return deleted;
    }

    public DeletableDO setDeleted(Integer deleted) {
        this.deleted = deleted;
        return this;
    }
}
