# csv-splitter
A command-line CSV splitter in Java.

    usage: java -jar csv-splitter-<VERSION>-all.jar
     -a,--avro_safe_header              make header row safe for Avro
     -h,--help                          print this help message
     -i,--input_file_path <arg>         path of CSV file to split
     -l,--lines <arg>                   number of lines in output files, 
                                        not including header row. Default 1000.
     -n,--no_header                     CSV has no header row
     -o,--output_directory_path <arg>   target directory for split documents

The input CSV is split into CSVs with no more than the specified number of rows. Each CSV will include the header row if there is one. Use the -n or --no_header param if there is no header row.

Currently the output files are named with a random GUID plus ".csv" and are written into the specified output directory.

The -a or --avro_safe_header options ensures that the header row conforms to the naming requirements of Avro, i.e. initial letter or underscore followed by letter, number or underscore. ([a-zA-Z_][a-zA-Z0-9_]*) This feature was added for CSV ingestion in Apache NiFi.
