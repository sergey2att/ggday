package com.ggday.rest_api.dto;

public class DataDTO extends DTO {
    private String id;
    private String type;
    private MetaDTO meta;

    public DataDTO(String id, String type, MetaDTO meta) {
        this(id, type);
        this.meta = meta;
    }

    public DataDTO(String id, String type) {
        this.id = id;
        this.type = type;
    }

    public MetaDTO getMeta() {
        return meta;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }
}
