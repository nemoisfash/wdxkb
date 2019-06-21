package cn.hxz.webapp.content.entity;

public enum BuiltinModelEnum {
	
	ChannelNODE(1L), 
	SinglesPAGE(2L),
	ArticleLIST(3L),
	RelatedLINK(4L),
	Download(5L);

	private long value;

	BuiltinModelEnum(long value) {
		this.value = value;
	}

	public long getValue() {
		return value;
	}
}
