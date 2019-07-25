package com.hsmy.dataprocess.pojo;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class SendLog {
    private Integer id;

    private Integer scount;

    private Integer fcount;

    private String filename;

    private Integer readbyte;

    private Integer totalbyte;

    private Integer vcount;

    private Integer ivcount;

    private Date starttime;

    @DateTimeFormat(pattern = "HH:mm:ss yyyy-MM-dd")
    @JsonFormat(pattern = "HH:mm:ss yyyy-MM-dd")
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

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename == null ? null : filename.trim();
    }

    public Integer getReadbyte() {
        return readbyte;
    }

    public void setReadbyte(Integer readbyte) {
        this.readbyte = readbyte;
    }

    public Integer getTotalbyte() {
        return totalbyte;
    }

    public void setTotalbyte(Integer totalbyte) {
        this.totalbyte = totalbyte;
    }

    public Integer getVcount() {
        return vcount;
    }

    public void setVcount(Integer vcount) {
        this.vcount = vcount;
    }

    public Integer getIvcount() {
        return ivcount;
    }

    public void setIvcount(Integer ivcount) {
        this.ivcount = ivcount;
    }

    public Date getStarttime() {
        return starttime;
    }

    public void setStarttime(Date starttime) {
        this.starttime = starttime;
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