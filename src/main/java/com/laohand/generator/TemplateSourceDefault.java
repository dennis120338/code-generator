package com.laohand.generator;

/**
 * @author dennis
 */
public class TemplateSourceDefault implements TemplateSource {
    /**
     * 控制器模板
     *
     * @return
     */
    @Override
    public String getControllerTemplate() {
        return "package PACKAGE.controller;\n" +
                "\n" +
                "\n" +
                "import com.foundao.common.pojo.PageParamWraper;\n" +
                "import PACKAGE.pojo.CLASS_NAME_UPPER;\n" +
                "import PACKAGE.service.impl.CLASS_NAME_UPPERServiceImpl;\n" +
                "import com.foundao.common.common.ServerResponse;\n" +
                "import org.springframework.beans.factory.annotation.Autowired;\n" +
                "import org.springframework.validation.annotation.Validated;\n" +
                "import org.springframework.web.bind.annotation.RequestMapping;\n" +
                "import org.springframework.web.bind.annotation.ResponseBody;\n" +
                "import org.springframework.web.bind.annotation.RestController;\n" +
                "\n" +
                "/**\n" +
                " * @author dennis\n" +
                " */\n" +
                "@RestController\n" +
                "@RequestMapping(path = \"/CLASS_NAME_LOWER\")\n" +
                "@Validated\n" +
                "public class CLASS_NAME_UPPERController {\n" +
                "    @Autowired\n" +
                "    private CLASS_NAME_UPPERServiceImpl CLASS_NAME_LOWERService;\n" +
                "\n" +
                "    /**\n" +
                "     * 新增/更新数据\n" +
                "     *\n" +
                "     * @param CLASS_NAME_LOWER\n" +
                "     * @return\n" +
                "     */\n" +
                "    @RequestMapping(\"/save\")\n" +
                "    @ResponseBody\n" +
                "    public ServerResponse save(CLASS_NAME_UPPER CLASS_NAME_LOWER) {\n" +
                "        Integer ret = CLASS_NAME_LOWERService.save(CLASS_NAME_LOWER);\n" +
                "        return ServerResponse.createBySuccessResReturnData(ret);\n" +
                "    }\n" +
                "\n" +
                "    /**\n" +
                "     * 分页列表\n" +
                "     *\n" +
                "     * @param pageParamWraper\n" +
                "     * @return\n" +
                "     */\n" +
                "    @RequestMapping(\"/list\")\n" +
                "    @ResponseBody\n" +
                "    public ServerResponse list(PageParamWraper pageParamWraper) {\n" +
                "        return ServerResponse.createBySuccessResReturnData(CLASS_NAME_LOWERService.list(pageParamWraper));\n" +
                "    }\n" +
                "\n" +
                "    /**\n" +
                "     * 更新数据\n" +
                "     *\n" +
                "     * @param id\n" +
                "     * @return\n" +
                "     */\n" +
                "    @RequestMapping(\"/updateStatus\")\n" +
                "    @ResponseBody\n" +
                "    public ServerResponse updateStatus(Integer id) {\n" +
                "        if (id == null || id < 0) {\n" +
                "            return ServerResponse.createByErrorResReturnMsg(\"Params id Error\");\n" +
                "        }\n" +
                "        return ServerResponse.createBySuccessResReturnData(CLASS_NAME_LOWERService.updateStatus(id, 2));\n" +
                "    }\n" +
                "}\n";
    }

    /**
     * pojo模板
     *
     * @return
     */
    @Override
    public String getPojoTemplate() {
        return "package PACKAGE.pojo;\n" +
                "\n" +
                "import lombok.Data;\n" +
                "\n" +
                "import javax.persistence.Column;\n" +
                "import javax.persistence.Id;\n" +
                "import javax.persistence.Table;\n" +
                "import java.io.Serializable;\n" +
                "import java.util.Date;\n" +
                "\n" +
                "/**\n" +
                " * TABLE_COMMENT\n" +
                " * @author dennis\n" +
                " */\n" +
                "@Data\n" +
                "@Table(name = \"TABLE_NAME\")\n" +
                "public class CLASS_NAME_UPPER implements Serializable {\n" +
                "POJO_FIELD" +
                "}";
    }

    @Override
    public String getServiceTemplate() {
        return "";
    }

