package setting;

public class BoardDTO {
	// 수집 데이터를 적재하기 위한 DTO
	
	// 제목
	private String subject = "";
	// 내용
	private String content = "";
	// 이름
	private String reg_nm = "";
	// 부서명
	private String dept_nm ="";
	// 전화번호
	private String tel = "";
	// 조회수
	private String read_cnt = "";
	// 등록일
	private String reg_dt = "";
	// 첨부파일
	private String attach_file_name = "";
	
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getReg_nm() {
		return reg_nm;
	}
	public void setReg_nm(String reg_nm) {
		this.reg_nm = reg_nm;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getRead_cnt() {
		return read_cnt;
	}
	public void setRead_cnt(String read_cnt) {
		this.read_cnt = read_cnt;
	}
	public String getReg_dt() {
		return reg_dt;
	}
	public void setReg_dt(String reg_dt) {
		this.reg_dt = reg_dt;
	}
	public String getDept_nm() {
		return dept_nm;
	}
	public void setDept_nm(String dept_nm) {
		this.dept_nm = dept_nm;
	}
	public String getAttach_file_name() {
		return attach_file_name;
	}
	public void setAttach_file_name(String attach_file_name) {
		this.attach_file_name = attach_file_name;
	}
	@Override
	public String toString() {
		return "BoardDTO [subject=" + subject + ", content=" + content
				+ ", reg_nm=" + reg_nm + ", dept_nm=" + dept_nm + ", tel="
				+ tel + ", read_cnt=" + read_cnt + ", reg_dt=" + reg_dt
				+ ", attach_file_name=" + attach_file_name + "]";
	}
	
}
