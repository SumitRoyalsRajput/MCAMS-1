package com.mcams.bean;

import java.time.LocalDate;

public class ComposerBean {
	private int id;
	private String name;
	private LocalDate bornDate;
	private LocalDate diedDate;
	private String caeipiNumber;
	private char[] musicSocietyId;
	private int createdBy;
	private LocalDate createdOn;
	private int updatedBy;
	private LocalDate updatedOn;
	private int deletedFlag;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public LocalDate getBornDate() {
		return bornDate;
	}
	public void setBornDate(LocalDate bornDate) {
		this.bornDate = bornDate;
	}
	public LocalDate getDiedDate() {
		return diedDate;
	}
	public void setDiedDate(LocalDate diedDate) {
		this.diedDate = diedDate;
	}
	public String getCaeipiNumber() {
		return caeipiNumber;
	}
	public void setCaeipiNumber(String caeipiNumber) {
		this.caeipiNumber = caeipiNumber;
	}
	public char[] getMusicSocietyId() {
		return musicSocietyId;
	}
	public void setMusicSocietyId(char[] musicSocietyId) {
		this.musicSocietyId = musicSocietyId;
	}
	public int getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}
	public LocalDate getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(LocalDate createdOn) {
		this.createdOn = createdOn;
	}
	public int getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}
	public LocalDate getUpdatedOn() {
		return updatedOn;
	}
	public void setUpdatedOn(LocalDate updatedOn) {
		this.updatedOn = updatedOn;
	}
	public int getDeletedFlag() {
		return deletedFlag;
	}
	public void setDeletedFlag(int deletedFlag) {
		this.deletedFlag = deletedFlag;
	}
	
}
