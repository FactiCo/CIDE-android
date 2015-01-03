package mx.com.factico.cide.beans;

import java.util.List;

import com.google.gson.annotations.Expose;

public class Testimonio {
	private long count;
	private List<Items> items;

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public List<Items> getItems() {
		return items;
	}

	public void setItems(List<Items> items) {
		this.items = items;
	}

	public class Items {
		private String _id = "";
		@Expose private String name = "";
		@Expose private String email = "";
		@Expose private String category = "";
		@Expose private String explanation = "";
		@Expose private String entidadFederativa = "";
		@Expose private String age = "";
		@Expose private String gender = "";
		@Expose private String grade = "";
		private String created = "";

		public String getId() {
			return _id;
		}

		public void setId(String id) {
			this._id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getCategory() {
			return category;
		}

		public void setCategory(String category) {
			this.category = category;
		}

		public String getExplanation() {
			return explanation;
		}

		public void setExplanation(String explanation) {
			this.explanation = explanation;
		}

		public String getEntidadFederativa() {
			return entidadFederativa;
		}

		public void setEntidadFederativa(String entidadFederativa) {
			this.entidadFederativa = entidadFederativa;
		}

		public String getAge() {
			return age;
		}

		public void setAge(String age) {
			this.age = age;
		}

		public String getGender() {
			return gender;
		}

		public void setGender(String gender) {
			this.gender = gender;
		}

		public String getGrade() {
			return grade;
		}

		public void setGrade(String grade) {
			this.grade = grade;
		}

		public String getCreated() {
			return created;
		}

		public void setCreated(String created) {
			this.created = created;
		}
	}
}
