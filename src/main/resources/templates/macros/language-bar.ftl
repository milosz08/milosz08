<#-- @ftlvariable name="stats" type="java.util.List<pl.miloszgilga.jargonaut.core.extractor.stats.LanguageStat>" -->

<#macro render stats>
  <#if stats?? && stats?has_content>
    <div class="project-langs">
      <div class="project-langs__bar">
        <#list stats as stat>
          <span class="project-langs__segment"
                style="width: ${stat.percentage()?c}%; background-color: ${stat.color()};"
                title="${stat.name()} ${stat.percentage()?string("0.0")}%"></span>
        </#list>
      </div>
      <ul class="project-langs__legend">
        <#list stats as stat>
          <li class="project-langs__item">
            <span class="project-langs__dot" style="background-color: ${stat.color()};"></span>
            <span class="project-langs__name">${stat.name()}</span>
            <span class="project-langs__percent">${stat.percentage()?string("0.0")}%</span>
          </li>
        </#list>
      </ul>
    </div>
  </#if>
</#macro>
