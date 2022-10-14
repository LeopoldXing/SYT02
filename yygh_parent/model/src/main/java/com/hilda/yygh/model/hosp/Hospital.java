package com.hilda.yygh.model.hosp;

import com.alibaba.fastjson.JSONObject;
import com.hilda.yygh.model.base.BaseMongoEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * <p>
 * Hospital
 * </p>
 *
 * @author qy
 */
@Data
@ApiModel(description = "Hospital")
@Document("Hospital")
public class Hospital extends BaseMongoEntity {
	
	private static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "医院编号")
	@Indexed(unique = true) //唯一索引
	private String hoscode;

	@ApiModelProperty(value = "医院名称")
	@Indexed //普通索引
	private String hosname;

	@ApiModelProperty(value = "医院类型")
	private String hostype;

	@ApiModelProperty(value = "省code")
	private String provinceCode;

	@ApiModelProperty(value = "市code")
	private String cityCode;

	@ApiModelProperty(value = "区code")
	private String districtCode;

	@ApiModelProperty(value = "详情地址")
	private String address;

	@ApiModelProperty(value = "医院logo")
	private String logoData;

	@ApiModelProperty(value = "医院简介")
	private String intro;


	@ApiModelProperty(value = "坐车路线")
	private String route;

	@ApiModelProperty(value = "状态 0：未上线 1：已上线")
	private Integer status;

	//预约规则
	@ApiModelProperty(value = "预约规则")
	private BookingRule bookingRule;

	public void setBookingRule(String bookingRule) {
		this.bookingRule = JSONObject.parseObject(bookingRule, BookingRule.class);
	}

	public Hospital() {
	}

	public Hospital(String hoscode, String hosname, String hostype, String provinceCode, String cityCode, String districtCode, String address, String logoData, String intro, String route, Integer status, BookingRule bookingRule) {
		this.hoscode = hoscode;
		this.hosname = hosname;
		this.hostype = hostype;
		this.provinceCode = provinceCode;
		this.cityCode = cityCode;
		this.districtCode = districtCode;
		this.address = address;
		this.logoData = logoData;
		this.intro = intro;
		this.route = route;
		this.status = status;
		this.bookingRule = bookingRule;
	}
}

