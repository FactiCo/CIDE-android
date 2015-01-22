package mx.com.factico.cide.fragments;

import java.util.ArrayList;
import java.util.List;

import mx.com.factico.cide.R;
import mx.com.factico.cide.beans.Comment;
import mx.com.factico.cide.beans.Facebook;
import mx.com.factico.cide.beans.Propuesta;
import mx.com.factico.cide.beans.Propuesta.Items.Comments.Data;
import mx.com.factico.cide.beans.Propuesta.Items.Comments.Data.From;
import mx.com.factico.cide.dialogues.Dialogues;
import mx.com.factico.cide.facebook.FacebookUtils;
import mx.com.factico.cide.httpconnection.HttpConnection;
import mx.com.factico.cide.parser.GsonParser;
import mx.com.factico.cide.preferences.PreferencesUtils;
import mx.com.factico.cide.views.CustomEditText;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PropuestasCommentsPageFragment extends Fragment implements OnClickListener {
	public static final String TAG_CLASS = PropuestasCommentsPageFragment.class.getName();
	
	/**
	 * Key to insert the background color into the mapping of a Bundle.
	 */
	private static final String PROPUESTA = "propuesta";

	/**
	 * Key to insert the index page into the mapping of a Bundle.
	 */
	private static final String INDEX = "index";

	private Propuesta.Items propuesta;
	private int index;

	private View rootView;

	/**
	 * Instances a new fragment with a background color and an index page.
	 * 
	 * @param propuesta
	 *            list of items
	 * @param index
	 *            index page
	 * @return a new page
	 */
	public static Fragment newInstance(int index, Propuesta.Items propuesta) {

		// Instantiate a new fragment
		PropuestasCommentsPageFragment fragment = new PropuestasCommentsPageFragment();

		// Save the parameters
		Bundle bundle = new Bundle();
		bundle.putInt(INDEX, index);
		bundle.putSerializable(PROPUESTA, propuesta);
		fragment.setArguments(bundle);
		fragment.setRetainInstance(true);

		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// Load parameters when the initial creation of the fragment is done
		this.propuesta = (Propuesta.Items) ((getArguments() != null) ? getArguments().getSerializable(PROPUESTA) : null);
		this.index = (getArguments() != null) ? getArguments().getInt(INDEX) : -1;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_propuestas_comments, container, false);
		
		loadPropuestasViews(propuesta);
		
		rootView.findViewById(R.id.propuestas_comments_btn_sendcomment).setOnClickListener(this);
		
		return rootView;
	}
	
	private void loadPropuestasViews(Propuesta.Items item) {
		if (item != null) {
			Dialogues.Log(TAG_CLASS, "Items Id: " + item.getId(), Log.INFO);
			Dialogues.Log(TAG_CLASS, "Items Category: " + item.getCategory(), Log.INFO);
			Dialogues.Log(TAG_CLASS, "Items CategoryId: " + item.getCategoryId(), Log.INFO);
			Dialogues.Log(TAG_CLASS, "Items Title: " + item.getTitle(), Log.INFO);
			Dialogues.Log(TAG_CLASS, "Items Description: " + item.getDescription(), Log.INFO);

			// Comments
			if (item.getComments() != null) {
				List<Propuesta.Items.Comments.Data> listCommentsData = item.getComments().getData();

				if (listCommentsData != null && listCommentsData.size() > 0) {
					LinearLayout vgContainer = (LinearLayout) rootView.findViewById(R.id.propuestas_comments_vg_container);
					vgContainer.removeAllViews();
					// Collections.reverse(listCommentsData); // Reverse the list
					
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
		View view = getActivity().getLayoutInflater().inflate(R.layout.item_propuestas_comments, null, false);
		
		if (data.getFrom() != null) {
			ImageView ivUser = (ImageView) view.findViewById(R.id.item_propuestas_comments_iv_user);
			FacebookUtils.loadImageProfileToImageView(ivUser, data.getFrom().getFcbookid());
		}
		
		TextView tvTitle = (TextView) view.findViewById(R.id.item_propuestas_comments_tv_title);
		TextView tvDescription = (TextView) view.findViewById(R.id.item_propuestas_comments_tv_description);
		
		if (data.getFrom() != null && data.getFrom().getName() != null)
			tvTitle.setText(data.getFrom().getName());
		tvDescription.setText(data.getMessage());
		
		return view;
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
		CustomEditText etComment = (CustomEditText) rootView.findViewById(R.id.propuestas_comments_et_comment);
		if (!etComment.isEmpty()) {
			String message = etComment.getText().toString();
			
			String jsonFacebook = PreferencesUtils.getPreference(getActivity().getApplication(), PreferencesUtils.FACEBOOK);
			Facebook facebook;
			try {
				facebook = GsonParser.getFacebookFromJSON(jsonFacebook);
				
				Comment.From from = new Comment().new From(facebook.getFcbookid(), facebook.getName());
				Comment comment = new Comment("", propuesta.getId(), message, from);
				
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
			dialog = new ProgressDialog(getActivity());
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
				CustomEditText etComment = (CustomEditText) rootView.findViewById(R.id.propuestas_comments_et_comment);
				etComment.setText("");
				
				Data data = new Propuesta().new Items().new Comments().new Data();
				data.setMessage(comment.getMessage());
				From from = new Propuesta().new Items().new Comments().new Data().new From();
				from.setFcbookid(comment.getFrom().getFcbookid());
				from.setName(comment.getFrom().getName());
				data.setFrom(from);
				
				List<Data> listData = new ArrayList<Data>();
				listData.add(data);
				listData.addAll(propuesta.getComments().getData());
				
				propuesta.getComments().setData(listData);
				
				loadPropuestasViews(propuesta);
				
			} else {
				Dialogues.Toast(getActivity().getApplicationContext(), getResources().getString(R.string.dialog_error), Toast.LENGTH_LONG);
			}
		}
	}
	
	private AlertDialog dialog;
	@SuppressLint("InflateParams")
	private void showResultDialog(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_result_post, null, false);

		((TextView) view.findViewById(R.id.dialog_result_post_message)).setText(message);
		
		view.findViewById(R.id.dialog_result_post_ok).setOnClickListener(this);
		//view.findViewById(R.id.dialog_result_post_share).setOnClickListener(this);
		view.findViewById(R.id.dialog_result_post_share).setVisibility(View.GONE);

		builder.setView(view);

		dialog = builder.create();
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}
}