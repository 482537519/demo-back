package com.example.demoback.baseModule.system.sysDict.service.impl;

import com.example.demoback.baseModule.system.sysDict.dto.DictDetailDTO;
import com.example.demoback.baseModule.system.sysDict.dto.DictDetailQueryCriteria;
import com.example.demoback.baseModule.system.sysDict.mapper.DictDetailMapper;
import com.example.demoback.baseModule.system.sysDict.model.SysDictDetail;
import com.example.demoback.baseModule.system.sysDict.repository.DictDetailRepository;
import com.example.demoback.baseModule.system.sysDict.service.DictDetailService;
import com.example.demoback.common.util.PageUtil;
import com.example.demoback.common.util.QueryHelp;
import com.example.demoback.common.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class DictDetailServiceImpl implements DictDetailService {

    @Autowired
    private DictDetailRepository dictDetailRepository;

    @Autowired
    private DictDetailMapper dictDetailMapper;

    @Override
    public Map queryAll(DictDetailQueryCriteria criteria, Pageable pageable) {
        Page<SysDictDetail> page = dictDetailRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(dictDetailMapper::toDto));
    }

    @Override
    public DictDetailDTO findById(String id) {
        Optional<SysDictDetail> dictDetail = dictDetailRepository.findById(id);
        ValidationUtil.isNull(dictDetail,"DictDetail","id",id);
        return dictDetailMapper.toDto(dictDetail.get());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DictDetailDTO create(SysDictDetail resources) {
        return dictDetailMapper.toDto(dictDetailRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SysDictDetail resources) {
        Optional<SysDictDetail> optionalDictDetail = dictDetailRepository.findById(resources.getId());
        ValidationUtil.isNull( optionalDictDetail,"DictDetail","id",resources.getId());
        SysDictDetail dictDetail = optionalDictDetail.get();
        resources.setId(dictDetail.getId());
        dictDetailRepository.save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) {
        dictDetailRepository.deleteById(id);
    }
}