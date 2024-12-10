package org.jnu.unimart.service.impl;

import org.jnu.unimart.pojo.Tag;
import org.jnu.unimart.repository.TagRepository;
import org.jnu.unimart.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TagServiceImpl implements TagService {
    @Autowired
    private TagRepository tagRepository;
    // 创建标签
    public Tag createTag(Tag tag) {
        // 校验标签名称是否合法
        String tagName = tag.getTagName();
        if (tagName.length() > 20) {
            throw new IllegalArgumentException("Tag name is too long. Maximum length is 20 characters.");
        }

        // 设置清理后的标签名称
        tag.setTagName(tagName);

        // 检查标签是否已经存在，避免重复创建
        Optional<Tag> existingTag = tagRepository.findByTagName(tagName);
        if (existingTag.isPresent()) {
            throw new IllegalArgumentException("Tag with this name already exists.");
        }

        // 保存标签
        return tagRepository.save(tag);
    }

    @Override
    public Tag getTagByName(String name) {
        Optional<Tag> optionalTag = tagRepository.findByTagName(name);
        if (optionalTag.isPresent()) {
            return optionalTag.get();
        }
        throw new IllegalArgumentException("Tag with this name does not exist.");
    }

    @Override
    public List<Tag> getAllTags() {
        return tagRepository.findAll();
    }
    @Override
    public Tag updateTag(Tag tag) {
        Optional<Tag> optionalTag = tagRepository.findById(tag.getTagId());
        if (optionalTag.isEmpty()) {
            throw new IllegalArgumentException("Tag with this name does not exist.");
        }
        Tag existingTag = optionalTag.get();
        existingTag.setTagName(tag.getTagName());
        return tagRepository.save(existingTag);
    }
}
