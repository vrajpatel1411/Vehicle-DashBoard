package org.vrajpatel.vehicledashboardbackend.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.vrajpatel.vehicledashboardbackend.Model.VehicleDashboard;
import org.vrajpatel.vehicledashboardbackend.Repository.VehicleDashboardRepository;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Service
public class VehicleDashboardService {

    @Autowired
    private VehicleDashboardRepository vehicleDashboardRepository;

    private static final Integer maxBatteryPercentage=100;
    private static final Integer minBatteryPercentage=0;
    private static final Integer chargingRate=5;
    private static final Integer disChargingRate=2;
    private static final Integer temperatureIncrementRate=2;
    private static final Integer temperatureDecrementRate=1;
    private static final Integer rpmMultiplier=200;

    public VehicleDashboard getVehicleDashboardInfo(){
        return vehicleDashboardRepository.getById(1);
    }

    @Scheduled(fixedRate = 2000)
    public void updateVehicleDashboardInfo(){
        Optional<VehicleDashboard> vehicleDashboardOptional = vehicleDashboardRepository.findById(1);

        if(vehicleDashboardOptional.isPresent()){
            VehicleDashboard vehicleDashboard = vehicleDashboardOptional.get();

            if(vehicleDashboard.isChargingState()){
                if(vehicleDashboard.getBatteryPercentage() < maxBatteryPercentage){
                    vehicleDashboard.setBatteryPercentage(Math.min(maxBatteryPercentage,vehicleDashboard.getBatteryPercentage() + chargingRate));
                }
                if(vehicleDashboard.getBatteryPercentage() == maxBatteryPercentage){
                    vehicleDashboard.setChargingState(!vehicleDashboard.isChargingState());
                }
                vehicleDashboard.setBatteryLow(vehicleDashboard.getBatteryPercentage() <= 20);
                vehicleDashboard.setMotorSpeedSetting(0);
                vehicleDashboard.setParkingBrake(true);
                vehicleDashboard.setMotorStatus(false);


                vehicleDashboard.setMotorRpmGauge(0);
                vehicleDashboard.setMotorSpeedSetting(0);
                vehicleDashboard.setBatteryTemperature(Math.min(20, vehicleDashboard.getBatteryTemperature() - temperatureDecrementRate));
                vehicleDashboard.setPowerGauge((int) (-((float) chargingRate / 10) * 1000));
            }

            else{
                if(vehicleDashboard.getBatteryPercentage() <= 20){
                    vehicleDashboard.setBatteryLow(true);
                }
                if(vehicleDashboard.isMotorStatus()){
                    if(vehicleDashboard.getMotorSpeedSetting()!=0){
                        int rpm=vehicleDashboard.getMotorSpeedSetting()*rpmMultiplier;
                        vehicleDashboard.setMotorStatus(true);
                        vehicleDashboard.setMotorRpmGauge(rpm);
                        vehicleDashboard.setBatteryPercentage(Math.max(minBatteryPercentage, vehicleDashboard.getBatteryPercentage()-disChargingRate));
                        vehicleDashboard.setBatteryTemperature(vehicleDashboard.getBatteryTemperature()+temperatureIncrementRate);
                        vehicleDashboard.setPowerGauge((int) ((rpm / 800.0) * 1000));
                    }
                    else{

                        vehicleDashboard.setMotorStatus(false);
                        vehicleDashboard.setMotorRpmGauge(0);
                        vehicleDashboard.setPowerGauge(0);
                        vehicleDashboard.setBatteryTemperature(Math.min(20, vehicleDashboard.getBatteryTemperature() - temperatureDecrementRate));
                    }
                }

            }

            vehicleDashboard.setLastUpdated(new Date());

            vehicleDashboardRepository.save(vehicleDashboard);

        }

    }
}
