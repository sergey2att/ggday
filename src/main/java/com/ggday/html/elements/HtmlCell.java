package com.ggday.html.elements;

import org.jsoup.nodes.Element;

import javax.annotation.Nonnull;

public class HtmlCell {
    private final String value;
    private final int colspan;
    private final int rowspan;

    public HtmlCell(@Nonnull Element cell) {
        this.value = cell.text().trim();
        this.colspan = getSpanValue(cell,"colspan");
        this.rowspan =  getSpanValue(cell,"rowspan");
    }

    public String getValue() {
        return value;
    }

    public int getColspan() {
        return colspan;
    }

    public int getRowspan() {
        return rowspan;
    }

    private static int getSpanValue(Element rawCell, String type) {
        return rawCell.hasAttr(type) ? Integer.valueOf(rawCell.attr(type)) - 1 : 0;
    }

    @Override
    public String toString() {
        return value;
    }
}
