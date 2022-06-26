package logParser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class LogParser {


	public static void main(String[] args){
		//String filePath = "\\input.log";
		String filePath = "/Users/yeonhyangju/Documents/workspace-spring/javaTest/src/input.log";
		//String outFilePath = ".\\output.log";
		List<LogVO> rList = FileUtil.readFile(filePath);

		// apikey count
		Map<String,Integer> apiCnt = new HashMap<String,Integer>();
		int aCnt =1;
		List<String> aKeys = new ArrayList<String>();

		// api Service id
		Map<String,Integer> idCnt = new HashMap<String,Integer>();
		int iCnt =1;
		List<String> iKeys = new ArrayList<String>();

		// 브라우저
		Map<String,Integer> browCnt = new HashMap<String,Integer>();
		int bCnt = 1;
		List<String> bKeys = new ArrayList<String>();
		
		
		/////////////////////
		// apiKey 중복 배제 
//		List<String> result = rList.stream().filter(distinctByKey(row -> row.getApikey())).map(LogVO::getApikey).collect(Collectors.toList()); 
//		
//		for(String str : result) {
//			System.out.println(str);
//		}
		/////////////////////
		
		//최다호출 apiKey
		
		// 상위 3개 api service id와 각각 요청수
		
		//브라우저별 사용비율 
		


		// 읽은 파일에서 값 도출
		// 상태, url, 브라우저, 시간, api service id, apikey
//		for(LogVO rowStr : rList) {
//
//			aCnt =1;
//			iCnt =1;
//			bCnt =1;
//
//			if(apiCnt.containsKey(rowStr[5])) {
//				aCnt = apiCnt.get(rowStr[5]);
//				apiCnt.put(rowStr[5], aCnt+1);
//			} else {
//				apiCnt.put(rowStr[5], aCnt);
//				aKeys.add(rowStr[5]);
//			}
//
//			if(idCnt.containsKey(rowStr[4])) {
//				iCnt = idCnt.get(rowStr[4]);
//				idCnt.put(rowStr[4], iCnt+1);
//			} else {
//				idCnt.put(rowStr[4], iCnt);
//				iKeys.add(rowStr[4]);
//			}
//
//			if(browCnt.containsKey(rowStr[2])) {
//				bCnt = browCnt.get(rowStr[2]);
//				browCnt.put(rowStr[2], bCnt+1);
//			} else {
//				browCnt.put(rowStr[2], bCnt);
//				bKeys.add(rowStr[2]);
//			}
//
//		}
//
//
//		List<Entry<String, Integer>> apiList = sortMap(apiCnt);
//		List<Entry<String, Integer>> idList = sortMap(idCnt);
//		List<Entry<String, Integer>> browList = browPer(rList.size(), sortMap(browCnt));
//
//
//
//		outFile(outFilePath,apiList,idList, browList);


	}
	
	/**
	 * 특정 키로 중복제거
	 *
	 * @param keyExtractor
	 * @param <T>
	 * @return
	 */
	private static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
		Map<Object, Boolean> map = new HashMap<>();
		return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
	}

	public static List<Entry<String, Integer>> browPer(int cnt ,List<Entry<String, Integer>> browList) {
		List<Entry<String, Integer>> perList = new ArrayList<Map.Entry<String,Integer>>();

		Entry<String, Integer> tempEntry = null;

		int perVal = 0;
		for(int i=0,len = browList.size();i<len;i++) {
			tempEntry = browList.get(i);

			perVal = (int)Math.round(((double)tempEntry.getValue() / cnt) * 100);

			tempEntry.setValue(perVal);

			perList.add(tempEntry);

		}

		return perList;
	}



	public static List<Entry<String, Integer>> sortMap(Map<String,Integer> pMap) {

		// Map.Entry 리스트 작성
		List<Entry<String, Integer>> list_entries = new ArrayList<Entry<String, Integer>>(pMap.entrySet());

		// 비교함수 Comparator를 사용하여 내림 차순으로 정렬
		Collections.sort(list_entries, new Comparator<Entry<String, Integer>>() {
			// compare로 값을 비교
			public int compare(Entry<String, Integer> obj1, Entry<String, Integer> obj2)
			{
				// 내림 차순으로 정렬
				return obj2.getValue().compareTo(obj1.getValue());
			}
		});

		return list_entries;
	}

}