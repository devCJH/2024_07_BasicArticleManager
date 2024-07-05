package com.koreaIT.BAM;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.koreaIT.BAM.dto.Article;
import com.koreaIT.BAM.dto.Member;
import com.koreaIT.BAM.util.Util;

public class Main {
	
	private static List<Article> articles;
	private static int lastArticleId;
	private static List<Member> members;
	private static int lastMemberId;

	static {
		articles = new ArrayList<>();
		lastArticleId = 0;
		members = new ArrayList<>();
		lastMemberId = 0;
	}
	
	public static void main(String[] args) {
		System.out.println("== 프로그램 시작 ==");
		
		Scanner sc = new Scanner(System.in);
		
		makeTestData();
		
		while (true) {
			System.out.printf("명령어) ");
			String cmd = sc.nextLine();

			if (cmd.equals("exit")) {
				break;
			}
			
			if (cmd.equals("member join")) {
				
				System.out.printf("아이디 : ");
				String loginId = sc.nextLine();
				
				String loginPw = null;
				while (true) {
					System.out.printf("비밀번호 : ");
					loginPw = sc.nextLine();
					System.out.printf("비밀번호 확인 : ");
					String loginPwChk = sc.nextLine();
					
					if (loginPw.equals(loginPwChk) == false) {
						System.out.println("비밀번호가 일치하지 않습니다");
						continue;
					}
					
					
					break;
				}
				System.out.printf("이름 : ");
				String name = sc.nextLine();
				
				Member member = new Member(++lastMemberId, Util.getDateStr(), Util.getDateStr(), loginId, loginPw, name);
				members.add(member);
				
				System.out.printf("%s님의 가입을 환영합니다~\n", loginId);
				
			} else if (cmd.equals("article write")) {
				System.out.printf("제목 : ");
				String title = sc.nextLine();
				System.out.printf("내용 : ");
				String content = sc.nextLine();
				lastArticleId++;
				
				Article article = new Article(lastArticleId, Util.getDateStr(), Util.getDateStr(), title, content);
				
				articles.add(article);
				
				System.out.printf("%d번 게시물이 생성되었습니다\n", lastArticleId);
				
			} else if (cmd.startsWith("article list")) {
				if (articles.size() == 0) {
					System.out.println("게시물이 존재하지 않습니다");
					continue;
				}
				
				List<Article> printArticles = articles;
				
				String searchKeyword = cmd.substring("article list".length()).trim();
				
				if (searchKeyword.length() != 0) {
					
					System.out.println("검색어 : " + searchKeyword);
					
					printArticles = new ArrayList<>();
					
					for (Article article : articles) {
						if (article.getTitle().contains(searchKeyword)) {
							printArticles.add(article);
						}
					}
					
					if (printArticles.size() == 0) {
						System.out.println("검색결과가 없습니다");
						continue;
					}
				}
				
				System.out.println("번호	|	제목	|	작성일");
				for (int i = printArticles.size() - 1; i >= 0; i--) {
					Article article = printArticles.get(i);
					System.out.printf("%d	|	%s	|	%s	\n", article.getId(), article.getTitle(), article.getRegDate());
				}
				
			} else if (cmd.startsWith("article detail ")) {
				
				String[] cmdBits = cmd.split(" ");
				int id = 0;
				
				try {
					id = Integer.parseInt(cmdBits[2]);
				} catch (NumberFormatException e) {
					System.out.println("잘못된 명령어입니다");
					continue;
				}

				Article foundArticle = null;
				
				for (Article article : articles) {
					if (article.getId() == id) {
						foundArticle = article;
						break;
					}
				}
				
				if (foundArticle == null) {
					System.out.printf("%d번 게시물은 존재하지 않습니다\n", id);
					continue;
				}
				
				System.out.printf("번호 : %d\n", foundArticle.getId());
				System.out.printf("작성일 : %s\n", foundArticle.getRegDate());
				System.out.printf("수정일 : %s\n", foundArticle.getUpdateDate());
				System.out.printf("제목 : %s\n", foundArticle.getTitle());
				System.out.printf("내용 : %s\n", foundArticle.getContent());
				
			} else if (cmd.startsWith("article modify ")) {
				
				String[] cmdBits = cmd.split(" ");
				int id = 0;
				
				try {
					id = Integer.parseInt(cmdBits[2]);
				} catch (NumberFormatException e) {
					System.out.println("잘못된 명령어입니다");
					continue;
				}

				Article foundArticle = null;
				
				for (Article article : articles) {
					if (article.getId() == id) {
						foundArticle = article;
						break;
					}
				}
				
				if (foundArticle == null) {
					System.out.printf("%d번 게시물은 존재하지 않습니다\n", id);
					continue;
				}
				
				System.out.printf("수정할 제목 : ");
				String title = sc.nextLine();
				System.out.printf("수정할 내용 : ");
				String content = sc.nextLine();
				
				foundArticle.setUpdateDate(Util.getDateStr());
				foundArticle.setTitle(title);
				foundArticle.setContent(content);
				
				System.out.printf("%d번 게시물을 수정했습니다\n", id);
				
			} else if (cmd.startsWith("article delete ")) {
				
				String[] cmdBits = cmd.split(" ");
				int id = 0;
				
				try {
					id = Integer.parseInt(cmdBits[2]);
				} catch (NumberFormatException e) {
					System.out.println("잘못된 명령어입니다");
					continue;
				}

				int foundIndex = -1;
				
				int i = 0;
				for (Article article : articles) {
					if (article.getId() == id) {
						foundIndex = i;
						break;
					}
					i++;
				}
				
				if (foundIndex == -1) {
					System.out.printf("%d번 게시물은 존재하지 않습니다\n", id);
					continue;
				}
				
				articles.remove(foundIndex);
				System.out.printf("%d번 게시물을 삭제했습니다\n", id);
			}
		}
		sc.close();
		System.out.println("== 프로그램 끝 ==");
	}
	
	private static void makeTestData() {
		System.out.println("테스트용 게시물 데이터 3개를 생성했습니다");
		
		for (int i = 1; i <= 3; i++) {
			articles.add(new Article(++lastArticleId, Util.getDateStr(), Util.getDateStr(), "제목" + i, "내용" + i));
		}
	}
}