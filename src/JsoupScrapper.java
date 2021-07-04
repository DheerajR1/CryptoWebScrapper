import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JsoupScrapper {
	private static final String CRYPTO_GLOBAL_DEALS_URL = "https://coinmarketcap.com/";
	private static final String urlBase = "https://coinmarketcap.com/";

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
			doc = Jsoup.connect(CRYPTO_GLOBAL_DEALS_URL).get();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		

		Element tableElements = doc.getElementsByTag("tbody").get(0);
		Elements productElements = tableElements.getElementsByTag("tr");
		int count = 0;
		for (Element productElement : productElements) {
			Product product = new Product();
			count++;
			Elements tdElements = productElement.getElementsByTag("td");
			for (int i = 0; i < tdElements.size(); i++) {
				if (tdElements.size() == 11) {
					if (i == 2) {
						product.setName(tdElements.get(i).getElementsByTag("div").get(0).getElementsByTag("div").get(0)
								.getElementsByTag("div").get(0).getElementsByTag("p").get(0).html());
						product.setLink(urlBase + tdElements.get(i).getElementsByTag("a").attr("href"));
					} else if (i == 3) {
						System.out.println(tdElements.get(i).getElementsByTag("a").get(0).html());
						product.setFormattedPrice(tdElements.get(i).getElementsByTag("a").get(0).html());
					}
				} else if (tdElements.size() == 5) {
					if (i == 2) {
						product.setName(
								tdElements.get(i).getElementsByTag("a").get(0).getElementsByTag("span").get(1).html());
						product.setLink(urlBase + tdElements.get(i).getElementsByTag("a").attr("href"));
					} else if (i == 3) {
						product.setFormattedPrice(tdElements.get(i).getElementsByTag("span").get(0).html().replace("<!-- -->", ""));
					}
				}
			}
			products.add(product);
		}

		return products;
	}

	public static void main(String[] args) {
		JsoupScrapper jsoupScrapper = new JsoupScrapper();
		List<Product> products = jsoupScrapper.extractProducts();
		for (Product product : products) {
			System.out.println(String.format("Product:\n%s\n%s\n%s\n\n", product.getName(), product.getFormattedPrice(),
					product.getLink()));
		}
	}
}