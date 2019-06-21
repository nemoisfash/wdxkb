package cn.hxz.webapp.content.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name="cms_model_field")
public class ModelField implements Serializable, AdditionalField {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name="model_id")
	private Long modelId;

	@Column(name="field")
	private String field;

	@Column(name="label")
	private String label;

	@Column(name="data_type")
	private Integer dataType;

	@Column(name="text_size")
	private String textSize;

	@Column(name="area_rows")
	private String areaRows;

	@Column(name="area_cols")
	private String areaCols;

	@Column(name="def_value")
	private String defValue;

	@Column(name="opt_value")
	private String optValue;

	@Column(name="singles")
	private Boolean singles;

	@Column(name="enabled")
	private Boolean enabled;

	@Column(name="priority")
	private Integer priority;


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
	public Long getModelId() {
		return modelId;
	}
	public void setModelId(Long modelId) {
		this.modelId = modelId;
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
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * 
	 */
	public Integer getDataType() {
		return dataType;
	}
	public void setDataType(Integer dataType) {
		this.dataType = dataType;
	}

	/**
	 * 
	 */
	public String getTextSize() {
		return textSize;
	}
	public void setTextSize(String textSize) {
		this.textSize = textSize;
	}

	/**
	 * 
	 */
	public String getAreaRows() {
		return areaRows;
	}
	public void setAreaRows(String areaRows) {
		this.areaRows = areaRows;
	}

	/**
	 * 
	 */
	public String getAreaCols() {
		return areaCols;
	}
	public void setAreaCols(String areaCols) {
		this.areaCols = areaCols;
	}

	/**
	 * 
	 */
	public String getDefValue() {
		return defValue;
	}
	public void setDefValue(String defValue) {
		this.defValue = defValue;
	}

	/**
	 * 
	 */
	public String getOptValue() {
		return optValue;
	}
	public void setOptValue(String optValue) {
		this.optValue = optValue;
	}

	/**
	 * 
	 */
	public Boolean getSingles() {
		return singles;
	}
	public void setSingles(Boolean singles) {
		this.singles = singles;
	}

	/**
	 * 
	 */
	public Boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
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
		ModelField other = (ModelField) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
