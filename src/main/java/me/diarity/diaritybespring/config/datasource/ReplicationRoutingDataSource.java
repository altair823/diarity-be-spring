package me.diarity.diaritybespring.config.datasource;

import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Map;
import java.util.stream.Collectors;

public class ReplicationRoutingDataSource extends AbstractRoutingDataSource {

    private CircularList<String> dataSourceNameList;

    @Override
    public void setTargetDataSources(@NotNull Map<Object, Object> targetDataSources) {
        super.setTargetDataSources(targetDataSources);

        dataSourceNameList = new CircularList<>(
                targetDataSources.keySet()
                        .stream()
                        .map(Object::toString)
                        .filter(string -> string.contains("slave"))
                        .collect(Collectors.toList())
        );
    }

    @Override
    protected Object determineCurrentLookupKey() {
        boolean isReadOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();
        if (isReadOnly) {
            return dataSourceNameList.getOne();
        } else {
            return "master";
        }
    }
}
