package com.pocopay.controller;

import com.pocopay.controller.dto.Result;



public abstract class PocoController {
    
    public Result ok() {
        return ok(null);
    }
    
	public Result ok(Object obj) {
		return new Result(true, obj);
	}
	
	public Result notOk() {
	    return notOk(null);
	}
	
	public Result notOk(Object obj) {
		return new Result(false, obj);
	}

}
