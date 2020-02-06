package com.behaviosec.config;

import javax.validation.constraints.NotNull;

//import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@ConfigurationProperties(prefix="behaviosec") // Not working...
@Data
public class AppServiceConfig {
//	@Value( "${behaviosec.behaviosecurl}" )
	@NotNull private String behaviosecurl;
//	@Value( "${behaviosec.tenantId}" )
    @NotNull private String tenantId;
//	@Value( "${behaviosec.loginControllerRedirectionurl}" )
    @NotNull private String loginControllerRedirectionurl;

    /*
    public AppServiceConfig() {
		System.out.println(this.toString());
    }
    
	public AppServiceConfig(@NotNull String behaviosecurl, @NotNull String tenantId,
			@NotNull String loginControllerRedirectionurl) {
		super();
		this.behaviosecurl = behaviosecurl;
		this.tenantId = tenantId;
		this.loginControllerRedirectionurl = loginControllerRedirectionurl;
		System.out.println(this.toString());
	}*/

	@Override
	public String toString() {
		return "AppServiceConfig [behaviosecurl=" + behaviosecurl + ", tenantId=" + tenantId
				+ ", loginControllerRedirectionurl=" + loginControllerRedirectionurl + "]";
	}
    
	
}


