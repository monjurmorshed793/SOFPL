package org.sofpl.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import org.sofpl.domain.enumeration.HolidayType;

/**
 * A Holiday.
 */
@Entity
@Table(name = "holiday")
@Document(indexName = "holiday")
public class Holiday implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "jhi_year")
    private Integer year;

    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_type")
    private HolidayType type;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @ManyToOne
    @JsonIgnoreProperties("holidays")
    private HolidayCat holidayCat;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getYear() {
        return year;
    }

    public Holiday year(Integer year) {
        this.year = year;
        return this;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public HolidayType getType() {
        return type;
    }

    public Holiday type(HolidayType type) {
        this.type = type;
        return this;
    }

    public void setType(HolidayType type) {
        this.type = type;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public Holiday startDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Holiday endDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public HolidayCat getHolidayCat() {
        return holidayCat;
    }

    public Holiday holidayCat(HolidayCat holidayCat) {
        this.holidayCat = holidayCat;
        return this;
    }

    public void setHolidayCat(HolidayCat holidayCat) {
        this.holidayCat = holidayCat;
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
        Holiday holiday = (Holiday) o;
        if (holiday.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), holiday.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Holiday{" +
            "id=" + getId() +
            ", year=" + getYear() +
            ", type='" + getType() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            "}";
    }
}
