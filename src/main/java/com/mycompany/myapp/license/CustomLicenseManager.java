package com.mycompany.myapp.license;

import com.adelelawady.konsol_license.LicenseCheckModel;
import de.schlichtherle.license.LicenseContent;
import de.schlichtherle.license.LicenseContentException;
import de.schlichtherle.license.LicenseManager;
import de.schlichtherle.license.LicenseNotary;
import de.schlichtherle.license.LicenseParam;
import de.schlichtherle.license.NoLicenseInstalledException;
import de.schlichtherle.xml.GenericCertificate;
import java.beans.XMLDecoder;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 自定义LicenseManager，用于增加额外的服务器硬件信息校验
 *
 * @author zifangsky
 * @date 2018/4/23
 * @since 1.0.0
 */
public class CustomLicenseManager extends LicenseManager {

    private static Logger logger = LogManager.getLogger(CustomLicenseManager.class);

    //XML编码
    private static final String XML_CHARSET = "UTF-8";
    //默认BUFSIZE
    private static final int DEFAULT_BUFSIZE = 8 * 1024;

    public CustomLicenseManager() {}

    public CustomLicenseManager(LicenseParam param) {
        super(param);
    }

    /**
     * 复写create方法
     * @author zifangsky
     * @date 2018/4/23 10:36
     * @since 1.0.0
     * @param
     * @return byte[]
     */
    @Override
    protected synchronized byte[] create(LicenseContent content, LicenseNotary notary) throws Exception {
        initialize(content);
        this.validateCreate(content);
        final GenericCertificate certificate = notary.sign(content);
        return getPrivacyGuard().cert2key(certificate);
    }

    /**
     * 复写install方法，其中validate方法调用本类中的validate方法，校验IP地址、Mac地址等其他信息
     * @author zifangsky
     * @date 2018/4/23 10:40
     * @since 1.0.0
     * @param
     * @return de.schlichtherle.license.LicenseContent
     */
    @Override
    protected synchronized LicenseContent install(final byte[] key, final LicenseNotary notary) throws Exception {
        final GenericCertificate certificate = getPrivacyGuard().key2cert(key);

        notary.verify(certificate);
        final LicenseContent content = (LicenseContent) this.load(certificate.getEncoded());
        this.validate(content);
        setLicenseKey(key);
        setCertificate(certificate);

        return content;
    }

    /**
     * 复写verify方法，调用本类中的validate方法，校验IP地址、Mac地址等其他信息
     * @author zifangsky
     * @date 2018/4/23 10:40
     * @since 1.0.0
     * @param
     * @return de.schlichtherle.license.LicenseContent
     */
    @Override
    protected synchronized LicenseContent verify(final LicenseNotary notary) throws Exception {
        GenericCertificate certificate = getCertificate();

        // Load license key from preferences,
        final byte[] key = getLicenseKey();
        if (null == key) {
            throw new NoLicenseInstalledException(getLicenseParam().getSubject());
        }

        certificate = getPrivacyGuard().key2cert(key);
        notary.verify(certificate);
        final LicenseContent content = (LicenseContent) this.load(certificate.getEncoded());
        this.validate(content);
        setCertificate(certificate);

        return content;
    }

    /**
     * 校验生成证书的参数信息
     * @author zifangsky
     * @date 2018/5/2 15:43
     * @since 1.0.0
     * @param content 证书正文
     */
    protected synchronized void validateCreate(final LicenseContent content) throws LicenseContentException {
        final LicenseParam param = getLicenseParam();

        final Date now = new Date();
        final Date notBefore = content.getNotBefore();
        final Date notAfter = content.getNotAfter();
        if (null != notAfter && now.after(notAfter)) {
            throw new LicenseContentException("证书失效时间不能早于当前时间");
        }
        if (null != notBefore && null != notAfter && notAfter.before(notBefore)) {
            throw new LicenseContentException("证书生效时间不能晚于证书失效时间");
        }
        final String consumerType = content.getConsumerType();
        if (null == consumerType) {
            throw new LicenseContentException("用户类型不能为空");
        }
    }

    /**
     * 复写validate方法，增加IP地址、Mac地址等其他信息校验
     * @author zifangsky
     * @date 2018/4/23 10:40
     * @since 1.0.0
     * @param content LicenseContent
     */
    @Override
    protected synchronized void validate(final LicenseContent content) throws LicenseContentException {
        //1. 首先调用父类的validate方法
        super.validate(content);

        LicenseCheckModel expectedCheckModel = (LicenseCheckModel) content.getExtra();
        LicenseCheckModel serverCheckModel = getServerInfos();

        if (expectedCheckModel != null && serverCheckModel != null) {
            //校验IP地址

            boolean validation =
                expectedCheckModel.getVendor().equals(serverCheckModel.getVendor()) &&
                expectedCheckModel.getProcessorIdentifier().equals(serverCheckModel.getProcessorIdentifier()) &&
                expectedCheckModel.getProcessorSerialNumber().equals(serverCheckModel.getProcessorSerialNumber()) &&
                expectedCheckModel.getProcessors().equals(serverCheckModel.getProcessors());

            if (!validation) {
                throw new LicenseContentException("License Content Validation Failed");
            }
        } else {
            throw new LicenseContentException("Unable to get server hardware information");
        }
    }

    /**
     * 重写XMLDecoder解析XML
     * @author zifangsky
     * @date 2018/4/25 14:02
     * @since 1.0.0
     * @param encoded XML类型字符串
     * @return java.lang.Object
     */
    private Object load(String encoded) {
        BufferedInputStream inputStream = null;
        XMLDecoder decoder = null;
        try {
            inputStream = new BufferedInputStream(new ByteArrayInputStream(encoded.getBytes(XML_CHARSET)));

            decoder = new XMLDecoder(new BufferedInputStream(inputStream, DEFAULT_BUFSIZE), null, null);

            return decoder.readObject();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } finally {
            try {
                if (decoder != null) {
                    decoder.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (Exception e) {
                logger.error("XMLDecoder解析XML失败", e);
            }
        }

        return null;
    }

    /**
     * 获取当前服务器需要额外校验的License参数
     * @author zifangsky
     * @date 2018/4/23 14:33
     * @since 1.0.0
     * @return demo.LicenseCheckModel
     */
    private LicenseCheckModel getServerInfos() {
        AbstractServerInfos abstractServerInfos = new AbstractServerInfos();
        return abstractServerInfos.getServerInfos();
    }
}
