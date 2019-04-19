package com.ggday.rest_api.dto;

public class RelationshipItemDTO extends DTO {
    private DataDTO data;

    public RelationshipItemDTO(DataDTO data) {
        this.data = data;
    }

    public DataDTO getData() {
        return data;
    }
}
