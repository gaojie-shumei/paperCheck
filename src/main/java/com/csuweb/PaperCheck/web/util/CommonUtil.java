package com.csuweb.PaperCheck.web.util;

import java.util.concurrent.ThreadPoolExecutor;

public class CommonUtil {
	public static int getCount(ThreadPoolExecutor pools){
		return pools.getQueue().size();
	}
}
