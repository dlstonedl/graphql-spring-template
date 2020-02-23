package com.dlstone.graphql.util.common;

import org.dataloader.BatchLoader;
import org.dataloader.DataLoader;
import org.dataloader.DataLoaderRegistry;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class DataLoaderRegistryFactory {

    private Map<String, BatchLoader<?,?>> batchLoaders = new ConcurrentHashMap<>();

    public DataLoaderRegistry newDataLoaderRegistry() {
        DataLoaderRegistry dataLoaderRegistry = new DataLoaderRegistry();
        batchLoaders.entrySet()
            .forEach(entry -> dataLoaderRegistry.register(entry.getKey(), DataLoader.newDataLoader(entry.getValue())));

        return dataLoaderRegistry;
    }

    public void registryBatchLoader(String name, BatchLoader<?,?> batchLoader) {
        this.batchLoaders.put(name, batchLoader);
    }

}
