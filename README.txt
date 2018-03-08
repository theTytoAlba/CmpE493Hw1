/**
  * Irmak Kavasoglu
  * 2013400090
  */

For detailed explanation about the project, please refer to the project report.

To run the executable;
Make sure that there is a folder Dataset in the same directory as the runnable.jar.
Make sure that there are reut2-0xx.sgm files in Dataset folder.
Make sure that there is the stopwords.txt file in Dataset folder.

Run the executable by using this command:
java -jar /path/to/directory/runnable.jar

In the first run, the dictionary and indexes files will be created within Dataset folder.
After the first run, if you delete these two files or one of them, they will be recreated.
If you delete the sgm files, as long as the dictionary and indexes files are there, program will run fine.

