package com.ggday.rest_api.dto;

import com.google.gson.annotations.SerializedName;

public class AttributesDTO extends DTO {
    @SerializedName(value = "drupal_internal__id", alternate = {"drupal_internal__nid", "drupal_internal__fid"})
    private int drupalInternalId;

    @SerializedName("drupal_internal__revision_id")
    private Integer drupalInternalRevisionId;

    @SerializedName("drupal_internal__vid")
    private Integer drupalInternalVid;

    @SerializedName("parent_id")
    private Integer parentId;

    @SerializedName("parent_type")
    private String parentType;

    @SerializedName("parent_field_name")
    private String parentFieldName;

    @SerializedName(value = "field_opisanie", alternate = {"body"})
    private TextAreaDTO body;

    @SerializedName(value = "title", alternate={ "field_zagolovok" })
    private String title;

    private String filename;
    private String filemime;

    @SerializedName("field_image_alt_text")
    private String altText;

    public AttributesDTO(int drupalInternalId, Integer drupalInternalVid, Integer drupalInternalRevisionId, Integer parentId, String parentType, String parentFieldName, TextAreaDTO body, String title, String filename, String filemime, String altText) {
        this.drupalInternalId = drupalInternalId;
        this.drupalInternalVid = drupalInternalVid;
        this.drupalInternalRevisionId = drupalInternalRevisionId;
        this.parentId = parentId;
        this.parentType = parentType;
        this.parentFieldName = parentFieldName;
        this.body = body;
        this.title = title;
        this.filename = filename;
        this.filemime = filemime;
        this.altText = altText;
    }

    public int getDrupalInternalId() {
        return drupalInternalId;
    }

    public int getDrupalInternalRevisionId() {
        return drupalInternalRevisionId;
    }

    public int getParentId() {
        return parentId;
    }

    public String getParentType() {
        return parentType;
    }

    public String getParentFieldName() {
        return parentFieldName;
    }

    public TextAreaDTO getBody() {
        return body;
    }

    public String getTitle() {
        return title;
    }

    public String getFilename() {
        return filename;
    }

    public String getFilemime() {
        return filemime;
    }

    public String getAltText() {
        return altText;
    }

    public Integer getDrupalInternalVid() {
        return drupalInternalVid;
    }
}
