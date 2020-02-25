package com.dlstone.graphql.webflux.contract;

import com.dlstone.graphql.webflux.ContractTestBase;
import org.junit.Test;

public class BookContractTest extends ContractTestBase {

    @Test
    public void should_return_book_when_given_book_id() {
        assertContract();
    }

    @Test
    public void should_update_book() {
        assertContract();
    }

}
