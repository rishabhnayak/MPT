package ml.rishabhnayak.mpt.POJO;

/**
 * Created by Rishabh Nayak on 21-09-2018.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Register {

    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("cid")
    @Expose
    private String cid;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

}