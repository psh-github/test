package logParser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileUtil {
	


	/**
	 * 파일을 읽어서 변수에 저장
	 * @return
	 */
	public static List<LogVO> readFile(String filePath) {
		List<LogVO> rList = new ArrayList<LogVO>();
		LogVO logVO = null;

		try{
			//파일 객체 생성
			File file = new File(filePath);
			
			//////////////
//			Scanner sc = new Scanner(file);
//			int cnt = 0;
//			while(sc.hasNextLine() && cnt < 100) {
//				System.out.println(cnt+"*"+sc.nextLine());
//				cnt++;
//			}
			Map<String, Long> freq;
			
			try (Stream<String> words = new Scanner(file).tokens()) {
				freq = words.collect(
				        Collectors.groupingBy(String::toLowerCase, Collectors.counting())
				);
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			
			//////////////
			//입력 스트림 생성
			FileReader filereader = new FileReader(file);
			//입력 버퍼 생성
			BufferedReader bufReader = new BufferedReader(filereader);
			String line = "";
			int rowCnt = 0;
			while((line = bufReader.readLine()) != null){

				logVO = stringToArr(line);
				rList.add(logVO);

			}
			bufReader.close();
		}catch (FileNotFoundException e) {
			e.printStackTrace();
			// TODO: handle exception
		}catch(IOException e){
			System.out.println(e);
		}


		return rList;
	}
	

	public static boolean outFile(String filePath,
			List<Entry<String, Integer>> apiList,
			List<Entry<String, Integer>> idList,
			List<Entry<String, Integer>> browList) {
		boolean result = false;

		Entry<String, Integer> tempEntry = null;
		String topApi = "";


		try {
			//파일 객체 생성
			File file = new File(filePath);
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));

			if(file.isFile() && file.canWrite()){

				bufferedWriter.write("최다호출 APIKEY");

				bufferedWriter.newLine();
				bufferedWriter.newLine();

				tempEntry = apiList.get(0);

				topApi = tempEntry.getKey();
				bufferedWriter.write(topApi);

				bufferedWriter.newLine();
				bufferedWriter.newLine();
				bufferedWriter.newLine();
				bufferedWriter.newLine();

				bufferedWriter.write("상위 3개의 API Service ID와 각각의 요청 수");

				bufferedWriter.newLine();
				bufferedWriter.newLine();

				for(int i=0;i<3;i++) {
					tempEntry = idList.get(i);

					bufferedWriter.write(tempEntry.getKey()+" : "+tempEntry.getValue());
					bufferedWriter.newLine();
					bufferedWriter.newLine();
				}


				bufferedWriter.newLine();
				bufferedWriter.newLine();
				bufferedWriter.newLine();

				bufferedWriter.write("웹브라우저별 사용 비율");

				bufferedWriter.newLine();
				bufferedWriter.newLine();

				for(int i=0,len = browList.size();i<len;i++) {
					tempEntry = browList.get(i);

					bufferedWriter.write(tempEntry.getKey()+" : "+tempEntry.getValue() + "%");
					bufferedWriter.newLine();
					bufferedWriter.newLine();
				}


				bufferedWriter.close();
			}

		} catch(Exception e) {
			e.printStackTrace();
		}




		return result;
	}
	


	/**
	 * 문자를 받아서 배열에 넣어줌(상태 200인것만)
	 * @param str 문자
	 * @return 문자 배열(상태 200이 아닌것은 null)
	 */
	public static LogVO stringToArr(String str) {
		// 상태, url, 브라우저, 시간, api service id, apikey
		String[] rStr = new String[6];

		String[] infoArr = null;

		infoArr = str.substring(1, str.length()-1).split("\\]\\[");
		
		LogVO logVO = new LogVO();
		//상태, url, 브라우저, 시간, api service id, apikey
		//[200, http://apis.daum.net/search/knowledge?apikey=23jf&q=daum, IE, 2012-06-10 08:00:00, knowledge, 23jf]

		
		logVO.setStatus(infoArr[0]);
//		logVO.setUrl(infoArr[1]);
		logVO.setBrowser(infoArr[2]);
//		logVO.setTime(infoArr[3]);
		
		try {
			URL aURL = new URL(infoArr[1]);
	        
	        Map<String, String> map=getQueryMap(aURL.getQuery());
	        // 상태코드도 넘어와서 apikey, p parmeter가 있는지 확인하여 상태가 맞는지 확인가능
	        
	        if(map != null &&map.containsKey("apikey")) {
	        	//System.out.println("**apikey=" + map.get("apikey"));
	        	logVO.setApikey(map.get("apikey"));
	        }
	        //System.out.println("**apiServiceId=" + aURL.getPath().replace("/search/", ""));
	        logVO.setApiServiceId(aURL.getPath().replace("/search/", ""));
	        

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return logVO;
	}
	
	

	public static String getCallId(String callUrl) {
		String apiServiceId = "";
		String apikey = "";
		/////////////////////////////////////////
		//http://apis.daum.net/search/knowledge?apikey=23jf&q=daum
		try {
			URL aURL = new URL(callUrl);
//			System.out.println("protocol = " + aURL.getProtocol());
//	        System.out.println("authority = " + aURL.getAuthority());
//	        System.out.println("host = " + aURL.getHost());
//	        System.out.println("port = " + aURL.getPort());
//	        System.out.println("path = " + aURL.getPath());
//	        System.out.println("query = " + aURL.getQuery());
//	        System.out.println("filename = " + aURL.getFile());
//	        System.out.println("ref = " + aURL.getRef());
	        
	        Map<String, String> map=getQueryMap(aURL.getQuery());
	        // 상태코드도 넘어와서 apikey, p parmeter가 있는지 확인하여 상태가 맞는지 확인가능
	        if(map.containsKey("apikey")) {
	        	System.out.println("**apikey=" + map.get("apikey"));
	        	apikey = map.get("apikey");
	        }
	        System.out.println("**apiServiceId=" + aURL.getPath().replace("/search/", ""));
	        apiServiceId = aURL.getPath().replace("/search/", "");
	        

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		/////////////////////////////////////////
		return "";
	}
	
	public static Map<String, String> getQueryMap(String query)
    {    	
    	if (query==null) return null;
    	
    	int pos1=query.indexOf("?");
    	if (pos1>=0) {
    		query=query.substring(pos1+1);
    	}
    	
        String[] params = query.split("&");
        Map<String, String> map = new HashMap<String, String>();
        for (String param : params)
        {
            String name = param.split("=")[0];
            String value = param.split("=")[1];
            map.put(name, value);
        }
        return map;
    }

}
