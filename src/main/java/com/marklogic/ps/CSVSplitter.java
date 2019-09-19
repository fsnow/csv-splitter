package com.marklogic.ps;

import java.io.*;
import java.util.*;

import com.opencsv.CSVIterator;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import org.apache.commons.cli.*;

public class CSVSplitter
{
    public static void main(String[] args) throws Exception
    {
        // create the command line parser
        CommandLineParser parser = new DefaultParser();

        // create the Options
        Options options = new Options();
        options.addOption(new Option("i", "input_file_path", true, "path of CSV file to split"));
        options.addOption(new Option("o", "output_directory_path", true, "target directory for split documents"));
        options.addOption(new Option("l", "lines", true, "number of lines in output files, not including header row"));
        options.addOption(new Option("n", "no_header", false, "CSV has no header row"));
        options.addOption(new Option("h", "help", false, "print this help message"));

        CommandLine line = null;
        try {
            line = parser.parse( options, args );
        }
        catch( ParseException exp ) {
            System.err.println( "Parsing failed.  Reason: " + exp.getMessage() );
            return;
        }

        if (line.hasOption("h")) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("java -jar csv-splitter-<VERSION>-all.jar", options);
            return;
        }

        CSVSplitter splitter = new CSVSplitter();

        splitter.setInputFilePath(line.getOptionValue("i"));
        splitter.setOutputDirectoryPath(line.getOptionValue("o"));
        splitter.setHasHeaderRow(!line.hasOption("n"));

        if (line.hasOption("l")) {
            try {
                splitter.setLines(Integer.parseInt(line.getOptionValue("l")));
            }
            catch (Exception ex) {
                System.out.println("Threw exception on the -l or --lines argument, not an integer.");
                System.out.println(ex.getMessage());
                return;
            }
        }

        splitter.split();
    }

    private String inputFilePath;
    private boolean hasHeaderRow = true;
    private int lines = 1000;
    private String outputDirectoryPath;

    public void setInputFilePath(String path) {
        if (path == null) {
            this.inputFilePath = "";
        } else {
            this.inputFilePath = path;
        }
    }

    public void setHasHeaderRow(boolean val) {
        this.hasHeaderRow = val;
    }

    public void setLines(int val) {
        if (val >= 1) {
            this.lines = val;
        }
        else {
            this.lines = 1000;
        }
    }

    public void setOutputDirectoryPath(String path) {
        if (path == null) {
            this.outputDirectoryPath = "";
        } else {
            this.outputDirectoryPath = path;
        }
    }

    private String newFilePath() {
        String fileName = UUID.randomUUID().toString() + ".csv";
        String outputFilePath = this.outputDirectoryPath +
                (this.outputDirectoryPath.length() > 0 ? File.separator : "") +
                fileName;
        return outputFilePath;
    }

    private void split() throws Exception
    {
        String[] headerLine = null;
        CSVReader reader = new CSVReader(new FileReader(this.inputFilePath));
        CSVWriter writer = null;
        int readerRowCount = 0;
        int thisFileRowCount = 0;
        if (!this.hasHeaderRow) readerRowCount++;

        CSVIterator iterator = new CSVIterator(reader);

        while (iterator.hasNext()) {
            String[] nextLine = iterator.next();

            // read the header row
            if (readerRowCount == 0) {
                headerLine = nextLine;
            }

            // open a new file
            if (writer == null) {
                writer = new CSVWriter(new FileWriter(newFilePath()));
                if (headerLine != null) writer.writeNext(headerLine);
                thisFileRowCount = 0;
            }

            // write a non-header row
            if (readerRowCount > 0) {
                writer.writeNext(nextLine);
                thisFileRowCount++;
            }

            // close a file that has reached its max row count
            if (thisFileRowCount >= this.lines) {
                writer.close();
                writer = null;
            }

            readerRowCount++;
        }

        // close the writer if open
        if (writer != null) writer.close();

        reader.close();
    }
}