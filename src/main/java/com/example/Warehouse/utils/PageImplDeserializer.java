package com.example.Warehouse.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.io.IOException;
import java.util.List;

public class PageImplDeserializer extends JsonDeserializer<PageImpl<?>> {

    @Override
    public PageImpl<?> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        ObjectMapper mapper = (ObjectMapper) p.getCodec();
        JsonNode node = mapper.readTree(p);

        // Deserialize content
        JsonNode contentNode = node.get("content");
        Class<?> contentType = Object.class; // Replace with your DTO class if necessary
        List<?> content = mapper.convertValue(contentNode, mapper.getTypeFactory().constructCollectionType(List.class, contentType));

        // Deserialize pageable information
        int page = node.get("number").asInt();
        int size = node.get("size").asInt();
        long total = node.get("totalElements").asLong();

        return new PageImpl<>(content, PageRequest.of(page, size), total);
    }
}
