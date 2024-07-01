package com.koreaIT.BAM;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		System.out.println("== 프로그램 시작 ==");
		
		Scanner sc = new Scanner(System.in);
		
		List<Article> articles = new ArrayList<>();
		
		int id = 0;
		
		while (true) {
			System.out.printf("명령어) ");
			String cmd = sc.nextLine();
			
			if (cmd.equals("article write")) {
				System.out.printf("제목 : ");
				String title = sc.nextLine();
				System.out.printf("내용 : ");
				String content = sc.nextLine();
				id++;
				
				Article article = new Article(id, title, content);
				
				articles.add(article);
				
				System.out.printf("%d번 게시물이 생성되었습니다\n", id);
			}
			
			if (cmd.equals("article list")) {
				System.out.println("게시물이 존재하지 않습니다");
			}
			
			if (cmd.equals("exit")) {
				break;
			}
		}
		sc.close();
		System.out.println("== 프로그램 끝 ==");
	}
}

class Article {
	int id;
	String title;
	String content;
	
	Article(int id, String title, String content) {
		this.id = id;
		this.title = title;
		this.content = content;
	}
}