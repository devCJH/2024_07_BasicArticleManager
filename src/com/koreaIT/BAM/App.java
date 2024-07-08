package com.koreaIT.BAM;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.koreaIT.BAM.dto.Article;
import com.koreaIT.BAM.dto.Member;
import com.koreaIT.BAM.util.Util;

public class App {
	private List<Article> articles;
	private int lastArticleId;
	private List<Member> members;
	private int lastMemberId;
	private Member loginedMember;

	public App() {
		this.articles = new ArrayList<>();
		this.lastArticleId = 0;
		this.members = new ArrayList<>();
		this.lastMemberId = 0;
		this.loginedMember = null;
	}
	
	public void run() {
		System.out.println("== 프로그램 시작 ==");
		
		Scanner sc = new Scanner(System.in);
		
		makeTestData();
		
		while (true) {
			System.out.printf("명령어) ");
			String cmd = sc.nextLine().trim();

			if (cmd.equals("exit")) {
				break;
			}
			
			if (cmd.equals("member join")) {
				
				if (isLogined()) {
					System.out.println("로그아웃 후 이용해주세요");
					continue;
				}
				
				String loginId = null;
				String loginPw = null;
				String name = null;
				
				while (true) {
					System.out.printf("아이디 : ");
					loginId = sc.nextLine().trim();
					
					if (loginId.length() == 0) {
						System.out.println("아이디는 필수 입력 정보입니다");
						continue;
					}
					
					Member member = getMemberByLoginId(loginId);
					
					if (member != null) {
						System.out.printf("[ %s ]은(는) 이미 사용중인 아이디입니다\n", loginId);
						continue;
					}
					
					System.out.printf("[ %s ]은(는) 사용가능한 아이디입니다\n", loginId);
					break;
				}
				
				while (true) {
					System.out.printf("비밀번호 : ");
					loginPw = sc.nextLine().trim();
					
					if (loginPw.length() == 0) {
						System.out.println("비밀번호는 필수 입력 정보입니다");
						continue;
					}
					
					System.out.printf("비밀번호 확인 : ");
					String loginPwChk = sc.nextLine().trim();
					
					if (loginPw.equals(loginPwChk) == false) {
						System.out.println("비밀번호가 일치하지 않습니다");
						continue;
					}
					
					break;
				}
				
				while (true) {
					System.out.printf("이름 : ");
					name = sc.nextLine().trim();
					
					if (name.length() == 0) {
						System.out.println("이름은 필수 입력 정보입니다");
						continue;
					}
					
					break;
				}
				
				Member member = new Member(++lastMemberId, Util.getDateStr(), Util.getDateStr(), loginId, loginPw, name);
				members.add(member);
				
				System.out.printf("%s님의 가입을 환영합니다~\n", loginId);
				
			} else if (cmd.equals("member login")) {
				
				if (isLogined()) {
					System.out.println("로그아웃 후 이용해주세요");
					continue;
				}
				
				System.out.printf("아이디 : ");
				String loginId = sc.nextLine().trim();
				System.out.printf("비밀번호 : ");
				String loginPw = sc.nextLine().trim();
				
				Member member = getMemberByLoginId(loginId);
				
				if (member == null) {
					System.out.printf("[ %s ]은(는) 존재하지 않는 아이디입니다\n", loginId);
					continue;
				}
				
				if (member.getLoginPw().equals(loginPw) == false) {
					System.out.println("비밀번호를 확인해주세요");
					continue;
				}
				
				this.loginedMember = member;
				System.out.printf("[ %s ]님 환영합니다~\n", member.getName());
				
			} else if (cmd.equals("member logout")) {
				
				if (isLogined() == false) {
					System.out.println("로그인 후 이용해주세요");
					continue;
				}
				
				this.loginedMember = null;
				
				System.out.println("정상적으로 로그아웃 되었습니다");
				
			} else if (cmd.equals("article write")) {
				
				if (isLogined() == false) {
					System.out.println("로그인 후 이용해주세요");
					continue;
				}
				
				System.out.printf("제목 : ");
				String title = sc.nextLine();
				System.out.printf("내용 : ");
				String content = sc.nextLine();
				lastArticleId++;
				
				Article article = new Article(lastArticleId, Util.getDateStr(), Util.getDateStr(), this.loginedMember.getId(), title, content);
				
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
				
				int id = getCmdNum(cmd);
				
				if (id == -1) {
					System.out.println("잘못된 명령어입니다");
					continue;
				}

				Article foundArticle = getArtcleById(id);
				
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
				
				int id = getCmdNum(cmd);
				
				if (id == -1) {
					System.out.println("잘못된 명령어입니다");
					continue;
				}

				Article foundArticle = getArtcleById(id);
				
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
				
				int id = getCmdNum(cmd);
				
				if (id == -1) {
					System.out.println("잘못된 명령어입니다");
					continue;
				}

				Article foundArticle = getArtcleById(id);
				
				if (foundArticle == null) {
					System.out.printf("%d번 게시물은 존재하지 않습니다\n", id);
					continue;
				}
				
				articles.remove(foundArticle);
				System.out.printf("%d번 게시물을 삭제했습니다\n", id);
			}
		}
		sc.close();
		System.out.println("== 프로그램 끝 ==");
	}
	
	private boolean isLogined() {
		return this.loginedMember != null;
	}

	private Member getMemberByLoginId(String loginId) {
		for (Member member : members) {
			if (member.getLoginId().equals(loginId)) {
				return member;
			}
		}
		return null;
	}

	private int getCmdNum(String cmd) {
		String[] cmdBits = cmd.split(" ");
		
		int id = -1;
		
		try {
			id = Integer.parseInt(cmdBits[2]);
			return id;
		} catch (NumberFormatException e) {
			return -1;
		}
	}

	private Article getArtcleById(int id) {
		for (Article article : articles) {
			if (article.getId() == id) {
				return article;
			}
		}
		return null;
	}

	private void makeTestData() {
		System.out.println("테스트용 게시물 데이터 3개를 생성했습니다");
		
		for (int i = 1; i <= 3; i++) {
			articles.add(new Article(++lastArticleId, Util.getDateStr(), Util.getDateStr(), 2, "제목" + i, "내용" + i));
		}
	}
}
