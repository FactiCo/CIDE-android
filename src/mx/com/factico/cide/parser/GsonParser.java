package mx.com.factico.cide.parser;

import java.lang.reflect.Type;
import java.util.List;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import mx.com.factico.cide.beans.Propuesta;
import mx.com.factico.cide.beans.Testimonio;
import mx.com.factico.cide.dialogues.Dialogues;

public class GsonParser {
	private static String TAG_CLASS = GsonParser.class.getName();
	
	public static final String json = "{"
			+ "\"id\": \"24\","
			+ "\"name\": \"Mario\","
			+ "\"email\": \"mario\","
			+ "\"category\": \"Justicia en el trabajo\","
			+ "\"description\": \"Prueba\","
			+ "\"city\": \"Distrito Federal\","
			+ "\"age\": \"24\","
			+ "\"gender\": \"Masculino\","
			+ "\"scholarity\": \"Superior\","
			+ "\"fecha_add\": \"0\","
			+ "\"dispositivo\": \"Android\","
			+ "\"\" }";
	
	public static final String gson = "{"
			// + "\"Propuesta\": "
			// + "["
				// + "{"
					+ "\"id\": \"001ABC\","
					+ "\"categoria\": \"Justicia para trabajadores\","
					+ "\"titulo\": \"Comprar una Cafetera para FactiCo\","
					+ "\"descripcion\": \"Lumbersexual heirloom drinking vinegar mustache, cliche put a bird on it forage messenger bag. Bespoke actually Blue Bottle deep v gastropub artisan, fap sartorial vinyl small batch. PBR yr twee Schlitz mlkshk. Roof party Vice disrupt, Kickstarter Portland cornhole twee swag High Life chillwave heirloom pug. Readymade wayfarers distillery letterpress, Tumblr gastropub forage meditation Austin Schlitz paleo cray. Asymmetrical 8-bit organic pop-up, Pitchfork normcore migas meditation. Fixie wayfarers pour-over PBR.Whatever sartorial lomo banjo, flexitarian Godard you probably haven't heard of them Shoreditch artisan slow-carb taxidermy flannel jean shorts. Banh mi keytar pickled, +1 leggings viral before they sold out. Fingerstache freegan four dollar toast butcher. Jean shorts salvia bespoke, street art meditation fingerstache trust fund cornhole tousled Pinterest Vice. Brunch gluten-free whatever typewriter, mixtape migas organic next level stumptown lo-fi Thundercats mustache. Bushwick cornhole mixtape ennui hella. Tilde cliche pop-up letterpress, you probably haven't heard of them mustache bitters vinyl Helvetica deep v meh.\","
					+ "\"autor\": {"
						+ "\"nombre\": \"Jordy\","
						+ "\"mail\": \"jordy@factico.com.mx\","
						+ "\"id_FB\": \"10152779934266189\""
					+ "},"
					+ "\"fecha_propuesta\": \"15/01/2015\","
					+ "\"votos\": {"
						+ "\"favor\": {"
							+ "\"link\": \"CIDE/Propuestas/votar/001ABC/favor\","
						+ "}"
					+ "}"
				// + "}"
			// + "]"
		+ "}";
			
	
	public static Testimonio getTestimonioFromJSON(String json) throws Exception {
		Gson gson = new Gson();
		Testimonio testimonio = gson.fromJson(json, Testimonio.class);
		
		return testimonio;
	}
	
	@SuppressWarnings("unchecked")
	public static List<Testimonio> getListTestimoniosFromJSON(String json) {
		Gson gson = new Gson();
		
		Type listType = new TypeToken<List<Testimonio>>(){}.getType();
		List<Testimonio> listTestimonios = (List<Testimonio>) gson.fromJson(json, listType);
		
		Dialogues.Log(TAG_CLASS, "List Testimonios: " + listTestimonios.size(), Log.INFO);
		
		return listTestimonios;
	}
	
	public static Propuesta getPropuestaFromJSON(String json) {
		Gson gson = new Gson();
		Propuesta propuesta = gson.fromJson(json, Propuesta.class);
		
		Dialogues.Log(TAG_CLASS, "Propuestas: " + propuesta.getTitulo(), Log.INFO);
		Dialogues.Log(TAG_CLASS, "Autor nombre: " + propuesta.getAutor().getNombre(), Log.INFO);
		
		return propuesta;
	}
	
	public static String createJSON(Propuesta propuesta) {
		Gson gson = new Gson();
		String json = gson.toJson(propuesta);
		
		Dialogues.Log(TAG_CLASS, "Json Propuestas: " + json, Log.INFO);
		
		return json;
	}
}
