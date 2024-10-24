package com.loser.backend.club.common.type;


import com.alibaba.fastjson.JSONObject;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.internal.types.JavaTypeResolverDefaultImpl;

import java.sql.Types;

public class SelfTypeResolver extends JavaTypeResolverDefaultImpl {

    public SelfTypeResolver(){
        super();
        super.typeMap.put(Types.OTHER,
                new JdbcTypeInformation("OTHER",
                        new FullyQualifiedJavaType(JSONObject.class.getName())));
    }
}
