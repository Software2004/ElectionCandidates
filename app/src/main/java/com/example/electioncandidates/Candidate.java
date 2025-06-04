package com.example.electioncandidates;

public class Candidate {
    private String candidateName;
    private String cnic;
    private String party;
    private String electoralAreaNo;
    private String detail;
    private String assemblyType;
    private String profilePicUrl;
    private String dialogPicUrl;

    public Candidate() {
        // Default constructor required for calls to DataSnapshot.getValue(Candidate.class)
    }

    public Candidate(String candidateName, String cnic, String party, String electoralAreaNo, String detail, String assemblyType, String profilePicUrl, String dialogPicUrl) {
        this.candidateName = candidateName;
        this.cnic = cnic;
        this.party = party;
        this.electoralAreaNo = electoralAreaNo;
        this.detail = detail;
        this.assemblyType = assemblyType;
        this.profilePicUrl = profilePicUrl;
        this.dialogPicUrl = dialogPicUrl;
    }

    // Getters and Setters
    public String getCandidateName() {
        return candidateName;
    }

    public void setCandidateName(String candidateName) {
        this.candidateName = candidateName;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public String getElectoralAreaNo() {
        return electoralAreaNo;
    }

    public void setElectoralAreaNo(String electoralAreaNo) {
        this.electoralAreaNo = electoralAreaNo;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getAssemblyType() {
        return assemblyType;
    }

    public void setAssemblyType(String assemblyType) {
        this.assemblyType = assemblyType;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public String getDialogPicUrl() {
        return dialogPicUrl;
    }

    public void setDialogPicUrl(String dialogPicUrl) {
        this.dialogPicUrl = dialogPicUrl;
    }
}
