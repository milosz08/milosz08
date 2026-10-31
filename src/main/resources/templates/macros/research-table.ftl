<#-- @ftlvariable name="researchList" type="java.util.List<pl.miloszgilga.jargonaut.page.education.dto.ResearchView>" -->
<#-- @ftlvariable name="i18n" type="java.util.Map" -->

<#import "./timeline.ftl" as Timeline>

<#macro render researchList>
  <#if researchList?? && researchList?has_content>
    <h2>${i18n.research.caption}</h2>
    <div class="table-wrapper">
      <table class="table">
        <colgroup>
          <col style="width: 16%;">
          <col style="width: 32%;">
          <col style="width: 52%;">
        </colgroup>
        <thead class="table__head">
        <tr class="table__row table__row--head">
          <th class="table__cell table__cell--head">${i18n.research.table.timeline}</th>
          <th class="table__cell table__cell--head">${i18n.research.table.topic}</th>
          <th class="table__cell table__cell--head">${i18n.research.table.methodology}</th>
        </tr>
        </thead>
        <tbody class="table__body">
        <#list researchList![] as item>
          <#assign isOngoing = !(item.dateEnd?? && item.dateEnd?has_content)>
          <#assign rowClasses = "table__row" + isOngoing?then(" table__row--ongoing", "")>
          <tr class="${rowClasses}">
            <td class="table__cell table__cell--center table__cell--nowrap">
              <@Timeline.render dateStart=item.dateStart dateEnd=item.dateEnd!"" />
            </td>
            <td class="table__cell">
              <strong>${item.name}</strong>
              <#if item.org?? && item.org?has_content>
                <span class="table__meta">@ ${item.org}</span>
              </#if>
            </td>
            <td class="table__cell">
              <p class="table__paragraph">${item.description}</p>
              <#if item.url?? && item.url?has_content>
                <a class="table__link" href="${item.url}" target="_blank" rel="noopener noreferrer">
                  ${i18n.research.readMore}
                </a>
              </#if>
            </td>
          </tr>
        </#list>
        </tbody>
      </table>
    </div>
  </#if>
</#macro>
