package org.tdds.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "znzz_running_record")
public class RunningRecord {

	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "machine_id")
	private Long machineId;
	
	@Column(name = "machine_name")
	private String machineName;
	
	@Column(name = "start_time")
	private Date  startTime;
	
	@Column(name = "end_time")
	private Date  endTime;
	
	@Column(name = "cnc_products")
	private Long cncProducts;
	
	@Column(name = "cnc_runstatus")
	private String cncRunstatus;
	
	@Column(name = "cnc_emer")
	private String cncEmer;
	
	@Column(name = "cnc_fload")
	private String cncFload;
	
	@Column(name = "cnc_runtime")
	private String cncRuntime;

	@Column(name = "cnc_srate")
	private String cncSrate;
	
	@Column(name = "cnc_actfspeed")
	private String cncActfspeed;
	
	@Column(name = "cnc_toolnum")
	private String cncToolnum;
	
	@Column(name = "cnc_sload")
	private String cncSload;
	
	@Column(name = "cnc_setspeed")
	private String cncSetspeed;
	
	@Column(name = "cnc_gcode")
	private String cncGcode;
	
	@Column(name = "cnc_alivetime")
	private String cncAlivetime;
	
	@Column(name = "cnc_currentpro")
	private String cncCurrentpro;
	
	@Column(name = "cnc_actspeed")
	private String cncActspeed;
	
	@Column(name = "cnc_rapidfeed")
	private String cncRapidfeed;
	
	@Column(name = "cnc_cuttime")
	private String cncCuttime;
	
	@Column(name = "cnc_tooloffsethnum")
	private String cncTooloffsethnum;
	
	@Column(name = "cnc_setfspeed")
	private String cncSetfspeed;
	
	@Column(name = "cnc_frate")
	private String cncFrate;
	
	@Column(name = "cnc_mainproname")
	private String cncMainproname;
	
	@Column(name = "timediff")
	private Long timediff;
	
	@Column(name = "cnc_mode")
	private String cncMode;
	
	@Column(name = "device_state")
	private Integer deviceState;
	
	@Column(name = "cnc_cycletime")
	private String cncCycletime;
	
	@Column(name = "cnc_relpos")
	private String cncRelpos;
	
	@Column(name = "cnc_mcX")
	private String cncMcX;
	
	@Column(name = "cnc_mcY")
	private String cncMcY;
	
	@Column(name = "cnc_mcZ")
	private String cncMcZ;
	
	@Column(name = "cnc_mcA")
	private String cncMcA;
	
	@Column(name = "cnc_rcX")
	private String cncRcX;
	
	@Column(name = "cnc_rcY")
	private String cncRcY;
	
	@Column(name = "cnc_rcA")
	private String cncRcA;
	
	@Column(name = "cnc_lX")
	private String cncLX;
	
	@Column(name = "cnc_lY")
	private String cncLY;

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

	public String getMachineName() {
		return machineName;
	}

	public void setMachineName(String machineName) {
		this.machineName = machineName;
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

	public Long getCncProducts() {
		return cncProducts;
	}

	public void setCncProducts(Long cncProducts) {
		this.cncProducts = cncProducts;
	}

	public String getCncRunstatus() {
		return cncRunstatus;
	}

	public void setCncRunstatus(String cncRunstatus) {
		this.cncRunstatus = cncRunstatus;
	}

	public String getCnc_emer() {
		return cncEmer;
	}

	public void setCnc_emer(String cnc_emer) {
		this.cncEmer = cnc_emer;
	}

	public String getCncFload() {
		return cncFload;
	}

	public void setCncFload(String cncFload) {
		this.cncFload = cncFload;
	}

	public String getCncRuntime() {
		return cncRuntime;
	}

	public void setCncRuntime(String cncRuntime) {
		this.cncRuntime = cncRuntime;
	}

	public String getCncSrate() {
		return cncSrate;
	}

	public void setCncSrate(String cncSrate) {
		this.cncSrate = cncSrate;
	}

	public String getCncActfspeed() {
		return cncActfspeed;
	}

	public void setCncActfspeed(String cncActfspeed) {
		this.cncActfspeed = cncActfspeed;
	}

	public String getCncToolnum() {
		return cncToolnum;
	}

	public void setCncToolnum(String cncToolnum) {
		this.cncToolnum = cncToolnum;
	}

	public String getCncSload() {
		return cncSload;
	}

	public void setCncSload(String cncSload) {
		this.cncSload = cncSload;
	}

	public String getCncSetspeed() {
		return cncSetspeed;
	}

	public void setCncSetspeed(String cncSetspeed) {
		this.cncSetspeed = cncSetspeed;
	}

	public String getCncGcode() {
		return cncGcode;
	}

	public void setCncGcode(String cncGcode) {
		this.cncGcode = cncGcode;
	}

	public String getCncAlivetime() {
		return cncAlivetime;
	}

	public void setCncAlivetime(String cncAlivetime) {
		this.cncAlivetime = cncAlivetime;
	}

	public String getCncCurrentpro() {
		return cncCurrentpro;
	}

	public void setCncCurrentpro(String cncCurrentpro) {
		this.cncCurrentpro = cncCurrentpro;
	}

	public String getCncActspeed() {
		return cncActspeed;
	}

	public void setCncActspeed(String cncActspeed) {
		this.cncActspeed = cncActspeed;
	}

	public String getCncRapidfeed() {
		return cncRapidfeed;
	}

	public void setCncRapidfeed(String cncRapidfeed) {
		this.cncRapidfeed = cncRapidfeed;
	}

	public String getCncCuttime() {
		return cncCuttime;
	}

	public void setCncCuttime(String cncCuttime) {
		this.cncCuttime = cncCuttime;
	}

	public String getCncTooloffsethnum() {
		return cncTooloffsethnum;
	}

	public void setCncTooloffsethnum(String cncTooloffsethnum) {
		this.cncTooloffsethnum = cncTooloffsethnum;
	}

	public String getCncSetfspeed() {
		return cncSetfspeed;
	}

	public void setCncSetfspeed(String cncSetfspeed) {
		this.cncSetfspeed = cncSetfspeed;
	}

	public String getCncFrate() {
		return cncFrate;
	}

	public void setCncFrate(String cncFrate) {
		this.cncFrate = cncFrate;
	}

	public String getCncMainproname() {
		return cncMainproname;
	}

	public void setCncMainproname(String cncMainproname) {
		this.cncMainproname = cncMainproname;
	}

	public Long getTimediff() {
		return timediff;
	}

	public void setTimediff(Long timediff) {
		this.timediff = timediff;
	}

	public String getCncMode() {
		return cncMode;
	}

	public void setCncMode(String cncMode) {
		this.cncMode = cncMode;
	}

	public Integer getDeviceState() {
		return deviceState;
	}

	public void setDeviceState(Integer deviceState) {
		this.deviceState = deviceState;
	}

	public String getCncCycletime() {
		return cncCycletime;
	}

	public void setCncCycletime(String cncCycletime) {
		this.cncCycletime = cncCycletime;
	}

	public String getCncRelpos() {
		return cncRelpos;
	}

	public void setCncRelpos(String cncRelpos) {
		this.cncRelpos = cncRelpos;
	}

	public String getCncMcX() {
		return cncMcX;
	}

	public void setCncMcX(String cncMcX) {
		this.cncMcX = cncMcX;
	}

	public String getCncMcY() {
		return cncMcY;
	}

	public void setCncMcY(String cncMcY) {
		this.cncMcY = cncMcY;
	}

	public String getCncMcZ() {
		return cncMcZ;
	}

	public void setCncMcZ(String cncMcZ) {
		this.cncMcZ = cncMcZ;
	}

	public String getCncMcA() {
		return cncMcA;
	}

	public void setCncMcA(String cncMcA) {
		this.cncMcA = cncMcA;
	}

	public String getCncRcX() {
		return cncRcX;
	}

	public void setCncRcX(String cncRcX) {
		this.cncRcX = cncRcX;
	}

	public String getCncRcY() {
		return cncRcY;
	}

	public void setCncRcY(String cncRcY) {
		this.cncRcY = cncRcY;
	}

	public String getCncRcA() {
		return cncRcA;
	}

	public void setCncRcA(String cncRcA) {
		this.cncRcA = cncRcA;
	}

	public String getCncLX() {
		return cncLX;
	}

	public void setCncLX(String cncLX) {
		this.cncLX = cncLX;
	}

	public String getCncLY() {
		return cncLY;
	}

	public void setCncLY(String cncLY) {
		this.cncLY = cncLY;
	}
	
}