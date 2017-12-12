/**
 * @Class ServiceCode
 * Copyright (c) 2014 Shanghai P&C Information Technology Co.,Ltd. All rights reserved.
 */
package com.bixian365.dzc.utils.httpClient;

/**
 * ***************************
 * 枚举值
 * @author mfx
 * 16/11/15 下午4:09
 * ***************************
 */
public enum ServiceCodeEnum {
     /**
 	 * 首页轮播图和公告图
 	 */
	CHANNELNT("quractMblistReqMsg","quractMblist",220),
	/**
	 * 获取密码因子
	 */
	GETPASSVALUE("getPasswordKeyReqMsg","getPasswordKeyStr",116),
	/**
	 * 会员登录
	 */
	LOGIN("memberReqMsg", "memberLoginForApp",74),
	/**
	 * 生活本地特产
	 */
	SHTC("localGoodsReqMsg", "localGoods",227),
	/**
	 * 首页重庆特产
	 */
	MAINTC("showConfigReqMsg", "showConfig",221),
//	/**
//	 * 验证验证码
//	 */
//	TESTCODE("phoneCodeReqMsg", "validatePhoneCode",119),
	/**
	 * 意见反馈
	 */
	FEEDBACK("feedBackReqMsg","saveFeedBack",223),
	/**
	 * 版本更新
	 */
	UPDATEVER("versionReqMsg", "queryVersionInfo",224),
	/**
	 * 实名认证第一步 身份信息上传
	 */
	SAVERELAUTHINFO("saveMemberRelAuthInfoReqMsg", "saveMemberRelAuthInfo",228),
	/**
	 * 修改用户头像
	 */
	UPDATEHEADIMG("updateMemberInfoReqMsg","updateMemberInfo",231),
	/**
	 * 上传身份证照片
	 */
	UPLOADIDCARDIMG("saveIdphotoInfoReqMsg","saveIdphotoInfo",229),
	/**
	 * 修改支付密码上传照片
	 */
	UPLOADPSDIDCARDIMG("saveIdphotoInfoReqMsg","uploadUserIdphoto",234),
	/**
	 * 查询余额
	 */
	SELECTBALANCE("memberbalanceandotherReqMsg","memberbalanceandother",109),
	/**
	 * 查询实名认证信息
	 */
	SELECTAUTHINFO("memberAuthReqMsg","memberLogin",233),
	/**
	 * 退出登录
	 */
	EXITLOGIN("memberLogOutReqMsg","memberLogOut",232),
	/**
	 * 便民服务
	 */
	SERVICE("hanyServiceReqMsg","hanyService",226),
	/**
	 * 查询银行卡列表
	 */
	BANKCARDLIST("memberacclistReqMsg","memberacclist",304),
	/**
	 * 查询银行卡详情信息
	 */
//	USERBANKINFO("MemberMessReqMsg","cabinQryReqMsg",0),
	/**
	 * 绑卡查询用户姓名 身份证
	 */
	SELECTIDCARD("memberMessReqMsg","checkMemberMess",300),
	/**
	 * 银行卡bin 类型查询
	 */
	CARDBINSELECT("cabinQryReqMsg","cabinQry",301),
	/**
	 *  验证绑卡发送获取验证码
	 */
	SENDCODE("checkCardAndSendMessReqMsg","CheckCardAndSend",302),
	/**
	 * 绑卡 验证短信短息
	 */
	TESTBANKCODE("checkCodeReqMsg","cabinQryReqMsg",303),
	/**
	 * 实名绑卡最后一步上传银行卡照片
	 */
	UPLOADBANKIMG("saveCardInfoReqMsg","saveCardInfo",230),
	/**
	 * 删除 设置默认银行卡
	 */
	DELSETBANKTYPE("delmemberaccReqMsg","delmemberacc",305),
	/**
	 * 支付 payNo 支付流水反查信息
	 */
	PAYNOINFO("memberBindReqMsg","checkMemberAndBind",306),
	/**
	 * 支付获取短信码
	 */
	PAYSENDCODE("quickPaySendCodeReqMsg","quickPaySendCode",307),
	/**
	 * 支付验证短信码支付
	 */
	PAYCODE("plugPayReqMsg","payHandling",308),
	/**
	 * 忘记支付密码 验证验证码
	 */
	FORGETPAYPSDGETCODE("resetPayPwdReqMsg","resetPayPassword",309),

	/**
	 * 忘记支付密码 重置支付密码  获取验证码
	 */
	FORGETPAYPSDSENDCODE("eCodeReqMsg","ecode",310),
	/**
	 * 主页弹出公告信息
	 *  "receivetype": "3",  3买家  2卖家 publicNoticeRspMsg
	 "  memberno": ""
	 */
	NOTICE("publicNoticeReqMsg","publicNotice",311),
	/**
	 * 手势密码开关状态，程序启动调用
	 */
	GESTURESSTATUS("patternLockReqMsg","queryPatternLock",194),
	/**
	 * 设置手势密码开关
	 */
	GESTURESSWITCH("patternLockReqMsg","setPatternLock",193),
	/**
	 * 设置手势密码
	 */
	GESTURESSET("patternLockReqMsg","resetPatternLock",195),
	/**
	 * 验证登录密码，并以修改手势密码前提
	 */
	CHECKLOGINPSD("memberReqMsg","checkMemberPwd",196),
	/**
	 * 查询用户消息   查询退出后台时间
	 */
	SELECTUSERMSG("innerLetterStatusReqMsg","innerletterStatus",312),
	/**
	 * 检查手机是否可注册
	 */
	REGISTPHONEEXIST("checkInfoExistReqMsg","checkInfoExist",198),
	/**
	 * 发送验证码  验证验证码  同一个接口 根据phonecode区分  有值为验证 短信码
	 */
	REGISTSENDCODE("eCodeReqMsg","sendCode",310),
	/**
	 * 上送注册信息，密码等
	 */
	REGISSUBMIT("mMemberReqMsg","addMember",200),
	;
	/**	 
	/**	 
	 * 根目录名称
	 */
	private String baseName;

	/**
	 * 服务名
	 */
	private String serviceName;
    /**
     * 接口ID
     */
	private int commonId;
	/**
	 * 
	 * @Description
	 */
	private ServiceCodeEnum(String baseName, String serviceName, int commonId) {
		this.baseName = baseName;
		this.serviceName = serviceName;
		this.commonId = commonId;
	}
	public String getBaseName() {
		return baseName;
	}

//	public void setBaseName(String baseName) {
//		this.baseName = baseName;
//	}

	public String getServiceName() {
		return serviceName;
	}

//	public void setServiceName(String serviceName) {
//		this.serviceName = serviceName;
//	}

	public int getCommonId() {
		return commonId;
	}

//	public void setCommonId(int commonId) {
//		this.commonId = commonId;
//	}

}
