<#-- @ftlvariable name="educationList" type="java.util.List<pl.miloszgilga.jargonaut.page.education.dto.EducationView>" -->
<#-- @ftlvariable name="i18n" type="java.util.Map" -->

<#import "./timeline.ftl" as Timeline>

<#macro render educationList>
  <#if educationList?? && educationList?has_content>
    <h2>${i18n.education.caption}</h2>
    <div class="table-wrapper">
      <table class="table">
        <colgroup>
          <col style="width: 16%;">
          <col style="width: 20%;">
          <col style="width: 18%;">
          <col style="width: 46%;">
        </colgroup>
        <thead class="table__head">
        <tr class="table__row table__row--head">
          <th class="table__cell table__cell--head">${i18n.education.table.timeline}</th>
          <th class="table__cell table__cell--head">${i18n.education.table.degree}</th>
          <th class="table__cell table__cell--head">${i18n.education.table.institution}</th>
          <th class="table__cell table__cell--head">${i18n.education.table.details}</th>
        </tr>
        </thead>
        <tbody class="table__body">
        <#list educationList![] as item>
          <#assign isOngoing = !(item.dateEnd?? && item.dateEnd?has_content)>
          <#assign rowClasses = "table__row" + isOngoing?then(" table__row--ongoing", "")>
          <tr class="${rowClasses}">
            <td class="table__cell table__cell--center table__cell--nowrap">
              <@Timeline.render dateStart=item.dateStart dateEnd=item.dateEnd!"" />
            </td>
            <td class="table__cell">
              <span class="table__meta">${item.type}</span>
              <strong>${item.spec}</strong>
            </td>
            <td class="table__cell">
              <strong>${item.org}</strong>
              <span class="table__meta">${item.location}</span>
            </td>
            <td class="table__cell">
              <#if item.grade?? && item.grade?has_content>
                <p class="table__paragraph">
                  <strong>${i18n.education.grade}:</strong> ${item.grade}
                </p>
              </#if>
              <#if (item.thesisDescription?? && item.thesisDescription?has_content)>
                <p class="table__paragraph">
                  ${item.thesisDescription}
                  <#if item.thesisUrl?? && item.thesisUrl?has_content>
                    <#if item.thesisDescription?? && item.thesisDescription?has_content><br></#if>
                    <a class="table__link" href="${item.thesisUrl}">
                      ${i18n.education.sourceCodeAndOrThesis}
                    </a>
                  </#if>
                </p>
              </#if>
              <#if item.additionalAchievements?? && item.additionalAchievements?has_content>
                <ul class="table__list">
                  <#list item.additionalAchievements as achievement>
                    <li>${achievement}</li>
                  </#list>
                </ul>
              </#if>
            </td>
          </tr>
        </#list>
        </tbody>
      </table>
    </div>
  </#if>
</#macro>
