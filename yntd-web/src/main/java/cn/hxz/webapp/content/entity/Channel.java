package cn.hxz.webapp.content.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Table(name="cms_channel")
public class Channel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name="site_id")
	private Long siteId;

	@Column(name="model_id")
	private Long modelId;

	@Column(name="parent_id")
	private Long parentId;

    @Column(name="name")
	private String name;

	@Column(name="code")
	private String node;

	@Column(name="href")
	private String href;

	@Column(name="logo")
	private String logo;

	@Column(name="remarks")
	private String intro;
	
	@Column(name="editable")
	private Boolean editable;
	
	@Column(name="enabled")
	private Boolean enabled;

	@Column(name = "trashed")
	private Boolean trashed;

	@Column(name="tpl_channel")
	private String tplChannel;

	@Column(name="tpl_content")
	private String tplContent;

	@Column(name="priority")
	private Long priority;


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
	public Long getSiteId() {
		return siteId;
	}
	public void setSiteId(Long siteId) {
		this.siteId = siteId;
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
	public Long getParentId() {
		return parentId;
	}
	public void setParentId(Long parentId) {
		this.parentId = parentId;
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

	/**
	 * 
	 */
	public String getNode() {
		return node;
	}
	public void setNode(String node) {
		this.node = node;
	}

	/**
	 * 
	 */
	public String getHref() {
		return href;
	}
	public void setHref(String href) {
		this.href = href;
	}
	
	/**
	 * 
	 */
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	
	/**
	 * 
	 */
	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
	
	/**
	 * 是否可编辑内容
	 * @return
	 */
	public Boolean getEditable() {
		return editable;
	}
	public void setEditable(Boolean editable) {
		this.editable = editable;
	}
	
	/**
	 * 是否启用
	 */
	public Boolean getEnabled() {
		return enabled;
	}
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * 是否废弃
	 */
	public Boolean getTrashed() {
		return trashed;
	}
	public void setTrashed(Boolean trashed) {
		this.trashed = trashed;
	}
	
	/**
	 * 
	 */
	public Long getPriority() {
		return priority;
	}
	public void setPriority(Long priority) {
		this.priority = priority;
	}

	/**
	 * 
	 */
	public String getTplChannel() {
		return tplChannel;
	}	
	public void setTplChannel(String tplChannel) {
		this.tplChannel = tplChannel;
	}

	/**
	 * 
	 */
	public String getTplContent() {
		return tplContent;
	}	
	public void setTplContent(String tplContent) {
		this.tplContent = tplContent;
	}
	
//	@Transient
//	public boolean isEnabled(){
//		return (getEnabled()!=null && getEnabled());
//	}
	
	@Transient
	public String getImageUrl(){
		return logo;
	}
	
	@Transient
	public String getCode(){
		return node;
	}
	
	@Transient
	public String getUrl(){
		if (href==null)
			return String.format("/%s/%s%s", "node", node, ".html");
		else
			return href;
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
		Channel other = (Channel) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
