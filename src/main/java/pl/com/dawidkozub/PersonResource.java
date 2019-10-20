package pl.com.dawidkozub;

import java.util.logging.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.xml.bind.JAXBElement;

public class PersonResource {

	public static final Logger logger = Logger.getLogger(PersonResource.class.getSimpleName());

	@Context
	UriInfo uriInfo;
	@Context
	Request request;
	String id;

	public PersonResource(UriInfo uriInfo, Request request, String id) {
		this.uriInfo = uriInfo;
		this.request = request;
		this.id = id;
	}

	@GET
	@Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
	public Person getPerson() {
		logger.info("getPerson()");
		Person person = PeopleDao.instance.getModel().get(id);
		if (person == null)
			throw new RuntimeException("Get: person with " + id + " not found");
		return person;
	}

	@GET
	@Produces(MediaType.TEXT_XML)
	public Person getPersonHTML() {
		logger.info("getPersonHTML()");
		Person person = PeopleDao.instance.getModel().get(id);
		if (person == null)
			throw new RuntimeException("Get: Person with " + id + " not found");
		return person;
	}

	@PUT
	@Consumes(MediaType.APPLICATION_XML)
	public Response putPerson(JAXBElement<Person> person) {
		logger.info("putPerson()");
		Person p = person.getValue();
		return putAndGetResponse(p);
	}

	@DELETE
	public void deleteTodo() {
		logger.info("deletePerson()");
		Person p = PeopleDao.instance.getModel().remove(id);
		if (p == null)
			throw new RuntimeException("Delete: Person with " + id + " not found");
	}

	private Response putAndGetResponse(Person person) {
		logger.info("putAndGetResponse()");
		Response res;
		if (PeopleDao.instance.getModel().containsKey(person.getId())) {
			res = Response.noContent().build();
		} else {
			res = Response.created(uriInfo.getAbsolutePath()).build();
		}
		PeopleDao.instance.getModel().put(person.getId(), person);
		return res;
	}

}
