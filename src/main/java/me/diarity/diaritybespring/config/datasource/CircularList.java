package me.diarity.diaritybespring.config.datasource;

import java.util.List;

public class CircularList<T> {
    private final List<T> list;
    private Integer counter = 0;

    public CircularList(List<T> list) {
        this.list = list;
    }

    public T getOne() {
        counter = (counter + 1) % list.size();
        return list.get(counter);
    }
}
