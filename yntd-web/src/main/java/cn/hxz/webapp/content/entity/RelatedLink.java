package cn.hxz.webapp.content.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Table(name="cms_related_link")
public class RelatedLink implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@Column(name = "title")
	private String title;

	@Column(name = "href")
	private String href;
	
	@Column(name = "logo")
	private String logo;

	@Column(name = "enabled")
	private Boolean enabled;

	@Column(name = "trashed")
	private Boolean trashed;

	@Column(name = "priority")
	private Long priority;

	@Column(name = "create_by")
	private Long createBy;

	@Column(name = "create_time")
	private Date createTime;

	@Column(name = "update_by")
	private Long updateBy;

	@Column(name = "update_time")
	private Date updateTime;

	@Column(name = "channel_id")
	private Long channelId;



	/**
	 * 
	 */
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}


	/**
	 * 
	 */
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
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
	public Long getCreateBy() {
		return createBy;
	}
	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}

	/**
	 * 
	 */
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * 
	 */
	public Long getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}

	/**
	 * 
	 */
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	/**
	 * 
	 */
	public Long getChannelId() {
		return channelId;
	}
	public void setChannelId(Long channelId) {
		this.channelId = channelId;
	}

	@Transient
	public String getUrl(){
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
		RelatedLink other = (RelatedLink) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

}
