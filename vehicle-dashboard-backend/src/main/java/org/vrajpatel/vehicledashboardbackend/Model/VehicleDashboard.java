package org.vrajpatel.vehicledashboardbackend.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Table
@Entity(name="vehicle_state")
@Data
public class VehicleDashboard {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;

    private boolean chargingState;

    private boolean parkingBrake;

    private boolean motorStatus;

    private boolean batteryLow;

    private Boolean checkEngine;

    private int batteryPercentage;



    private int motorSpeedSetting;

    private int batteryTemperature;

    private float gearRatio;

    private int powerGauge;

    private int motorRpmGauge;

    private Date lastUpdated;



}
