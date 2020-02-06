package com.behaviosec.isdk.client;

public class ClientConfiguration {
    private String endPoint;
    private String tenantId;


    public ClientConfiguration(String endPoint){
        this.endPoint = endPoint;
        this.tenantId = null;
    }

    public ClientConfiguration(String endPoint, String tenantId){
        this.endPoint = endPoint;
        this.tenantId = tenantId;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }


    public boolean isMultitenant() {
        return this.tenantId != null;
    }
}
