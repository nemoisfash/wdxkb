package cn.hxz.webapp.content.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.commons.lang3.StringUtils;

@Table(name = "cms_article")
public class Article implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "title")
	private String title;

	@Column(name = "color")
	private String color;

	@Column(name = "author")
	private String author;

	@Column(name = "source")
	private String source;

	@Column(name = "keyword")
	private String keyword;

	@Column(name = "sticky")
	private Boolean sticky;

	@Column(name = "promote")
	private Boolean promote;

	@Column(name = "checked")
	private Boolean checked;

	@Column(name = "enabled")
	private Boolean enabled;

	@Column(name = "trashed")
	private Boolean trashed;

	@Column(name = "priority")
	private Long priority;

	@Column(name = "channel_id")
	private Long channelId;
	
	@Column(name = "canteen_id")
	private Long canteenId;

	@Column(name = "create_by")
	private Long createBy;

	@Column(name = "create_time")
	private Date createTime;

	@Column(name = "update_by")
	private Long updateBy;

	@Column(name = "update_time")
	private Date updateTime;

	@Column(name = "image_uri")
	private String imageUri;

	@Column(name = "thumb_uri")
	private String thumbUri;

	@Column(name = "summary")
	private String summary;

	@Column(name = "content")
	private String content;

	@Column(name = "hits")
	private Long visits;

	@Column(name = "meta")
	private String meta;

	@Transient
	private Map<String, Object> attr = new HashMap<String, Object>();

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
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	/**
	 * 
	 */
	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	/**
	 * 
	 */
	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	/**
	 * 
	 */
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	/**
	 * 
	 */
	public Boolean getSticky() {
		return sticky;
	}

	public void setSticky(Boolean sticky) {
		this.sticky = sticky;
	}

	/**
	 * 
	 */
	public Boolean getPromote() {
		return promote;
	}

	public void setPromote(Boolean promote) {
		this.promote = promote;
	}

	/**
	 * 
	 */
	public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
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
	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId(Long channelId) {
		this.channelId = channelId;
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
	public String getImageUri() {
		return imageUri;
	}

	public void setImageUri(String imageUri) {
		this.imageUri = imageUri;
	}

	/**
	 * 
	 */
	public String getThumbUri() {
		return thumbUri;
	}

	public void setThumbUri(String thumbUri) {
		this.thumbUri = thumbUri;
	}

	/**
	 * 
	 */
	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	/**
	 * 
	 */
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * 
	 */
	public String getMeta() {
		return meta;
	}

	public void setMeta(String meta) {
		this.meta = meta;
	}

	/**
	 * 
	 */
	public Long getVisits() {
		return visits;
	}

	public void setVisits(Long visits) {
		this.visits = visits;
	}

	// @Transient
	// public boolean isEnabled(){
	// return (getEnabled()!=null && getEnabled());
	// }
	@Transient
	public Map<String, Object> getAttr() {
		return attr;
	}

	public void setAttr(Map<String, Object> attr) {
		this.attr = attr;
	}

	@Transient
	public Long getHits() {
		return visits;
	}

	@Transient
	public String getUrl() {
		return String.format("/%s/%s%s", "article", StringUtils.leftPad(Objects.toString(id), 8, "0"), ".html");
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	public Long getCanteenId() {
		return canteenId;
	}

	public void setCanteenId(Long canteenId) {
		this.canteenId = canteenId;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Article other = (Article) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
