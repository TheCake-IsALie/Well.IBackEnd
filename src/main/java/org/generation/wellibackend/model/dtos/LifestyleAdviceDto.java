package org.generation.wellibackend.model.dtos;

public class LifestyleAdviceDto {
    private String advice;
    private Integer kcalToday;
    private Double hoursStanding;
    private Double hoursOutdoors;
    private Integer mindfulnessMinutes;
    private String sex;
    private Integer kcalRef;
    private String standingRef;
    private String outdoorRef;
    private String mindfulnessRef;

    public String getAdvice() { return advice; }
    public void setAdvice(String advice) { this.advice = advice; }
    public Integer getKcalToday() { return kcalToday; }
    public void setKcalToday(Integer kcalToday) { this.kcalToday = kcalToday; }
    public Double getHoursStanding() { return hoursStanding; }
    public void setHoursStanding(Double hoursStanding) { this.hoursStanding = hoursStanding; }
    public Double getHoursOutdoors() { return hoursOutdoors; }
    public void setHoursOutdoors(Double hoursOutdoors) { this.hoursOutdoors = hoursOutdoors; }
    public Integer getMindfulnessMinutes() { return mindfulnessMinutes; }
    public void setMindfulnessMinutes(Integer mindfulnessMinutes) { this.mindfulnessMinutes = mindfulnessMinutes; }
    public String getSex() { return sex; }
    public void setSex(String sex) { this.sex = sex; }
    public Integer getKcalRef() { return kcalRef; }
    public void setKcalRef(Integer kcalRef) { this.kcalRef = kcalRef; }
    public String getStandingRef() { return standingRef; }
    public void setStandingRef(String standingRef) { this.standingRef = standingRef; }
    public String getOutdoorRef() { return outdoorRef; }
    public void setOutdoorRef(String outdoorRef) { this.outdoorRef = outdoorRef; }
    public String getMindfulnessRef() { return mindfulnessRef; }
    public void setMindfulnessRef(String mindfulnessRef) { this.mindfulnessRef = mindfulnessRef; }
}
