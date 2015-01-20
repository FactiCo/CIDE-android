package mx.com.factico.cide;

import java.util.List;

import mx.com.factico.cide.beans.Facebook;
import mx.com.factico.cide.beans.Propuesta;
import mx.com.factico.cide.beans.Vote;
import mx.com.factico.cide.dialogues.Dialogues;
import mx.com.factico.cide.httpconnection.HttpConnection;
import mx.com.factico.cide.parser.GsonParser;
import mx.com.factico.cide.preferences.PreferencesUtils;
import mx.com.factico.cide.typeface.TypefaceFactory;
import mx.com.factico.cide.views.CustomTextView;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PropuestasActivity extends ActionBarActivity implements OnClickListener {
	public static final String TAG_CLASS = PropuestasActivity.class.getName();

	public static final String TAG_PROPUESTA = "propuesta";

	private Propuesta.Items propuesta = null;

	private String proposalId;

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
	}

	private void initUI() {
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			propuesta = (Propuesta.Items) bundle.getSerializable(TAG_PROPUESTA);

			loadPropuestasViews(propuesta);
		}
	}

	private void loadPropuestasViews(Propuesta.Items item) {
		if (item != null) {
			((TextView) findViewById(R.id.propuestas_tv_title)).setText(item.getTitle());
			((TextView) findViewById(R.id.propuestas_tv_description)).setText(Html.fromHtml(item.getDescription().toString()));

			proposalId = item.getId();

			Dialogues.Log(TAG_CLASS, "Items Id: " + item.getId(), Log.INFO);
			Dialogues.Log(TAG_CLASS, "Items Category: " + item.getCategory(), Log.INFO);
			Dialogues.Log(TAG_CLASS, "Items CategoryId: " + item.getCategoryId(), Log.INFO);
			Dialogues.Log(TAG_CLASS, "Items Title: " + item.getTitle(), Log.INFO);
			Dialogues.Log(TAG_CLASS, "Items Description: " + item.getDescription(), Log.INFO);

			// Comments
			if (item.getComments() != null) {
				List<Propuesta.Items.Comments.Data> listCommentsData = item.getComments().getData();

				if (listCommentsData != null && listCommentsData.size() > 0) {
					Dialogues.Log(TAG_CLASS, "/***** Comments", Log.INFO);
					Dialogues.Log(TAG_CLASS, "Items Comments Data Size: " + listCommentsData.size(), Log.INFO);

					for (Propuesta.Items.Comments.Data data : listCommentsData) {
						findViewById(R.id.propuestas_btn_answers_vermas).setVisibility(View.VISIBLE);
						findViewById(R.id.propuestas_btn_answers_vermas).setOnClickListener(this);
						
						Dialogues.Log(TAG_CLASS, "Items Comments Data Id: " + data.getId(), Log.INFO);
						Dialogues.Log(TAG_CLASS, "Items Comments Data Parent: " + data.getParent(), Log.INFO);
						Dialogues.Log(TAG_CLASS, "Items Comments Data Message: " + data.getMessage(), Log.INFO);
						Dialogues.Log(TAG_CLASS, "Items Comments Data Created: " + data.getCreated(), Log.INFO);

						Propuesta.Items.Comments.Data.From from = data.getFrom();
						if (from != null) {
							Dialogues.Log(TAG_CLASS, "Items Comments Data From Id: " + from.getFcbookid(), Log.INFO);
							Dialogues.Log(TAG_CLASS, "Items Comments Data From Name: " + from.getName(), Log.INFO);
						}
					}
				}
			}

			// Question
			Propuesta.Items.Question question = item.getQuestion();
			if (question != null) {
				((TextView) findViewById(R.id.propuestas_tv_question)).setText(question.getTitle());

				Dialogues.Log(TAG_CLASS, "/***** Question", Log.INFO);
				Dialogues.Log(TAG_CLASS, "Items Question Id: " + question.getId(), Log.INFO);
				Dialogues.Log(TAG_CLASS, "Items Question Title: " + question.getTitle(), Log.INFO);

				if (question.getAnswers() != null) {
					Dialogues.Log(TAG_CLASS, "/***** Answers", Log.INFO);
					Dialogues.Log(TAG_CLASS, "Items Question Answers Size: " + question.getAnswers().size(), Log.INFO);

					List<Propuesta.Items.Question.Answers> listAnswers = question.getAnswers();

					if (listAnswers != null && listAnswers.size() > 0) {
						LinearLayout vgAnswers = (LinearLayout) findViewById(R.id.propuestas_vg_answers);

						for (Propuesta.Items.Question.Answers data : listAnswers) {
							Dialogues.Log(TAG_CLASS, "Items Question Answers Id: " + data.getId(), Log.INFO);
							Dialogues.Log(TAG_CLASS, "Items Question Answers Title: " + data.getTitle(), Log.INFO);
							Dialogues.Log(TAG_CLASS, "Items Question Answers Count: " + data.getCount(), Log.INFO);

							View answerView = createAnswerButton(data, question.getId());
							if (answerView != null)
								vgAnswers.addView(answerView);
						}
					}
				}
			}

			// Votes
			Propuesta.Items.Votes votes = item.getVotes();
			Dialogues.Log(TAG_CLASS, "/***** Votes", Log.INFO);

			if (votes != null) {
				// Votes Favor
				Propuesta.Items.Votes.Favor votesFavor = votes.getFavor();
				if (votesFavor != null) {
					List<Propuesta.Items.Votes.Participantes> listVotesFavorParticipantes = votesFavor.getParticipantes();

					if (listVotesFavorParticipantes != null && listVotesFavorParticipantes.size() > 0) {
						Dialogues.Log(TAG_CLASS, "/***** Favor Participantes", Log.INFO);
						Dialogues.Log(TAG_CLASS, "Items Votes Favor Size: " + listVotesFavorParticipantes.size(), Log.INFO);

						for (Propuesta.Items.Votes.Participantes participantes : listVotesFavorParticipantes) {
							Dialogues.Log(TAG_CLASS, "Items Votes Favor Participantes Id: " + participantes.getId(), Log.INFO);
							Dialogues
									.Log(TAG_CLASS, "Items Votes Favor Participantes FacebookId: " + participantes.getFcbookid(), Log.INFO);
						}
					}
				}

				// Votes Contra
				Propuesta.Items.Votes.Contra votesContra = votes.getContra();
				if (votesContra != null) {
					List<Propuesta.Items.Votes.Participantes> listVotesContraParticipantes = votesContra.getParticipantes();

					if (listVotesContraParticipantes != null && listVotesContraParticipantes.size() > 0) {
						Dialogues.Log(TAG_CLASS, "/***** Contra Participantes", Log.INFO);
						Dialogues.Log(TAG_CLASS, "Items Votes Contra Size: " + listVotesContraParticipantes.size(), Log.INFO);

						for (Propuesta.Items.Votes.Participantes participantes : listVotesContraParticipantes) {
							Dialogues.Log(TAG_CLASS, "Items Votes Contra Participantes Id: " + participantes.getId(), Log.INFO);
							Dialogues.Log(TAG_CLASS, "Items Votes Contra Participantes FacebookId: " + participantes.getFcbookid(),
									Log.INFO);
						}
					}
				}

				// Votes Abstencion
				Propuesta.Items.Votes.Abstencion votesAbstencion = votes.getAbtencion();
				if (votesAbstencion != null) {
					List<Propuesta.Items.Votes.Participantes> listVotesAbstencionParticipantes = votesAbstencion.getParticipantes();

					if (listVotesAbstencionParticipantes != null && listVotesAbstencionParticipantes.size() > 0) {
						Dialogues.Log(TAG_CLASS, "/***** Abstencion Participantes", Log.INFO);
						Dialogues.Log(TAG_CLASS, "Items Votes Abstencion Size: " + listVotesAbstencionParticipantes.size(), Log.INFO);

						for (Propuesta.Items.Votes.Participantes participantes : listVotesAbstencionParticipantes) {
							Dialogues.Log(TAG_CLASS, "Items Votes Abstencion Participantes Id: " + participantes.getId(), Log.INFO);
							Dialogues.Log(TAG_CLASS, "Items Votes Abstencion Participantes FacebookId: " + participantes.getFcbookid(),
									Log.INFO);
						}
					}
				}
			}
		}
	}

	private View createAnswerButton(Propuesta.Items.Question.Answers answer, String idQuestion) {
		CustomTextView btnAnswer = new CustomTextView(getBaseContext());
		btnAnswer.setBackgroundResource(R.drawable.selector_btn_ligth);
		btnAnswer.setPadding(10, 10, 10, 10);
		btnAnswer.setGravity(Gravity.CENTER);
		btnAnswer.setText(answer.getTitle());
		btnAnswer.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1));
		btnAnswer.setTextColor(getResources().getColor(R.color.title_color));
		btnAnswer.setTextAppearance(getBaseContext(), android.R.attr.textAppearanceSmall);
		btnAnswer.setOnClickListener(AnswerOnClickListener);
		btnAnswer.setTag(idQuestion + HttpConnection.ACTION_ANSWER + answer.getId());

		Typeface typeface = TypefaceFactory.createTypeface(getBaseContext(), TypefaceFactory.RobotoSlab_Regular);
		btnAnswer.setTypeface(typeface);
		
		return btnAnswer;
	}

	View.OnClickListener AnswerOnClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			String ids = v.getTag().toString();
			SendAnswerAsyncTask task = new SendAnswerAsyncTask(ids);
			task.execute();
		}
	};

	private String VOTE_FAVOR = "favor";
	private String VOTE_CONTRA = "contra";
	private String VOTE_ABSTENCION = "abstencion";

	public void voteOnClick(View view) {
		switch (view.getId()) {
		case R.id.propuestas_btn_favor:
			voteProposal(VOTE_FAVOR);
			break;
		case R.id.propuestas_btn_contra:
			voteProposal(VOTE_CONTRA);
			break;
		case R.id.propuestas_btn_abstencion:
			voteProposal(VOTE_ABSTENCION);
			break;

		default:
			break;
		}
	}

	private void voteProposal(String voteString) {
		String json = PreferencesUtils.getPreference(getApplication(), PreferencesUtils.FACEBOOK);
		Facebook facebook;
		try {
			if (json != null) {
				facebook = GsonParser.getFacebookFromJSON(json);

				Vote vote = new Vote(proposalId, facebook.getFcbookid(), voteString);

				SendVoteAsyncTask task = new SendVoteAsyncTask(vote);
				task.execute();
			} else {
				Dialogues
						.Toast(getApplicationContext(), getResources().getString(R.string.facebook_session_is_not_open), Toast.LENGTH_LONG);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private class SendVoteAsyncTask extends AsyncTask<String, String, String> {
		private ProgressDialog dialog;
		private Vote vote;

		public SendVoteAsyncTask(Vote vote) {
			this.vote = vote;
		}

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
			String json = GsonParser.createJsonFromObject(vote);

			String result = HttpConnection.POST(HttpConnection.ACTION_VOTE, json);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (dialog != null && dialog.isShowing()) {
				dialog.dismiss();
			}

			Dialogues.Log(TAG_CLASS, "Result: " + result, Log.INFO);
			
			String resultCode = GsonParser.getResultFromJSON(result);
			if (resultCode.equals(GsonParser.TAG_RESULT_OK)) {
				showResultDialog(getResources().getString(R.string.dialog_message_propuesta_vote));
				
			} else {
				Dialogues.Toast(getApplicationContext(), getResources().getString(R.string.dialog_error), Toast.LENGTH_LONG);
				
			}
		}
	}

	private class SendAnswerAsyncTask extends AsyncTask<String, String, String> {
		private ProgressDialog dialog;
		private String ids;

		public SendAnswerAsyncTask(String ids) {
			this.ids = ids;
		}

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
			String jsonFacebook = PreferencesUtils.getPreference(getApplication(), PreferencesUtils.FACEBOOK);
			Facebook facebook;
			String result = null;
			
			try {
				facebook = GsonParser.getFacebookFromJSON(jsonFacebook);
				
				String json = GsonParser.createJsonFromObjectWithoutExposeAnnotations(facebook);
				
				result = HttpConnection.POST(HttpConnection.ACTION_PREGUNTAS + "/" + ids, json);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (dialog != null && dialog.isShowing()) {
				dialog.dismiss();
			}
			Dialogues.Log(TAG_CLASS, "Result: " + result, Log.INFO);
			
			if (result != null) {
				String resultCode = GsonParser.getResultFromJSON(result);
				if (resultCode.equals(GsonParser.TAG_RESULT_OK)) {
					showResultDialog(getResources().getString(R.string.dialog_message_propuesta_answer));
					
				} else if (resultCode.equals(GsonParser.TAG_RESULT_ERROR)) {
					Dialogues.Toast(getApplicationContext(), getResources().getString(R.string.dialog_error), Toast.LENGTH_LONG);
				}
			} else {
				Dialogues.Toast(getApplicationContext(), getResources().getString(R.string.dialog_error), Toast.LENGTH_LONG);
			}
		}
	}

	private AlertDialog dialog;
	@SuppressLint("InflateParams")
	private void showResultDialog(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		View view = getLayoutInflater().inflate(R.layout.dialog_result_post, null, false);

		((TextView) view.findViewById(R.id.dialog_result_post_message)).setText(message);
		
		view.findViewById(R.id.dialog_result_post_ok).setOnClickListener(this);
		view.findViewById(R.id.dialog_result_post_share).setOnClickListener(this);

		builder.setView(view);

		dialog = builder.create();
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.close_white, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_close) {
			finish();

			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dialog_result_post_ok:
			if (dialog != null && dialog.isShowing())
				dialog.dismiss();
			break;

		case R.id.dialog_result_post_share:
			
			break;

		case R.id.propuestas_btn_answers_vermas:
			openPropuestasComentariosIntent();
			break;
			
		default:
			break;
		}
	}
	
	private void openPropuestasComentariosIntent() {
		Intent intent = new Intent(PropuestasActivity.this, PropuestasComentariosActivity.class);
		intent.putExtra(PropuestasComentariosActivity.TAG_PROPUESTA, propuesta);
		startActivity(intent);
	}
}
