package com.feiqu.framwork.support.store;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONObject;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.feiqu.common.config.Global;
import com.feiqu.common.enums.FileTypeEnum;
import com.feiqu.framwork.constant.CommonConstant;
import com.feiqu.system.pojo.response.FileItemDTO;
import com.jeesuite.filesystem.FileSystemClient;
import com.jeesuite.filesystem.UploadTokenParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by ZhangShuzheng on 2017/5/15.
 */
@Service
public class AliyunOssService {

    public static final char DELIMITER = '/';

    public static final String HTTP_PROTOCAL = "http://";

    public static final String HTTPS_PROTOCAL = "https://";

    private static final Logger LOGGER = LoggerFactory.getLogger(AliyunOssService.class);

    private AmazonS3 s3Client = null;

    private String basePath = "";

    /**
     * 移除 URL 中的第一个 '/'
     *
     * @return 如 path = '/folder1/file1', 返回 'folder1/file1'
     */
    public static String removeFirstSeparator(String path) {
        if (!"".equals(path) && path.charAt(0) == DELIMITER) {
            path = path.substring(1);
        }
        return path;
    }

    public String getFullPath(String path) {
        String basePath = ObjectUtil.defaultIfNull(this.basePath, "");
        path = ObjectUtil.defaultIfNull(path, "");
        return removeDuplicateSeparator(basePath + "/" + path);
    }

    public static String removeDuplicateSeparator(String path) {
        if (path == null || path.length() < 2) {
            return path;
        }

        StringBuilder sb = new StringBuilder();

        if (path.indexOf(HTTP_PROTOCAL) == 0) {
            sb.append(HTTP_PROTOCAL);
        } else if (path.indexOf(HTTPS_PROTOCAL) == 0) {
            sb.append(HTTPS_PROTOCAL);
        }

        for (int i = sb.length(); i < path.length() - 1; i++) {
            char current = path.charAt(i);
            char next = path.charAt(i + 1);
            if (!(current == DELIMITER && next == DELIMITER)) {
                sb.append(current);
            }
        }
        sb.append(path.charAt(path.length() - 1));
        return sb.toString();
    }

    public List<FileItemDTO> fileList(String path) {
        if (s3Client == null) {
            BasicAWSCredentials credentials = new BasicAWSCredentials(Global.getOssAccessKey(), Global.getOssSecretKey());
            s3Client = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(CommonConstant.ALIYUN_OSS_ENDPOINT, "oss")).build();
            ;
        }
//        移除 URL 中的第一个 '/'                * @return 如 path = '/folder1/file1', 返回 'folder1/file1'
        path = removeFirstSeparator(path);
        List<FileItemDTO> fileItemList = new ArrayList<>();
        ObjectListing objectListing = s3Client.listObjects(new ListObjectsRequest(CommonConstant.ALIYUN_OSS_BUCKET_NAME, path, "", "/", 1000));

        for (
                S3ObjectSummary s : objectListing.getObjectSummaries()) {
            FileItemDTO fileItemDTO = new FileItemDTO();
            if (s.getKey().equals(path)) {
                continue;
            }
            fileItemDTO.setName(s.getKey().substring(path.length()));
            fileItemDTO.setSize(s.getSize());
            fileItemDTO.setTime(s.getLastModified());
            fileItemDTO.setType(FileTypeEnum.FILE);
            fileItemDTO.setPath(path);
            fileItemDTO.setUrl(CommonConstant.ALIYUN_OSS_ACTION_URL + removeDuplicateSeparator(DELIMITER + path + DELIMITER + fileItemDTO.getName()));
            fileItemList.add(fileItemDTO);
        }
        for (String commonPrefix : objectListing.getCommonPrefixes()) {
            FileItemDTO fileItemDTO = new FileItemDTO();
            if (Objects.equals(commonPrefix, "/")) {
                continue;
            }
            fileItemDTO.setName(commonPrefix.substring(path.length(), commonPrefix.length() - 1));
            fileItemDTO.setType(FileTypeEnum.FOLDER);
            fileItemDTO.setPath(path);
            fileItemList.add(fileItemDTO);
        }
        return fileItemList;
    }

    /**
     * 签名生成
     *
     * @return
     */
    public JSONObject policy() {

        FileSystemClient fileSystemClient = FileSystemClient.getClient(CommonConstant.FILE_SYSTEM_ALIYUN);
        UploadTokenParam uploadTokenParam = new UploadTokenParam();
        // 存储目录
        String dir = DateUtil.format(new Date(), "yyyy/MM/dd");
        uploadTokenParam.setUploadDir(dir);
        // 签名有效期
        long expireEndTime = CommonConstant.ALIYUN_OSS_EXPIRE;
        uploadTokenParam.setExpires(expireEndTime);
        //取消回调
//		uploadTokenParam.setCallbackUrl(CommonConstant.ALIYUN_OSS_CALLBACK);
//		uploadTokenParam.setCallbackHost(CommonConstant.ALIYUN_OSS_ENDPOINT);
        uploadTokenParam.setFsizeMax(CommonConstant.ALIYUN_OSS_MAX_SIZE * 1024 * 1024);
        Map<String, Object> map = fileSystemClient.createUploadToken(uploadTokenParam);
// 提交节点
        String action = "https://" + CommonConstant.ALIYUN_OSS_BUCKET_NAME + "." + CommonConstant.ALIYUN_OSS_ENDPOINT;
        map.put("action", action);
        map.put("myDomain", action);
        JSONObject result = new JSONObject(map);
        return result;
    }

    public void delFile(String bucketName, String filename) {
        DeleteObjectRequest request = new DeleteObjectRequest(bucketName,filename);
        s3Client.deleteObject(request);
    }
}
/*Date expiration = new Date(expireEndTime);
		// 文件大小
		long maxSize = CommonConstant.ALIYUN_OSS_MAX_SIZE * 1024 * 1024;
		// 回调
		JSONObject callback = new JSONObject();
		callback.put("callbackUrl", CommonConstant.ALIYUN_OSS_CALLBACK);
		callback.put("callbackBody", "filename=${object}&size=${size}&mimeType=${mimeType}&height=${imageInfo.height}&width=${imageInfo.width}");
		callback.put("callbackBodyType", "application/x-www-form-urlencoded");
		// 提交节点
		String action = "http://" + CommonConstant.ALIYUN_OSS_BUCKET_NAME + "." + CommonConstant.ALIYUN_OSS_ENDPOINT;
		try {
			PolicyConditions policyConds = new PolicyConditions();
			policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, maxSize);
			policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);
			String postPolicy = aliyunOssClient.generatePostPolicy(expiration, policyConds);
			byte[] binaryData = postPolicy.getBytes("utf-8");
			String policy = BinaryUtil.toBase64String(binaryData);
			String signature = aliyunOssClient.calculatePostSignature(postPolicy);
			String callbackData = BinaryUtil.toBase64String(callback.toString().getBytes("utf-8"));
			// 返回结果
			result.put("OSSAccessKeyId", aliyunOssClient.getCredentialsProvider().getCredentials().getAccessKeyId());
			result.put("policy", policy);
			result.put("signature", signature);
			result.put("dir", dir);
			result.put("callback", callbackData);
			result.put("myDomain", CommonConstant.ALIOSS_URL_PREFIX);
			result.put("action", action);
		} catch (Exception e) {
			LOGGER.error("签名生成失败", e);
		}
		return result;*/