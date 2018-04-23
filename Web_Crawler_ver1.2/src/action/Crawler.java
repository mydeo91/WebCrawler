package action;

import java.util.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.Jsoup;

import setting.BoardDTO;
import setting.DefaultDAO;
import setting.BoardDAO;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Crawler {
	// 데이터 수집 클래스

	// DB 관련
	private BoardDTO boardDTO;
	
	// LOG 관련
	private String file_path = "C:\\\\devself\\crawl_data.txt";
	
	
	// 1. 코어 메소드
	public void crawl_main(List<String> url_list) {
		
		String jsonStr = "";
		
		try {
			// 수집 메소드 --> JSON 문자열
			jsonStr = gathering(url_list);
			
			// 적재 메소드 
			int result = accumulate(jsonStr);
			
			if(result > 0) {
				System.out.printf("%d개의 데이터 입력 성공", result);
			} else {
				System.out.println("데이터 입력 실패");
			}
			
		} catch (ParseException | JsonMappingException e1) {
			e1.printStackTrace();
		} catch (IllegalArgumentException | IOException e2) {
			e2.printStackTrace();
		}
		
	}
	
	
	// 2. 수집 메소드
	@SuppressWarnings("unchecked")
	private String gathering(List<String> url_list) throws IllegalArgumentException, IOException {
		
		JSONObject jsonObj = new JSONObject();
		JSONArray jsonArr = new JSONArray();
		String jsonStr = "";
		int order = 1;
		
		for(String url : url_list) {
			System.out.printf("\n%d번째 URL 파싱 중 : %s\n", order, url);
			order++;
			
			// 환경설정
			Connection.Response response = Jsoup.connect(url).method(Connection.Method.GET).execute();
			JSONObject temp = new JSONObject();
			temp.putAll(getElement(response));
			jsonArr.add(temp);
		}
		
		jsonObj.put("board", jsonArr);
		jsonStr = jsonObj.toJSONString();
		
		return jsonStr;
	}

	
	// 3. 적재 메소드
	private int accumulate(String jsonStr) throws JsonMappingException, ParseException, IOException  {
		int result = 0;
		
		// 데이터 로그 입력
		StringBuilder sb = new StringBuilder();
		String data = "";
		
		// 데이터 입력을 위한 준비
		List<BoardDTO> db_list = new ArrayList<BoardDTO>();

		// 입력시간
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		String now = sdf.format(date);
		sb.append("\n"+now+"\n\n");
		
		
		// JSON 문자열 -> JSON 배열 -> JSON 객체
		// DB와 txt파일에 입력
		JSONParser parser = new JSONParser();
		JSONObject jsonObj = new JSONObject();
		jsonObj=(JSONObject)parser.parse(jsonStr);
		
		JSONArray jsonArr = new JSONArray();
		jsonArr = (JSONArray)jsonObj.get("board");
		
		// 데이터 추출 구간 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
		for(int i = 0; i < jsonArr.size(); ++i) {
			
			// JSON 배열 내의 JSON 객체 1개씩 꺼내서 데이터 추출
			JSONObject temp = new JSONObject();
			temp = (JSONObject)jsonArr.get(i);
			
			boardDTO = new BoardDTO();
			
			// 꺼낸 1개의 JSON 객체에서 keySet 추출 - 자동 매칭 - 반복문으로 모든 키의 값을 추출
			for(Object jsonKey : temp.keySet()) {
				
				// String으로 형변환
				String key = String.valueOf(jsonKey);
				
				// 불필요 컬럼 제외
				if(key.contains("이전글") || key.contains("다음글")) continue;
				
				// 1. 로그용 데이터 생성
				sb.append(String.format("%s : %s \n", key, temp.get(key)));
				
				// 2. DB 입력 데이터 생성
				setData(key, String.valueOf(temp.get(key)));
				
			}
			
			// 1개의 파싱 로그 종료
			sb.append("------------------------\n\n");
			
			// 1개의 파싱 데이터를 리스트에 추가
			db_list.add(boardDTO);
			
		}
		
		// 로그 입력 종료
		data = sb.toString();
		// 파일에 로그 입력
		print_log(data, file_path);
		
		// DB 입력 구간 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
		BoardDAO test = new BoardDAO();
		
		// 리스트에 있는 DTO 객체를 통해 DB에 순차적으로 입력
		for (BoardDTO dto : db_list) { 
			result += test.insert(dto);
		}
		
		System.out.printf("*****%d개의 데이터 입력 완료*****\n\n", result);
		
		return result;
	}
	
	
	// (SUB METHOD) txt파일에 로그 입력
	private void print_log(String data, String file_path2) throws IOException {
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(file_path, true));
		
		// LOG 입력
		bw.write(data);
		bw.flush();

		// 종료
		bw.close();
		
	}


	// (SUB METHOD) DTO에 데이터를 입력
	private void setData(String key, String value) {
		
		switch (key) {
		case "제목":
			boardDTO.setSubject(value);
			System.out.printf("%s  :  입력완료 \n", key);
			break;
		case "내용":
			boardDTO.setContent(value);
			System.out.printf("%s  :  입력완료 \n", key);
			break;
		case "작성자":
		case "등록자":
		case "글쓴이":
			boardDTO.setReg_nm(value);
			System.out.printf("%s  :  입력완료 \n", key);
			break;
		case "부서명":
			boardDTO.setDept_nm(value);
			System.out.printf("%s  :  입력완료 \n", key);
			break;
		case "작성일":
		case "등록일":
			boardDTO.setReg_dt(value);
			System.out.printf("%s  :  입력완료 \n", key);
			break;
		case "조회":
		case "조회수":
			boardDTO.setRead_cnt(value);
			System.out.printf("%s  :  입력완료 \n", key);
			break;
		case "첨부":
		case "첨부파일":
			boardDTO.setAttach_file_name(value);
			System.out.printf("%s  :  입력완료 \n", key);
			break;
		default :
			System.out.printf("%s : 확인되지 않은 필드 \n", key);
			break;
		}
		
	}

	
	// (SUB METHOD) 데이터 추출용 메소드
	private static HashMap<String, Object> getElement(Response response) throws IllegalArgumentException, IOException{
		
		// Hash맵 객체
		HashMap<String, Object> data = new HashMap<String, Object>();
				
		// 페이지를 파싱해서 DOM으로 제작
		Document page = response.parse();
// 추출용 알고리즘 작성 구간 START >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

		// 제작된 DOM에서 Element 추출
		Elements th = page.select("th");
		Elements td = page.select("td");
		
		for(int i = 0; i < th.size(); ++i) {
			System.out.println(th.get(i).text() + " : " + td.get(i).text());
			// 추출된 Element를 K, V에 따라 입력
			data.put(th.get(i).text(), td.get(i).text());
		}

// 추출용 알고리즘 작성 구간 END >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
		return data;
	}
	
}
