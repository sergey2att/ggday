package com.ggday;

import com.ggday.scraper.Scraper;
import com.ggday.utils.FTPClient;
import org.apache.commons.cli.*;

public class Starter {
    public static void main(String[] args) {

       /*FTPClient ftpClient = new FTPClient("zsergeyu.beget.tech");
       if (ftpClient.login("", "")) {
           if (ftpClient.openPublicFilesDirectory()) {
               if (ftpClient.createTodayImageDirectory()) {
                   System.out.println(ftpClient.getCurrentDirectoryPath());
               }
           }

           String p = ftpClient.getCurrentDirectoryPath();
       }*/

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
        String url = cmd.getOptionValue("url");

        Scraper scraper = new Scraper(url);
        scraper.parse();


    }
}
