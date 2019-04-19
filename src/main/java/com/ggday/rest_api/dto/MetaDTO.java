package com.ggday.rest_api.dto;

import com.google.gson.annotations.SerializedName;

public class MetaDTO extends DTO {
    @SerializedName("target_revision_id")
    private Integer targetRevisionId;
    private String alt;
    private String title;

    public MetaDTO(String alt, String title) {
        this.alt = alt;
        this.title = title;
    }

    public MetaDTO(String alt, String title, Integer targetRevisionId) {
        this(alt, title);
        this.targetRevisionId = targetRevisionId;
    }

    public MetaDTO(String alt) {
        this.alt = alt;
    }

    public String getAlt() {
        return alt;
    }

    public String getTitle() {
        return title;
    }

    public Integer getTargetRevisionId() {
        return targetRevisionId;
    }
}
