package mx.com.factico.cide.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Propuesta implements Serializable {
	private static final long serialVersionUID = 1L;

	private int count;
	private List<Items> items;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public List<Items> getItems() {
		return items;
	}

	public void setItems(List<Items> items) {
		this.items = items;
	}

	public class Items implements Serializable {
		private static final long serialVersionUID = 1L;

		private String _id;
		private String category;
		private String categoryId;
		private String title;
		private String description;
		private String created;
		private String name;
		private String state;
		private Comments comments;
		private Question question;
		private Votes votes;

		public String getId() {
			return _id;
		}

		public void setId(String _id) {
			this._id = _id;
		}

		public String getCategory() {
			return category;
		}

		public void setCategory(String category) {
			this.category = category;
		}

		public String getCategoryId() {
			return categoryId;
		}

		public void setCategoryId(String categoryId) {
			this.categoryId = categoryId;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public String getCreated() {
			return created;
		}

		public void setCreated(String created) {
			this.created = created;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getState() {
			return state;
		}

		public void setState(String state) {
			this.state = state;
		}
		
		public Comments getComments() {
			return comments;
		}

		public void setComments(Comments comments) {
			this.comments = comments;
		}

		public Question getQuestion() {
			return question;
		}

		public void setQuestion(Question question) {
			this.question = question;
		}

		public Votes getVotes() {
			return votes;
		}

		public void setVotes(Votes votes) {
			this.votes = votes;
		}

		public class Comments implements Serializable {
			private static final long serialVersionUID = 1L;

			private List<Data> data = new ArrayList<Propuesta.Items.Comments.Data>();

			public List<Data> getData() {
				return data;
			}

			public void setData(List<Data> data) {
				this.data = data;
			}

			public class Data implements Serializable {
				private static final long serialVersionUID = 1L;

				private String _id;
				private String parent;
				private String message;
				private String created;
				private From from;

				public String getId() {
					return _id;
				}

				public void setId(String _id) {
					this._id = _id;
				}

				public String getParent() {
					return parent;
				}

				public void setParent(String parent) {
					this.parent = parent;
				}

				public String getMessage() {
					return message;
				}

				public void setMessage(String message) {
					this.message = message;
				}

				public String getCreated() {
					return created;
				}

				public void setCreated(String created) {
					this.created = created;
				}

				public From getFrom() {
					return from;
				}

				public void setFrom(From from) {
					this.from = from;
				}

				public class From implements Serializable {
					private static final long serialVersionUID = 1L;

					private String fcbookid;
					private String name;

					public String getFcbookid() {
						return fcbookid;
					}

					public void setFcbookid(String fcbookid) {
						this.fcbookid = fcbookid;
					}

					public String getName() {
						return name;
					}

					public void setName(String name) {
						this.name = name;
					}
				}
			}
		}

		public class Question implements Serializable {
			private static final long serialVersionUID = 1L;

			private String _id;
			private String title;
			private List<Answers> answers = new ArrayList<Propuesta.Items.Question.Answers>();

			public String getId() {
				return _id;
			}

			public void setId(String _id) {
				this._id = _id;
			}

			public String getTitle() {
				return title;
			}

			public void setTitle(String title) {
				this.title = title;
			}

			public List<Answers> getAnswers() {
				return answers;
			}

			public void setAnswers(List<Answers> answers) {
				this.answers = answers;
			}

			public class Answers implements Serializable {
				private static final long serialVersionUID = 1L;

				private String title;
				private String _id;
				private String count;

				public String getTitle() {
					return title;
				}

				public void setTitle(String title) {
					this.title = title;
				}

				public String getId() {
					return _id;
				}

				public void setId(String _id) {
					this._id = _id;
				}

				public String getCount() {
					return count;
				}

				public void setCount(String count) {
					this.count = count;
				}
			}
		}

		public class Votes implements Serializable {
			private static final long serialVersionUID = 1L;

			private Favor favor;
			private Contra contra;
			private Abstencion abstencion;

			public Favor getFavor() {
				return favor;
			}

			public void setFavor(Favor favor) {
				this.favor = favor;
			}

			public Contra getContra() {
				return contra;
			}

			public void setContra(Contra contra) {
				this.contra = contra;
			}

			public Abstencion getAbtencion() {
				return abstencion;
			}

			public void setAbtencion(Abstencion abstencion) {
				this.abstencion = abstencion;
			}

			public class Favor implements Serializable {
				private static final long serialVersionUID = 1L;

				private List<Participantes> participantes = new ArrayList<Propuesta.Items.Votes.Participantes>();

				public List<Participantes> getParticipantes() {
					return participantes;
				}

				public void setParticipantes(List<Participantes> participantes) {
					this.participantes = participantes;
				}
			}

			public class Contra implements Serializable {
				private static final long serialVersionUID = 1L;

				private List<Participantes> participantes = new ArrayList<Propuesta.Items.Votes.Participantes>();

				public List<Participantes> getParticipantes() {
					return participantes;
				}

				public void setParticipantes(List<Participantes> participantes) {
					this.participantes = participantes;
				}
			}

			public class Abstencion implements Serializable {
				private static final long serialVersionUID = 1L;

				private List<Participantes> participantes = new ArrayList<Propuesta.Items.Votes.Participantes>();

				public List<Participantes> getParticipantes() {
					return participantes;
				}

				public void setParticipantes(List<Participantes> participantes) {
					this.participantes = participantes;
				}
			}

			public class Participantes implements Serializable {
				private static final long serialVersionUID = 1L;

				private String _id;
				private String fcbookid;

				public String getId() {
					return _id;
				}

				public void setId(String _id) {
					this._id = _id;
				}

				public String getFcbookid() {
					return fcbookid;
				}

				public void setFcbookid(String fcbookid) {
					this.fcbookid = fcbookid;
				}
			}
		}
	}
}
