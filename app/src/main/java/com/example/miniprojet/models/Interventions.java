package com.example.miniprojet.models;


import java.sql.Date;
import java.sql.Time;

public class Interventions {

    private String id, title, datedeb, datefin, heuredeb, heurefin, commentaire, dateterminer;
    private Boolean terminer;

    private String clientId, siteId, employeeId;

    public Interventions() {
    }

    public Interventions(String id, String title, String datedeb, String datefin, String heuredeb, String heurefin, String commentaire, String dateterminer, Boolean terminer) {
        this.id = id;
        this.title = title;
        this.commentaire = commentaire;
        this.datedeb = datedeb;
        this.datefin = datefin;
        this.heuredeb = heuredeb;
        this.heurefin = heurefin;
        this.dateterminer = dateterminer;
        this.terminer = terminer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public String getDatedeb() {
        return datedeb;
    }

    public void setDatedeb(String datedeb) {
        this.datedeb = datedeb;
    }

    public String getDatefin() {
        return datefin;
    }

    public void setDatefin(String datefin) {
        this.datefin = datefin;
    }

    public String getDateterminer() {
        return dateterminer;
    }

    public void setDateterminer(String dateterminer) {
        this.dateterminer = dateterminer;
    }

    public String getHeuredeb() {
        return heuredeb;
    }

    public void setHeuredeb(String heuredeb) {
        this.heuredeb = heuredeb;
    }

    public String getHeurefin() {
        return heurefin;
    }

    public void setHeurefin(String heurefin) {
        this.heurefin = heurefin;
    }

    public Boolean getTerminer() {
        return terminer;
    }

    public void setTerminer(Boolean terminer) {
        this.terminer = terminer;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    @Override
    public String toString() {
        return "Interventions{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", commentaire='" + commentaire + '\'' +
                ", datedeb=" + datedeb +
                ", datefin=" + datefin +
                ", dateterminer=" + dateterminer +
                ", heuredeb=" + heuredeb +
                ", heurefin=" + heurefin +
                ", terminer=" + terminer +
                '}';
    }

}
