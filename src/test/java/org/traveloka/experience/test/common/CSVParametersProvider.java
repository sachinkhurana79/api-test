package org.traveloka.experience.test.common;

/**
 * // TODO Comment
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import au.com.bytecode.opencsv.CSVReader;
import org.testng.ITestContext;
import org.testng.annotations.DataProvider;




public class CSVParametersProvider {

  private static Scanner scanner = null;
  private static List<String[]> testData = new ArrayList<>();
  private static String[] data = null;
  private static String separator = "|";
  private static String filePath = null;
  private static String encoding = null;
  private static boolean skipHeader = true;
  private static boolean debug = false;

  @DataProvider(parallel = false, name = "csv")
  public static Object[][] createData_from_csv(final ITestContext context,
                                               final Method method) {

    DataFileParameters parameters = method
        .getAnnotation(DataFileParameters.class);
    if (parameters != null) {
      System.out.println("---->>>>>> Working Directory = " + System.getProperty("user.dir"));
      filePath = System.getProperty("user.dir")+String.format("%s/%s",
          (parameters.path().isEmpty() || parameters.path().matches("^\\.$"))
              ? System.getProperty("user.dir")
              : Utils.resolveEnvVars(parameters.path()),
          parameters.name());
      encoding = parameters.encoding().isEmpty() ? "UTF-8"
          : parameters.encoding();
    } else {
      throw new RuntimeException(
          "Missing / invalid DataFileParameters annotation");
    }
    int linenum = 0;
    if (debug) {
      System.err
          .println(String.format("Reading configuration file: '%s'", filePath));
    }
    try {
      CSVReader reader = new CSVReader(new FileReader(filePath), ',');

      while ((data = reader.readNext()) != null) {
        // nextLine[] is an array of values from the line
        linenum++;
        //String line = scanner.next();
        if (skipHeader && linenum == 1)
          continue;
        testData.add(data);
      }
    } catch (Exception e) {
      System.err.println(String.format("File was not found: '%s'", filePath));
      e.printStackTrace();
    }
    Object[][] testDataArray = new String[testData.size()][];
    testData.toArray(testDataArray);
    return testDataArray;
  }

  private static String readFile(String path, Charset encoding)
      throws IOException {
    byte[] encoded = Files.readAllBytes(Paths.get(path));
    return new String(encoded, encoding);
  }

}
