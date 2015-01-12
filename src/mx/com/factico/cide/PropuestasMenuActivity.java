package mx.com.factico.cide;

import java.util.List;

import mx.com.factico.cide.adapters.PropuestasPagerAdapter;
import mx.com.factico.cide.beans.Propuesta;
import mx.com.factico.cide.dialogues.Dialogues;
import mx.com.factico.cide.fragments.PropuestasMenuPageFragment;
import mx.com.factico.cide.httpconnection.HttpConnection;
import mx.com.factico.cide.pager.transformers.ZoomOutPageTransformer;
import mx.com.factico.cide.parser.GsonParser;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

public class PropuestasMenuActivity extends ActionBarActivity {
	public static final String TAG_CLASS = PropuestasMenuActivity.class.getName();

	private Propuesta propuesta = null;

	private PropuestasPagerAdapter mPagerAdapter;
	private ViewPager mViewPager;

	private TabHost mTabHost;

	private String[] listCategories;

	private Toolbar mToolbar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_propuestas_menu);

		setSupportActionBar();

		startFacebookLoginIntent();
		
		// initUI(FacebookLoginActivity.RESULT_CODE_LOGIN_OK);
	}

	private void setSupportActionBar() {
		mToolbar = (Toolbar) findViewById(R.id.toolbar);
		mToolbar.setTitle("");
		mToolbar.setTitleTextColor(getResources().getColor(R.color.white));
		mToolbar.getBackground().setAlpha(0);
		setSupportActionBar(mToolbar);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	private void startFacebookLoginIntent() {
		Intent intent = new Intent(PropuestasMenuActivity.this, FacebookLoginActivity.class);
		startActivityForResult(intent, FacebookLoginActivity.REQUEST_CODE_LOGIN);
		overridePendingTransition(0, 0);
	}

	private void initUI(int result) {
		if (result == FacebookLoginActivity.RESULT_CODE_LOGIN_OK) {
			getDataPropuestas();
			
		} else {
			finish();
		}
	}
	
	private void getDataPropuestas() {
		GetDataAsyncTask task = new GetDataAsyncTask();
		task.execute();
	}
	
	private void setTabsToActioBar() {
		mTabHost = (TabHost) findViewById(R.id.propuestas_menu_tabhost);
		mTabHost.setup();
		
		listCategories = getResources().getStringArray(R.array.testimonios_categories_titles);
		
		mToolbar.setTitle(listCategories[0]);
		
		for (int i = 0; i < listCategories.length; i++) {
			setupTab(new TextView(this), i, listCategories[i]);
		}
		
		mTabHost.setOnTabChangedListener(new OnTabChangeListener() {
			@Override
			public void onTabChanged(String tabId) {
				int position = Integer.parseInt(tabId);
				mViewPager.setCurrentItem(position, false);
				
				mToolbar.setTitle(listCategories[position]);
			}
		});
		
		mViewPager = (ViewPager) findViewById(R.id.propuestas_menu_pager);
		
		// Defining a listener for pageChange
        ViewPager.SimpleOnPageChangeListener pageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                
                mTabHost.setCurrentTab(position);
                
                mToolbar.setTitle(listCategories[position]);
            }
        };
		
        // Setting the pageChange listener to the viewPager
        mViewPager.setOnPageChangeListener(pageChangeListener);
        mViewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        mViewPager.setOffscreenPageLimit(listCategories.length);
        
        // Creating an instance of PagerAdapter
		mPagerAdapter = new PropuestasPagerAdapter(getSupportFragmentManager());
		
		for (int i = 0; i < listCategories.length; i++) {
			String category = listCategories[i];
			mPagerAdapter.addFragment(PropuestasMenuPageFragment.newInstance(i, propuesta, category));
		}
		
		// Setting the PagerAdapter object to the viewPager object
		mViewPager.setAdapter(mPagerAdapter);
	}
	
	private void setupTab(final View view, final int index, String tag) {
		View tabview = createTabView(mTabHost.getContext(), index);
		TabSpec setContent = mTabHost.newTabSpec(String.valueOf(index)).setIndicator(tabview).setContent(new TabContentFactory() {
			@Override
			public View createTabContent(String tag) {
				return view;
			}
		});
		mTabHost.addTab(setContent);
	}

	@SuppressLint("InflateParams")
	private static View createTabView(Context context, int index) {
		View view = LayoutInflater.from(context).inflate(R.layout.item_tabhost, null);
		ImageView ivIcon = (ImageView) view.findViewById(R.id.item_tabhost_iv_logo);
		
		switch (index) {
		case 0:
			ivIcon.setImageResource(R.drawable.ic_justicia_trabajo);
			break;
		case 1:
			ivIcon.setImageResource(R.drawable.ic_justicia_familiar);
			break;
		case 2:
			ivIcon.setImageResource(R.drawable.ic_justicia_vecinal);
			break;
		case 3:
			ivIcon.setImageResource(R.drawable.ic_justicia_ciudadanos);
			break;
		case 4:
			ivIcon.setImageResource(R.drawable.ic_justicia_emprendedores);
			break;

		default:
			break;
		}
		return view;
	}
	
	private class GetDataAsyncTask extends AsyncTask<String, String, String> {
		private ProgressDialog dialog;
		
		public GetDataAsyncTask() {}
		
		@Override
		protected void onPreExecute() {
			dialog = new ProgressDialog(PropuestasMenuActivity.this);
			dialog.setMessage(getResources().getString(R.string.getdata_loading));
			dialog.setCanceledOnTouchOutside(false);
			dialog.setCancelable(false);
			dialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			String result = HttpConnection.GET(HttpConnection.ACTION_PROPUESTAS);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			if (dialog != null && dialog.isShowing()) {
				dialog.dismiss();
			}

			Dialogues.Log(TAG_CLASS, "Result: " + result, Log.INFO);

			loadDataFromJson(result);
		}
	}

	private void loadDataFromJson(String json) {
		try {
			propuesta = GsonParser.getPropuestaFromJSON(json);

			if (propuesta != null) {
				setTabsToActioBar();
				
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

		if (listItems != null && listItems.size() > 0) {
			Dialogues.Log(TAG_CLASS, "/***** Items", Log.INFO);
			Dialogues.Log(TAG_CLASS, "Items Size: " + listItems.size(), Log.INFO);

			for (int count = 0; count < listItems.size(); count++) {
				Propuesta.Items item = listItems.get(count);

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
									Dialogues.Log(TAG_CLASS, "Items Votes Favor Participantes FacebookId: " + participantes.getFcbookid(), Log.INFO);
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
									Dialogues.Log(TAG_CLASS, "Items Votes Contra Participantes FacebookId: " + participantes.getFcbookid(), Log.INFO);
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
									Dialogues.Log(TAG_CLASS, "Items Votes Abstencion Participantes FacebookId: " + participantes.getFcbookid(), Log.INFO);
								}
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == FacebookLoginActivity.REQUEST_CODE_LOGIN) {

			if (resultCode == FacebookLoginActivity.RESULT_CODE_LOGIN_OK) {
				initUI(FacebookLoginActivity.RESULT_CODE_LOGIN_OK);

			} else if (resultCode == FacebookLoginActivity.RESULT_CODE_LOGIN_CANCEL) {
				initUI(FacebookLoginActivity.RESULT_CODE_LOGIN_CANCEL);
			}
		}
	}
}
