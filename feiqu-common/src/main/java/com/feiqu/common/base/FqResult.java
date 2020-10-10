package com.feiqu.common.base;

import com.feiqu.common.enums.ResultEnum;

/**
 * @author cwd
 * @version FqResult, v 0.1 2019/2/20 cwd 1049766
 */
public class FqResult extends BaseResult {

    private Boolean success = true;

    public FqResult() {
    }

    public void setResult(ResultEnum result){
        super.setResult(result);
        if(!"0000".equals(result.getCode())){
            this.setSuccess(false);
        }
    }

    public FqResult(Boolean success) {
        this.success = success;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public FqResult fail(String message){
        this.setMessage(message);
        this.setSuccess(false);
        return this;
    }

}
