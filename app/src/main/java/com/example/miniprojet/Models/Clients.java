package com.example.miniprojet.Models;

public class Clients {

    private int id, valsync;
    private String nom, adresse, tel, fax, email, contact, telcontact;

    public Clients() {
    }

    public Clients(int id, int valsync, String nom, String adresse, String tel, String fax, String email, String contact, String telcontact) {
        this.id = id;
        this.valsync = valsync;
        this.nom = nom;
        this.adresse = adresse;
        this.tel = tel;
        this.fax = fax;
        this.email = email;
        this.contact = contact;
        this.telcontact = telcontact;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getValsync() {
        return valsync;
    }

    public void setValsync(int valsync) {
        this.valsync = valsync;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getTelcontact() {
        return telcontact;
    }

    public void setTelcontact(String telcontact) {
        this.telcontact = telcontact;
    }

    @Override
    public String toString() {
        return "Clients{" +
                "id=" + id +
                ", valsync=" + valsync +
                ", nom='" + nom + '\'' +
                ", adresse='" + adresse + '\'' +
                ", tel='" + tel + '\'' +
                ", fax='" + fax + '\'' +
                ", email='" + email + '\'' +
                ", contact='" + contact + '\'' +
                ", telcontact='" + telcontact + '\'' +
                '}';
    }
}
