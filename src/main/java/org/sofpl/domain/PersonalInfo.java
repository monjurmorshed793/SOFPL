package org.sofpl.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import org.sofpl.domain.enumeration.MaritalStatus;

import org.sofpl.domain.enumeration.Gender;

import org.sofpl.domain.enumeration.Religion;

/**
 * A PersonalInfo.
 */
@Entity
@Table(name = "personal_info")
@Document(indexName = "personalinfo")
public class PersonalInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "employee_id")
    private String employeeId;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "fathers_name")
    private String fathersName;

    @Column(name = "mothers_name")
    private String mothersName;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "marital_status")
    private MaritalStatus maritalStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Enumerated(EnumType.STRING)
    @Column(name = "religion")
    private Religion religion;

    @Column(name = "permanent_address")
    private String permanentAddress;

    @Column(name = "present_address")
    private String presentAddress;

    @ManyToOne
    @JsonIgnoreProperties("personalInfos")
    private Department department;

    @ManyToOne
    @JsonIgnoreProperties("personalInfos")
    private Designation designation;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public PersonalInfo employeeId(String employeeId) {
        this.employeeId = employeeId;
        return this;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getFullName() {
        return fullName;
    }

    public PersonalInfo fullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFathersName() {
        return fathersName;
    }

    public PersonalInfo fathersName(String fathersName) {
        this.fathersName = fathersName;
        return this;
    }

    public void setFathersName(String fathersName) {
        this.fathersName = fathersName;
    }

    public String getMothersName() {
        return mothersName;
    }

    public PersonalInfo mothersName(String mothersName) {
        this.mothersName = mothersName;
        return this;
    }

    public void setMothersName(String mothersName) {
        this.mothersName = mothersName;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public PersonalInfo birthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
        return this;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public MaritalStatus getMaritalStatus() {
        return maritalStatus;
    }

    public PersonalInfo maritalStatus(MaritalStatus maritalStatus) {
        this.maritalStatus = maritalStatus;
        return this;
    }

    public void setMaritalStatus(MaritalStatus maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public Gender getGender() {
        return gender;
    }

    public PersonalInfo gender(Gender gender) {
        this.gender = gender;
        return this;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Religion getReligion() {
        return religion;
    }

    public PersonalInfo religion(Religion religion) {
        this.religion = religion;
        return this;
    }

    public void setReligion(Religion religion) {
        this.religion = religion;
    }

    public String getPermanentAddress() {
        return permanentAddress;
    }

    public PersonalInfo permanentAddress(String permanentAddress) {
        this.permanentAddress = permanentAddress;
        return this;
    }

    public void setPermanentAddress(String permanentAddress) {
        this.permanentAddress = permanentAddress;
    }

    public String getPresentAddress() {
        return presentAddress;
    }

    public PersonalInfo presentAddress(String presentAddress) {
        this.presentAddress = presentAddress;
        return this;
    }

    public void setPresentAddress(String presentAddress) {
        this.presentAddress = presentAddress;
    }

    public Department getDepartment() {
        return department;
    }

    public PersonalInfo department(Department department) {
        this.department = department;
        return this;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Designation getDesignation() {
        return designation;
    }

    public PersonalInfo designation(Designation designation) {
        this.designation = designation;
        return this;
    }

    public void setDesignation(Designation designation) {
        this.designation = designation;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PersonalInfo personalInfo = (PersonalInfo) o;
        if (personalInfo.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), personalInfo.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PersonalInfo{" +
            "id=" + getId() +
            ", employeeId='" + getEmployeeId() + "'" +
            ", fullName='" + getFullName() + "'" +
            ", fathersName='" + getFathersName() + "'" +
            ", mothersName='" + getMothersName() + "'" +
            ", birthDate='" + getBirthDate() + "'" +
            ", maritalStatus='" + getMaritalStatus() + "'" +
            ", gender='" + getGender() + "'" +
            ", religion='" + getReligion() + "'" +
            ", permanentAddress='" + getPermanentAddress() + "'" +
            ", presentAddress='" + getPresentAddress() + "'" +
            "}";
    }
}
