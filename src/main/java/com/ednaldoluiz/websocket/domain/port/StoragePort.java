package com.ednaldoluiz.websocket.domain.port;

public interface StoragePort {
    
    void save(String key, String content);
    
    String retrieve(String key);
    
    void delete(String key);

}
