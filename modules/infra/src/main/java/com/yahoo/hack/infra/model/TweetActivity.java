package com.yahoo.hack.infra.model;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * @since 10/5/11
 */
@Entity
public class TweetActivity extends Activity {

    @Column
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
