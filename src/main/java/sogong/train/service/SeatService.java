package sogong.train.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import sogong.train.entity.SeatEntity;
import sogong.train.repository.SeatRepository;
import sogong.train.repository.TrainRepository;
import java.util.List;
import java.util.Optional;

@Service
public class SeatService {

    private final SeatRepository seatRepository;
    private final TrainRepository trainRepository;

    public SeatService(SeatRepository seatRepository, TrainRepository trainRepository) {
        this.seatRepository = seatRepository;
        this.trainRepository = trainRepository;
    }

    public SeatEntity saveSeat(SeatEntity seat) {
        return seatRepository.save(seat); // SeatEntity를 저장
    }

    public SeatEntity getSeatDetail(Long id) {
        // SeatEntity를 ID로 조회
        return seatRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 좌석 정보를 찾을 수 없습니다."));
    }

    public Optional<SeatEntity> findSeatById(Long seatId) {
        return seatRepository.findById(seatId);
    }

    @Transactional
    public void removeSeatAndTrainIfNecessary(Long seatId) {
        // 좌석 조회
        SeatEntity seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Seat ID"));

        Long trainId = seat.getTrain().getTrainId();

        // 좌석 삭제
        seatRepository.delete(seat);

        // 동일 trainId를 가진 다른 좌석이 존재하는지 확인
        boolean isLastSeat = seatRepository.countByTrain_TrainId(trainId) == 0;

        // 마지막 좌석이라면 열차 삭제
        if (isLastSeat) {
            trainRepository.deleteById(trainId);
        }
    }
}