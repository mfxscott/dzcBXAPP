package com.bixian365.dzc.utils.httpClient;

import com.bixian365.dzc.entity.car.ShoppingCartLinesEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ***************************
 * 公共资源
 * @author mfx
 * 深圳市优讯信息技术有限公司
 * 16/10/28 下午3:50
 * ***************************
 */
public class AppClient{
    //缓存平板购物车商品列表
    public static List<ShoppingCartLinesEntity>  padCarGoodsList = new ArrayList<>();
    public static final  int PADEVENT00001 =00001;//刷新计算设置购物车总价
    public static final  int PADEVENT00002 =00002;//实时当前更新时间
    public static final  int PADEVENT00003 =00003;//实时当前更新时间
    public static  String  SELECTSALE="svc.select.sale.commonly";//查询销售清单
    public static final  int HANDLERLOCK = 100001;//解锁密码



    public static Map<String,Boolean>  goodsMap = new HashMap<String ,Boolean>();
    public static Map<String,Boolean> storeMap = new HashMap<String ,Boolean>();
    public static  boolean  isSubAdd=false;
    public static int carNumber=0;//购物车商品个数
    public static final  int GETCODEMSG = 1001;//获取短信验证码成功
    public static final  int ERRORCODE = 4040;//非正常数据返回码
    public static final  int UPDATEVER = 1002;//版本升级返回码

    public static final  int EVENT555 =555;//地址列表接收
    public static final  int EVENT1 =1;//刷新个人中心
    public static final  int EVENT10002 =10002;//刷新常用清单采购列表
    public static final  int EVENT10003 =10003;//刷新首页常用清单采购列表
    public static final  int EVENT10006 =10006;//购物车设置价格
    public static final  int EVENT10007 =10007;//修改商品详情金额
    public static final  int EVENT100011 =100011;//释放finish activity  回到制定界面
    public static final  int EVENT100019 =100019;//设置webview标题
    public static final  int EVENT100020 =100020;//设置调用h5方法传参数
    public static final  int EVENT100021 =100021;//购车数据变动通知H5界面刷新
    public static final  int EVENT100024 =100024;//修改购物车按钮 是否免运费
    public static final  int EVENT100026 =100026;//刷新清单 商品列表 加入和减少购物车




    public static boolean isDelCarGoods= false; //true 显示删除按钮  false 显示结算按钮


    public static final String  ACTIVITYURL = "http://h5.xianhao365.com/views/topic.html?id=";
    public static final  String  MYNEWDEMAND ="http://h5.xianhao365.com/views/demand.html";//新品需求
    public static final  String  MYINVOICE = "http://h5.xianhao365.com/views/invoice.html";//我的发票
    public static final  String  MYFOOT="http://h5.xianhao365.com/views/footprint.html";//我的足迹
    public static final  String  FAQ = "http://h5.xianhao365.com/views/helpcenter/index.html";//帮助中心
    public static final  String  SERVICECENTER = "http://h5.xianhao365.com/views/servicecenter/index.html";//服务中心
    public static final  String  FEEDBACK = "http://h5.xianhao365.com/views/feedback.html";//意见反馈
    public static final  String  MESSAGE = "http://h5.xianhao365.com/views/messagecenter/index.html";
    public static final  String  SERVICERULE="http://h5.xianhao365.com/views/servicecenter/termsofservice.html";//服务条款

