package util;

public class CenterTM {
    private String centerId;
    private String centerName;
    private String centerCity;
    private String centerDistrict;
    private String director;

    public CenterTM() {
    }

    public CenterTM(String centerId, String centerName, String centerCity, String centerDistrict, String director) {
        this.centerId = centerId;
        this.centerName = centerName;
        this.centerCity = centerCity;
        this.centerDistrict = centerDistrict;
        this.director = director;
    }

    public String getCenterId() {
        return centerId;
    }

    public void setCenterId(String centerId) {
        this.centerId = centerId;
    }

    public String getCenterName() {
        return centerName;
    }

    public void setCenterName(String centerName) {
        this.centerName = centerName;
    }

    public String getCenterCity() {
        return centerCity;
    }

    public void setCenterCity(String centerCity) {
        this.centerCity = centerCity;
    }

    public String getCenterDistrict() {
        return centerDistrict;
    }

    public void setCenterDistrict(String centerDistrict) {
        this.centerDistrict = centerDistrict;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    @Override
    public String toString() {
        return "CenterTM{" +
                "centerId='" + centerId + '\'' +
                ", centerName='" + centerName + '\'' +
                ", centerCity='" + centerCity + '\'' +
                ", centerDistrict='" + centerDistrict + '\'' +
                ", director='" + director + '\'' +
                '}';
    }
}
