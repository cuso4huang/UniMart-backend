package org.jnu.unimart.service;

import org.jnu.unimart.pojo.Tag;

import java.util.List;

public interface TagService {
    public Tag createTag(Tag tag);
    public Tag getTagByName(String name);
    public List<Tag> getAllTags();
    public Tag updateTag(Tag tag);
}
