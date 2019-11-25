package io.github.zhenyed.core.service;

import io.github.zhenyed.api.common.vo.CommonResult;
import io.github.zhenyed.api.order.vo.OrderVO;
import io.github.zhenyed.api.order.vo.PreOrderVO;

public interface CoreService {
    CommonResult<String> createOrder(Integer userId, Integer productId, Integer quantity);

    void prepareSetCache();
}
