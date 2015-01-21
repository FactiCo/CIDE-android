package mx.com.factico.cide.fragments;

import java.util.List;

import mx.com.factico.cide.R;
import mx.com.factico.cide.beans.Facebook;
import mx.com.factico.cide.beans.Propuesta;
import mx.com.factico.cide.beans.Propuesta.Items.Votes.Participantes;
import mx.com.factico.cide.beans.Vote;
import mx.com.factico.cide.dialogues.Dialogues;
import mx.com.factico.cide.facebook.FacebookUtils;
import mx.com.factico.cide.httpconnection.HttpConnection;
import mx.com.factico.cide.parser.GsonParser;
import mx.com.factico.cide.preferences.PreferencesUtils;
import mx.com.factico.cide.spannables.SpannableFactory;
import mx.com.factico.cide.views.CircleChartView;
import mx.com.factico.cide.views.CustomTextView;
import mx.com.factico.cide.views.CustomWebView;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class PropuestasVotesPageFragment extends Fragment implements OnClickListener {
	public static final String TAG_CLASS = PropuestasVotesPageFragment.class.getName();
	
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

	private String VOTE_FAVOR = "favor";
	private String VOTE_CONTRA = "contra";
	private String VOTE_ABSTENCION = "abstencion";
	
	private boolean alreadyVote = false;
	
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
		PropuestasVotesPageFragment fragment = new PropuestasVotesPageFragment();

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
		rootView = inflater.inflate(R.layout.fragment_propuestas_votes, container, false);
		
		loadPropuestasViews(propuesta);
		
		return rootView;
	}
	
	@SuppressLint("SetJavaScriptEnabled")
	@SuppressWarnings("deprecation")
	private void loadPropuestasViews(Propuesta.Items item) {
		if (item != null) {
			rootView.findViewById(R.id.propuestas_votes_btn_favor).setOnClickListener(this);
			rootView.findViewById(R.id.propuestas_votes_btn_abstencion).setOnClickListener(this);
			rootView.findViewById(R.id.propuestas_votes_btn_contra).setOnClickListener(this);
			
			ImageView ivUser = (ImageView) rootView.findViewById(R.id.propuestas_votes_iv_userphoto);
			FacebookUtils.loadImageProfileToImageView(ivUser, getResources().getString(R.string.facebook_userid));
			
			// User name
			((CustomTextView) rootView.findViewById(R.id.propuestas_votes_tv_username)).setText(item.getName());
						
			// Title
			((CustomTextView) rootView.findViewById(R.id.propuestas_votes_tv_title)).setText(item.getTitle());
			
			// Description
			CustomWebView webviewDescription = (CustomWebView)rootView.findViewById(R.id.webview_data);
			webviewDescription.loadData(item.getDescription(), "text/html", "UTF-8");
			webviewDescription.setWebChromeClient(new WebChromeClient());
			webviewDescription.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
			webviewDescription.getSettings().setPluginState(WebSettings.PluginState.ON);
			webviewDescription.getSettings().setPluginState(WebSettings.PluginState.ON_DEMAND);
			webviewDescription.setWebViewClient(new WebViewClient());
			webviewDescription.getSettings().setJavaScriptEnabled(true);
			
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
				((CustomTextView) rootView.findViewById(R.id.propuestas_votes_tv_question)).setText(question.getTitle());

				Dialogues.Log(TAG_CLASS, "/***** Question", Log.INFO);
				Dialogues.Log(TAG_CLASS, "Items Question Id: " + question.getId(), Log.INFO);
				Dialogues.Log(TAG_CLASS, "Items Question Title: " + question.getTitle(), Log.INFO);

				if (question.getAnswers() != null) {
					Dialogues.Log(TAG_CLASS, "/***** Answers", Log.INFO);
					Dialogues.Log(TAG_CLASS, "Items Question Answers Size: " + question.getAnswers().size(), Log.INFO);

					List<Propuesta.Items.Question.Answers> listAnswers = question.getAnswers();

					if (listAnswers != null && listAnswers.size() > 0) {
						LinearLayout vgAnswers = (LinearLayout) rootView.findViewById(R.id.propuestas_votes_vg_answers);

						int index = 0;
						for (Propuesta.Items.Question.Answers data : listAnswers) {
							Dialogues.Log(TAG_CLASS, "Items Question Answers Id: " + data.getId(), Log.INFO);
							Dialogues.Log(TAG_CLASS, "Items Question Answers Title: " + data.getTitle(), Log.INFO);
							Dialogues.Log(TAG_CLASS, "Items Question Answers Count: " + data.getCount(), Log.INFO);

							View answerView = createAnswerButton(data, question.getId(), index);
							if (answerView != null)
								vgAnswers.addView(answerView);
							
							index++;
						}
					}
					
					startChart(listAnswers);
				}
			} else {
				rootView.findViewById(R.id.propuestas_votes_vg_question).setVisibility(View.GONE);
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

						if (!alreadyVote) {
							if (alreadyVote = isAlreadyVote(listVotesFavorParticipantes)) {
								changeStateOfVoteView(VOTE_FAVOR);
								
								setNumberOfVotes(VOTE_FAVOR);
							}
						}
						
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

						if (!alreadyVote) {
							if (alreadyVote = isAlreadyVote(listVotesContraParticipantes)) {
								changeStateOfVoteView(VOTE_CONTRA);
								
								setNumberOfVotes(VOTE_CONTRA);
							}
						}
						
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

						if (!alreadyVote) {
							if (alreadyVote = isAlreadyVote(listVotesAbstencionParticipantes)) {
								changeStateOfVoteView(VOTE_ABSTENCION);
								
								setNumberOfVotes(VOTE_ABSTENCION);
							}
						}
						
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

	private boolean isAlreadyVote(List<Participantes> participantes) {
		String jsonFacebook = PreferencesUtils.getPreference(getActivity().getApplication(), PreferencesUtils.FACEBOOK);
		try {
			Facebook facebook = GsonParser.getFacebookFromJSON(jsonFacebook);
			
			for (Participantes participante : participantes) {
				if (participante.getFcbookid().equals(facebook.getFcbookid())) {
					
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	private String[] colorsAnswers = {
			"#FF4A8293",
			"#FF4FB7AD",
			"#FF58CA9C",
			"#FF6CDA84" };
	
	@SuppressLint("InflateParams")
	private View createAnswerButton(Propuesta.Items.Question.Answers answer, String idQuestion, int index) {
		View view = getActivity().getLayoutInflater().inflate(R.layout.item_propuestas_answer, null, false);
		
		Drawable drawable = getResources().getDrawable(R.drawable.drawable_circle_default);
		drawable.setColorFilter(Color.parseColor(colorsAnswers[index]), PorterDuff.Mode.SRC_ATOP);
		
		CustomTextView btnNumberAnswer = (CustomTextView) view.findViewById(R.id.item_propuestas_answer_tv_number);
		btnNumberAnswer.setBackground(drawable);
		btnNumberAnswer.setText(String.valueOf(index + 1));
		
		CustomTextView btnAnswer = (CustomTextView) view.findViewById(R.id.item_propuestas_answer_tv_title);
		btnAnswer.setText(answer.getTitle());
		btnAnswer.setTag(idQuestion + HttpConnection.ACTION_ANSWER + answer.getId());
		btnAnswer.setOnClickListener(AnswerOnClickListener);
		
		return view;
	}

	View.OnClickListener AnswerOnClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			String ids = v.getTag().toString();
			SendAnswerAsyncTask task = new SendAnswerAsyncTask(ids);
			task.execute();
		}
	};

	private void voteProposal(String voteString) {
		String json = PreferencesUtils.getPreference(getActivity().getApplication(), PreferencesUtils.FACEBOOK);
		Facebook facebook;
		try {
			if (json != null) {
				facebook = GsonParser.getFacebookFromJSON(json);

				Vote vote = new Vote(propuesta.getId(), facebook.getFcbookid(), voteString);

				SendVoteAsyncTask task = new SendVoteAsyncTask(vote);
				task.execute();
			} else {
				Dialogues.Toast(getActivity().getApplicationContext(), getResources().getString(R.string.facebook_session_is_not_open), Toast.LENGTH_LONG);
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
			dialog = new ProgressDialog(getActivity());
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
				setNumberOfVotes(vote.getValue());
				
				changeStateOfVoteView(vote.getValue());
				
				showResultDialog(getResources().getString(R.string.dialog_message_propuesta_vote));
				
			} else {
				Dialogues.Toast(getActivity().getApplicationContext(), getResources().getString(R.string.dialog_error), Toast.LENGTH_LONG);
			}
		}
	}
	
	private void setNumberOfVotes(String value) {
		Resources resources = getResources();
		if (propuesta.getVotes() != null) {
			int color = resources.getColor(R.color.btn_other_green);
			CharSequence text = "";
			CharSequence number = "";
			CharSequence type = "";
			
			if (value.equals(VOTE_FAVOR)) {
				if (propuesta.getVotes().getFavor() != null) {
					number = SpannableFactory.color(color, String.valueOf(propuesta.getVotes().getFavor().getParticipantes().size()));
					type = SpannableFactory.color(color, getResources().getString(R.string.propuestas_votes_favor));
					
					text = String.format(resources.getString(R.string.propuestas_votes_same, number, type));
				}
					
			} else if (value.equals(VOTE_ABSTENCION)) {
				if (propuesta.getVotes().getAbtencion() != null) {
					number = SpannableFactory.color(color, String.valueOf(propuesta.getVotes().getAbtencion().getParticipantes().size()));
					type = SpannableFactory.color(color, getResources().getString(R.string.propuestas_votes_abstencion));
					
					text = String.format(getResources().getString(R.string.propuestas_votes_same, number, type));
				}
			} else if (value.equals(VOTE_CONTRA)) {
				if (propuesta.getVotes().getContra() != null) {
					number = SpannableFactory.color(color, String.valueOf(propuesta.getVotes().getContra().getParticipantes().size()));
					type = SpannableFactory.color(color, getResources().getString(R.string.propuestas_votes_contra));
					
					text = String.format(getResources().getString(R.string.propuestas_votes_same, number, type));
				}
			}
			
			if (!text.equals("")) {
				CustomTextView tvVotesSame = (CustomTextView) rootView.findViewById(R.id.propuestas_votes_tv_votes_same);
				tvVotesSame.setVisibility(View.VISIBLE);
				//tvVotesSame.setText(text, TextView.BufferType.SPANNABLE);
				setSpannableColor(tvVotesSame, String.valueOf(text), String.valueOf(number), color);
				setSpannableColor(tvVotesSame, tvVotesSame.getText().toString(), String.valueOf(type), color);
			}
		}
	}
	
	private void setSpannableColor(TextView view, String fulltext, String subtext, int color) {
	      view.setText(fulltext, TextView.BufferType.SPANNABLE);
	      Spannable str = (Spannable) view.getText();
	      int i = fulltext.indexOf(subtext);
	      str.setSpan(new ForegroundColorSpan(color), i, i+subtext.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	   }
	
	private void changeStateOfVoteView(String value) {
		CustomTextView tvVoteLabel = (CustomTextView) rootView.findViewById(R.id.propuestas_votes_tv_vote_label);
		tvVoteLabel.setText(getResources().getString(R.string.dialog_message_propuesta_vote));
		
		ImageView btnFavor = (ImageView) rootView.findViewById(R.id.propuestas_votes_btn_favor);
		ImageView btnAbstencion = (ImageView) rootView.findViewById(R.id.propuestas_votes_btn_abstencion);
		ImageView btnContra = (ImageView) rootView.findViewById(R.id.propuestas_votes_btn_contra);
		
		if (value.equals(VOTE_FAVOR)) {
			btnFavor.setOnClickListener(null);
			btnAbstencion.setEnabled(false);
			btnContra.setEnabled(false);
			
		} else if (value.equals(VOTE_ABSTENCION)) {
			btnFavor.setEnabled(false);
			btnAbstencion.setOnClickListener(null);
			btnContra.setEnabled(false);
			
		} else if (value.equals(VOTE_CONTRA)) {
			btnFavor.setEnabled(false);
			btnAbstencion.setEnabled(false);
			btnContra.setOnClickListener(null);
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
			dialog = new ProgressDialog(getActivity());
			dialog.setMessage(getResources().getString(R.string.getdata_loading));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			String jsonFacebook = PreferencesUtils.getPreference(getActivity().getApplication(), PreferencesUtils.FACEBOOK);
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
					
					// startChart();
					
				} else if (resultCode.equals(GsonParser.TAG_RESULT_ERROR)) {
					Dialogues.Toast(getActivity().getApplicationContext(), getResources().getString(R.string.dialog_error), Toast.LENGTH_LONG);
				}
			} else {
				Dialogues.Toast(getActivity().getApplicationContext(), getResources().getString(R.string.dialog_error), Toast.LENGTH_LONG);
			}
		}
	}

	private void startChart(List<Propuesta.Items.Question.Answers> listAnswers) {
		ViewFlipper vfContainer = (ViewFlipper) rootView.findViewById(R.id.propuestas_votes_vf_question);
		vfContainer.setDisplayedChild(1);
		
		LinearLayout chartContainer = (LinearLayout) rootView.findViewById(R.id.propuestas_votes_vg_chart);
		CircleChartView pieChart = new CircleChartView(getActivity());
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, 1);
		params.gravity = Gravity.CENTER;
		pieChart.setLayoutParams(params);
		pieChart.startDraw(listAnswers);
		chartContainer.addView(pieChart);
	}
	
	private AlertDialog dialog;
	@SuppressLint("InflateParams")
	private void showResultDialog(String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_result_post, null, false);

		((TextView) view.findViewById(R.id.dialog_result_post_message)).setText(message);
		
		view.findViewById(R.id.dialog_result_post_ok).setOnClickListener(this);
		view.findViewById(R.id.dialog_result_post_share).setOnClickListener(this);

		builder.setView(view);

		dialog = builder.create();
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.propuestas_votes_btn_favor:
			voteProposal(VOTE_FAVOR);
			break;
			
		case R.id.propuestas_votes_btn_contra:
			voteProposal(VOTE_CONTRA);
			break;
			
		case R.id.propuestas_votes_btn_abstencion:
			voteProposal(VOTE_ABSTENCION);
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
}