package org.jnu.unimart.service.impl;

import org.jnu.unimart.pojo.Tag;
import org.jnu.unimart.service.TagService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TagServiceImplTest {

    @Autowired
    private TagService tagService;

    @Test
    void createTag() {
        Tag tag = new Tag();
        tag.setTagName("test");
        Tag tag1 = tagService.createTag(tag);
        System.out.println(tag1);
    }

    @Test
    void getTagByName() {
        Tag tag = tagService.getTagByName("test");
        System.out.println(tag);
    }

    @Test
    void getAllTags() {
        List<Tag> tags = tagService.getAllTags();
        for(Tag tag:tags)
            System.out.println(tag);
    }

    @Test
    void updateTag() {
        Tag tag  = tagService.getTagByName("test");
        tag.setTagName("test2");
        System.out.println(tagService.updateTag(tag));
    }
}