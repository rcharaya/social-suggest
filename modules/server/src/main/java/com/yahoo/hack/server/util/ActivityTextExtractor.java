package com.yahoo.hack.server.util;

import com.yahoo.hack.infra.model.Activity;
import com.yahoo.hack.infra.model.StatusActivity;
import com.yahoo.hack.infra.model.TweetActivity;

/**
 * @since 10/10/11
 */
public class ActivityTextExtractor {

    public String extract(Activity activity) {
        if(activity instanceof StatusActivity) {
            StatusActivity a = (StatusActivity)activity;
            return a.getMsg();
        }

        if (activity instanceof TweetActivity) {
            TweetActivity a = (TweetActivity)activity;
            return a.getStatus();
        }

        return null;
    }

}
