package com.numino.sbjs.dbmodel;

import javax.persistence.*;


@Entity
@Table(name = "asynch_events")
public class AsyncEvents {

	@Id
	@Column(name = "row_id")
	private String row_id;
	private String event_type;
	private String event_subtype;
	private String status;

	public String getRow_id() {
		return row_id;
	}

	public void setRow_id(String row_id) {
		this.row_id = row_id;
	}

	public String getEvent_type() {
		return event_type;
	}

	public void setEvent_type(String event_type) {
		this.event_type = event_type;
	}

	public String getEvent_subtype() {
		return event_subtype;
	}

	public void setEvent_subtype(String event_subtype) {
		this.event_subtype = event_subtype;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
