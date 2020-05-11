package util;

public class HospitalTM {
    private String hospitalId;
    private String hospitalName;
    private String hospitalCity;
    private String hospitalDestric;
    private String director;

    public HospitalTM() {
    }

    public HospitalTM(String hospitalId, String hospitalName, String hospitalCity, String hospitalDestric, String director) {
        this.hospitalId = hospitalId;
        this.hospitalName = hospitalName;
        this.hospitalCity = hospitalCity;
        this.hospitalDestric = hospitalDestric;
        this.director = director;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getHospitalCity() {
        return hospitalCity;
    }

    public void setHospitalCity(String hospitalCity) {
        this.hospitalCity = hospitalCity;
    }

    public String getHospitalDestric() {
        return hospitalDestric;
    }

    public void setHospitalDestric(String hospitalDestric) {
        this.hospitalDestric = hospitalDestric;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    @Override
    public String toString() {
        return "HospitalTM{" +
                "hospitalId='" + hospitalId + '\'' +
                ", hospitalName='" + hospitalName + '\'' +
                ", hospitalCity='" + hospitalCity + '\'' +
                ", hospitalDestric='" + hospitalDestric + '\'' +
                ", director='" + director + '\'' +
                '}';
    }
}
