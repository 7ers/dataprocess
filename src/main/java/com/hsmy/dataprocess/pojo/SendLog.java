package com.hsmy.dataprocess.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class SendLog {
    private Integer id;

    private Integer scount;

    private Integer fcount;

    private Date sendtime;

    @DateTimeFormat(pattern = "yyyy")
    @JsonFormat(pattern = "yyyy")
    private Date sdate;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getScount() {
        return scount;
    }

    public void setScount(Integer scount) {
        this.scount = scount;
    }

    public Integer getFcount() {
        return fcount;
    }

    public void setFcount(Integer fcount) {
        this.fcount = fcount;
    }

    public Date getSendtime() {
        return sendtime;
    }

    public void setSendtime(Date sendtime) {
        this.sendtime = sendtime;
    }

    public Date getSdate() {
        return sdate;
    }

    public void setSdate(Date sdate) {
        this.sdate = sdate;
    }
}