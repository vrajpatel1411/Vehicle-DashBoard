package org.vrajpatel.vehicledashboardbackend.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.vrajpatel.vehicledashboardbackend.Model.VehicleDashboard;

@Repository
public interface VehicleDashboardRepository extends JpaRepository<VehicleDashboard, Integer> {
}
