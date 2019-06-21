package org.tdds.entity;


import javax.persistence.Column;
import javax.persistence.Table;

@Table(name="Monitoring_List")
public class MonitoringList {
	
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 1L;

	@Column(name = "Machine_Name")
	private String machineName;

	@Column(name = "Update_Date")
	private String updateDate;

	@Column(name = "Update_Time")
	private String updateTime;

	@Column(name = "Machine_Signal")
	private String machineSignal;

	@Column(name = "Machine_Mode")
	private String machineMode;

	@Column(name = "Machine_Status")
	private String machineStatus;

	@Column(name = "Alarm_No")
	private String alarmNo;

	@Column(name = "Alarm_Message")
	private String alarmMessage;

	@Column(name = "Alarm_TextColor")
	private String alarmTextcolor;

	@Column(name = "Alarm_BackgroundColor")
	private String alarmBackgroundcolor;

	@Column(name = "Maintenance_Signal")
	private String maintenanceSignal;

	@Column(name = "MainProgram_No")
	private String mainprogramNo;

	@Column(name = "MainProgram_Comment")
	private String mainprogramComment;

	@Column(name = "SubProgram_No")
	private String subprogramNo;

	@Column(name = "SubProgram_Comment")
	private String subprogramComment;

	@Column(name = "Tool_No")
	private String toolNo;

	@Column(name = "Tool_Suffix")
	private String toolSuffix;

	@Column(name = "Tool_SuffixColor")
	private String toolSuffixColor;

	@Column(name = "Tool_Name")
	private String toolName;

	@Column(name = "Tool_Part")
	private String toolPart;

	@Column(name = "Tool_PartBackgroundColor")
	private String toolPartBackgroundColor;

	@Column(name = "Tool2_No")
	private String tool2No;

	@Column(name = "Tool2_Suffix")
	private String tool2Suffix;

	@Column(name = "Tool2_SuffixColor")
	private String tool2SuffixColor;

	@Column(name = "Tool2_Name")
	private String tool2Name;

	@Column(name = "Tool2_Part")
	private String tool2Part;

	@Column(name = "Tool2_PartBackgroundColor")
	private String tool2PartBackgroundColor;

	@Column(name = "PartsCount_Target")
	private String partscountTarget;

	@Column(name = "PartsCount_Result")
	private String partscountResult;

	@Column(name = "MachiningTime_Progress")
	private String machiningTimeProgress;

	@Column(name = "Override_Rapid")
	private String overrideRapid;

	@Column(name = "Override_Spindle")
	private String overrideSpindle;

	@Column(name = "Override_Feed")
	private String overrideFeed;

	@Column(name = "Spindle_Mode")
	private String spindleMode;

	public String getMachineName() {
		return machineName;
	}

	public void setMachineName(String machineName) {
		this.machineName = machineName;
	}

	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getMachineSignal() {
		return machineSignal;
	}

	public void setMachineSignal(String machineSignal) {
		this.machineSignal = machineSignal;
	}

	public String getMachineMode() {
		return machineMode;
	}

	public void setMachineMode(String machineMode) {
		this.machineMode = machineMode;
	}

	public String getMachineStatus() {
		return machineStatus;
	}

	public void setMachineStatus(String machineStatus) {
		this.machineStatus = machineStatus;
	}

	public String getAlarmNo() {
		return alarmNo;
	}

	public void setAlarmNo(String alarmNo) {
		this.alarmNo = alarmNo;
	}

	public String getAlarmMessage() {
		return alarmMessage;
	}

	public void setAlarmMessage(String alarmMessage) {
		this.alarmMessage = alarmMessage;
	}

	public String getAlarmTextcolor() {
		return alarmTextcolor;
	}

	public void setAlarmTextcolor(String alarmTextcolor) {
		this.alarmTextcolor = alarmTextcolor;
	}

	public String getAlarmBackgroundcolor() {
		return alarmBackgroundcolor;
	}

	public void setAlarmBackgroundcolor(String alarmBackgroundcolor) {
		this.alarmBackgroundcolor = alarmBackgroundcolor;
	}

	public String getMaintenanceSignal() {
		return maintenanceSignal;
	}

	public void setMaintenanceSignal(String maintenanceSignal) {
		this.maintenanceSignal = maintenanceSignal;
	}

