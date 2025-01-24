package com.example.pcmthymeleaf1.handler;

public class JwtAuthenticationEntryPoint{

	private String status;
	private String error;
	private String timestamp;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	//	@Autowired
//	ObjectMapper mapper ;
//
//	@Override
//	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e)
//			throws IOException, ServletException {
//
//		response.setHeader("Content-Type", "application/json");
//		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//		Map<String, Object> data = new HashMap<>();
//		data.put("status", false);
//		data.put("timestamp", Calendar.getInstance().getTime());
//		data.put("error", e.getMessage());
//		response.getOutputStream().println(mapper.writeValueAsString(data));
//	}
}