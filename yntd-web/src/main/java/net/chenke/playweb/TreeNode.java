package net.chenke.playweb;

import java.util.Map;

public class TreeNode {

	private String id;
	private String parent;
	private String text;
	private String icon;
	private TreeState state;
	private Map<String, Object> a_attr;
	private Map<String, Object> li_attr;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public TreeState getState() {
		return state;
	}

	public void setState(TreeState state) {
		this.state = state;
	}

	public Map<String, Object> getA_attr() {
		return a_attr;
	}

	public void setA_attr(Map<String, Object> a_attr) {
		this.a_attr = a_attr;
	}

	public Map<String, Object> getLi_attr() {
		return li_attr;
	}

	public void setLi_attr(Map<String, Object> li_attr) {
		this.li_attr = li_attr;
	}

}
