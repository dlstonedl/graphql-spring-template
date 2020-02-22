package com.dlstone.graphql.common;

import com.dlstone.graphql.annotation.FetcherMapping;
import com.dlstone.graphql.annotation.FetcherController;
import com.dlstone.graphql.annotation.LoaderController;
import com.dlstone.graphql.annotation.LoaderMapping;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import graphql.GraphQL;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.dataloader.BatchLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

@Slf4j
@Component
public class GraphqlFactory {

    @Getter
    private GraphQL graphQL;

    private DataLoaderRegistryFactory dataLoaderRegistryFactory;

    private final ApplicationContext applicationContext;

    @Autowired
    public GraphqlFactory(ApplicationContext applicationContext, DataLoaderRegistryFactory dataLoaderRegistryFactory) {
        this.applicationContext = applicationContext;
        this.dataLoaderRegistryFactory = dataLoaderRegistryFactory;
    }

    @PostConstruct
    public void init() throws IOException {
        URL url = Resources.getResource("schema.graphql");
        String sdl = Resources.toString(url, Charsets.UTF_8);
        GraphQLSchema graphQLSchema = buildSchema(sdl);
        this.graphQL = GraphQL.newGraphQL(graphQLSchema).build();
        initDataLoaderRegistryFactory();
    }

    private void initDataLoaderRegistryFactory() {
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(LoaderController.class);
        beans.values().forEach(bean -> {
            Method[] methods = bean.getClass().getMethods();
            Stream.of(methods)
                .filter(method -> Objects.nonNull(method.getAnnotation(LoaderMapping.class)))
                .forEach(method -> {
                    LoaderMapping loaderMapping = method.getAnnotation(LoaderMapping.class);
                    dataLoaderRegistryFactory.registryBatchLoader(loaderMapping.name(), (BatchLoader<?, ?>) getMappingMethod(method, bean));
                });
        });
    }

    private GraphQLSchema buildSchema(String sdl) {
        TypeDefinitionRegistry typeDefinitionRegistry = new SchemaParser().parse(sdl);
        RuntimeWiring runtimeWiring = buildWiring();
        return new SchemaGenerator().makeExecutableSchema(typeDefinitionRegistry, runtimeWiring);
    }

    private RuntimeWiring buildWiring() {
        RuntimeWiring.Builder runtimeWiringBuilder = RuntimeWiring.newRuntimeWiring();

        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(FetcherController.class);
        beans.values().stream().forEach(bean -> handleFetcherMapping(runtimeWiringBuilder, bean));

        return runtimeWiringBuilder.build();
    }

    private void handleFetcherMapping(RuntimeWiring.Builder runtimeWiringBuilder, Object bean) {
        Method[] methods = bean.getClass().getMethods();
        Stream.of(methods)
            .filter(method -> Objects.nonNull(method.getAnnotation(FetcherMapping.class)))
            .forEach(method -> {
                FetcherMapping fetcherMapping = method.getAnnotation(FetcherMapping.class);
                runtimeWiringBuilder.type(fetcherMapping.typeName(),
                    builder -> builder.dataFetcher(fetcherMapping.fileName(), (DataFetcher) getMappingMethod(method, bean)));
            });
    }

    private Object getMappingMethod(Method method, Object clazz) {
        try {
            return method.invoke(clazz);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
