package com.example.routeplanner.repository;


import com.example.routeplanner.model.FavoriteRoute;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteRouteRepository extends JpaRepository<FavoriteRoute, Integer> {
    List<FavoriteRoute> findAllByUsername(String username);
}
