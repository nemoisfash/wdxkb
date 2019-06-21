package cn.hxz.webapp.syscore.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 * Option.java Create on 2017-03-20 16:03:57
 * <p>
 *  实体类
 * </p>
 *
 * @author cn.feeboo
 * @version 1.0
 */
@Table(name="sys_option")
public class Option implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@Column(name = "group_id")
	private Long groupId;

	@Column(name = "name")
	private String name;
	
	@Column(name = "value")
	private String value;

	@Column(name = "details")
	private String details;
	
	@Column(name = "name_en")
	private String nameEn;

	@Column(name = "name_ar")
	private String nameAr;

	@Column(name = "priority")
	private Integer priority;

	@Column(name = "enabled")
	private Boolean enabled;

	@Column(name = "trashed")
	private Boolean trashed;



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
	public Long getGroupId() {
		return groupId;
	}
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	/**
	 * 
	 */
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}

	/**
	 * 
	 */
	public String getNameEn() {
		return nameEn;
	}
	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}

	/**
	 * 
	 */
	public String getNameAr() {
		return nameAr;
	}
	public void setNameAr(String nameAr) {
		this.nameAr = nameAr;
	}

	/**
	 * 
	 */
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	/**
	 * 
	 */
	public Boolean getEnabled() {
		return enabled;
	}
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * 
	 */
	public Boolean getTrashed() {
		return trashed;
	}
	public void setTrashed(Boolean trashed) {
		this.trashed = trashed;
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
		Option other = (Option) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
