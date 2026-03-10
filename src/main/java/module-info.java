module weavedreamer {
    requires org.apache.commons.lang3;
    requires java.datatransfer;
    requires java.desktop;
    requires java.logging;
    requires java.prefs;
    requires org.apache.commons.configuration2;

    opens com.jenkins.weavingsimulator.datatypes;
}