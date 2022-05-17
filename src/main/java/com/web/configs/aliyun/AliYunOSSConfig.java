package com.web.configs.aliyun;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.context.annotation.Bean;

@Data
@ToString
@EqualsAndHashCode
public class AliYunOSSConfig {
	private final String endpoint = "urAliYunOSSConfig.endpoint";

	private final String accessKeyId= "urAliYunOSSConfig.accessKeyId";

	private final String accessKeySecret="urAliYunOSSConfig.accessKeySecret";

	private String bucketName="urAliYunOSSConfig.bucketName";

	@Bean(name = "ossClient")
	public OSS oSSClient() {
		return new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
	}
}
