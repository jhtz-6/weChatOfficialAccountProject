package org.myf.wechatofficialaccountproject.infrastructure.util.client;

import com.qcloud.cos.exception.CosClientException;
import com.qcloud.cos.exception.CosServiceException;
import com.qcloud.cos.model.*;
import com.qcloud.cos.transfer.TransferManager;
import com.qcloud.cos.transfer.TransferManagerConfiguration;
import com.qcloud.cos.transfer.Upload;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.CommonUtil;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.InitData;
import org.myf.wechatofficialaccountproject.infrastructure.util.helper.WeChatUtil;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: myf
 * @CreateTime: 2023-06-22 16:14
 * @Description: TencentCosClient
 */
@Component
public class TencentCosClient {

    private static final ExecutorService threadPool = Executors.newFixedThreadPool(16);

    private static TransferManager createTransferManager() {
        TransferManager transferManager = new TransferManager(InitData.COS_CLIENT, threadPool);
        TransferManagerConfiguration transferManagerConfiguration = new TransferManagerConfiguration();
        transferManagerConfiguration.setMultipartUploadThreshold(5 * 1024 * 1024);
        transferManagerConfiguration.setMinimumUploadPartSize(1 * 1024 * 1024);
        transferManager.setConfiguration(transferManagerConfiguration);
        return transferManager;
    }

    public String uploadByImageUrl(String imageUrl) {
        String tencentCosImageUrl = "";
        TransferManager transferManager = createTransferManager();
        InputStream inputStream = null;
        try {
            // 对象键(Key)是对象在存储桶中的唯一标识。
            String key = getKey();
            ObjectMetadata objectMetadata = new ObjectMetadata();
            URL url = new URL(imageUrl);
            URLConnection connection = url.openConnection();
            inputStream = connection.getInputStream();
            PutObjectRequest putObjectRequest = new PutObjectRequest(
                WeChatUtil.CONFIGURATION_MAP.get(WeChatUtil.TENCENT_COS_BUCKET_NAME), key, inputStream, objectMetadata);
            putObjectRequest.setStorageClass(StorageClass.Standard_IA);
            Upload upload = transferManager.upload(putObjectRequest);
            upload.waitForUploadResult();
            tencentCosImageUrl = InitData.TENCENT_PHOTO_DOMAIN_NAME + key;
        } catch (CosServiceException e) {
            e.printStackTrace();
        } catch (CosClientException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            shutdownTransferManager(transferManager);
            if (Objects.nonNull(inputStream)) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return tencentCosImageUrl;
    }

    private static void shutdownTransferManager(TransferManager transferManager) {
        if (Objects.nonNull(transferManager)) {
            transferManager.shutdownNow(true);
        }
    }

    private static String getKey() {
        String time = String.valueOf(System.currentTimeMillis());
        return time + CommonUtil.getRandomNumber(0, 20) + ".jpg";
    }
}
