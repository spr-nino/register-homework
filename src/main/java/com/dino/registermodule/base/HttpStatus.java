package com.dino.registermodule.base;

/**
 * HTTP 状态码常量集合，以字符串形式集中定义标准 HTTP 状态码（1xx 至 5xx）。
 * <p>各常量值对应 IANA 注册的状态码，便于在响应中直接使用字符串形式的状态码。
 *
 * @see <a href="http://www.iana.org/assignments/http-status-codes">HTTP Status Code Registry</a>
 * @see <a href="http://en.wikipedia.org/wiki/List_of_HTTP_status_codes">List of HTTP status codes - Wikipedia</a>
 */
public final class HttpStatus {
    /**
     * Continue，继续：客户端可继续发送请求体
     */
    public static String CONTINUE = "100";
    /**
     * Switching Protocols，切换协议：服务端按客户端请求切换协议
     */
    public static String SWITCHING_PROTOCOLS = "101";
    /**
     * Processing，处理中：服务端已收到请求但尚未完成处理
     */
    public static String PROCESSING = "102";
    /**
     * Checkpoint，检查点：WebDAV 扩展状态码
     */
    public static String CHECKPOINT = "103";
    /**
     * OK，请求成功
     */
    public static String OK = "200";
    /**
     * Created，已创建：请求成功并创建了新资源
     */
    public static String CREATED = "201";
    /**
     * Accepted，已接受：请求已被接受但尚未处理完成
     */
    public static String ACCEPTED = "202";
    /**
     * Non Authoritative Information，非权威信息：返回的信息可能来自缓存或副本
     */
    public static String NON_AUTHORITATIVE_INFORMATION = "203";
    /**
     * No Content，无内容：请求成功但响应中没有实体内容
     */
    public static String NO_CONTENT = "204";
    /**
     * Reset Content，重置内容：客户端应重置当前页面表单
     */
    public static String RESET_CONTENT = "205";
    /**
     * Partial Content，部分内容：服务端只返回请求范围内的部分数据
     */
    public static String PARTIAL_CONTENT = "206";
    /**
     * Multi-Status，多状态：WebDAV 扩展状态码，返回多个操作的结果
     */
    public static String MULTI_STATUS = "207";
    /**
     * Already Reported，已报告：WebDAV 扩展状态码，成员资源的状态已在前序响应中报告
     */
    public static String ALREADY_REPORTED = "208";
    /**
     * IM Used，IM 已使用：HTTP 增量编码扩展状态码
     */
    public static String IM_USED = "226";
    /**
     * Multiple Choices，多种选择：资源存在多个可选表示
     */
    public static String MULTIPLE_CHOICES = "300";
    /**
     * Moved Permanently，永久移动：资源已分配到新的永久 URI
     */
    public static String MOVED_PERMANENTLY = "301";
    /**
     * Found，临时移动：资源临时位于其他 URI
     */
    public static String FOUND = "302";
    /**
     * See Other，查看其他位置：响应指向另一个 URI，应使用 GET 获取
     */
    public static String SEE_OTHER = "303";
    /**
     * Not Modified，未修改：资源自上次请求后未发生变化，可使用缓存
     */
    public static String NOT_MODIFIED = "304";
    /**
     * Temporary Redirect，临时重定向：资源临时位于其他 URI，客户端应保持原请求方法
     */
    public static String TEMPORARY_REDIRECT = "307";
    /**
     * Permanent Redirect，永久重定向：资源永久位于其他 URI，客户端应使用新 URI
     */
    public static String PERMANENT_REDIRECT = "308";
    /**
     * Bad Request，请求错误：请求语法有误，服务端无法理解
     */
    public static String BAD_REQUEST = "400";
    /**
     * Unauthorized，未授权：请求缺少有效的身份验证凭据
     */
    public static String UNAUTHORIZED = "401";
    /**
     * Payment Required，需要付款：保留状态码，通常用于付费场景
     */
    public static String PAYMENT_REQUIRED = "402";
    /**
     * Forbidden，禁止访问：服务端拒绝执行请求
     */
    public static String FORBIDDEN = "403";
    /**
     * Not Found，未找到：请求的资源不存在
     */
    public static String NOT_FOUND = "404";
    /**
     * Method Not Allowed，方法不允许：资源不支持请求使用的 HTTP 方法
     */
    public static String METHOD_NOT_ALLOWED = "405";
    /**
     * Not Acceptable，不可接受：服务端无法生成客户端可接受的响应内容
     */
    public static String NOT_ACCEPTABLE = "406";
    /**
     * Proxy Authentication Required，需要代理身份验证：客户端需先通过代理认证
     */
    public static String PROXY_AUTHENTICATION_REQUIRED = "407";
    /**
     * Request Timeout，请求超时：客户端未在服务端等待时间内完成请求发送
     */
    public static String REQUEST_TIMEOUT = "408";
    /**
     * Conflict，冲突：请求与资源当前状态发生冲突
     */
    public static String CONFLICT = "409";
    /**
     * Gone，已失效：请求的资源已永久删除且没有可用地址
     */
    public static String GONE = "410";
    /**
     * Length Required，需要有效长度：请求必须携带 Content-Length 头
     */
    public static String LENGTH_REQUIRED = "411";
    /**
     * Precondition Failed，前提条件失败：请求头中的前置条件校验未通过
     */
    public static String PRECONDITION_FAILED = "412";
    /**
     * Payload Too Large，请求实体过大：请求体超过服务端处理上限
     */
    public static String PAYLOAD_TOO_LARGE = "413";
    /**
     * URI Too Long，URI 过长：请求 URI 超过服务端可解析长度
     */
    public static String URI_TOO_LONG = "414";
    /**
     * Unsupported Media Type，不支持的媒体类型：请求实体的格式不受服务端支持
     */
    public static String UNSUPPORTED_MEDIA_TYPE = "415";
    /**
     * Requested range not satisfiable，请求范围不满足：请求指定的 Range 超出资源可用范围
     */
    public static String REQUESTED_RANGE_NOT_SATISFIABLE = "416";
    /**
     * Expectation Failed，期望失败：服务端无法满足 Expect 请求头中的期望
     */
    public static String EXPECTATION_FAILED = "417";
    /**
     * I'm a teapot，我是茶壶：源于 RFC 2324 的玩笑状态码，表示无法冲泡咖啡
     */
    public static String I_AM_A_TEAPOT = "418";
    /**
     * Unprocessable Entity，无法处理的实体：请求格式正确但语义有误，常见于表单校验失败
     */
    public static String UNPROCESSABLE_ENTITY = "422";
    /**
     * Locked，已锁定：目标资源当前被锁定
     */
    public static String LOCKED = "423";
    /**
     * Failed Dependency，依赖失败：请求失败是因为其依赖的另一个请求失败
     */
    public static String FAILED_DEPENDENCY = "424";
    /**
     * Upgrade Required，需要升级：客户端需切换到其他协议才能继续
     */
    public static String UPGRADE_REQUIRED = "426";
    /**
     * Precondition Required，需要前提条件：请求必须携带前置条件头字段
     */
    public static String PRECONDITION_REQUIRED = "428";
    /**
     * Too Many Requests，请求过多：客户端在给定时间内发送了过多请求
     */
    public static String TOO_MANY_REQUESTS = "429";
    /**
     * Request Header Fields Too Large，请求头字段过大：请求头总长度或单个字段超过限制
     */
    public static String REQUEST_HEADER_FIELDS_TOO_LARGE = "431";
    /**
     * Unavailable For Legal Reasons，因法律原因不可用：资源因法律要求而被限制访问
     */
    public static String UNAVAILABLE_FOR_LEGAL_REASONS = "451";
    /**
     * Internal Server Error，服务器内部错误：服务端处理请求时发生未知错误
     */
    public static String INTERNAL_SERVER_ERROR = "500";
    /**
     * Not Implemented，尚未实现：服务端不支持请求所需的功能
     */
    public static String NOT_IMPLEMENTED = "501";
    /**
     * Bad Gateway，错误网关：作为网关或代理的服务端从上游收到无效响应
     */
    public static String BAD_GATEWAY = "502";
    /**
     * Service Unavailable，服务不可用：服务端暂时无法处理请求，通常因过载或维护
     */
    public static String SERVICE_UNAVAILABLE = "503";
    /**
     * Gateway Timeout，网关超时：网关或代理等待上游响应超时
     */
    public static String GATEWAY_TIMEOUT = "504";
    /**
     * HTTP Version not supported，HTTP 版本不支持：服务端不支持请求所用的 HTTP 版本
     */
    public static String HTTP_VERSION_NOT_SUPPORTED = "505";
    /**
     * Variant Also Negotiates，变体也会协商：透明内容协商出现循环
     */
    public static String VARIANT_ALSO_NEGOTIATES = "506";
    /**
     * Insufficient Storage，存储空间不足：服务端无法存储完成请求所需的数据
     */
    public static String INSUFFICIENT_STORAGE = "507";
    /**
     * Loop Detected，检测到循环：WebDAV 扩展状态码，处理过程中检测到无限循环
     */
    public static String LOOP_DETECTED = "508";
    /**
     * Bandwidth Limit Exceeded，带宽超出限制：服务端带宽用量超过配额
     */
    public static String BANDWIDTH_LIMIT_EXCEEDED = "509";
    /**
     * Not Extended，未扩展：请求缺少服务端所需的扩展声明
     */
    public static String NOT_EXTENDED = "510";
    /**
     * Network Authentication Required，需要网络认证：客户端需先在网络网关处完成认证
     */
    public static String NETWORK_AUTHENTICATION_REQUIRED = "511";

}
