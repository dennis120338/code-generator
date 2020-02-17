package com.laohand.generator;

/**
 * @author dennis
 */
public interface TemplateSource {
    /**
     * 控制器模板
     *
     * @return
     */
    String getControllerTemplate();

    /**
     * pojo模板
     *
     * @return
     */
    String getPojoTemplate();

    /**
     * service接口模板
     *
     * @return
     */
    String getServiceTemplate();

    /**
     * service实现模板
     *
     * @return
     */
    String getServiceImplTemplate();

    /**
     * mapper模板
     *
     * @return
     */
    String getMapperTemplate();


    /**
     * sql生成器模板
     *
     * @return
     */
    String getSqlBuilderTemplate();
}
