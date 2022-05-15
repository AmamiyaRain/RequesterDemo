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
	private final String endpoint = "oss-cn-shanghai.aliyuncs.com";

	private final String accessKeyId= "LTAI5tSC7R8RYRAhtPAdsoQC";

	private final String accessKeySecret="dqcsM82PVeS2KwNEMRXDK5jhOfU2kO";

	private String bucketName="amamiyarenbucket";

	@Bean(name = "ossClient")
	public OSS oSSClient() {
		return new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
	}
}
