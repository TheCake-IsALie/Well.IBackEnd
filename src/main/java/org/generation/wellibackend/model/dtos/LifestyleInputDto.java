package org.generation.wellibackend.model.dtos;

public class LifestyleInputDto {
    private Integer kcalToday;
    private Double hoursStanding;
    private Double hoursOutdoors;
    private Integer mindfulnessMinutes;
    private String sex;

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
}
