import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;



public class JsoupScrapper {
    private static final String EBAY_GLOBAL_DEALS_URL = "https://coinmarketcap.com/";
    
    private static final String PRODUCT_CARD_CLASS = "dne-itemtile-detail";
    private static final String PRODUCT_TITLE_CLASS = "dne-itemtile-title";
    private static final String PRODUCT_LINK_SELECTOR = ".dne-itemtile-title a";
    private static final String PRODUCT_PRICE_SELECTOR = ".dne-itemtile-price .first";

    class Product {
        private String name;
        private String link;
        private String formattedPrice;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }
        
        public String getFormattedPrice() {
            return formattedPrice;
        }

        public void setFormattedPrice(String formattedPrice) {
            this.formattedPrice = formattedPrice;
        }
    }
    
    public List<Product> extractProducts() {
        List<Product> products = new ArrayList<>();
        
        Document doc;
        try {
            doc = Jsoup.connect(EBAY_GLOBAL_DEALS_URL).get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
        Elements productElements = doc.getElementsByClass(PRODUCT_CARD_CLASS);
        for (Element productElement : productElements) {
            Product product = new Product();
            Elements titleElements = productElement.getElementsByClass(PRODUCT_TITLE_CLASS);
            if (!titleElements.isEmpty()) {
                product.setName(titleElements.get(0).attr("title"));
            }
            Elements linkElements = productElement.select(PRODUCT_LINK_SELECTOR);
            if (!linkElements.isEmpty()) {
                product.setLink(linkElements.get(0).attr("href"));
            }
            Elements priceElements = productElement.select(PRODUCT_PRICE_SELECTOR);
            if (!priceElements.isEmpty()) {
                product.setFormattedPrice(priceElements.get(0).text());
            }
            products.add(product);
        }
        
        return products;
    }
    
    public static void main(String[] args) {
        JsoupScrapper jsoupScrapper = new JsoupScrapper();
        List<Product> products = jsoupScrapper.extractProducts();
        for (Product product : products) {
            System.out.println(
                    String.format("Product:\n%s\n%s\n%s\n\n", product.getName(), product.getFormattedPrice(), product.getLink())
            );
        }
    }
}