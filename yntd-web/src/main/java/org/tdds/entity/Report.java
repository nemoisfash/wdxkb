package org.tdds.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "znzz_reports")
public class Report {

	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "machine_id")
	private Long machineId;
	
	@Column(name = "planned_otime")
	private Integer plannedOtime;

	@Column(name = "actual_otime")
	private Integer actualOtime;
	
	@Column(name = "planned_capacity")
	private Integer plannedCapacity;
	
	@Column(name = "actual_capacity")
	private Integer actualCapacity;
	
	@Column(name = "number")
	private Integer number;
	
	@Column(name = "good_number")
	private Integer goodNumber ;

	@Column(name = "machine_name")
	private String machineName;
	
	public String getMachineName() {
		return machineName;
	}

	public void setMachineName(String machineName) {
		this.machineName = machineName;
	}

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

	public Integer getPlannedOtime() {
		return plannedOtime;
	}

	public void setPlannedOtime(Integer plannedOtime) {
		this.plannedOtime = plannedOtime;
	}

	public Integer getActualOtime() {
		return actualOtime;
	}

	public void setActualOtime(Integer actualOtime) {
		this.actualOtime = actualOtime;
	}

	public Integer getPlannedCapacity() {
		return plannedCapacity;
	}

	public void setPlannedCapacity(Integer plannedCapacity) {
		this.plannedCapacity = plannedCapacity;
	}

	public Integer getActualCapacity() {
		return actualCapacity;
	}

	public void setActualCapacity(Integer actualCapacity) {
		this.actualCapacity = actualCapacity;
	}

	public Integer getGoodNumber() {
		return goodNumber;
	}

	public void setGoodNumber(Integer goodNumber) {
		this.goodNumber = goodNumber;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}
}
