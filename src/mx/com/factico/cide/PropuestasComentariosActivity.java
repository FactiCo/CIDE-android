package mx.com.factico.cide;

import java.util.Collections;
import java.util.List;

import mx.com.factico.cide.beans.Comment;
import mx.com.factico.cide.beans.Facebook;
import mx.com.factico.cide.beans.Propuesta;
import mx.com.factico.cide.beans.Propuesta.Items.Comments.Data;
import mx.com.factico.cide.dialogues.Dialogues;
import mx.com.factico.cide.httpconnection.HttpConnection;
import mx.com.factico.cide.parser.GsonParser;
import mx.com.factico.cide.preferences.PreferencesUtils;
import mx.com.factico.cide.views.CustomEditText;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PropuestasComentariosActivity extends ActionBarActivity implements OnClickListener {
	public static final String TAG_CLASS = PropuestasActivity.class.getName();

	public static final String TAG_PROPUESTA = "propuesta";

	private Propuesta.Items propuesta = null;
	
	private String proposalId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_propuestas_comments);

		setSupportActionBar();
		initUI();
	}

	public void setSupportActionBar() {
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
			
			findViewById(R.id.propuestas_comments_btn_sendcomment).setOnClickListener(this);
		}
	}

	private void loadPropuestasViews(Propuesta.Items item) {
		if (item != null) {
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
					LinearLayout vgContainer = (LinearLayout) findViewById(R.id.propuestas_comments_vg_container);
					Collections.reverse(listCommentsData); // Reverse the list
					
					Dialogues.Log(TAG_CLASS, "/***** Comments", Log.INFO);
					Dialogues.Log(TAG_CLASS, "Items Comments Data Size: " + listCommentsData.size(), Log.INFO);

					for (Propuesta.Items.Comments.Data data : listCommentsData) {
						Dialogues.Log(TAG_CLASS, "Items Comments Data Id: " + data.getId(), Log.INFO);
						Dialogues.Log(TAG_CLASS, "Items Comments Data Parent: " + data.getParent(), Log.INFO);
						Dialogues.Log(TAG_CLASS, "Items Comments Data Message: " + data.getMessage(), Log.INFO);
						Dialogues.Log(TAG_CLASS, "Items Comments Data Created: " + data.getCreated(), Log.INFO);

						Propuesta.Items.Comments.Data.From from = data.getFrom();
						if (from != null) {
							Dialogues.Log(TAG_CLASS, "Items Comments Data From Id: " + from.getFcbookid(), Log.INFO);
							Dialogues.Log(TAG_CLASS, "Items Comments Data From Name: " + from.getName(), Log.INFO);
						}
						
						View answerView = createCommentView(data);
						if (answerView != null)
							vgContainer.addView(answerView);
					}
				}
			}

			// Question
			Propuesta.Items.Question question = item.getQuestion();
			if (question != null) {
				Dialogues.Log(TAG_CLASS, "/***** Question", Log.INFO);
				Dialogues.Log(TAG_CLASS, "Items Question Id: " + question.getId(), Log.INFO);
				Dialogues.Log(TAG_CLASS, "Items Question Title: " + question.getTitle(), Log.INFO);

				if (question.getAnswers() != null) {
					Dialogues.Log(TAG_CLASS, "/***** Answers", Log.INFO);
					Dialogues.Log(TAG_CLASS, "Items Question Answers Size: " + question.getAnswers().size(), Log.INFO);

					List<Propuesta.Items.Question.Answers> listAnswers = question.getAnswers();

					if (listAnswers != null && listAnswers.size() > 0) {
						for (Propuesta.Items.Question.Answers data : listAnswers) {
							Dialogues.Log(TAG_CLASS, "Items Question Answers Id: " + data.getId(), Log.INFO);
							Dialogues.Log(TAG_CLASS, "Items Question Answers Title: " + data.getTitle(), Log.INFO);
							Dialogues.Log(TAG_CLASS, "Items Question Answers Count: " + data.getCount(), Log.INFO);
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
	
	@SuppressLint("InflateParams")
	private View createCommentView(Data data) {
		View view = getLayoutInflater().inflate(R.layout.item_propuestas_comments, null, false);
		
		TextView tvTitle = (TextView) view.findViewById(R.id.item_propuestas_comments_tv_title);
		TextView tvDescription = (TextView) view.findViewById(R.id.item_propuestas_comments_tv_description);
		
		tvTitle.setText(data.getFrom().getName());
		tvDescription.setText(data.getMessage());
		
		return view;
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
		case R.id.propuestas_comments_btn_sendcomment:
			sendComment();
			break;
			
		case R.id.dialog_result_post_ok:
			if (dialog != null && dialog.isShowing())
				dialog.dismiss();
			break;
			
		case R.id.dialog_result_post_share:
			
			break;
			
		default:
			break;
		}
	}
	
	private void sendComment() {
		CustomEditText etComment = (CustomEditText) findViewById(R.id.propuestas_comments_et_comment);
		if (!etComment.isEmpty()) {
			String message = etComment.getText().toString();
			
			String jsonFacebook = PreferencesUtils.getPreference(getApplication(), PreferencesUtils.FACEBOOK);
			Facebook facebook;
			try {
				facebook = GsonParser.getFacebookFromJSON(jsonFacebook);
				
				Comment.From from = new Comment().new From(facebook.getFcbookid(), facebook.getName());
				Comment comment = new Comment("", proposalId, message, from);
				
				SendCommentAsyncTask task = new SendCommentAsyncTask(comment);
				task.execute();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private class SendCommentAsyncTask extends AsyncTask<String, String, String> {
		private ProgressDialog dialog;
		private Comment comment;

		public SendCommentAsyncTask(Comment comment) {
			this.comment = comment;
		}

		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(PropuestasComentariosActivity.this);
			dialog.setMessage(getResources().getString(R.string.getdata_loading));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			String json = GsonParser.createJsonFromObject(comment);

			String result = HttpConnection.POST(HttpConnection.ACTION_COMENTARIOS, json);
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
				showResultDialog(getResources().getString(R.string.dialog_message_propuesta_comment));
				CustomEditText etComment = (CustomEditText) findViewById(R.id.propuestas_comments_et_comment);
				etComment.setText("");
				
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
}
