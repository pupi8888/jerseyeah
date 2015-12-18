
package zettasoftware.jerseyeah;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import zettasoftware.jerseyeah.model.Position;

@Path("/endpoint")
public class Endpoint {

	/**
	 * 
	 * This arrayList keeps the saved positions.
	 * 
	 */
	private static ArrayList<Position> positions = new ArrayList<Position>();

	/**
	 * 
	 * This method is called when no parameter has been given to the endpoint.
	 * 
	 * 
	 */
	@GET
	@Produces(MediaType.TEXT_HTML)
	public Response getEmptyEndpoint() {

		String output = "GET /jerseyeah/rest/endpoint/1/{latitude, longitude} to save a new position.<br/>"
				+ "GET /jerseyeah/rest/endpoint/2/ to show all saved positions.<br/>"
				+ "GET /jerseyeah/rest/endpoint/2/maps to show all saved positions on Google Maps.<br/>";

		return Response.status(200).entity(output).build();
	}

	/**
	 * 
	 * This method is called when no parameter has been given to the first
	 * endpoint (endpoint/1).
	 *
	 */
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/1") // param has the following format: (latitude, longitude)
	public Response getEndpoint1() {

		String output = "Empty parameters";

		return Response.status(200).entity(output).build();
	}

	/**
	 * 
	 * This method saves a new position according to the received parameters.
	 * 
	 **/
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/1/{param}") // param must be: (latitude, longitude)
	public Response getEndpoint1(@PathParam("param") String msg) {

		String output = "";
		String paramErrorMessage = "Invalid parameters";

		// Validate params

		String params[] = msg.split(",");

		try {
			
			double latitude = Double.parseDouble(params[0]);
			double longitude = Double.parseDouble(params[1]);

			Position p = new Position(latitude, longitude);
			positions.add(p);
			output = "Your position " + latitude + ", " + longitude + " has been saved...";
			return Response.status(200).entity(output).build();
			
		} catch (Exception ex) {
			
			output = paramErrorMessage;
			return Response.status(500).entity(output).build();
			
		}

		// ------

		
	}

	/**
	 * 
	 * This method shows all saved positions in JSON format.
	 * 
	 **/
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/2")
	public Response getEndpoint2() {

		String output = "";
		Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls()
				.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();
		output = gson.toJson(positions);

		return Response.status(200).entity(output).build();
	}

	/**
	 * 
	 * This method shows all saved positions on Google Maps.
	 * 
	 **/
	@GET
	@Produces(MediaType.TEXT_HTML)
	@Path("/2/maps")
	public Response getMaps(@PathParam("param") String msg) {

		if (positions.isEmpty()) {
			return Response.status(200).entity("No positions found. Please create one position at least.").build();
		}

		String url = "../renderMap.html?"; // The webpage location containing
											// the Google Maps API Calls.
											// (src/main/webapp/renderMap.html)
		String param = "";
		int i = 1;

		for (Position p : positions) {
			// Google Maps API accepts the following url format:
			// ../renderMap.html?q={Position
			// Name}@{latitude,longitude},q={Position
			// Name}@{latitude,longitude},q=.......
			param = "q=Position%20" + i + "@" + p.getLatitude() + "," + p.getLongitude() + "&";
			url = url + param;
			i++;
		}

		try {
			return Response.temporaryRedirect(new URI(url)).entity("").build();
		} catch (URISyntaxException e) {

			return Response.status(200).entity("Google maps error.").build();
		}

	}

}
