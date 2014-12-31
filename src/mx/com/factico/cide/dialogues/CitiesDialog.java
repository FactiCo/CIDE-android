package mx.com.factico.cide.dialogues;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import mx.com.factico.cide.R;
import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.TextWatcher;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CitiesDialog extends Activity {
	public static final String TAG_CLASS = CitiesDialog.class.getName();

	private String[] listAbc = {"A", "B", "C", "D", "E", "F", "G", "H", "I",
								"J", "K", "L", "M", "N", "O", "P", "Q", "R",
								"S", "T", "U", "V", "W", "X", "Y", "Z"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_cities);
		
		/* Propuesta propuesta = new Propuesta();
		propuesta.setId(1234567890);
		propuesta.setCategoria("Justicia");
		propuesta.setTitulo("Titulo de la propuesta");
		propuesta.setDescripcion("Lorem Ipsum");
		
		Propuesta.Autor autor = propuesta.new Autor();
		autor.setId_FB(987654321);
		autor.setMail("carlos@factico.com.mx");
		autor.setNombre("Carlos");
		propuesta.setAutor(autor);
		
		String json = GsonParser.createJSON(propuesta);
		
		Propuesta propuesta2 = GsonParser.getPropuestaFromJSON(json); */
		
		initUI();
	}
	
	private LinearLayout container;
	private List<String> listCities;
	private EditText etSearch;
	private void initUI() {
		String[] arrayCities = getResources().getStringArray(R.array.testimonios_add_cities);
		listCities = new ArrayList<String>(Arrays.asList(arrayCities));
		
		sortCitiesByAbc(listCities);
		
		container = (LinearLayout) findViewById(R.id.dialog_cities_vg_container);
		
		etSearch = (EditText) findViewById(R.id.dialog_cities_et_search);
		etSearch.addTextChangedListener(new TextWatcher() {
			@Override public void afterTextChanged(Editable s) {}
			@Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			@Override public void onTextChanged(CharSequence s, int start, int before, int count) {
				loadCitiesView();
			}
		});
		
		loadCitiesView();
	}
	
	private void loadCitiesView() {
		container.removeAllViews();
		
		for (String letter : listAbc) {
			View sectionView = createSectionView(letter);
			
			LinearLayout llSectionView = new LinearLayout(this);
			llSectionView.setOrientation(LinearLayout.VERTICAL);
			boolean flag = false;
			boolean flagSpannable = false;
			for (String city : listCities) {
				if (city.startsWith(letter)) {
					flag = true;
					
					TextView cityView = createCityView(city);
					
					String prefix = etSearch.getText().toString();
					flagSpannable = setSpannableColor(cityView, city, prefix, getResources().getColor(R.color.white), getResources().getColor(R.color.primary_color));
					
					llSectionView.addView(cityView);
					
					Dialogues.Log(TAG_CLASS, "City: " + city + ", Flag: " + flag + ", FlagSpannable: " + flagSpannable, Log.INFO);
				}
			}
			
			if (flag && flagSpannable) {
				container.addView(sectionView);
				
				container.addView(llSectionView);
			}
		}
	}
	
	private TextView createCityView(String city) {
		TextView tvCity = new TextView(this);
		tvCity.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		tvCity.setBackgroundResource(R.drawable.selector_btn_dark);
		tvCity.setPadding(5, 5, 5, 5);
		tvCity.setOnClickListener(CityOnClickListener);
		// tvCity.setText(city);
		
		return tvCity;
	}

	public View createSectionView(String letter) {
		LinearLayout layout = new LinearLayout(this);
		
		TextView tvLetter = new TextView(this);
		tvLetter.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		tvLetter.setBackgroundColor(getResources().getColor(R.color.primary_color));
		tvLetter.setPadding(20, 0, 0, 0);
		tvLetter.setText(letter);
		tvLetter.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
		tvLetter.setTextColor(getResources().getColor(R.color.white));
		
		View view = new View(this);
		view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 2));
		
		layout.addView(tvLetter);
		layout.addView(view);
		
		return layout;
	}
	
	private boolean setSpannableColor(TextView view, String text, String prefix, int textColor, int backgroundColor) {
		view.setText(text, TextView.BufferType.SPANNABLE);
		Spannable spannableTextColor = (Spannable) view.getText();
		Spannable spannableBackground = (Spannable) view.getText();
		
		boolean flagSpannable = text.startsWith(prefix);
		if (flagSpannable) {
			int i = text.indexOf(prefix);
			Dialogues.Log(TAG_CLASS, text + " contiene prefix: " + prefix + " at " + i + " _ " + i + prefix.length(), Log.INFO);
			spannableTextColor.setSpan(new ForegroundColorSpan(textColor), i, i + prefix.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			spannableBackground.setSpan(new BackgroundColorSpan(backgroundColor),  i, i + prefix.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		
		return flagSpannable;
	}
	
	View.OnClickListener CityOnClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			String city = ((TextView) v).getText().toString();
			
			Dialogues.Toast(getApplicationContext(), "City selected: " + city, Toast.LENGTH_LONG);
		}
	};
	
	private List<String> sortCitiesByAbc(List<String> list) {
		Collections.sort(list, new Comparator<String>() {
			@Override
			public int compare(String s1, String s2) {
				return s1.compareToIgnoreCase(s2);
			}
		});
		
		return list;
	}
}
