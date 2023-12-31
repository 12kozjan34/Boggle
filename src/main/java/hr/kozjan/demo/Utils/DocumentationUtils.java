package hr.kozjan.demo.Utils;

import javafx.scene.control.Alert;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public class DocumentationUtils {
    public static void generateDocumentation() {

        Path targetPath = Path.of("target");

        try (Stream<Path> paths = Files.walk(targetPath)) {
            List<String> classFiles = paths
                    .map(Path::toString)
                    .filter(file -> file.endsWith(".class"))
                    .filter(file -> !file.endsWith("module-info.class"))
                    .toList();

            String headerHtml = """
                    <!DOCTYPE html>
                    <html>
                    <head>
                    <title>Page Title</title>
                    </head>
                    <body>
                                    <h1>Project documentation</h1>
                    """;

            for(String classFile : classFiles) {
                String[] classFileTokens = classFile.split("classes");
                String classFilePath = classFileTokens[1];
                String reducedClassFilePath =
                        classFilePath.substring(1, classFilePath.lastIndexOf('.'));
                String fullyQualifiedName = reducedClassFilePath.replace('\\', '.');
                //System.out.println("FQN: " + fullyQualifiedName);

                headerHtml += "<h2>" + fullyQualifiedName + "</h2>";

                Class<?> deserializedClass = Class.forName(fullyQualifiedName);

                Field[] classFields = deserializedClass.getDeclaredFields();

                for(Field field : classFields) {

                    headerHtml += "<h3>";

                    int modifiers = field.getModifiers();

                    if(Modifier.isPublic(modifiers)) {
                        headerHtml += "public ";
                    }
                    else if(Modifier.isPrivate(modifiers)) {
                        headerHtml += "private ";
                    }
                    else if(Modifier.isProtected(modifiers)) {
                        headerHtml += "protected ";
                    }

                    if(Modifier.isStatic(modifiers)) {
                        headerHtml += "static ";
                    }

                    if(Modifier.isFinal(modifiers)) {
                        headerHtml += "final ";
                    }

                    headerHtml += field.getType().getTypeName() + " ";

                    headerHtml += field.getName() + "\n";

                    headerHtml += "</h3>";
                }
            }

            String footerHtml = """
                    </body>
                    </html>
                    """;

            Path documentationFilePath = Path.of("files/documentation.html");

            String fullHtml = headerHtml + footerHtml;

            Files.write(documentationFilePath, fullHtml.getBytes());

            MessageUtils.showDialog(Alert.AlertType.INFORMATION,"Documentation generation", "Documentation was successfully generated!");
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
