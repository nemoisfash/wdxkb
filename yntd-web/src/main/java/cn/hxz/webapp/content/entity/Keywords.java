package cn.hxz.webapp.content.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
/*
 * 
 * 关键字
 */
@Table(name="triz_keyword")
public class Keywords implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "type")
	private Long type;
	
	@Column(name = "type_id")
	private Long typeId;
	
	@Column(name = "name")
	private String name;
	
	@Column(name = "create_time")
	private Date createTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getType() {
		return type;
	}

	public void setType(Long type) {
		this.type = type;
	}

	public Long getTypeId() {
		return typeId;
	}

	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	public enum TypeEnum {
		AbrPrinciple(1L), InvPrinciple(2L),Solution(3L),Article(4L);
		private long value;

		TypeEnum(long value) {
			this.value = value;
		}

		public long getValue() {
			return value;
		}
	}
	
}
