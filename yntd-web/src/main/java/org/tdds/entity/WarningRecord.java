package org.tdds.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "znzz_warning_record")
public class WarningRecord {

	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "machine_id")
	private Long machineId;
	
	@Column(name = "start_time")
	private Date  startTime;
	
	@Column(name = "end_time")
	private Date  endTime;
	
	@Column(name = "machine_name")
	private String machineName;
	
	@Column(name = "timediff")
	private Long timediff;
	
	@Column(name = "cnc_alarm_message")
	private String cncAlarmMessage;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getMachineId() {
		return machineId;
	}

	public void setMachineId(Long machineId) {
		this.machineId = machineId;
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

	public String getMachineName() {
		return machineName;
	}

	public void setMachineName(String machineName) {
		this.machineName = machineName;
	}

	public Long getTimediff() {
		return timediff;
	}

	public void setTimediff(Long timediff) {
		this.timediff = timediff;
	}

	public String getCncAlarmMessage() {
		return cncAlarmMessage;
	}

	public void setCncAlarmMessage(String cncAlarmMessage) {
		this.cncAlarmMessage = cncAlarmMessage;
	}
	
	
}
