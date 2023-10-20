package com.itsolution.electronichealthrecord;

public class model_for_owner_dash {
    String Physician_Name;
    String Specialization;
    String Note;
    String Description;
    String Record_Type;
    String report_img;
    String visit_date;
    String diseases;
    String dr_img;
    String contact_info;

    public model_for_owner_dash() {

    }

    public String getPhysician_Name() {
        return Physician_Name;
    }

    public void setPhysician_Name(String physician_Name) {
        Physician_Name = physician_Name;
    }

    public String getSpecialization() {
        return Specialization;
    }

    public void setSpecialization(String specialization) {
        Specialization = specialization;
    }

    public String getNote() {
        return Note;
    }

    public void setNote(String note) {
        Note = note;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getRecord_Type() {
        return Record_Type;
    }

    public void setRecord_Type(String record_Type) {
        Record_Type = record_Type;
    }

    public String getReport_img() {
        return report_img;
    }

    public void setReport_img(String report_img) {
        this.report_img = report_img;
    }

    public String getVisit_date() {
        return visit_date;
    }

    public void setVisit_date(String visit_date) {
        this.visit_date = visit_date;
    }

    public String getDiseases() {
        return diseases;
    }

    public void setDiseases(String diseases) {
        this.diseases = diseases;
    }

    public String getDr_img() {
        return dr_img;
    }

    public void setDr_img(String dr_img) {
        this.dr_img = dr_img;
    }

    public String getContact_info() {
        return contact_info;
    }

    public void setContact_info(String contact_info) {
        this.contact_info = contact_info;
    }

    public model_for_owner_dash(String Physician_Name, String Specialization, String Note, String Description, String report_img, String Record_Type, String dr_img, String diseases, String visit_date, String contact_info) {
        this.Physician_Name = Physician_Name;
        this.Specialization = Specialization;
        this.Note = Note;
        this.Description=Description;
        this.report_img = report_img;
        this.Record_Type=Record_Type;
        this.diseases=diseases;
        this.dr_img=dr_img;
        this.visit_date=visit_date;
        this.contact_info=contact_info;
    }





}
