package com.ednaldoluiz.websocket.v1.architecture.global;

import com.ednaldoluiz.websocket.v1.architecture.BaseArchitectureTest;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaPackage;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.library.metrics.ArchitectureMetrics;
import com.tngtech.archunit.library.metrics.ComponentDependencyMetrics;
import com.tngtech.archunit.library.metrics.LakosMetrics;
import com.tngtech.archunit.library.metrics.MetricsComponents;
import com.tngtech.archunit.library.metrics.VisibilityMetrics;
import org.junit.jupiter.api.Test;

import java.util.Set;

public class MetricsArchitectureTest extends BaseArchitectureTest {

    @Test
    void calculateLakosMetrics() {
        JavaPackage domainRoot = importedClasses.getPackage("com.ednaldoluiz.websocket.domain");
        Set<JavaPackage> domainSubpackages = domainRoot.getSubpackages();

        MetricsComponents<JavaClass> domainComponents = MetricsComponents.fromPackages(domainSubpackages);
        LakosMetrics lakos = ArchitectureMetrics.lakosMetrics(domainComponents);

        log.info("----- Lakos Metrics -----");
        log.info("CCD (Cumulative Component Dependency): {}", lakos.getCumulativeComponentDependency());
        log.info("ACD (Average Component Dependency)   : {}", lakos.getAverageComponentDependency());
        log.info("RACD (Relative ACD)                    : {}", lakos.getRelativeAverageComponentDependency());
        log.info("NCCD (Normalized CCD)                  : {}", lakos.getNormalizedCumulativeComponentDependency());
    }

    @Test
    void calculateRobertMartinMetrics() {
        JavaPackage appRoot = importedClasses.getPackage("com.ednaldoluiz.websocket.app");
        Set<JavaPackage> appSubPackages = appRoot.getSubpackages();
        MetricsComponents<JavaClass> appComponents = MetricsComponents.fromPackages(appSubPackages);

        ComponentDependencyMetrics metrics = ArchitectureMetrics.componentDependencyMetrics(appComponents);

        log.info("----- Robert Martin Metrics for package 'app' -----");
        appSubPackages.forEach(pkg -> {
            String pkgName = pkg.getName();
            double ce = metrics.getEfferentCoupling(pkgName);
            double ca = metrics.getAfferentCoupling(pkgName);
            double instability = metrics.getInstability(pkgName);
            double abs = metrics.getAbstractness(pkgName);
            double distance = metrics.getNormalizedDistanceFromMainSequence(pkgName);

            String formatted = String.format(
                    """
                    Pacote: %s
                      - Efferent Coupling (Ce)         : %.2f
                      - Afferent Coupling (Ca)         : %.2f
                      - Instability (I = Ce/(Ca+Ce))     : %.2f
                      - Abstractness (A)               : %.2f
                      - Distance from Main Seq (D)     : %.2f
                    """, pkgName, ce, ca, instability, abs, distance);
            log.info(formatted);
        });
    }

    @Test
    void calculateVisibilityMetrics() {
        JavaPackage infraRoot = importedClasses.getPackage("com.ednaldoluiz.websocket.infra");
        Set<JavaPackage> infraSubPackages = infraRoot.getSubpackages();
        MetricsComponents<JavaClass> infraComponents = MetricsComponents.fromPackages(infraSubPackages);
        VisibilityMetrics vm = ArchitectureMetrics.visibilityMetrics(infraComponents);

        log.info("----- Visibility Metrics for package 'infra' -----");
        infraSubPackages.forEach(pkg -> {
            String pkgName = pkg.getName();
            double rv = vm.getRelativeVisibility(pkgName);
            log.info("Pacote: {} -> Relative Visibility (RV) = {}", pkgName, String.format("%.2f", rv));
        });
        log.info("ARV (Average Relative Visibility) = {}", String.format("%.2f", vm.getAverageRelativeVisibility()));
        log.info("GRV (Global Relative Visibility)  = {}", String.format("%.2f", vm.getGlobalRelativeVisibility()));
    }
}
