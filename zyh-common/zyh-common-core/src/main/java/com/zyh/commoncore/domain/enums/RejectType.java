package com.zyh.commoncore.domain.enums;

import lombok.Data;
import lombok.Getter;

/**
 * @author zhangyuheng
 */
@Getter
public enum RejectType {
    /**
     * AbortPolicy策略
     */
    AbortPolicy(1),

    /**
     * CallerRunsPolicy策略
     */
    CallerRunsPolicy(2),

    /**
     * DiscardOldestPolicy策略
     */
    DiscardOldestPolicy(3),

    /**
     * DiscardPolicy策略
     */
    DiscardPolicy(4);


    private final Integer value;

    RejectType(Integer value) {
        this.value = value;
    }
}
