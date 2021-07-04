import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JsoupScrapper {
	private static final String EBAY_GLOBAL_DEALS_URL = "https://coinmarketcap.com/";

	private static final String COIN_TABLE_CLASS = "h7vnx2-2 eppVuM cmc-table th{position:sticky;top:0;}th:nth-child(3),td:nth-child(3){white-space:initial;}td:nth-child(4){min-width:8em;}@media (min-width:576px) and (max-width:1200px){th{top:0;}th:nth-child(1),td:nth-child(1){position:sticky;left:-5px;z-index:100;}th:nth-child(2),td:nth-child(2){position:sticky;left:28px;z-index:100;}th:nth-child(3),td:nth-child(3){position:sticky;left:63px;z-index:100;}}@media (max-width:780px){th,td{padding:10px 4px;img{z-index:30;}}th:nth-child(2),td:nth-child(2){display:none;}th:nth-child(1),td:nth-child(1){position:sticky;left:0px;z-index:100;min-width:24px;}th:nth-child(3),td:nth-child(3){position:sticky;left:24px;z-index:100;}[class^='PercentageBar__PercentageOuter']{z-index:30;}} ";
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
		Product product = new Product();

		Element tableElements = doc.getElementsByTag("tbody").get(0);
		Elements productElements = tableElements.getElementsByTag("tr");
		System.out.println(productElements.size());
		for (Element productElement : productElements) {
			Elements tdElements = productElement.getElementsByTag("td");
			for (int i = 0; i < tdElements.size(); i++) {
//				System.out.println(tdElements.get(i));
				if (tdElements.size() == 11) {
					if (i == 2) {
						System.out.println(tdElements.get(i).getElementsByTag("div").get(0).getElementsByTag("div").get(0).getElementsByTag("div").get(0).getElementsByTag("p").get(0).html());
						product.setName(":");
					}
				} else if {
					
				}
			}
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
			System.out.println(String.format("Product:\n%s\n%s\n%s\n\n", product.getName(), product.getFormattedPrice(),
					product.getLink()));
		}
	}
}