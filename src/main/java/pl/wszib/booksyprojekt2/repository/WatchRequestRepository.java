package pl.wszib.booksyprojekt2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wszib.booksyprojekt2.entity.WatchRequest;

import java.util.List;

public interface WatchRequestRepository extends JpaRepository<WatchRequest, Long> {
    List<WatchRequest> findByNeedToFindNewSlotsTrue();
}
