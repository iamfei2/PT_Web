package org.stuinfo.pt_back.common;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.querys.MySqlQuery;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.baomidou.mybatisplus.generator.query.SQLQuery;

import java.sql.Types;
import java.util.Collections;

public class codeGenerator {
    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/ptback?useUnicode=true&serverTimezone=Asia/Shanghai&useSSL=false&characterEncoding=utf-8", "root", "123456")
                .globalConfig(builder -> {
                    builder.author("lt") // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .outputDir("E:\\Aa-school\\Aa-大三下\\Aa-大三下实训\\code\\Pt_Back\\\\src\\main\\java");
                    // 指定输出目录
                })
                .dataSourceConfig(builder ->
                        builder.typeConvertHandler((globalConfig, typeRegistry, metaInfo) -> {
                            int typeCode = metaInfo.getJdbcType().TYPE_CODE;
                            if (typeCode == Types.SMALLINT) {
                                // 自定义类型转换
                                return DbColumnType.INTEGER;
                            }
                            return typeRegistry.getColumnType(metaInfo);
                        })
                )
                .packageConfig(builder ->
                        builder.parent("org.stuinfo") // 设置父包名
                                .moduleName("pt_back") // 设置父包模块名
                                .entity("entity")
                                .mapper("mapper")
                                .service("service")
                                .serviceImpl("service.impl")
                                .controller("controller")
                                .pathInfo(Collections.singletonMap(OutputFile.xml, "E:\\Aa-school\\Aa-大三下\\Aa-大三下实训\\code\\Pt_Back\\src\\main\\resources\\mapper")) // 设置mapperXml生成路径
                )
                .strategyConfig(builder ->
                        builder.addInclude("user_torrents") // 设置需要生成的表名
                                .addTablePrefix("t_", "c_") // 设置过滤表前缀
                )
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}

