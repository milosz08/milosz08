<#-- @ftlvariable name="calendar" type="pl.miloszgilga.jargonaut.core.extractor.contributions.ContributionCalendarView" -->
<#-- @ftlvariable name="i18n" type="java.util.Map" -->

<#macro render calendar>
  <#if calendar??>
    <div class="contribution-calendar">
      <div class="contribution-calendar__info">
        ${i18n.totalContributions}: <span
          class="contribution-calendar__info-count">${calendar.totalContributions}</span>
      </div>
      <div class="contribution-calendar__main">
        <div class="contribution-calendar__left-axis">
          <#list calendar.dayLabels as dayLabel>
            <#if dayLabel?has_content>
              <span class="contribution-calendar__day-label">${dayLabel}</span>
            <#else>
              <span></span>
            </#if>
          </#list>
        </div>
        <div class="contribution-calendar__months">
          <#list calendar.monthLabels as monthLabel>
            <#if monthLabel?has_content>
              <span class="contribution-calendar__month-label">${monthLabel}</span>
            <#else>
              <span></span>
            </#if>
          </#list>
        </div>
        <div class="contribution-calendar__grid">
          <#list calendar.weeks as week>
            <#list week.contributionDays as day>
              <span class="contribution-calendar__day contribution-calendar__day--level-${day.level}"
                    title="(${day.contributionCount}) ${day.date}"></span>
            </#list>
          </#list>
        </div>
      </div>
    </div>
  </#if>
</#macro>
