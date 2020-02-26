package com.dlstone.graphql.util.common;

import org.dataloader.*;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class DataLoaderRegistryFactory {

    private Map<String, Object> batchLoaders = new ConcurrentHashMap<>();

    public DataLoaderRegistry newDataLoaderRegistry(Object context) {
        DataLoaderRegistry dataLoaderRegistry = new DataLoaderRegistry();
        batchLoaders.entrySet()
            .forEach(entry -> dataLoaderRegistry.register(entry.getKey(), newDataLoader(context, entry.getValue())));
        return dataLoaderRegistry;
    }

    private DataLoader newDataLoader(Object context, Object loader) {
        DataLoader dataLoader;
        if (loader instanceof BatchLoader) {
            BatchLoader batchLoader = (BatchLoader) loader;
            dataLoader = DataLoader.newDataLoader(batchLoader);
        } else {
            BatchLoaderWithContext batchLoaderWithContext = (BatchLoaderWithContext) loader;
            DataLoaderOptions loaderOptions = DataLoaderOptions.newOptions().setBatchLoaderContextProvider(() -> context);
            dataLoader = DataLoader.newDataLoader(batchLoaderWithContext, loaderOptions);
        }
        return dataLoader;
    }

    public void registryBatchLoader(String name, Object batchLoader) {
        this.batchLoaders.put(name, batchLoader);
    }

}
