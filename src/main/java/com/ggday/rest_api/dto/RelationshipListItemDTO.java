package com.ggday.rest_api.dto;

public class RelationshipListItemDTO extends DTO {
    private DataDTO[] data;

    public RelationshipListItemDTO(DataDTO[] data) {
        this.data = data;
    }

    public DataDTO[] getData() {
        return data;
    }
}
