package com.pocopay.controller.dto;

import lombok.Data;

@Data
public class Result {

	private boolean ok;
	private Object content;

	public Result(boolean ok, Object obj) {
		this.ok = ok;
		this.content = obj;
	}

}
