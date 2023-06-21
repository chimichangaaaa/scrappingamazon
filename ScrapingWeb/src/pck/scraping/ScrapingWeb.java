package pck.scraping;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ScrapingWeb {

    public static void main(String[] args) {
        String searchTerm = "juegos";
        String url = "https://www.amazon.es/s?k=" + searchTerm;

        try {
            Document document = Jsoup.connect(url).get();
            Elements productElements = document.select(".sg-col-inner");

            String fileName = "amazon_products.csv";
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
