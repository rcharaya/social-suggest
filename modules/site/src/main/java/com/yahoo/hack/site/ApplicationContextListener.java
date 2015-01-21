package com.yahoo.hack.site;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @since 10/4/11
 */
public class ApplicationContextListener implements ServletContextListener {

    private static SitePersistence SITE_PERSISTENCE;

    public ApplicationContextListener() {
        try {
            SITE_PERSISTENCE = new SitePersistence();
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }


    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }

    public void contextInitialized(ServletContextEvent servletContextEvent) {
        servletContextEvent.getServletContext().setAttribute("persistence", SITE_PERSISTENCE);
    }

}
