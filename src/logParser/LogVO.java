package logParser;

public class LogVO {

	private String status;
	private String url;
	private String browser;
	private String time;
	private String apiServiceId;
	private String apikey;


	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getBrowser() {
		return browser;
	}
	public void setBrowser(String browser) {
		this.browser = browser;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getApiServiceId() {
		return apiServiceId;
	}
	public void setApiServiceId(String apiServiceId) {
		this.apiServiceId = apiServiceId;
	}
	public String getApikey() {
		return apikey;
	}
	public void setApikey(String apikey) {
		this.apikey = apikey;
	}
}