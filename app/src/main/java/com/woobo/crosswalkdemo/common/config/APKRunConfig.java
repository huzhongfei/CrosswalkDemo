package com.woobo.crosswalkdemo.common.config;

/**
 * 版本环境管理：开发模式、内测模式和生产模式
 * @author sanji
 */
public class APKRunConfig {

    /**
     * 开发模式
     */
    public static final int RUN_DEBUG = 1;

    /**
     * 内测模式
     */
    public static final int RUN_NEICHE = 2;

    /**
     * 生产模式
     */
    public static final int RUN_PRODUCT = 3;

    /**
     * 运行模式 写死为开发模式
     */
    public static int RUN_MODE = RUN_DEBUG;

}