	public String getMainprogramNo() {
		return mainprogramNo;
	}

	public void setMainprogramNo(String mainprogramNo) {
		this.mainprogramNo = mainprogramNo;
	}

	public String getMainprogramComment() {
		return mainprogramComment;
	}

	public void setMainprogramComment(String mainprogramComment) {
		this.mainprogramComment = mainprogramComment;
	}

	public String getSubprogramNo() {
		return subprogramNo;
	}

	public void setSubprogramNo(String subprogramNo) {
		this.subprogramNo = subprogramNo;
	}

	public String getSubprogramComment() {
		return subprogramComment;
	}

	public void setSubprogramComment(String subprogramComment) {
		this.subprogramComment = subprogramComment;
	}

	public String getToolNo() {
		return toolNo;
	}

	public void setToolNo(String toolNo) {
		this.toolNo = toolNo;
	}

	public String getToolSuffix() {
		return toolSuffix;
	}

	public void setToolSuffix(String toolSuffix) {
		this.toolSuffix = toolSuffix;
	}

	public String getToolSuffixColor() {
		return toolSuffixColor;
	}

	public void setToolSuffixColor(String toolSuffixColor) {
		this.toolSuffixColor = toolSuffixColor;
	}

	public String getToolName() {
		return toolName;
	}

	public void setToolName(String toolName) {
		this.toolName = toolName;
	}

	public String getToolPart() {
		return toolPart;
	}

	public void setToolPart(String toolPart) {
		this.toolPart = toolPart;
	}

	public String getToolPartBackgroundColor() {
		return toolPartBackgroundColor;
	}

	public void setToolPartBackgroundColor(String toolPartBackgroundColor) {
		this.toolPartBackgroundColor = toolPartBackgroundColor;
	}

	public String getTool2No() {
		return tool2No;
	}

	public void setTool2No(String tool2No) {
		this.tool2No = tool2No;
	}

	public String getTool2Suffix() {
		return tool2Suffix;
	}

	public void setTool2Suffix(String tool2Suffix) {
		this.tool2Suffix = tool2Suffix;
	}

	public String getTool2SuffixColor() {
		return tool2SuffixColor;
	}

	public void setTool2SuffixColor(String tool2SuffixColor) {
		this.tool2SuffixColor = tool2SuffixColor;
	}

	public String getTool2Name() {
		return tool2Name;
	}

	public void setTool2Name(String tool2Name) {
		this.tool2Name = tool2Name;
	}

	public String getTool2Part() {
		return tool2Part;
	}

	public void setTool2Part(String tool2Part) {
		this.tool2Part = tool2Part;
	}

	public String getTool2PartBackgroundColor() {
		return tool2PartBackgroundColor;
	}

	public void setTool2PartBackgroundColor(String tool2PartBackgroundColor) {
		this.tool2PartBackgroundColor = tool2PartBackgroundColor;
	}

	public String getPartscountTarget() {
		return partscountTarget;
	}

	public void setPartscountTarget(String partscountTarget) {
		this.partscountTarget = partscountTarget;
	}

	public String getPartscountResult() {
		return partscountResult;
	}

	public void setPartscountResult(String partscountResult) {
		this.partscountResult = partscountResult;
	}

	public String getMachiningTimeProgress() {
		return machiningTimeProgress;
	}

	public void setMachiningTimeProgress(String machiningTimeProgress) {
		this.machiningTimeProgress = machiningTimeProgress;
	}

	public String getOverrideRapid() {
		return overrideRapid;
	}

	public void setOverrideRapid(String overrideRapid) {
		this.overrideRapid = overrideRapid;
	}

	public String getOverrideSpindle() {
		return overrideSpindle;
	}

	public void setOverrideSpindle(String overrideSpindle) {
		this.overrideSpindle = overrideSpindle;
	}

	public String getOverrideFeed() {
		return overrideFeed;
	}

	public void setOverrideFeed(String overrideFeed) {
		this.overrideFeed = overrideFeed;
	}

	public String getSpindleMode() {
		return spindleMode;
	}

	public void setSpindleMode(String spindleMode) {
		this.spindleMode = spindleMode;
	}

	/*public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}*/
}
