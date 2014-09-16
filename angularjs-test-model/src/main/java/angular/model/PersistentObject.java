package angular.model;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.SequenceGenerator;

@MappedSuperclass
@Access(AccessType.FIELD)
@SequenceGenerator(name="jpa_seq")
public abstract class PersistentObject {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="jpa_seq")
	private Long id;

	// getters & setters

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

}