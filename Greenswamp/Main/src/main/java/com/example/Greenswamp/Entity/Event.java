package com.example.Greenswamp.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", unique = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Post post;

    @NotNull
    @Column(name = "event_time")
    private LocalDateTime eventTime;

    @NotNull
    @Column(name = "location")
    private String location;

    @Column(name = "host_org")
    private String hostOrg;

    @Column(name = "rsvp_count")
    private Integer rsvpCount;

    @Column(name = "max_capacity")
    private Integer maxCapacity;

    // Конструкторы
    public Event() {
    }

    public Event(Long id, Post post, LocalDateTime eventTime, String location,
                 String hostOrg, Integer rsvpCount, Integer maxCapacity) {
        this.id = id;
        this.post = post;
        this.eventTime = eventTime;
        this.location = location;
        this.hostOrg = hostOrg;
        this.rsvpCount = rsvpCount;
        this.maxCapacity = maxCapacity;
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public LocalDateTime getEventTime() {
        return eventTime;
    }

    public void setEventTime(LocalDateTime eventTime) {
        this.eventTime = eventTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getHostOrg() {
        return hostOrg;
    }

    public void setHostOrg(String hostOrg) {
        this.hostOrg = hostOrg;
    }

    public Integer getRsvpCount() {
        return rsvpCount;
    }

    public void setRsvpCount(Integer rsvpCount) {
        this.rsvpCount = rsvpCount;
    }

    public Integer getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(Integer maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    // equals и hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(id, event.id) &&
                Objects.equals(post, event.post) &&
                Objects.equals(eventTime, event.eventTime) &&
                Objects.equals(location, event.location) &&
                Objects.equals(hostOrg, event.hostOrg) &&
                Objects.equals(rsvpCount, event.rsvpCount) &&
                Objects.equals(maxCapacity, event.maxCapacity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, post, eventTime, location, hostOrg, rsvpCount, maxCapacity);
    }

    // toString
    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", post=" + post +
                ", eventTime=" + eventTime +
                ", location='" + location + '\'' +
                ", hostOrg='" + hostOrg + '\'' +
                ", rsvpCount=" + rsvpCount +
                ", maxCapacity=" + maxCapacity +
                '}';
    }
}