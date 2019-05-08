package com.ggday;

import com.ggday.content_type.*;
import com.ggday.rest_api.RestManager;
import com.ggday.rest_api.dto.DrupalElementDTO;
import com.ggday.scraper.Scraper;
import com.ggday.utils.SysProperties;
import com.google.common.base.Preconditions;
import org.apache.commons.cli.*;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class Starter {

    private static final RestManager restManager = new RestManager();

    public static void main(String[] args) {

        Scraper scraper = new Scraper(readUrlFromArgs(args));
        Article article = scraper.parse();
        DrupalElementDTO createdArticle = restManager.postArticle(article);
        Preconditions.checkNotNull(createdArticle, "Article was not created");
        postPrimaryImage(article, createdArticle);
        if (article instanceof ArticlePortfolio) {
            List<DrupalElementDTO> paragraphs = new ArrayList<>();
            ((ArticlePortfolio) article).getItems().forEach(item ->
                    paragraphs.add(restManager.postParagraph(item, createdArticle.getAttributes().getDrupalInternalId()))
            );
            for (int i = 0; i < ((ArticlePortfolio) article).getItems().size(); i++) {
                restManager.postParagraphImage(((ArticlePortfolio) article).getItems().get(i).getImage(), paragraphs.get(i).getId());
            }
            restManager.patchArticleListItems(paragraphs, createdArticle.getId());
        }
    }

    @Nullable
    private static DrupalElementDTO postPrimaryImage(Article article, DrupalElementDTO postedArticle) {
        DrupalElementDTO image = null;
        if (article instanceof ArticleStructured) {
            image = restManager.postPrimaryImage(((ArticleStructured)article).getPrimaryImage(), postedArticle.getId());
        }
        if (article instanceof ArticleList) {
            image = restManager.postPrimaryImage(((ArticleList)article).getPrimaryImage(), postedArticle.getId());
        }
        return image;
    }

    private static String readUrlFromArgs(String... args) {
        Options options = new Options();
        Option input = new Option("u", "url", true, "input document url");
        input.setRequired(true);
        options.addOption(input);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name", options);
            System.exit(1);
        }
        return cmd.getOptionValue("url");
    }
}
