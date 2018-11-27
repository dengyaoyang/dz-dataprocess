package com.cecgw.cq.pojo;
/**
 * EPC解码类
 * @author 曹华鹏
 *
 */
public class EPC_DICT {
	private String code;
	private String type;
	private String sub_type;
	private String description;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSub_type() {
		return sub_type;
	}
	public void setSub_type(String sub_type) {
		this.sub_type = sub_type;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

    public EPC_DICT(String code, String type, String sub_type, String description) {
        this.code = code;
        this.type = type;
        this.sub_type = sub_type;
        this.description = description;
    }
}
