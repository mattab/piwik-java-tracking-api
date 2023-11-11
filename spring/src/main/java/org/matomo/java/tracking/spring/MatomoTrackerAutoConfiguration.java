/*
 * Matomo Java Tracker
 *
 * @link https://github.com/matomo/matomo-java-tracker
 * @license https://github.com/matomo/matomo-java-tracker/blob/master/LICENSE BSD-3 Clause
 */

package org.matomo.java.tracking.spring;

import java.net.URI;
import java.util.List;
import org.matomo.java.tracking.MatomoTracker;
import org.matomo.java.tracking.TrackerConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * {@link AutoConfiguration Auto configuration} for Matomo Tracker.
 *
 * @see MatomoTrackerProperties
 * @see TrackerConfiguration
 * @see MatomoTracker
 */
@AutoConfiguration
@ConditionalOnProperty(prefix = "matomo.tracker", name = "api-endpoint")
@EnableConfigurationProperties(MatomoTrackerProperties.class)
public class MatomoTrackerAutoConfiguration {

  /**
   * Creates a {@link TrackerConfiguration.TrackerConfigurationBuilder} and applies all
   * {@link TrackerConfigurationBuilderCustomizer}s. Can be overridden by custom beans.
   *
   * @param customizers the customizers to apply to the builder instance (never {@code null})
   * @return the {@link TrackerConfiguration.TrackerConfigurationBuilder} instance (never {@code null})
   * @see TrackerConfiguration#builder()
   */
  @Bean
  @ConditionalOnMissingBean
  public TrackerConfiguration.TrackerConfigurationBuilder trackerConfigurationBuilder(
      List<TrackerConfigurationBuilderCustomizer> customizers
  ) {
    TrackerConfiguration.TrackerConfigurationBuilder builder = TrackerConfiguration.builder();
    customizers.forEach(customizer -> customizer.customize(builder));
    return builder;
  }

  /**
   * Creates a {@link TrackerConfiguration} instance based on the current configuration. Can be
   * overridden by custom beans.
   *
   * <p>If you define your own {@link TrackerConfiguration} bean, please don't forget to set the
   * API endpoint.
   *
   * @param builder the {@link TrackerConfiguration.TrackerConfigurationBuilder} instance (never {@code null})
   * @return the {@link TrackerConfiguration} instance (never {@code null})
   * @see TrackerConfiguration#builder()
   * @see TrackerConfiguration.TrackerConfigurationBuilder#apiEndpoint(URI)
   */
  @Bean
  @ConditionalOnMissingBean
  public TrackerConfiguration trackerConfiguration(
      TrackerConfiguration.TrackerConfigurationBuilder builder
  ) {
    return builder.build();
  }

  /**
   * Configures the {@link TrackerConfiguration.TrackerConfigurationBuilder} with the properties from
   * {@link MatomoTrackerProperties}.
   *
   * @param properties the {@link MatomoTrackerProperties} instance (never {@code null})
   * @return the {@link StandardTrackerConfigurationBuilderCustomizer} instance (never {@code null})
   * @see MatomoTrackerProperties
   * @see TrackerConfiguration.TrackerConfigurationBuilder
   */
  @Bean
  public StandardTrackerConfigurationBuilderCustomizer standardTrackerConfigurationBuilderCustomizer(
      MatomoTrackerProperties properties
  ) {
    return new StandardTrackerConfigurationBuilderCustomizer(properties);
  }

  /**
   * A {@link MatomoTracker} instance based on the current configuration. Only created if a bean of the same type is not
   * already configured.
   *
   * @param trackerConfiguration the {@link TrackerConfiguration} instance (never {@code null})
   * @return the {@link MatomoTracker} instance (never {@code null})
   * @see MatomoTracker
   * @see TrackerConfiguration
   */
  @Bean
  @ConditionalOnMissingBean
  public MatomoTracker matomoTracker(TrackerConfiguration trackerConfiguration) {
    return new MatomoTracker(trackerConfiguration);
  }

}
