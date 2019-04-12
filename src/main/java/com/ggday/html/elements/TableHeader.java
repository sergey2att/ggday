package com.ggday.html.elements;

import org.jsoup.select.Elements;

import java.util.List;
import java.util.stream.Collectors;

public class TableHeader {
    private List<HtmlCell> htmlCells;
    private Elements header;

    public TableHeader(Elements headerList) {
        this.header = headerList;
        this.htmlCells = calcCells();
    }

    public List<HtmlCell> getHtmlCells() {
        return htmlCells;
    }

    public int cellIndex(String name) {
        return htmlCells.indexOf(htmlCells.stream()
                .filter(t-> t.getValue().equals(name))
                .findFirst()
                .orElseThrow(()->new IllegalArgumentException("can't find element by name: " + name)));
    }


    private List<HtmlCell> calcCells() {
        return header.stream().map(HtmlCell::new).collect(Collectors.toList());
    }
}
