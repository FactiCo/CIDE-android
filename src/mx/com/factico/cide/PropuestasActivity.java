package mx.com.factico.cide;

import java.util.List;

import mx.com.factico.cide.beans.Propuesta;
import mx.com.factico.cide.dialogues.Dialogues;
import mx.com.factico.cide.httpconnection.HttpConnection;
import mx.com.factico.cide.parser.GsonParser;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

public class PropuestasActivity extends ActionBarActivity {
	public static final String TAG_CLASS = PropuestasActivity.class.getName();
	
	private Propuesta propuesta = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_propuestas);
		
		setSupportActionBar();
		initUI();
	}
	
	private void setSupportActionBar() {
		Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
		mToolbar.setTitle("");
		mToolbar.setTitleTextColor(getResources().getColor(R.color.white));
		mToolbar.getBackground().setAlpha(0);
        setSupportActionBar(mToolbar);
        
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}
	
	private void initUI() {
		GetDataAsyncTask task = new GetDataAsyncTask();
		task.execute();
	}
	
	private class GetDataAsyncTask extends AsyncTask<String, String, String> {
		private ProgressDialog dialog;
		
		public GetDataAsyncTask() {}
		
		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(PropuestasActivity.this);
			dialog.setMessage(getResources().getString(R.string.getdata_loading));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
		}
		
		@Override
		protected String doInBackground(String... params) {
			String result = HttpConnection.GET(HttpConnection.URL_PROPUESTAS);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (dialog != null && dialog.isShowing()) {
				dialog.dismiss();
			}
			
			// Dialogues.Toast(getApplicationContext(), "Result: " + result, Toast.LENGTH_LONG);
			Dialogues.Log(TAG_CLASS, "Result: " + result, Log.INFO);
			
			loadDataFromJson(result);
		}
	}
	
	private void loadDataFromJson(String json) {
		try {
			propuesta = GsonParser.getPropuestaFromJSON(json);
			
			if (propuesta != null) {
				loadPropuestasViews(propuesta);
			} else {
				Dialogues.Toast(getBaseContext(), getString(R.string.error_testimonios_recientes), Toast.LENGTH_LONG);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void loadPropuestasViews(Propuesta propuesta) {
		Dialogues.Log(TAG_CLASS, "/***** Propuesta", Log.INFO);
		Dialogues.Log(TAG_CLASS, "Count: " + propuesta.getCount(), Log.INFO);
	
		List<Propuesta.Items> listItems = propuesta.getItems();
		
		Dialogues.Log(TAG_CLASS, "/***** Items", Log.INFO);
		Dialogues.Log(TAG_CLASS, "Items Size: " + listItems.size(), Log.INFO);
		
		if (listItems != null  && listItems.size() > 0) {
			boolean hasInCategory = false;
			
			for (int count = 0; count < listItems.size(); count++) {
				Propuesta.Items item = listItems.get(count);
				
				if (item != null) {
					Dialogues.Log(TAG_CLASS, "Items Id: " + item.getId(), Log.INFO);
					Dialogues.Log(TAG_CLASS, "Items Category: " + item.getCategory(), Log.INFO);
					Dialogues.Log(TAG_CLASS, "Items CategoryId: " + item.getCategoryId(), Log.INFO);
					Dialogues.Log(TAG_CLASS, "Items Title: " + item.getTitle(), Log.INFO);
					Dialogues.Log(TAG_CLASS, "Items Description: " + item.getDescription(), Log.INFO);
					
					List<Propuesta.Items.Comments.Data> listCommentsData = item.getComments().getData();
					Dialogues.Log(TAG_CLASS, "/***** Comments", Log.INFO);
					Dialogues.Log(TAG_CLASS, "Items Comments Data Size: " + listCommentsData.size(), Log.INFO);
					
					if (listCommentsData != null  && listCommentsData.size() > 0) {
						for (Propuesta.Items.Comments.Data data : listCommentsData) {
							Dialogues.Log(TAG_CLASS, "Items Comments Data Id: " + data.getId(), Log.INFO);
							Dialogues.Log(TAG_CLASS, "Items Comments Data Parent: " + data.getParent(), Log.INFO);
							Dialogues.Log(TAG_CLASS, "Items Comments Data ProposalId: " + data.getProposalId(), Log.INFO);
							Dialogues.Log(TAG_CLASS, "Items Comments Data Message: " + data.getMessage(), Log.INFO);
							Dialogues.Log(TAG_CLASS, "Items Comments Data Created: " + data.getCreated(), Log.INFO);
							
							Propuesta.Items.Comments.Data.From from = data.getFrom();
							Dialogues.Log(TAG_CLASS, "Items Comments Data From Id: " + from.getFcbookid(), Log.INFO);
							Dialogues.Log(TAG_CLASS, "Items Comments Data From Name: " + from.getName(), Log.INFO);
						}
					}
					
					Propuesta.Items.Votes votes = item.getVotes();
					Dialogues.Log(TAG_CLASS, "/***** Votes", Log.INFO);
					
					Propuesta.Items.Votes.Favor votesFavor = votes.getFavor();
					List<Propuesta.Items.Votes.Participantes> listVotesFavorParticipantes = votesFavor.getParticipantes();
					Dialogues.Log(TAG_CLASS, "/***** Favor Participantes", Log.INFO);
					Dialogues.Log(TAG_CLASS, "Items Votes Favor Size: " + listVotesFavorParticipantes.size(), Log.INFO);
					
					if (listVotesFavorParticipantes != null  && listVotesFavorParticipantes.size() > 0) {
						for (Propuesta.Items.Votes.Participantes participantes : listVotesFavorParticipantes) {
							Dialogues.Log(TAG_CLASS, "Items Votes Favor Participantes Id: " + participantes.getId(), Log.INFO);
							Dialogues.Log(TAG_CLASS, "Items Votes Favor Participantes FacebookId: " + participantes.getFcbookid(), Log.INFO);
						}
					}
					
					Propuesta.Items.Votes.Contra votesContra = votes.getContra();
					List<Propuesta.Items.Votes.Participantes> listVotesContraParticipantes = votesContra.getParticipantes();
					Dialogues.Log(TAG_CLASS, "/***** Contra Participantes", Log.INFO);
					Dialogues.Log(TAG_CLASS, "Items Votes Contra Size: " + listVotesContraParticipantes.size(), Log.INFO);
					
					if (listVotesContraParticipantes != null  && listVotesContraParticipantes.size() > 0) {
						for (Propuesta.Items.Votes.Participantes participantes : listVotesContraParticipantes) {
							Dialogues.Log(TAG_CLASS, "Items Votes Contra Participantes Id: " + participantes.getId(), Log.INFO);
							Dialogues.Log(TAG_CLASS, "Items Votes Contra Participantes FacebookId: " + participantes.getFcbookid(), Log.INFO);
						}
					}
					
					Propuesta.Items.Votes.Abstencion votesAbstencion = votes.getAbtencion();
					List<Propuesta.Items.Votes.Participantes> listVotesAbstencionParticipantes = votesAbstencion.getParticipantes();
					Dialogues.Log(TAG_CLASS, "/***** Abstencion Participantes", Log.INFO);
					Dialogues.Log(TAG_CLASS, "Items Votes Abstencion Size: " + listVotesAbstencionParticipantes.size(), Log.INFO);
					
					if (listVotesAbstencionParticipantes != null  && listVotesAbstencionParticipantes.size() > 0) {
						for (Propuesta.Items.Votes.Participantes participantes : listVotesAbstencionParticipantes) {
							Dialogues.Log(TAG_CLASS, "Items Votes Abstencion Participantes Id: " + participantes.getId(), Log.INFO);
							Dialogues.Log(TAG_CLASS, "Items Votes Abstencion Participantes FacebookId: " + participantes.getFcbookid(), Log.INFO);
						}
					}
					
					/*if (item.getCategory().equals(categoryName)) {
						hasInCategory = true;
						i += 1;
						
						if (i <= countToShow) {
							View view = createItemView(item);
							containerTestimonios.addView(view);
						} else {
							break;
						}
					}*/
				}
			}
			
			if (hasInCategory) {
				
			}
		}
	}
}
