package com.brainstation23.skeleton.data.repository;

import com.brainstation23.skeleton.data.entity.ApplicationSetting;
import com.brainstation23.skeleton.presenter.domain.response.ApplicationSettingsResponse;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApplicationSettingsRepository extends JpaRepository<ApplicationSetting, Long> {

    @Query("SELECT o FROM ApplicationSetting o WHERE o.isDeleted = false")
    Page<ApplicationSetting> findAll(Pageable pageable);

    Optional<ApplicationSetting> findById(Long id);

    @Query("SELECT new com.brainstation23.skeleton.presenter.domain.response.ApplicationSettingsResponse(a.settingCode, a.settingName, a.settingValue) FROM ApplicationSetting a WHERE a.isDeleted = false AND a.isActive = true AND a.settingCode = :code")
    ApplicationSettingsResponse getApplicationSettingByCode(@Param("code") String code);

    @Query("SELECT o.settingValue FROM ApplicationSetting o WHERE o.settingCode = :code ")
    String getApplicationSettingValueBYSettingCode(@Param("code") String code);

    ApplicationSetting findApplicationSettingBySettingCode(String settingCode);

    ApplicationSetting findByIsActiveTrueAndSettingCode(String settingCode);

    @Query("select aps from ApplicationSetting aps where " +
            "(:settingName is null or aps.settingName like %:settingName%) and " +
            "(:settingCode is null or aps.settingCode = :settingCode) and " +
            "(:active is null or aps.isActive = :active)")
    Page<ApplicationSetting> findAllByParam(Pageable pageable, String settingName, String settingCode, Boolean active);

    Optional<ApplicationSetting> findBySettingCode(String settingCode);
}
