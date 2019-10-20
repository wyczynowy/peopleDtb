package pl.com.dawidkozub;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.UriInfo;

@Path("/people")
public class PeopleResource {

	public static final Logger logger = Logger.getLogger(PeopleResource.class.getSimpleName());

	@Context
	UriInfo uriInfo;
	@Context
	Request request;

	@GET
	@Produces(MediaType.TEXT_XML)
	public List<Person> getPeopleBrowser() {
		logger.info("getPeopleBrowser()");
		List<Person> people = new ArrayList<>();
		people.addAll(PeopleDao.instance.getModel().values());
		return people;
	}

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<Person> getPeople() {
		logger.info("getPeople()");
		List<Person> people = new ArrayList<>();
		people.addAll(PeopleDao.instance.getModel().values());
		return people;
	}

	@GET
	@Path("count")
	@Produces(MediaType.TEXT_PLAIN)
	public String getCount() {
		logger.info("getCount()");
		int count = PeopleDao.instance.getModel().size();
		return String.valueOf(count);
	}

	@POST
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public void newPerson(@FormParam("id") String id,
			@FormParam("name") String name,
			@FormParam("surname") String surname,
			@Context HttpServletResponse servletResponse) throws IOException {
		logger.info("newPerson()");
		Person person = new Person(id, name, surname);
		PeopleDao.instance.getModel().put(id, person);

		servletResponse.sendRedirect("../create_person.html");
	}

	@GET
	@Path("searchByName/{name}")
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public List<Person> searchByName(@PathParam("name") String name) {
		logger.info("searchByName()");
		return PeopleDao.instance.getModel().values().stream().filter(e -> e.getName().equals(name)).collect(Collectors.toList());
	}

	@Path("processById/{id}")
	public PersonResource processById(@PathParam("id") String id) {
		logger.info("processById()");
		return new PersonResource(uriInfo, request, id);
	}

}
