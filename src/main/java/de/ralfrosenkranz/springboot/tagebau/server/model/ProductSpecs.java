package de.ralfrosenkranz.springboot.tagebau.server.model;

import jakarta.persistence.*;

@Entity
@Table(name = "product_specs")
public class ProductSpecs {

    @Id
    @Column(name = "product_id", length = 32)
    private String productId;

    @OneToOne(optional = false)
    @MapsId
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "machine_type", length = 128)
    private String machineType;

    @Column(name = "operating_weight_t")
    private Double operatingWeightT;

    @Column(name = "bucket_capacity_m3")
    private Double bucketCapacityM3;

    @Column(name = "engine_power_kw")
    private Integer enginePowerKw;

    @Column(name = "hours_used")
    private Integer hoursUsed;

    @Column(name = "boom_length_m")
    private Double boomLengthM;

    @Column(name = "payload_t")
    private Double payloadT;

    @Column(name = "tire_size", length = 64)
    private String tireSize;

    @Column(name = "throughput_tph")
    private Double throughputTph;

    @Column(name = "belt_width_mm")
    private Integer beltWidthMm;

    @Column(name = "wheel_diameter_m")
    private Double wheelDiameterM;

    @Column(name = "bucket_count")
    private Integer bucketCount;

    @Column(name = "blade_capacity_m3")
    private Double bladeCapacityM3;

    @Column(name = "hole_diameter_mm")
    private Integer holeDiameterMm;

    @Column(name = "max_hole_depth_m")
    private Double maxHoleDepthM;

    public String getProductId() { return productId; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public String getMachineType() { return machineType; }
    public void setMachineType(String machineType) { this.machineType = machineType; }
    public Double getOperatingWeightT() { return operatingWeightT; }
    public void setOperatingWeightT(Double operatingWeightT) { this.operatingWeightT = operatingWeightT; }
    public Double getBucketCapacityM3() { return bucketCapacityM3; }
    public void setBucketCapacityM3(Double bucketCapacityM3) { this.bucketCapacityM3 = bucketCapacityM3; }
    public Integer getEnginePowerKw() { return enginePowerKw; }
    public void setEnginePowerKw(Integer enginePowerKw) { this.enginePowerKw = enginePowerKw; }
    public Integer getHoursUsed() { return hoursUsed; }
    public void setHoursUsed(Integer hoursUsed) { this.hoursUsed = hoursUsed; }
    public Double getBoomLengthM() { return boomLengthM; }
    public void setBoomLengthM(Double boomLengthM) { this.boomLengthM = boomLengthM; }
    public Double getPayloadT() { return payloadT; }
    public void setPayloadT(Double payloadT) { this.payloadT = payloadT; }
    public String getTireSize() { return tireSize; }
    public void setTireSize(String tireSize) { this.tireSize = tireSize; }
    public Double getThroughputTph() { return throughputTph; }
    public void setThroughputTph(Double throughputTph) { this.throughputTph = throughputTph; }
    public Integer getBeltWidthMm() { return beltWidthMm; }
    public void setBeltWidthMm(Integer beltWidthMm) { this.beltWidthMm = beltWidthMm; }
    public Double getWheelDiameterM() { return wheelDiameterM; }
    public void setWheelDiameterM(Double wheelDiameterM) { this.wheelDiameterM = wheelDiameterM; }
    public Integer getBucketCount() { return bucketCount; }
    public void setBucketCount(Integer bucketCount) { this.bucketCount = bucketCount; }
    public Double getBladeCapacityM3() { return bladeCapacityM3; }
    public void setBladeCapacityM3(Double bladeCapacityM3) { this.bladeCapacityM3 = bladeCapacityM3; }
    public Integer getHoleDiameterMm() { return holeDiameterMm; }
    public void setHoleDiameterMm(Integer holeDiameterMm) { this.holeDiameterMm = holeDiameterMm; }
    public Double getMaxHoleDepthM() { return maxHoleDepthM; }
    public void setMaxHoleDepthM(Double maxHoleDepthM) { this.maxHoleDepthM = maxHoleDepthM; }
}
