package com.studio.core.global.utils;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.core.types.dsl.PathBuilder;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

public class QueryDslUtil {
    public static <T> OrderSpecifier<?>[] getOrderSpecifiers(Sort sort, PathBuilder<T> entityPath) {
        List<OrderSpecifier<?>> orders = new ArrayList<>();
        for (Sort.Order order : sort) {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            ComparableExpressionBase<?> path = entityPath.getComparable(order.getProperty(), Comparable.class);
            orders.add(new OrderSpecifier<>(direction, path));
        }
        return orders.toArray(new OrderSpecifier<?>[0]);
    }
}