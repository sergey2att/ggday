package com.ggday.rest_api.dto;

import com.google.gson.annotations.SerializedName;

public class RelationshipsDTO extends DTO {

    @SerializedName(value = "paragraph_type", alternate = {"file_type", "node_type"})
    private RelationshipItemDTO type;

    @SerializedName(value = "field_izobrazhenie", alternate = {"field_image"})

    private RelationshipItemDTO fieldImg;

    private RelationshipItemDTO uid;

    @SerializedName("field_list_item")
    private RelationshipListItemDTO fieldListItem;


}
