# csv-splitter
A command-line CSV splitter in Java.

    usage: java -jar csv-splitter-<VERSION>-all.jar
     -h,--help                          print this help message
     -i,--input_file_path <arg>         path of CSV file to split
     -l,--lines <arg>                   number of lines in output files, 
                                        not including header row. Default 1000.
     -n,--no_header                     CSV has no header row
     -o,--output_directory_path <arg>   target directory for split documents

Currently the output files are named with a random GUID plus ".xml" and are written into the specified output directory.
