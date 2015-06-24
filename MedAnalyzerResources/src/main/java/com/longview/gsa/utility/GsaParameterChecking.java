package com.longview.gsa.utility;

import java.util.Collection;

import com.longview.gsa.exception.MedCheckerException;

public class GsaParameterChecking {
	
	public static void check(String value){
		if(NullCheck.isNullish(value)){
			throw new MedCheckerException("Invalid paramater");
		}
	}
	
	public static void check(Collection<?> value){
		if(NullCheck.isNullish(value)){
			throw new MedCheckerException("Invalid paramaters");
		}
	}
}
