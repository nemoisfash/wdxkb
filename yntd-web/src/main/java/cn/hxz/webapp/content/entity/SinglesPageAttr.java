package cn.hxz.webapp.content.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="cms_singles_page_attr")
public class SinglesPageAttr implements Serializable {
    private static final long serialVersionUID = 1L;


    @Id
    @Column(name = "id")
	private Long id;

    @Id
    @Column(name = "field")
	private String field;

    @Column(name = "value")
	private String value;


	/**
	 * 
	 */
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * 
	 */
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}

	/**
	 * 
	 */
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SinglesPageAttr other = (SinglesPageAttr) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
