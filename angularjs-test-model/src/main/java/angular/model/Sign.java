package angular.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="guestbook")
public final class Sign extends PersistentObject {

	@Basic(optional=false)
	@Column(name="name",nullable=false,length=50)
	private String name;

	@Basic(optional=false)
	@Column(name="text",nullable=false,length=150)
	private String text;

	// getters & setters

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}

}
