package com.dlstone.graphql.webflux;

import com.github.macdao.moscow.ContractAssertion;
import com.github.macdao.moscow.ContractContainer;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.nio.file.Paths;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class ContractTestBase {

    @LocalServerPort
    private int port;

    @Rule
    public TestName testName = new TestName();

    private ContractContainer contractContainer = new ContractContainer(Paths.get("src/test/resources/contracts"));

    protected void assertContract() {
        new ContractAssertion(contractContainer.findContracts(testName.getMethodName()))
            .setPort(port)
            .assertContract();
    }
}
