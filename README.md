# graphql-spring-template

- graphql-spring-common
  - 注解定义：1.DataFetcher注解； 2.BatchLoader注解；
  - GraphQLClient生成(mvc与webflux统一入口)
  
**maven仓库**
```
dependencies {
    implementation 'com.github.dlstonedl:graphql-java-client:0.1.0'
}
```

- graphql-spring-mvc-template
  - 与spring mvc集成
  - 通过注解定义DataFetcher和BatchLoader
  - 调用GraphQLClient.invoke获取结果

- graphql-spring-webflux-template
  - 与spring webflux集成
  - 通过注解定义DataFetcher和BatchLoader
  - 调用GraphQLClient.invoke获取结果
