package com.loser.backend.club.common.plugin;


import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.TopLevelClass;

import java.util.List;
import java.util.Properties;

@Data
@EqualsAndHashCode(callSuper = false)
public class SlefModelPlugin extends PluginAdapter {

    private String seq_prefix;

    private String seq_suffix;

    private boolean useKeySql; // 用来控制是否需要添加@KeySql

    public SlefModelPlugin() {
    }

    public SlefModelPlugin(String seq_prefix, String seq_suffix, boolean useKeySql) {
        this.seq_prefix = seq_prefix;
        this.seq_suffix = seq_suffix;
        this.useKeySql = useKeySql;
    }

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    @Override
    public boolean modelFieldGenerated(
            Field field,
            TopLevelClass topLevelClass,
            IntrospectedColumn introspectedColumn,
            IntrospectedTable introspectedTable,
            ModelClassType modelClassType) {

        List<IntrospectedColumn> primaryKeyColumns = introspectedTable.getPrimaryKeyColumns();
        List<IntrospectedColumn> allColumns = introspectedTable.getAllColumns();
        if (!this.useKeySql) {
            return true;
        }
        /*if (StringUtils.isAllBlank(seq_prefix, seq_suffix)) {
            System.out.println("prefix和suffix均为null，不需要添加KeySql");
            return true;
        }*/
        String seq = null;
        for (IntrospectedColumn column : allColumns) {
            if (introspectedColumn == column) {
                String actualColumnName = column.getActualColumnName();
                if (actualColumnName.equals("id")) {
                    seq = introspectedTable.getFullyQualifiedTableNameAtRuntime();
                    if (StringUtils.isNotBlank(seq_prefix)) {
                        seq = seq_prefix + seq;
                    }
                    if (StringUtils.isNotBlank(seq_suffix)) {
                        seq += seq_suffix;
                    }
                    String sql = "SELECT " + seq + ".NEXTVAL FROM DUAL";
                    String keySql = "@KeySql(useGeneratedKeys = true)";
                    String columnAnno = "@Column(name = \"id\", insertable = false)";

                    field.addAnnotation(keySql);
                    field.addAnnotation(columnAnno);

                    // 添加 keySql 注释相关引用
                    topLevelClass.addImportedType("tk.mybatis.mapper.annotation.KeySql");
                    // topLevelClass.addImportedType("tk.mybatis.mapper.code.ORDER");
                }
                else {
                    if (!actualColumnName.contains("_")) {
                        String columnAnno = "@Column(name = \"" + actualColumnName + "\")";
                        field.addAnnotation(columnAnno);
                    }
                }
            }
        }

        return true;
    }

    @Override
    public boolean modelBaseRecordClassGenerated(TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        return true;
    }

    // /**
    //  * mapper 文件添加@Mapper注解
    //  */
    // @Override
    // public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass,
    //                                IntrospectedTable introspectedTable) {
    //     interfaze.addAnnotation("@Mapper");
    //     FullyQualifiedJavaType mapperImport = new FullyQualifiedJavaType("org.apache.ibatis.annotations.Mapper");
    //     interfaze.addImportedType(mapperImport);
    //     return true;
    // }

    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
        this.seq_prefix = getProperty("seq_prefix");
        this.seq_suffix = getProperty("seq_suffix");
        this.useKeySql = Boolean.parseBoolean(getProperty("useKeySql"));
    }

    public String getProperty(String key) {
        return this.properties.getProperty(key);
    }

}
