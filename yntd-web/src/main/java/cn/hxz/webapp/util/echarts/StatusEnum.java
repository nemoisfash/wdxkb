package cn.hxz.webapp.util.echarts;

public enum StatusEnum{
	        Enum1("RUNNING","正常运行"),Enum2("POWEROFF","停机"),Enum3("ALARM","报警"),Enum4("WAITING","等待"),Enum5("MANUAL","手动");
	        private String key;
	        private String status_cn;
	        StatusEnum(String key,String status_cn){
	            this.setKey(key);
	            this.setStatus_cn(status_cn);
	        }
	        
	        public String getKey() {
				return key;
			}

			public void setKey(String key) {
				this.key = key;
			}

			public String getStatus_cn() {
				return status_cn;
			}

			public void setStatus_cn(String status_cn) {
				this.status_cn = status_cn;
			}

			public static String getValue(String key){
				for (StatusEnum ele : values()) {
					if(ele.getKey().equals(key)) 
						return ele.getStatus_cn();
				}
				return null;
	        }
	    }
