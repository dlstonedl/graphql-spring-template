package com.dlstone.graphql.common;

import com.dlstone.graphql.annotation.FetcherController;
import com.dlstone.graphql.annotation.FetcherMapping;
import com.dlstone.graphql.annotation.LoaderController;
import com.dlstone.graphql.annotation.LoaderMapping;
import graphql.GraphQL;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.dataloader.BatchLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

@Slf4j
@Component
public class GraphQLFactory {

    @Getter
    private GraphQL graphQL;

    private DataLoaderRegistryFactory dataLoaderRegistryFactory;

    private final ApplicationContext applicationContext;

    @Autowired
    public GraphQLFactory(ApplicationContext applicationContext, DataLoaderRegistryFactory dataLoaderRegistryFactory) {
        this.applicationContext = applicationContext;
        this.dataLoaderRegistryFactory = dataLoaderRegistryFactory;
    }

    @PostConstruct
    public void init() {
        URL url = Thread.currentThread().getContextClassLoader().getResource("schema.graphql");
        File file = Paths.get(url.getFile()).toFile();
        GraphQLSchema graphQLSchema = buildSchema(file);
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

    private GraphQLSchema buildSchema(File file) {
        TypeDefinitionRegistry typeDefinitionRegistry = new SchemaParser().parse(file);
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
