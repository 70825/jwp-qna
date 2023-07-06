package subway;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestConstructor;

import static org.assertj.core.api.Assertions.assertThat;

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL) // autowired 사용없이 하는법
@DataJpaTest
class StationRepositoryTest {

    private final StationRepository stations; // 컬렉션과 역할이 비슷함. 컬렉션 친화적임
    private final LineRepository lines;

    public StationRepositoryTest(final StationRepository stations,
                                 final LineRepository lines) {
        this.stations = stations;
        this.lines = lines;
    }

    @Test
    void save() {
        final Station expected = new Station("잠실역");
        final Station actual = stations.save(expected);
        // final Station actual2 = stations.save(new Station("잠실역"));
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getName()).isEqualTo("잠실역");
    }

    @Test
    void findByName() {
        stations.save(new Station("잠실역"));
        final Station actual = stations.findByName("잠실역");
        assertThat(actual.getId()).isNotNull();
    }

    @Test
    void identity() {
        final Station station1 = stations.save(new Station("잠실역"));
        final Station station2 = stations.findById(station1.getId()).get(); // 쿼리문이 나오지 않는 이유 : 1차 캐시에 저장되어 있음
        assertThat(station1 == station2).isTrue();
    }

    @Test
    void identity2() {
        final Station station1 = stations.save(new Station("잠실역"));
        final Station station2 = stations.findByName("잠실역");
        assertThat(station1 == station2).isTrue();
    }

    @Test
    void update() {
        final Station station1 = stations.save(new Station("잠실역"));
        station1.changeName("선릉역");
        final Station station2 = stations.findByName("선릉역");
        assertThat(station2).isNotNull();
    }

    @Test
    void saveWithLine() {
        final Station expected = new Station("선릉역");
        expected.setLine(lines.save(new Line("2호선")));
        final Station actual = stations.save(expected);
        stations.flush();
    }

    @Test
    void findByNameWithLine() {
        final Station actual = stations.findByName("교대역");
        assertThat(actual).isNotNull();
        assertThat(actual.getLine()).isNotNull();
    }

    @Test
    void updateWithLine() {
        final Station expected = stations.findByName("교대역");
        expected.setLine(lines.save(new Line("2호선")));
        stations.flush();
    }

    @Test
    void removeLineInStation() {
        final Station expected = stations.findByName("교대역");
        expected.setLine(null); // 객체 중심으로 생각하기 -> null을 넣으면 Line을 가지지 않게 된다.
        stations.flush();
    }

    @Test
    void removeLine() {
        final Line line = lines.findByName("3호선");
        lines.delete(line);
        lines.flush();
    }
}
