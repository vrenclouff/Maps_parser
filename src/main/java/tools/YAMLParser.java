package tools;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Optional;

public class YAMLParser {

    public static <T> Optional<T> parse(String fileName, Class<T> tClass) {
        try {
            Yaml yaml = new Yaml(new Constructor(tClass));
            InputStream ss = new FileInputStream(Paths.get(fileName).toFile());
            T result = yaml.load(ss);
            ss.close();
            return Optional.of(result);
        } catch (IOException e) {
            return Optional.empty();
        }
    }
}
