package com.behaviosec.model;

public class MessageModel {
	private long id;
    private String content;
    private String other;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

	public String getOther() {
		return other;
	}

	public void setOther(String other) {
		this.other = other;
	}
}
