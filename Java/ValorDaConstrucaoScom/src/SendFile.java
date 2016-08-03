import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class SendFile {

	public static void main(String[] args) {
		try {
			// URL do servidor
			String url = "http://valor-da-construcao.herokuapp.com/scom.xml";

			// Usuário
			String username = "USUARIO";

			// Senha
			String pass = "SENHA";

			// Arquivo de integração
			String file = "/caminho_do_arquivo/26042013110500_17200461000100_prod.txt";

			// Envia o arquivo
			List<Map<String, String>> products = postFile(url, file, username, pass);

			// Imprime o retorno com o status de cada produto do arquivo
			for (Map<String, String> product : products) {
				System.out.println("#################### " + product.get("item") + " ####################");
				System.out.println("Cadastrado? " + product.get("created"));
				System.out.println("Erros: " + product.get("errors"));
			}

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

	public static List<Map<String, String>> postFile(String url, String file, String username, String pass) throws Exception {

		try {

			// Cria o cliente HTTP
			DefaultHttpClient httpClient = new DefaultHttpClient();
			MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

			// Configura a autenticação do cliente HTTP
			httpClient.getCredentialsProvider().setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, pass));

			// Anexa o arquivo no post
			File fileToUpload = new File(file);
			FileBody fileBody = new FileBody(fileToUpload, "application/octet-stream");
			entity.addPart("products_file", fileBody);

			// Executa o post
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(entity);
			HttpResponse response = httpClient.execute(httpPost);

			// Lê o XML retornado
			DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			InputSource is = new InputSource();
			String s = EntityUtils.toString(response.getEntity());
			is.setCharacterStream(new StringReader(s));

			NodeList nodes = db.parse(is).getElementsByTagName("product");

			if (response.getStatusLine().getStatusCode() != 200) {

				System.out.println(s);

				throw new RuntimeException("Failed : HTTP error code : " + response.getStatusLine().getStatusCode());
			}

			// Converte o XML em uma lista de produtos
			List<Map<String, String>> products = new ArrayList<Map<String, String>>();

			for (int i = 0; i < nodes.getLength(); i++) {
				Element element = (Element) nodes.item(i);

				Map<String, String> product = new HashMap<String, String>();

				product.put("created", getCharacterDataFromElement((Element) element.getElementsByTagName("created").item(0)));
				product.put("errors", getCharacterDataFromElement((Element) element.getElementsByTagName("errors").item(0)));
				product.put("item", getCharacterDataFromElement((Element) element.getElementsByTagName("item").item(0)));

				products.add(product);
			}

			// Mata a conexão
			httpClient.getConnectionManager().shutdown();

			return products;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public static String getCharacterDataFromElement(Element e) {
		Node child = e.getFirstChild();
		if (child instanceof CharacterData) {
			CharacterData cd = (CharacterData) child;
			return cd.getData();
		}
		return "";
	}

}