/**
 * The Selexml query API
 */
module com.legyver.selexml.api {
    requires com.legyver.utils.graphjxml;
    requires com.legyver.core;

    //export everything but the 'poc' package
    exports com.legyver.selexml.api;
    exports com.legyver.selexml.api.query.where.comparator;
    exports com.legyver.selexml.api.query.where;
    exports com.legyver.selexml.api.query.exception;
    exports com.legyver.selexml.api.query.text;
    exports com.legyver.selexml.api.query.select;
    exports com.legyver.selexml.api.query.from;
    exports com.legyver.selexml.api.query;

    requires org.apache.logging.log4j;

    provides com.legyver.core.license.LicenseService with com.legyver.selexml.api.license.LicenseServiceImpl;
}