package html.elements;

import org.jsoup.nodes.Element;

import java.util.List;
import java.util.stream.Collectors;

public class HtmlRow {
    private final List<HtmlCell> htmlCells;

    public HtmlRow(Element element) {
        this.htmlCells = element.getElementsByTag("td").stream().map(HtmlCell::new).collect(Collectors.toList());
    }

    public HtmlCell getCell(int index) {
        return htmlCells.get(index);
    }

    public HtmlCell getIndex(String text) {
        return htmlCells.stream()
                .filter(v -> v.getValue().trim().equals(text.trim()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Can't find cell " + text + "from " + htmlCells.stream()
                        .map(HtmlCell::getValue)
                        .collect(Collectors.joining(", "))));
    }

    public List<HtmlCell> getHtmlCells() {
        return htmlCells;
    }
}
