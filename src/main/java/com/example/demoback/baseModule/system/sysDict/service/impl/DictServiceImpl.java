package com.example.demoback.baseModule.system.sysDict.service.impl;

import com.example.demoback.baseModule.system.sysDict.dto.DictDTO;
import com.example.demoback.baseModule.system.sysDict.mapper.DictMapper;
import com.example.demoback.baseModule.system.sysDict.model.SysDict;
import com.example.demoback.baseModule.system.sysDict.repository.DictRepository;
import com.example.demoback.baseModule.system.sysDict.service.DictService;
import com.example.demoback.common.util.PageUtil;
import com.example.demoback.common.util.QueryHelp;
import com.example.demoback.common.util.ValidationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class DictServiceImpl implements DictService {

    @Autowired
    private DictRepository dictRepository;

    @Autowired
    private DictMapper dictMapper;

    @Override
    public Object queryAll(DictDTO dict, Pageable pageable) {
        Page<SysDict> page = dictRepository.findAll((root, query, cb) -> QueryHelp.getPredicate(root, dict, cb), pageable);
        return PageUtil.toPage(page.map(dictMapper::toDto));
    }

    @Override
    public DictDTO findById(String id) {
        Optional<SysDict> dict = dictRepository.findById(id);
        ValidationUtil.isNull(dict, "Dict", "id", id);
        return dictMapper.toDto(dict.get());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DictDTO create(SysDict resources) {
        return dictMapper.toDto(dictRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(SysDict resources) {
        Optional<SysDict> optionalDict = dictRepository.findById(resources.getId());
        ValidationUtil.isNull(optionalDict, "Dict", "id", resources.getId());
        SysDict dict = optionalDict.get();
        resources.setId(dict.getId());
        dictRepository.save(resources);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) {
        dictRepository.deleteById(id);
    }

    @Override
    public SysDict findByName(String name) {
        return dictRepository.findByName(name);
    }
}