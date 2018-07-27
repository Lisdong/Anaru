package com.example.lrd.bean;

/**
 * Created By LRD
 * on 2018/6/28
 */
public class VersionBean {

    private int Code;
    private String Message;
    private int ClientVersionId;
    private String ClientVersionName;
    private String ClientVersionURL;
    private String ForceUpdate;
    private String HintMessage;
    private String ClientUpdate;

    public int getCode() {
        return Code;
    }

    public void setCode(int Code) {
        this.Code = Code;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public int getClientVersionId() {
        return ClientVersionId;
    }

    public void setClientVersionId(int ClientVersionId) {
        this.ClientVersionId = ClientVersionId;
    }

    public String getClientVersionName() {
        return ClientVersionName;
    }

    public void setClientVersionName(String ClientVersionName) {
        this.ClientVersionName = ClientVersionName;
    }

    public String getClientVersionURL() {
        return ClientVersionURL;
    }

    public void setClientVersionURL(String ClientVersionURL) {
        this.ClientVersionURL = ClientVersionURL;
    }

    public String getForceUpdate() {
        return ForceUpdate;
    }

    public void setForceUpdate(String ForceUpdate) {
        this.ForceUpdate = ForceUpdate;
    }

    public String getHintMessage() {
        return HintMessage;
    }

    public void setHintMessage(String HintMessage) {
        this.HintMessage = HintMessage;
    }

    public String getClientUpdate() {
        return ClientUpdate;
    }

    public void setClientUpdate(String ClientUpdate) {
        this.ClientUpdate = ClientUpdate;
    }
}
