package com.betafore.evoting.mappers;

import java.util.List;

public interface Mapper<T, R> {

    List<T> collection(List<R> data);
    T single(R data);

}
