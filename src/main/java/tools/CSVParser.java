package tools;

import filter.Filter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.lang.String.join;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.of;
import static java.util.stream.StreamSupport.stream;

public class CSVParser {

    private static final String DELIMETER = String.valueOf(CSVFormat.EXCEL.getDelimiter());


    public static List<String> read(String fileName, Class<? extends Filter> filterClass) {
        try {
            Filter filter = filterClass.newInstance();
            Iterable<CSVRecord> records = CSVFormat.EXCEL.withFirstRecordAsHeader().parse(new FileReader(fileName));
            return  stream(records.spliterator(), false)
                    .map(r -> filter.process(r.get(1))).collect(toList());
        } catch (IOException | InstantiationException | IllegalAccessException e) {
            return Collections.emptyList();
        }
    }

    public static void write(Object object, BufferedWriter writer) {

        Field[] fields = object.getClass().getDeclaredFields();
        List<String> components = new ArrayList<>(fields.length);
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                Object val = field.get(object);
                components.add(val != null ? val.toString() : "");
            } catch (IllegalAccessException e) {}
        }

        String str = join(DELIMETER, components) + "\n";

        try {
            writer.write(str);
        } catch (IOException e) {
            System.err.println("Cannot write a line to the output file.");
        }
    }

    public static void writeHeader(Class<? extends Object> oClass, BufferedWriter writer) {

        String header = join(DELIMETER, of(oClass.getDeclaredFields()).map(Field::getName).collect(toList()))  + "\n";

        try {
            writer.write(header);
        } catch (IOException e) {
            System.err.println("Cannot write a header to the output file.");
        }
    }
}
