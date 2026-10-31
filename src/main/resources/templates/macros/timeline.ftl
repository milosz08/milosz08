<#-- @ftlvariable name="i18n" type="java.util.Map" -->

<#macro render dateStart dateEnd="">
  <#local isOngoing = !(dateEnd?? && dateEnd?has_content)>
  <span class="table__timeline">
    ${dateStart} - <#if isOngoing>${i18n.timeline.current}<#else>${dateEnd}</#if>
  </span>
  <#if isOngoing>
    <span class="status-badge status-badge--ongoing">
      ${i18n.status.ongoing!"In progress"}
    </span>
  <#else>
    <span class="status-badge status-badge--completed">
      ${i18n.status.completed!"Completed"}
    </span>
  </#if>
</#macro>
