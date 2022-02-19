package com.mycompany.myapp.license;

import com.adelelawady.konsol_license.LicenseCheckModel;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.ComputerSystem;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.OperatingSystem;

/**
 * 用于获取客户服务器的基本信息，如：IP、Mac地址、CPU序列号、主板序列号等
 *
 * @author zifangsky
 * @date 2018/4/23
 * @since 1.0.0
 */
public class AbstractServerInfos {

    private static Logger logger = LogManager.getLogger(AbstractServerInfos.class);

    /**
     * 组装需要额外校验的License参数
     * @author zifangsky
     * @date 2018/4/23 14:23
     * @since 1.0.0
     * @return demo.LicenseCheckModel
     */
    public LicenseCheckModel getServerInfos() {
        LicenseCheckModel result = new LicenseCheckModel();

        try {
            SystemInfo systemInfo = new SystemInfo();
            OperatingSystem operatingSystem = systemInfo.getOperatingSystem();
            HardwareAbstractionLayer hardwareAbstractionLayer = systemInfo.getHardware();
            CentralProcessor centralProcessor = hardwareAbstractionLayer.getProcessor();
            ComputerSystem computerSystem = hardwareAbstractionLayer.getComputerSystem();

            String vendor = operatingSystem.getManufacturer();
            String processorSerialNumber = computerSystem.getSerialNumber();
            String processorIdentifier = centralProcessor.getIdentifier();
            int processors = centralProcessor.getLogicalProcessorCount();

            result.setVendor(vendor);
            result.setProcessorSerialNumber(processorSerialNumber);
            result.setProcessorIdentifier(processorIdentifier);
            result.setProcessors(String.valueOf(processors));
        } catch (Exception e) {
            logger.error("获取服务器硬件信息失败", e);
        }

        return result;
    }
}
