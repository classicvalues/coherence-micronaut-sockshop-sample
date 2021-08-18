/*
 * Copyright (c) 2021 Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * https://oss.oracle.com/licenses/upl.
 */

package io.micronaut.examples.sockshop.catalog;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;

/**
 * Abstract base class containing tests for all
 * {@link CatalogRepository} implementations.
 */
public abstract class CatalogRepositoryTest {
    private CatalogRepository catalog;

    protected abstract CatalogRepository getCatalogRepository();

    int pagesize = 100;

    @BeforeEach
    void init() {
        catalog = getCatalogRepository();
    }

    @Test
    void testQueryWithoutFilter() {
        Collection<? extends Sock> socks = catalog.getSocks(null, "price", 1, pagesize);
        assertThat(socks.size(), is(9));
    }

    @Test
    void testQueryWithFilter() {
        Collection<? extends Sock> socks = catalog.getSocks("blue,green", "price", 1, pagesize);
        assertThat(socks.size(), is(6));
    }

    @Test
    void testQueryPaging() {
        Collection<? extends Sock> socks = catalog.getSocks(null, "name", 2, 3);
        assertThat(socks.size(), is(3));

        String[] names = socks.stream().map(Sock::getName).toArray(String[]::new);
        assertThat(names, arrayContaining("Crossed", "Figueroa", "Holy"));
    }

    @Test
    void testGetById() {
        Sock sock = catalog.getSock("03fef6ac-1896-4ce8-bd69-b798f85c6e0b");
        assertThat(sock.getName(), is("Holy"));
    }

    @Test
    void testSockCountWithoutFilter() {
        assertThat(catalog.getSockCount(null), is(9L));
    }

    @Test
    void testSockCountWithFilter() {
        assertThat(catalog.getSockCount("brown"), is(3L));
    }

    @Test
    void testTags() {
        assertThat(catalog.getTags(), containsInAnyOrder("formal", "red", "magic", "green", "blue",
                                                         "geek", "black", "skin", "action", "brown", "sport"));
    }
}
