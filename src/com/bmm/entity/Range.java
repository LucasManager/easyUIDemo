package com.bmm.entity;


public class Range {

	private String id;
	private String code;
	private String parentId;
	private String level;
	private String nonWave1Min;
	private String nonWave1Max;
	private String nonWave1Last;
	private String wave1Min;
	private String wave1Max;
	private String wave1Last;
	private String isDelete;
	private String editable;
	private String local;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getNonWave1Min() {
		return nonWave1Min;
	}

	public void setNonWave1Min(String nonWave1Min) {
		this.nonWave1Min = nonWave1Min;
	}

	public String getNonWave1Max() {
		return nonWave1Max;
	}

	public void setNonWave1Max(String nonWave1Max) {
		this.nonWave1Max = nonWave1Max;
	}

	public String getNonWave1Last() {
		return nonWave1Last;
	}

	public void setNonWave1Last(String nonWave1Last) {
		this.nonWave1Last = nonWave1Last;
	}

	public String getWave1Min() {
		return wave1Min;
	}

	public void setWave1Min(String wave1Min) {
		this.wave1Min = wave1Min;
	}

	public String getWave1Max() {
		return wave1Max;
	}

	public void setWave1Max(String wave1Max) {
		this.wave1Max = wave1Max;
	}

	public String getWave1Last() {
		return wave1Last;
	}

	public void setWave1Last(String wave1Last) {
		this.wave1Last = wave1Last;
	}
	
	public String getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(String isDelete) {
		this.isDelete = isDelete;
	}


	public String getEditable() {
		return editable;
	}

	public void setEditable(String editable) {
		this.editable = editable;
	}
	
	@Override
	public String toString() {
		return "Range [id=" + id + ", code=" + code + ", parentId=" + parentId + ", level=" + level + ", nonWave1Min="
				+ nonWave1Min + ", nonWave1Max=" + nonWave1Max + ", nonWave1Last=" + nonWave1Last + ", wave1Min="
				+ wave1Min + ", wave1Max=" + wave1Max + ", wave1Last=" + wave1Last + "]";
	}

	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}


}
