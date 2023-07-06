package subway;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "line")
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "line", cascade = CascadeType.REMOVE)
    private List<Station> stations = new ArrayList<>();

    protected Line() {
    }

    public Line(final String name) {
        this.name = name;
    }

    public List<Station> getStations() {
        return stations;
    }

    public void addStation(final Station station) {
        station.setLine(this);
        stations.add(station);
    }
}
