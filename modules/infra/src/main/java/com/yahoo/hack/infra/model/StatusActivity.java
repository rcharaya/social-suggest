package com.yahoo.hack.infra.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.lang.annotation.Inherited;

/**
 * @since 10/10/11
 */
@Entity
public class StatusActivity extends Activity {

    @Column(length = 1024)
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
