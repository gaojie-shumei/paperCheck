package com.csuweb.PaperCheck.core.common;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
@Component
public class ClusterProgram {
	private ThreadPoolExecutor pools=(ThreadPoolExecutor) Executors.newFixedThreadPool(10); 
	
	

	
}
