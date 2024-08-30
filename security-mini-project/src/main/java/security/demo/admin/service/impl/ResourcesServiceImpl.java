package security.demo.admin.service.impl;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import security.demo.admin.repository.ResourcesRepository;
import security.demo.admin.service.ResourcesService;
import security.demo.domain.entity.Resources;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResourcesServiceImpl implements ResourcesService {

    private final ResourcesRepository resourcesRepository;

    @Transactional
    public Resources getResources(long id) {
        return resourcesRepository.findById(id).orElse(new Resources());
    }

    @Transactional
    public List<Resources> getResources() {
        return resourcesRepository.findAll(Sort.by(Sort.Order.asc("orderNum")));
    }

    @Transactional
    public void createResources(Resources resources){
        resourcesRepository.save(resources);
    }

    @Transactional
    public void deleteResources(long id) {
        resourcesRepository.deleteById(id);
    }
}