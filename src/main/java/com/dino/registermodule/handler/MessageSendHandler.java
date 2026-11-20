package com.dino.registermodule.handler;

import com.google.common.net.HttpHeaders;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 短信网关调用处理器：封装对第三方短信平台的 HTTPS 调用。
 *
 * <p>负责拼装表单请求体（from/to/templateId/templateParas/statusCallback）与 WSSE
 * UsernameToken 鉴权头，并以 POST 方式请求短信批量发送接口。</p>
 *
 * Created by RunYee on 2018/6/28.
 */
@Component
public class MessageSendHandler {
    /** WSSE 鉴权头格式：依次填入用户名、密码摘要、随机数与 UTC 创建时间 */
    private static final String WSSE_HEADER_FORMAT = "UsernameToken Username=\"%s\",PasswordDigest=\"%s\",Nonce=\"%s\",Created=\"%s\"";

    /** Authorization 请求头固定值，声明使用 Appkey 方式的 UsernameToken 鉴权 */
    private static final String AUTH_HEADER_VALUE = "WSSE realm=\"SDP\",profile=\"UsernameToken\",type=\"Appkey\"";

    /**
     * 向短信网关发送一条短信。
     *
     * @param paramMap 短信参数，需包含 phone（接收号码）与 code（写入模板的变量值）
     * @throws Exception 构建鉴权头或调用短信网关过程中发生异常时抛出
     */
    public void msgSend(Map<String,String> paramMap) throws Exception{

        //请从应用管理页面获取APP接入地址，替换url中的ip地址和端口
        String url = "https://117.78.29.66:10443/sms/batchSendSms/v1";
        //请从应用管理页面获取APP_Key和APP_Secret进行替换
        String appKey = "RZG6t7TkvXn3l2GPpz5lzGICy718";
        String appSecret = "1ja74udMhF4v7cDAXJCoMpxZ29Cd";

        //填写短信签名中的通道号，请从签名管理页面获取
        String sender = "csms18062905";
        //填写短信接收人号码
        String receiver = paramMap.get("phone");
        //状态报告接收地址，为空或者不填表示不接收状态报告
        String statusCallBack = "";

        //请从模板管理页面获取模板ID进行替换
        String templateId = "72312cb3031f4a95810b120810f45548";
        //模板变量请务必根据实际情况修改，查看更多模板变量规则
        //如模板内容为“您有${NUM_2}件快递请到${TXT_32}领取”时，templateParas可填写为[\"3\",\"人民公园正门\"]
        //双变量示例：String templateParas = "[\"3\",\"人民公园正门\"]";
        String templateParas = "["+paramMap.get("code")+"]";

        String body = buildRequestBody(sender, receiver, templateId, templateParas, statusCallBack);
        System.out.println("body is " + body);

        String wsseHeader = buildWsseHeader(appKey, appSecret);
        System.out.println("wsse header is " + wsseHeader);

        //如果JDK版本是1.8，可使用如下代码
        CloseableHttpClient client = HttpClients.custom()
                .setSSLContext(new SSLContextBuilder().loadTrustMaterial(null,
                        (x509CertChain, authType) -> true).build())
                .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                .build();

        HttpResponse response = client.execute(RequestBuilder.create("POST")
                .setUri(url)
                .addHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
                .addHeader(HttpHeaders.AUTHORIZATION, AUTH_HEADER_VALUE)
                .addHeader("X-WSSE", wsseHeader)
                .setEntity(new StringEntity(body)).build());

        System.out.println(response.toString());
        System.out.println(EntityUtils.toString(response.getEntity()));

    }

    /**
     * 将短信参数拼装为 application/x-www-form-urlencoded 格式的请求体。
     *
     * @param sender           短信签名通道号（from）
     * @param receiver         短信接收号码（to）
     * @param templateId       短信模板 ID
     * @param templateParas    模板变量 JSON 字符串
     * @param statusCallbackUrl 状态报告接收地址，空字符串表示不接收状态报告
     * @return 按 UTF-8 编码后的表单请求体字符串
     */
    static String buildRequestBody(String sender, String receiver, String templateId, String templateParas,
                                   String statusCallbackUrl) {

        List<BasicNameValuePair> keyValues = new ArrayList<>();

        keyValues.add(new BasicNameValuePair("from", sender));
        keyValues.add(new BasicNameValuePair("to", receiver));
        keyValues.add(new BasicNameValuePair("templateId", templateId));
        keyValues.add(new BasicNameValuePair("templateParas", templateParas));
        keyValues.add(new BasicNameValuePair("statusCallback", statusCallbackUrl));

        return URLEncodedUtils.format(keyValues, StandardCharsets.UTF_8);
    }

    /**
     * 构造 WSSE UsernameToken 鉴权头。
     *
     * <p>用 UUID 生成随机数 nonce，将 nonce + 当前时间 + appSecret 做 SHA-256 后转为十六进制，
     * 再进行 Base64 编码得到 PasswordDigest，最后套用 {@link #WSSE_HEADER_FORMAT}。</p>
     *
     * @param appKey    应用 Key
     * @param appSecret 应用密钥
     * @return WSSE 鉴权头字符串
     */
    static String buildWsseHeader(String appKey, String appSecret) {
        SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd'T'HH:mm:ss'Z'");
        String time = sdf.format(new Date());
        String nonce = UUID.randomUUID().toString().replace("-", "");

        byte[] passwordDigest = DigestUtils.sha256(nonce + time + appSecret);
        String hexDigest = Hex.encodeHexString(passwordDigest);
        String passwordDigestBase64Str = org.apache.commons.codec.binary.Base64.encodeBase64String(hexDigest.getBytes(Charset.forName("utf-8")));
        return String.format(WSSE_HEADER_FORMAT, appKey, passwordDigestBase64Str, nonce, time);
    }
}
