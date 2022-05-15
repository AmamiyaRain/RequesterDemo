package com.web.services.oss.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.model.ObjectMetadata;
import com.sun.istack.NotNull;
import com.web.base.enums.BusinessErrorEnum;
import com.web.base.enums.FileTypeEnum;
import com.web.base.exceptions.BusinessException;
import com.web.configs.aliyun.AliYunOSSConfig;
import com.web.services.oss.OssService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.UUID;

@Service
public class OssServiceImpl implements OssService {

	private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd");

	private static final String HTTPS_PREFIX = "https://";

	@Resource
	OSS ossClient;

	@Resource
	AliYunOSSConfig aliYunOSSConfig;


	private String generateRandomFileName(@NotNull String imageType) {
		// 新文件名称
		String newFileName = UUID.randomUUID().toString().replace("-", "") + imageType;
		// 构建日期路径, 例如：OSS目标文件夹/2020/10/31/文件名
		String filePath = DATE_TIME_FORMATTER.format(LocalDateTime.now());
		// 文件上传的路径地址
		return filePath + "/" + newFileName;
	}

	private String generateAccessPath(String fileName) {
		return String.format("%s%s.%s/%s", HTTPS_PREFIX, aliYunOSSConfig.getBucketName(), aliYunOSSConfig.getEndpoint(), fileName);
	}

	@Override
	public String uploadImage(MultipartFile imageFile) {
		final String imageType = checkImageFormat(imageFile);
		String uploadImageUrl = generateRandomFileName(imageType);
		try (InputStream inputStream = imageFile.getInputStream()) {
			ObjectMetadata objectMetadata = new ObjectMetadata();
			if (imageType.endsWith("svg")) {
				objectMetadata.setContentType("text/xml");
			} else {
				objectMetadata.setContentType("image/jpg");
			}
			ossClient.putObject(aliYunOSSConfig.getBucketName(), uploadImageUrl, inputStream, objectMetadata);

			String imageAccessAddress = generateAccessPath(uploadImageUrl);
			//HTTPS_PREFIX + aliYunOssConfig.getBucketName() + "." + aliYunOssConfig.getEndpoint() + "/" + uploadImageUrl;
			System.out.println("============>上传图片成功！访问地址：{} ============>" + imageAccessAddress);
			return imageAccessAddress;
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessException(BusinessErrorEnum.UPLOAD_IMAGE_ERROR);
		}
	}

	public String checkImageFormat(MultipartFile imageFile) {
		String imageFileSuffix;
		try {
			byte[] bytes = new byte[5];
			//noinspection ResultOfMethodCallIgnored
			imageFile.getInputStream().read(bytes, 0, 5);
			String byteHex = byteToHex(bytes).toUpperCase(Locale.ROOT);
			if (byteHex.startsWith(FileTypeEnum.JPG.getStart())) {
				imageFileSuffix = ".jpg";
			} else if (byteHex.startsWith(FileTypeEnum.PNG.getStart())) {
				imageFileSuffix = ".png";
			} else if (byteHex.startsWith(FileTypeEnum.GIF.getStart())) {
				imageFileSuffix = ".gif";
			} else if (byteHex.startsWith(FileTypeEnum.BMP.getStart())) {
				imageFileSuffix = ".bmp";
			} else {
				throw new BusinessException(BusinessErrorEnum.WRONG_FILE_TYPE);
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new BusinessException(BusinessErrorEnum.UNRECOGNIZED_FILE_TYPE);
		}
		return imageFileSuffix;
	}

	public static String byteToHex(byte[] bytes) {
		String strHex;
		StringBuilder sb = new StringBuilder();
		for (byte aByte : bytes) {
			strHex = Integer.toHexString(aByte & 0xFF);
			// 每个字节由两个字符表示，位数不够，高位补0
			sb.append((strHex.length() == 1) ? "0" + strHex : strHex);
		}
		return sb.toString().trim();
	}
}
