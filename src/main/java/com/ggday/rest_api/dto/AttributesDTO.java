package com.ggday.rest_api.dto;

import com.google.gson.annotations.SerializedName;

public class AttributesDTO extends DTO {
    @SerializedName(value = "drupal_internal__id", alternate = {"drupal_internal__nid", "drupal_internal__fid"})
    private Integer drupalInternalId;

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

    private TextAreaDTO body;

    @SerializedName("field_body")
    private TextAreaDTO paragraphBody;

    private String title;

    @SerializedName("field_subtitle")
    private String paragraphTitle;

    private String filename;
    private String filemime;

    @SerializedName("field_image_alt_text")
    private String altText;

    public Integer getDrupalInternalId() {
        return drupalInternalId;
    }

    public Integer getDrupalInternalRevisionId() {
        return drupalInternalRevisionId;
    }

    public Integer getParentId() {
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

    public TextAreaDTO getParagraphBody() {
        return paragraphBody;
    }

    public String getTitle() {
        return title;
    }

    public String getParagraphTitle() {
        return paragraphTitle;
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


    public static class Builder {
        private Integer drupalInternalId;
        private Integer drupalInternalRevisionId;
        private Integer drupalInternalVid;
        private Integer parentId;
        private String parentType;
        private String parentFieldName;
        private TextAreaDTO body;
        private TextAreaDTO paragraphBody;
        private String title;
        private String paragraphTitle;
        private String filename;
        private String filemime;
        private String altText;

        public Builder() {

        }

        public Builder drupalInternalId(Integer id) {
            this.drupalInternalId = id;
            return this;
        }

        public Builder drupalInternalRevisionId(Integer drupalInternalRevisionId) {
            this.drupalInternalRevisionId = drupalInternalRevisionId;
            return this;
        }

        public Builder drupalInternalVid(Integer drupalInternalVid) {
            this.drupalInternalVid = drupalInternalVid;
            return this;
        }

        public Builder parentId(Integer parentId) {
            this.parentId = parentId;
            return this;
        }


        public Builder parentType(String parentType) {
            this.parentType = parentType;
            return this;
        }

        public Builder parentFieldName(String parentFieldName) {
            this.parentFieldName = parentFieldName;
            return this;
        }

        public Builder body(TextAreaDTO body) {
            this.body = body;
            return this;
        }

        public Builder paragraphBody(TextAreaDTO paragraphBody) {
            this.paragraphBody = paragraphBody;
            return this;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder paragraphTitle(String paragraphTitle) {
            this.paragraphTitle = paragraphTitle;
            return this;
        }


        public Builder filename(String filename) {
            this.filename = filename;
            return this;
        }

        public Builder filemime(String filemime) {
            this.filemime = filemime;
            return this;
        }

        public Builder altText(String altText) {
            this.altText = altText;
            return this;
        }

        public AttributesDTO build() {
            AttributesDTO attributesDTO = new AttributesDTO();
            attributesDTO.drupalInternalId = this.drupalInternalId;
            attributesDTO.drupalInternalRevisionId = this.drupalInternalRevisionId;
            attributesDTO.drupalInternalVid = this.drupalInternalVid;
            attributesDTO.parentId = this.parentId;
            attributesDTO.parentType = this.parentType;
            attributesDTO.parentFieldName = this.parentFieldName;
            attributesDTO.body = this.body;
            attributesDTO.paragraphBody = this.paragraphBody;
            attributesDTO.title = this.title;
            attributesDTO.paragraphTitle = this.paragraphTitle;
            attributesDTO.filename = this.filename;
            attributesDTO.filemime = this.filemime;
            attributesDTO.altText = this.altText;
            return attributesDTO;
        }
    }

    private AttributesDTO() {
    }
}
