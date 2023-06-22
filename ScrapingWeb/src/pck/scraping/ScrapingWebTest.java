package pck.scraping;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ScrapingWebTest {

    @Test
    void testScrapingWeb() {
        String searchTerm = "juegos";
        String url = "https://www.amazon.es/s?k=" + searchTerm;
        String fileName = "amazon_products.csv";

        try {
            Document document = Jsoup.connect(url).get();
            Elements productElements = document.select(".sg-col-inner");

            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));

            for (Element productElement : productElements) {
                Element titleElement = productElement.select(".a-link-normal .a-text-normal").first();
                Element priceElement = productElement.select(".a-price-whole").first();

                if (titleElement != null && priceElement != null) {
                    String title = titleElement.text();
                    String price = priceElement.text().replaceAll("[^\\d.,]", "");

                    writer.write(title + "," + price);
                    writer.newLine();
                }
            }

            writer.close();
            System.out.println("Datos de productos guardados en " + fileName);

            // Verificar que el archivo se haya creado y no esté vacío
            assertTrue(Files.exists(Path.of(fileName)));
            List<String> lines = Files.readAllLines(Path.of(fileName));
            assertFalse(lines.isEmpty());
        } catch (IOException e) {
            e.printStackTrace();
            fail("Se produjo una excepción: " + e.getMessage());
        }
    }
}
