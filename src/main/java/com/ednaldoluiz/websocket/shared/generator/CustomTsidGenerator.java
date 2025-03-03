package com.ednaldoluiz.websocket.shared.generator;

import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Value;

import io.hypersistence.tsid.TSID;

public class CustomTsidGenerator implements Supplier<TSID.Factory> {

    @Value("${tsid.node}")
    private int node;

    @Value("${tsid.node-bits}")
    private int nodeBits;
    
    @Override
    public TSID.Factory get() {
        return TSID.Factory.builder()
            .withNodeBits(nodeBits)
            .withNode(node)
            .build();
    }
}
