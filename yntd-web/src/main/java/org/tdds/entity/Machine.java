package org.tdds.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "znzz_machine")
public class Machine {

	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	private Long id;

	@Column(name = "name")
	private String name;

	@Column(name = "m_ip")
	private String mIp;

	@Column(name = "machine_no")
	private String machineNo;

	@Column(name = "code")
	private String code;

	@Column(name = "status")
	private String status;

	@Column(name = "type")
	private Long type;

	@Column(name = "img_url")
	private String imgUrl;

	@Column(name = "company_id")
	private Long companyId;

	@Column(name = "start_time")
	private Date startTime;

	@Column(name = "end_time")
	private Date endTime;

	@Column(name = "r_times")
	private Long rTimes;

	@Column(name = "p_times")
	private Long pTimes;

	@Column(name = "a_times")
	private Long aTimes;

	@Column(name = "w_times")
	private Long wTimes;
	
	@Column(name = "io")
	private Boolean io;
	
	public Boolean getIo() {
		return io;
	}

	public void setIo(Boolean io) {
		this.io = io;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getmIp() {
		return mIp;
	}

	public void setmIp(String mIp) {
		this.mIp = mIp;
	}

	public String getMachineNo() {
		return machineNo;
	}

	public void setMachineNo(String machineNo) {
		this.machineNo = machineNo;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getType() {
		return type;
	}

	public void setType(Long type) {
		this.type = type;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Long getrTimes() {
		return rTimes;
	}

	public void setrTimes(Long rTimes) {
		this.rTimes = rTimes;
	}

	public Long getpTimes() {
		return pTimes;
	}

	public void setpTimes(Long pTimes) {
		this.pTimes = pTimes;
	}

	public Long getaTimes() {
		return aTimes;
	}

	public void setaTimes(Long aTimes) {
		this.aTimes = aTimes;
	}

	public Long getwTimes() {
		return wTimes;
	}

	public void setwTimes(Long wTimes) {
		this.wTimes = wTimes;
	}

}