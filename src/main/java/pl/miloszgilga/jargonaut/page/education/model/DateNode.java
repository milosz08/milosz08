package pl.miloszgilga.jargonaut.page.education.model;

import pl.miloszgilga.jargonaut.core.DataUtils;
import pl.miloszgilga.jargonaut.core.GenerationHaltedException;
import pl.miloszgilga.jargonaut.core.parser.validation.YamlRequired;
import pl.miloszgilga.jargonaut.core.processor.MergeableMetadata;

public class DateNode implements MergeableMetadata<DateNode> {
  @YamlRequired
  private Integer year;
  private Integer month;

  public DateNode() {
  }

  @Override
  public DateNode merge(DateNode override) {
    if (override == null) {
      return this;
    }
    final DateNode merged = new DateNode();
    merged.year = DataUtils.mergeValues(year, override.year);
    merged.month = DataUtils.mergeValues(month, override.month);
    return merged;
  }

  public Integer getYear() {
    return year;
  }

  public void setYear(Integer year) {
    this.year = year;
  }

  public Integer getMonth() {
    return month;
  }

  public void setMonth(Integer month) {
    this.month = month;
  }

  public void validate() {
    if (year == null && month != null) {
      throw new GenerationHaltedException("Month cannot be provided without a year");
    }
    if (month != null && (month < 1 || month > 12)) {
      throw new GenerationHaltedException("Month must be between 1 and 12, provided: " + month);
    }
  }

  public String asText() {
    if (month == null) {
      return String.valueOf(year);
    }
    return "%d.%d".formatted(month, year);
  }
}
