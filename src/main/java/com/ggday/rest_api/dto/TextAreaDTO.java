package com.ggday.rest_api.dto;

public class TextAreaDTO extends DTO {

    private final String value;
    private final String format;

    public TextAreaDTO(String value, String format) {
        this.value = value;
        this.format = format;
    }

    public String getValue() {
        return value;
    }

    public String getFormat() {
        return format;
    }
}
