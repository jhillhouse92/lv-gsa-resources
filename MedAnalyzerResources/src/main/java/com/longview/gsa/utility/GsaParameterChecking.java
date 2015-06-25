package com.longview.gsa.utility;

import java.util.Collection;

import com.longview.gsa.exception.MedCheckerException;

public class GsaParameterChecking {
	
	public static void check(String value){
		if(NullCheck.isNullish(value)){
			throw new MedCheckerException("Invalid parameter");
		}
	}
	
	public static void check(Collection<?> value){
		if(NullCheck.isNullish(value)){
			throw new MedCheckerException("Invalid parameters");
		}
	}
}
