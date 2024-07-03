package com.koreaIT.BAM.dto;

public class Article {
	public int id;
	public String regDate;
	public String updateDate;
	public String title;
	public String content;
	
	public Article(int id, String regDate, String updateDate, String title, String content) {
		this.id = id;
		this.regDate = regDate;
		this.updateDate = updateDate;
		this.title = title;
		this.content = content;
	}
}