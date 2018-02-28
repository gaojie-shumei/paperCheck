package com.csuweb.PaperCheck.core.bean;

public class ProxyIP {
		private String ip;
		private int port;
		
		public ProxyIP(String ip, int port)
		{
			this.ip = ip;
			this.port = port;
		}
		
		public String getIP()
		{
			return this.ip;
		}
		public int getPort()
		{
			return this.port;
		}
//		public boolean getFlag()
//		{
//			return this.flag;
//		}
//		public void setFlag(boolean flag)
//		{
//			this.flag = flag;
//		}

}
