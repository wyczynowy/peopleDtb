package pl.com.dawidkozub;

import java.net.URI;
import java.util.logging.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.jersey.client.ClientConfig;

public class Tester {

	public static final Logger logger = Logger.getLogger(Tester.class.getSimpleName());

	public static void main(String[] args) {

		ClientConfig config = new ClientConfig();
		Client client = ClientBuilder.newClient(config);
		WebTarget service = client.target(getBaseURI());

		// Pobieranie wszyskich ludzi
		logger.info(service.request().accept(MediaType.TEXT_XML).get(String.class));

		// Dodawanie nowej osoby
		Person person = new Person("3", "Jamie", "Cullum");
		Response response = service.path("processById").path(person.getId()).request(MediaType.APPLICATION_XML).put(Entity.entity(person, MediaType.APPLICATION_XML),
				Response.class);
		logger.info("Status odpowiedzi akcji dodania nowej osoby (201 zasob dodany): " + response.getStatus());

		// Pobieranie wszystkich ludzi - format XML
		logger.info(service.request().accept(MediaType.APPLICATION_XML).get(String.class));

		// Pobieranie wszystkich ludzi - format JSON (Musi byc dodana zaleznosc
		// jersey-media-json-jackson
		// wspierajaca JSON)
		logger.info(service.request().accept(MediaType.APPLICATION_JSON).get(String.class));

		// Wyszukiwanie osoby po imieniu
		logger.info(service.path("searchByName/Jamie").request().accept(MediaType.APPLICATION_JSON).get(String.class));

		// Pobiera osobe o id 1
		response = service.path("processById/1").request().accept(MediaType.APPLICATION_JSON).get();
		logger.info("Status odpowiedzi akcji zapytania o osobe o id = 1 (200 zasob dostepny): " + response.getStatus());
		if (response.getStatus() == 200) {
			Person personId1 = response.readEntity(Person.class);
			logger.info("Pobrana osoba o id = 1:\r\n" + personId1.toString());
		}

		// Usuwa osobe o id 1
		service.path("processById/1").request().delete();

		// Pobiera wszystkich ludzi (osoba o id 1 powinna byc usunieta)
		logger.info(service.request().accept(MediaType.APPLICATION_XML).get(String.class));

		// Utworzenie nowej osoby
		Form form = new Form();
		form.param("id", "4");
		form.param("name", "Marek");
		form.param("surname", "Grechuta");
		response = service.request().post(Entity.entity(form, MediaType.APPLICATION_FORM_URLENCODED), Response.class);
		logger.info("Form response " + response.getStatus());

		// Pobierane wszystkich ludzi
		logger.info(service.request().accept(MediaType.APPLICATION_XML).get(String.class));

		response.close();

	}

	private static URI getBaseURI() {
		return UriBuilder.fromUri("http://localhost:8080/peopleDtb/rest/people").build();
	}
}