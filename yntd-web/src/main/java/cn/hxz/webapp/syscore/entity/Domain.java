package cn.hxz.webapp.syscore.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Domain.java Create on 2017-01-16 23:12:09
 * <p>
 *  实体类
 * </p>
 *
 * @author cn.feeboo
 * @version 1.0
 */
@Table(name="sys_domain")
public class Domain implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String domain;
	
	@Transient
	private String fullname;
	
	private String skin;
	
	private String port;

	@Column(name="site_id")
	private Long siteId;


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
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}

	/**
	 * 
	 */
	public String getSkin() {
		return skin;
	}
	public void setSkin(String skin) {
		this.skin = skin;
	}
	
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
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
	
	

	public String getFullname() {
		String _port = (port==null || "80".equalsIgnoreCase(port)) ? "" : ":"+port;
		return "http://" + domain + _port;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
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
		Domain other = (Domain) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
