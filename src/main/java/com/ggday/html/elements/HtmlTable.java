package com.ggday.html.elements;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class HtmlTable {
    private final Optional<TableHeader> tableHeader;
    private final List<HtmlRow> htmlRows;

    public HtmlTable(Element table) {
        if (!table.nodeName().equals("table")) {
            throw new IllegalArgumentException("Incorrect element: " + table.nodeName());
        }
        Elements headerList = table.getElementsByTag("th");
        if (headerList.isEmpty()) {
            this.tableHeader = Optional.empty();
        } else {
            this.tableHeader = Optional.of(new TableHeader(headerList));
        }
        this.htmlRows = table.getElementsByTag("tr").stream()
                .filter(r-> r.getElementsByTag("th").isEmpty())
                .map(HtmlRow::new).collect(Collectors.toList());
    }

    public Optional<TableHeader> getTableHeader() {
        return tableHeader;
    }

    public List<HtmlRow> getHtmlRows() {
        return htmlRows;
    }

    public HtmlRow getRow(int index) {
        return htmlRows.get(index);
    }
}