    /**
     * service实现模板
     *
     * @return
     */
    @Override
    public String getServiceImplTemplate() {
        return "package PACKAGE.service.impl;\n" +
                "\n" +
                "import com.foundao.common.pojo.PageParamWraper;\n" +
                "import com.foundao.common.pojo.PageWraper;\n" +
                "import PACKAGE.mapper.CLASS_NAME_UPPERMapper;\n" +
                "import PACKAGE.pojo.CLASS_NAME_UPPER;\n" +
                "import com.github.pagehelper.PageHelper;\n" +
                "import com.github.pagehelper.PageInfo;\n" +
                "import lombok.extern.slf4j.Slf4j;\n" +
                "import org.springframework.beans.factory.annotation.Autowired;\n" +
                "import org.springframework.stereotype.Service;\n" +
                "\n" +
                "import java.util.List;\n" +
                "\n" +
                "/**\n" +
                " * @author Dennis\n" +
                " */\n" +
                "@Service\n" +
                "@Slf4j\n" +
                "public class CLASS_NAME_UPPERServiceImpl {\n" +
                "\n" +
                "    @Autowired\n" +
                "    private CLASS_NAME_UPPERMapper CLASS_NAME_LOWERMapper;\n" +
                "\n" +
                "    /**\n" +
                "     * 新增/更新数据\n" +
                "     *\n" +
                "     * @param CLASS_NAME_LOWER\n" +
                "     * @return Integer\n" +
                "     */\n" +
                "    public Integer save(CLASS_NAME_UPPER CLASS_NAME_LOWER) {\n" +
                "        int ret;\n" +
                "        if (CLASS_NAME_LOWER.getId() == null || CLASS_NAME_LOWER.getId() <= 0) {\n" +
                "            ret = CLASS_NAME_LOWERMapper.insertSelective(CLASS_NAME_LOWER);\n" +
                "        } else {\n" +
                "            ret = CLASS_NAME_LOWERMapper.updateByPrimaryKeySelective(CLASS_NAME_LOWER);\n" +
                "        }\n" +
                "        return ret;\n" +
                "    }\n" +
                "\n" +
                "    /**\n" +
                "     * 通过id获取一条记录\n" +
                "     *\n" +
                "     * @param id\n" +
                "     * @return CLASS_NAME_UPPER\n" +
                "     */\n" +
                "    public CLASS_NAME_UPPER getRowById(Integer id) {\n" +
                "        return CLASS_NAME_LOWERMapper.getRowById(id);\n" +
                "    }\n" +
                "\n" +
                "    /**\n" +
                "     * 分页列表\n" +
                "     *\n" +
                "     * @param pageParamWraper\n" +
                "     * @return\n" +
                "     */\n" +
                "    public PageWraper list(PageParamWraper pageParamWraper) {\n" +
                "        PageHelper.startPage(pageParamWraper.getPage(), pageParamWraper.getNum());\n" +
                "        List<CLASS_NAME_UPPER> list = CLASS_NAME_LOWERMapper.list(pageParamWraper);\n" +
                "        PageInfo<CLASS_NAME_UPPER> pageInfo = new PageInfo<>(list);\n" +
                "        long total = pageInfo.getTotal();\n" +
                "        return new PageWraper(total, list);\n" +
                "    }\n" +
                "\n" +
                "    /**\n" +
                "     * 更新数据\n" +
                "     *\n" +
                "     * @param id\n" +
                "     * @param status\n" +
                "     * @return\n" +
                "     */\n" +
                "    public Integer updateStatus(Integer id, Integer status) {\n" +
                "        CLASS_NAME_UPPER CLASS_NAME_LOWER = new CLASS_NAME_UPPER();\n" +
                "        CLASS_NAME_LOWER.setId(id);\n" +
                "        //CLASS_NAME_LOWER.setStatus(status);\n" +
                "        return CLASS_NAME_LOWERMapper.updateByPrimaryKeySelective(CLASS_NAME_LOWER);\n" +
                "    }\n" +
                "\n" +
                "}\n";
    }

    /**
     * mapper模板
     *
     * @return
     */
    @Override
    public String getMapperTemplate() {
        return "package PACKAGE.mapper;\n" +
                "\n" +
                "import com.foundao.common.pojo.PageParamWraper;\n" +
                "import PACKAGE.pojo.CLASS_NAME_UPPER;\n" +
                "import org.apache.ibatis.annotations.Mapper;\n" +
                "import org.apache.ibatis.annotations.Select;\n" +
                "import org.apache.ibatis.annotations.SelectProvider;\n" +
                "import org.springframework.stereotype.Repository;\n" +
                "import tk.mybatis.mapper.common.BaseMapper;\n" +
                "\n" +
                "import java.util.List;\n" +
                "\n" +
                "\n" +
                "/**\n" +
                " * @author dennis\n" +
                " */\n" +
                "@Mapper\n" +
                "@Repository\n" +
                "public interface CLASS_NAME_UPPERMapper extends BaseMapper<CLASS_NAME_UPPER> {\n" +
                "    /**\n" +
                "     * 列表\n" +
                "     *\n" +
                "     * @param pageParamWraper\n" +
                "     * @return\n" +
                "     */\n" +
                "    @SelectProvider(type = CLASS_NAME_UPPERSqlBuilder.class, method = \"list\")\n" +
                "    List<CLASS_NAME_UPPER> list(PageParamWraper pageParamWraper);\n" +
                "\n" +
                "    /**\n" +
                "     * 获取一条记录\n" +
                "     *\n" +
                "     * @param id\n" +
                "     * @return\n" +
                "     */\n" +
                "    @Select(\"select * from TABLE_NAME where id=#{id} limit 1\")\n" +
                "    CLASS_NAME_UPPER getRowById(Integer id);\n" +
                "}";
    }

    /**
     * sql生成器模板
     *
     * @return
     */
    @Override
    public String getSqlBuilderTemplate() {
        return "package PACKAGE.mapper;\n" +
                "\n" +
                "import com.foundao.common.pojo.PageParamWraper;\n" +
                "import org.apache.ibatis.jdbc.SQL;\n" +
                "\n" +
                "/**\n" +
                " * @author dennis\n" +
                " */\n" +
                "public class CLASS_NAME_UPPERSqlBuilder {\n" +
                "    public String list(PageParamWraper pageParamWraper) {\n" +
                "        SQL sql = new SQL();\n" +
                "        sql.SELECT(\"*\").FROM(\"TABLE_NAME\");\n" +
                "        sql.ORDER_BY(\"id DESC\");\n" +
                "        return sql.toString();\n" +
                "    }\n" +
                "}";
    }
}
