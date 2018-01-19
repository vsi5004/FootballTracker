package com.example.android.persistence.db;

import com.example.android.persistence.db.entity.CommentEntity;
import com.example.android.persistence.db.entity.TeamEntity;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Utility class that holds values to be used for testing.
 */
public class TestData {

    static final TeamEntity PRODUCT_ENTITY = new TeamEntity(1, "name", "desc",
            3);
    static final TeamEntity PRODUCT_ENTITY2 = new TeamEntity(2, "name2", "desc2",
            20);

    static final List<TeamEntity> PRODUCTS = Arrays.asList(PRODUCT_ENTITY, PRODUCT_ENTITY2);

    static final CommentEntity COMMENT_ENTITY = new CommentEntity(1, PRODUCT_ENTITY.getId(),
            "desc", new Date());
    static final CommentEntity COMMENT_ENTITY2 = new CommentEntity(2,
            PRODUCT_ENTITY2.getId(), "desc2", new Date());

    static final List<CommentEntity> COMMENTS = Arrays.asList(COMMENT_ENTITY, COMMENT_ENTITY2);


}
