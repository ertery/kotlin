package br.com.testkotlinboot.pocKotlinBoot.repository

import br.com.testkotlinboot.pocKotlinBoot.entity.Device
import org.springframework.data.jpa.repository.JpaRepository

interface DeviceInfoRepository : JpaRepository<Device, Long> {
}