package sogong.train.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sogong.train.entity.TrainEntity;
import sogong.train.repository.TrainRepository;
import java.util.List;

@Service
public class TrainService {

    @Autowired
    private TrainRepository trainRepository;

    public List<TrainEntity> getTrainsByMemberId(Long memberId) {
        return trainRepository.findByMemberEntityId(memberId);
    }

    public void saveTrain(TrainEntity trainEntity) {
        trainRepository.save(trainEntity);
    }

    public TrainEntity getTrainDetail(Long id) {
        // TrainEntity를 ID로 조회
        return trainRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 열차 정보를 찾을 수 없습니다."));
    }
}