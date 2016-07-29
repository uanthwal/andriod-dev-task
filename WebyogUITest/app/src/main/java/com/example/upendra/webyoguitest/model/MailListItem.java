package com.example.upendra.webyoguitest.model;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.example.upendra.webyoguitest.R;

public class MailListItem {
    private String mailTitle;
    private String mailID;
    private String mailSubject;
    private String mailBody;
    private boolean isStarred;

    public MailListItem() {
    }

    public String getMailTitle() {
        return mailTitle;
    }

    public String getMailID() {
        return mailID;
    }

    public String getMailSubject() {
        return mailSubject;
    }

    public boolean getMailStar() {
        return isStarred;
    }

    public String getMailBody() {
        return mailBody;
    }

    public void setMailTitle(String mailTitle) {
        this.mailTitle = mailTitle;
    }

    public void setMailID(String mailID) {
        this.mailID = mailID;
    }

    public void setMailSubject(String mailSubject) {
        this.mailSubject = mailSubject;
    }

    public void setMailBody(String mailBody) {
        this.mailBody = mailBody;
    }

    public void setMailStar(boolean value) {
        this.isStarred = value;
    }
}
