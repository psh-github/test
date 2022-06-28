package logParser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Map.Entry;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.swing.plaf.synth.SynthSeparatorUI;

public class LogParser {


	public static void main(String[] args){
		//String filePath = "\\input.log";
		String path = System.getProperty("user.dir");
	      System.out.println("Working Directory = " + path);
		String filePath = path+"/src/input.log";
		String outFilePath = path+"/src/output.log";
		
		List<String[]> logList = FileUtil.readFile(filePath);

		Map logInfoMap = getLogInfo(logList);
		
		int totCnt = logList.size();
		
		FileUtil.outFile(outFilePath,totCnt,logInfoMap);


	}
	
	private static Map getLogInfo(List<String[]> logList) {
		Map logMap = new HashMap<>();
		
		List<String> urlList = new ArrayList<>();
		
		logList.stream().map(line -> line[1]).forEach(urlList::add);
		
		String schStr = "/search/";
        String apiStr = "apikey=";
        logList.stream().map(line -> line[1]).forEach(urlList::add);
        
        String maxApiKey = urlList.stream().
        map(url -> url.substring(url.indexOf(apiStr)+apiStr.length(),url.indexOf(apiStr)+apiStr.length()+4)).
        collect(Collectors.groupingBy(Function.identity(), Collectors.counting())).entrySet()
        .stream().max(Comparator.comparing(Map.Entry::getValue)).orElseThrow(NoSuchElementException::new).getKey();
        
        logMap.put("maxApiKey", maxApiKey);
        
        List serviceTop3 = urlList.stream()
        	.map(url -> url.substring(url.indexOf(schStr)+schStr.length(),url.indexOf("?")))
        	.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
        	.entrySet().stream().sorted(Comparator.comparing(Map.Entry::getValue,Comparator.reverseOrder()))
        	.limit(3).collect(Collectors.toList());
        
        logMap.put("serviceTop3", serviceTop3);

        
		List browserList = logList.stream()
			.map(line -> line[2])
			.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
        	.entrySet().stream()
        	.sorted(Comparator.comparing(Map.Entry::getValue,Comparator.reverseOrder())).collect(Collectors.toList());
		
		logMap.put("browserList", browserList);
				
		return logMap;
	}
}