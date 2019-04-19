package com.ggday.rest_api.dto;

public class DrupalElementDTO extends DTO {
    private final String id;
    private final String type;
    private final AttributesDTO attributes;
    private final RelationshipsDTO relationships;

    public DrupalElementDTO(String id, String type, AttributesDTO attributes, RelationshipsDTO relationships) {
        this.id = id;
        this.type = type;
        this.attributes = attributes;
        this.relationships = relationships;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public AttributesDTO getAttributes() {
        return attributes;
    }

    public RelationshipsDTO getRelationships() {
        return relationships;
    }
}
