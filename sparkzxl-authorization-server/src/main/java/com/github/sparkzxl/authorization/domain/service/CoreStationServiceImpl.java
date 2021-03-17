package com.github.sparkzxl.authorization.domain.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageInfo;
import com.github.sparkzxl.authorization.application.service.ICoreStationService;
import com.github.sparkzxl.authorization.domain.repository.ICoreStationRepository;
import com.github.sparkzxl.authorization.domain.repository.IIdSegmentRepository;
import com.github.sparkzxl.authorization.infrastructure.constant.CacheConstant;
import com.github.sparkzxl.authorization.infrastructure.convert.CoreStationConvert;
import com.github.sparkzxl.authorization.infrastructure.entity.CoreStation;
import com.github.sparkzxl.authorization.infrastructure.mapper.CoreStationMapper;
import com.github.sparkzxl.authorization.interfaces.dto.station.StationQueryDTO;
import com.github.sparkzxl.authorization.interfaces.dto.station.StationSaveDTO;
import com.github.sparkzxl.authorization.interfaces.dto.station.StationUpdateDTO;
import com.github.sparkzxl.database.base.service.impl.SuperCacheServiceImpl;
import com.github.sparkzxl.database.dto.PageParams;
import com.github.sparkzxl.database.utils.PageInfoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * description: 岗位 服务实现类
 *
 * @author charles.zhou
 * @date 2020-06-07 13:37:46
 */
@Service
public class CoreStationServiceImpl extends SuperCacheServiceImpl<CoreStationMapper, CoreStation> implements ICoreStationService {

    @Autowired
    private ICoreStationRepository coreStationRepository;

    @Autowired
    private IIdSegmentRepository segmentRepository;


    @Override
    public PageInfo<CoreStation> getStationPageList(PageParams<StationQueryDTO> params) {
        return PageInfoUtils.pageInfo(coreStationRepository.getStationPageList(params.getPageNum(),
                params.getPageSize(), params.getModel().getName(),
                params.getModel().getOrg()));
    }

    @Override
    public boolean saveCoreStation(StationSaveDTO stationSaveDTO) {
        CoreStation coreStation = CoreStationConvert.INSTANCE.convertCoreStation(stationSaveDTO);
        long id = segmentRepository.getIdSegment("core_station").longValue();
        coreStation.setId(id);
        return save(coreStation);
    }

    @Override
    public boolean updateCoreStation(StationUpdateDTO stationUpdateDTO) {
        CoreStation coreStation = CoreStationConvert.INSTANCE.convertCoreStation(stationUpdateDTO);
        return updateById(coreStation);
    }

    @Override
    public CoreStation getCoreStationByName(String stationName) {
        LambdaQueryWrapper<CoreStation> stationLambdaQueryWrapper = new LambdaQueryWrapper<>();
        stationLambdaQueryWrapper.eq(CoreStation::getName, stationName);
        stationLambdaQueryWrapper.eq(CoreStation::getStatus, true).last("limit 1");
        ;
        return getOne(stationLambdaQueryWrapper);
    }


    @Override
    public boolean deleteCoreStation(List<Long> ids) {
        return coreStationRepository.deleteCoreStation(ids);
    }

    @Override
    protected String getRegion() {
        return CacheConstant.STATION;
    }
}
