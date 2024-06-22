## Payment 
Payment是对接第三方支付服务的系统，完成收单和退款功能。支持灵活扩展第三方支付渠道，支持分布式部署。
目前已经接入微信、支付宝支付渠道。
## 模块介绍
| 模块名称  | 解释 |
| ------------- | ------------- |
| payment-api  | 接口模块，dubbo服务的API  |
| payment-archive  | 支付数据归档，非必要服务。需独立部署，依赖mongoDB。如果没有数据归档需求，无需部署  |
| payment-common  | 通用包模块，共用model、常量和工具类  |
| payment-sdk-imp  | 第三方接口访问实现，微信、支付宝等  |
| payment-server | 支付server服务，必要服务，需独立部署  |
| payment-web | 支付Web服务，必要服务，需独立部署  |

## 快速开始
#### 环境准备
- jdk21+
- zookeeper 3.7+
- kafka 2.13+
- mysql 5.7+
- mongoDB 7.0+ (如果没有数据归档需求，无需部署)
#### 如何部署
1. 下载项目代码
```
git clone https://github.com/shaozhiwei1989/Payment.git
```
2. 修改payment-server、payment-web、payment-archive模块的yml文件
3. 初始化数据库，执行payment-server/resources/database/init.sql
4. 项目打包
```
cd Payment/
mvn clean package -Dmaven.test.skip=true -U
```
5. 启动服务
```
 java -jar payment-server.jar &
 java -jar payment-web.jar &
 java -jar payment-archive.jar &  (如果没有数据归档需求，无需部署)
```
## 如何接入第三方支付
Payment支持灵活扩展第三方支付渠道，只需4步即可完成。
1. 在payment-sdk-imp模块中建立实现类，实现com.szw.payment.sdk.Pay接口
```
public interface Pay {

	/**
	 * 初始化配置
	 */
	void initConfig(ConfigInfo configInfo) throws Exception;

	/**
	 * 预支付
	 */
	PrepayResponse prepay(Prepay prepay);

	/**
	 * 创建退款
	 */
	RefundCreateResponse createRefund(Refund refund);

	/**
	 * 查询支付订单
	 */
	PayOrderQueryResponse queryPayOrder(Prepay prepay);

	/**
	 * 查询退款订单
	 */
	RefundQueryResponse queryRefundOrder(Refund refund);

}
```
2. 配置映射文件 mapping.json
```
[
  {
    "channel": "wx_app",
    "apiVersion": "v3",
    "cls": "com.szw.payment.sdk.wxpay.WxAppPay",
    "desc": "微信App支付"
  },
  {
    "channel": "wx_mini",
    "apiVersion": "v3",
    "cls": "com.szw.payment.sdk.wxpay.WxMiniPay",
    "desc": "微信小程序支付"
  },
  {
    "channel": "alipay",
    "apiVersion": "1.0",
    "cls": "com.szw.payment.sdk.alipay.AliPay",
    "desc": "支付宝支付"
  },
  {
    "channel": "mock_pay",
    "apiVersion": "1.0",
    "cls": "com.szw.payment.sdk.mockpay.MockPay",
    "desc": "Mock支付"
  }
]
```
3. 新增枚举值 com.szw.payment.common.ChannelEnum
```
@Getter
@AllArgsConstructor
public enum ChannelEnum {
	WX_APP("wx_app", "1", "微信App支付"),

	WX_MINI("wx_mini", "2", "微信小程序支付"),

	ALIPAY("alipay", "3", "支付宝支付"),

	MOCK_PAY("mock_pay", "4", "mock支付"),
	;

	private final String code;
	private final String identifier;
	private final String desc;


	public static ChannelEnum fromCode(String code) {
		return Arrays
				.stream(values())
				.filter(o -> Objects.equals(o.code, code))
				.findFirst()
				.orElseThrow();
	}

}
```
4. 新增配置表记录 payment.config
```
INSERT INTO payment.config
( channel, app_id, mch_id, mch_serial_number, api_key, public_key, private_key, notify_url, refund_url, api_version, description, is_deleted)
VALUES('mock_pay', 'mock_app_id', 'mock_mch_id', NULL, NULL, NULL, NULL, NULL, NULL, '1.0', 'mock支付', '0');
```
## 联系我
mail: shaozhiwei1989@gmail.com
   


