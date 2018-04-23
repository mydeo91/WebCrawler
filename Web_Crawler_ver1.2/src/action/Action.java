package action;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Action {

	public static void main(String[] args) {
		// 크롤링 프로그램의 메인이 위치한 가장 상위 클래스
		// 선언부
		Scanner sc = new Scanner(System.in); // 스캐너 객체 인스턴스화
		List<String> url_list = new ArrayList<String>(); // URL 축적 변수
		int max_url = 10; // 최대 수용 URL 값 
		boolean br = true; // 반복문 종료
		
		// 1. Input target URL
		do {
			int cnt = url_list.size() + 1;
			String order_ck = "";
			
			switch (cnt) {
			case 1:
				order_ck = "st";
				break;
			case 2 :
				order_ck = "nd";
				break;
			case 3 :				
				order_ck = "rd";
				break;
			default:
				order_ck = "th";
				break;
			}
			
			System.out.printf("Target URL, %d%s(Quit : X) : ", cnt, order_ck);
			String target_url = sc.nextLine();
			
			// URL 축적
			if(!target_url.equalsIgnoreCase("x")) url_list.add(target_url);
			
			// 사용자가 'X'를 입력하거나, URL 숫자가 최대 수용치를 초과하는 경우 URL 입력 종료
			if(target_url.equalsIgnoreCase("x") || url_list.size() > max_url) {
				br = false; 
				System.out.println("URL input is over.");
			}
			
		} while(br);
		
		sc.close();
		
		// 2. Crawling action
		Crawler cw = new Crawler(); // Crawler 클래스 인스턴스화 
		cw.crawl_main(url_list);
		
		System.out.println("Crawl action is Over");

	}

}