    public static  String PayTag="1";//支付标识
    /**
     * EventBus 返回接收码说明
     */
    public static final  int COUPONS_RETRURN=3001;//提交订单进入优惠券选择返回劵couponsNo
    public static  String iv="";
    public static  String SecretKey="";
    public static  boolean LOGOUT=false; //用于标识是否刚刚点击退出，来判断用户再次登录显示相关界面
    public static String USERNAME = "";
    public static String USERPHONE= "";
    public static String USER_ID = "";//用户ID
    public static String USER_SESSION = ""; //用户会话
    public static String CUST_TELEPHONE="4000131419";//我的 客服电话号码
    public static final String CACHDATAPATH ="/sx/cacheData";
//    public static  int  WIDTH;
//    public static  int  HEIGHT;
    public static String  CarJSONInfo="";//购物车数据，用于js调用传给js
    public static String WCAPPID="";//微信appid
    /**
     *  用户登录不同角色区分
     *  32 商户摊主
     *  64 个人用户
     *  用户标签，1:后台用户,2:城市采购中心,4:供应商,8:联创中心,16:合伙人,32:摊主店铺,64:消费者,128:供应商司机,256:采购中心司机
     */
    public static String USERROLETAG = "";//tag 用户类型
    /**
     *
     * 用户相关接口
     */
    public static String FORGET_PSD1 = "svc.forget.password1";//忘记密码步骤1
    public static String FORGET_PSD2 = "svc.forget.password2";//忘记密码步骤2
    public static String USER_LOGIN =  "svc.sign.in";//		用户登录
    public static String USER_REGIST = "svc.sign.up";	//	用户注册
    public static String USER_LOGINOUAt = "svc.sign.out";//		用户登出
    public static String GET_TOKEN = "svc.pull.token";//		拉取令牌
    public static String USER_INFO="svc.get.user.info";	//获取用户信息
    public static String USER_ISPPLY_NFO="svc.serch.supply.info";	//获取供货商用户信息
    public static String USER_NUM_NFO="svc.user.center.render";//获取我的中 用户的订单数量
    public static String USER_REMIND="svc.order.remind";//用户订单列表提醒发货
    public static String USER_CANCEL_ORDER="svc.order.cancel";//用户取消订单
    public static String  UPDATE_LOGINPSD="svn.update.user.password";//修改登录密码
    /**
     * 普通消费者
     */
    public static String  USER_CONFRIM_GET="svc.consumer.order.confirm";//普通消费者确认收货
    public static String  USER_ORDER_LIST="svc.consumer.order.list";//普通消费订单列表

    public static String HOME_DATA="svc.page.home";//首页数据
//收货地址
    public static String ADDRESS_ADD="svc.add.shipping.address";//		添加收货地址
    public static String ADDRESS_UPDATE="svc.mod.shipping.address";//	修改收货地址
    public static String ADDRESS_DEL="svc.del.shipping.address";//		删除收货地址
    public static String ADDRESS_SELECT="svc.my.shipping.address";//		我的收货地址
    //优惠券劵

    public static String COUPONS_USE="svc.usable.coupons";//未使用优惠券
    public static String COUPONS_NOUSE="svc.used.coupons";//已使用优惠券
    public static String COUPONS_PASSUSE="svc.expired.coupons";//已过期

    /**
     *订单相关接口
     */
    public static String ORDER_FORM="svc.order.form";//订单结算
    public static String ORDER_SUBMIT = "svc.submit.order";//提交订单
    public static String  ORDER_LIST= "svc.order.list";//订单列表
    public static String  ORDER_DETAIL= "svc.order.detail";//订单详情

    public static String  UPDATE_USER_INFO = "svc.update.user.base.info";//修改用户基本信息
    public static String  UPDATE_PASSWORD="";//修改用户密码信息
    public static String  UPDATEPAYPSD="svc.user.wallet.updatepwd";//修改支付密码
    public static String  SETPAYPSD="svc.user.wallet.setpwd";//设置支付密码
    public static String USER_ORDERS="svc.get.user.order.info";//获取用户订单信息

    public static String  GYS_BILLLIST ="svc.purchase.mypurchaselist";//供应商的采购单列表
    public static String  GYS_CONFIRM_PURCHASE ="svc.purchase.confirmPurchase";//供应商确认采购订单
    //钱包相关接口
    public static String  MYTRADELOG="svc.user.list.mytradelog";//我的钱包-收入明细
    public static String  USER_WALLET="svc.get.user.wallet.info";//获取用户钱包信息
    public static String  WITHDRAWAPPLY="svc.user.withdraw.apply";//提现申请
    public static String  ADDBANKCARD="svc.user.wallet.addcard";//添加银行卡
    public static String  UPDATE_STORE_INFO="svc.user.add.shop";//添加修改获取门店信息
    public static String  ADD_STORE_INFO="svc.user.shop";//首次店主注册添加店铺信息
    public static String  GET_BANKLIST="svc.user.list.bank";//获取开户行
    public static String  GET_CITYAREA="svc.user.area";//获取省市区

    public static String  GYS_CPURCHASE_DELIVER ="svc.purchase.deliverGoods";//供应商采购单发货

    public static String GET_CODEMSG = "svc.pull.sms";//拉取短信
    public static String APP_UPDATE="svc.check.update";//app版本更新
    public static String APP_LAUNCH="svc.launch.image";//启动广告图
    public static String APP_SWIPER="svc.swiper";//轮播广告图
    public static String STORE_LOCATION="svc.shop.location";//查询门店地理位置
    public static String HOTSEARCH = "svc.search.word";//热门搜索词汇 svc.search.word --svc.search.keyword
    //商品信息相关接口
    public static String GOODS_DETAIL="svc.goods.ById";//商品详情
    public static String GOODS_TYPE="svc.cat.list";//商品分类
    public static String GOODS_LIST="svc.goods.list";//商品列表 根据参数进行分类查询
    public static String GOODS_FEEDBACK="svc.add.feedback";//商品意见反馈
    public static String GOODS_ADDCOMMON="svc.add.common";
    public static String GOODS_DELDCOMMON="svc.delete.common";

     //常用清单
    public static String  COMMONBILL="svc.common.goods.list";
    //购物车
    public static String CARLIST = "svc.shoppingcart.get";//查询购物车
    public static String CARADDUPDATA="svc.shoppingcart.update";//增加和删除购物车
    public static String CLEARCAR="svc.shoppingcart.empty";//清空购物车
    //摊主相关接口
    public static String TZ_ORDER_LIST="svc.shopowner.order.list";//摊主订单列表查询
    public static String ORDER_CONFIRM="svc.shopowner.order.confirm";//确认订单到货
    public static String STOCK_SUBMIT="svc.order.lessGoodsApply";//摊主缺货少货上报
    //合伙人
    public static String PARTNER_ORDER_LIST="svc.lianchuang.order.list";//联创中心订单查询
    public static String HHR_ORDER_LIST="svc.partner.order.list";//合伙人订单列表
    public static String  SELECT_SCAN_SEND="svc.deliveryBill.detail";//查询合伙人扫码提货
    public static String  SCAN_CONFIRM="svc.partner.order.confirm";//确认批量收货
    public static String  PARTNER_ORDER_DETAIL="svc.partner.order.goodslist";//合伙人订单详情
    public static String CHECKBOXCAR="svc.shoppingcart.check";//选择购物车商品 取消选中调用接口

    public static String headImg  = "http://e.hiphotos.baidu.com/zhidao/wh%3D450%2C600/sign=0197b59000087bf47db95fedc7e37b1a/38dbb6fd5266d016152614f3952bd40735fa3529.jpg";
     //生成支付流程接口
    public static String PAY = "svc.settlement.order";//支付接口
    public static String RECHARGE_PAY ="svc.recharge.create";//账号充值接口


    public static int  fullWidth; //屏幕宽，高
    public static int  fullHigh;
    // 返回表示跳转指定目录
    public static boolean TAG1=false;//首页
    public static boolean TAG2=false;//所有菜品
    public static boolean TAG3=false;//购物清单
    public static boolean TAG4=false;//购物车
    public static boolean TAG5=false;//我的
//    public static final String CACHDATAPATH = getSDPath() + "/json/cacheData";//缓存json数据路径
    public static String MAINBANNER = "/main.txt";//首页广告内容缓存文件名

    public static Map<String,String> carSKUNumMap=new HashMap<>();//保存购物车 商品及数量

}