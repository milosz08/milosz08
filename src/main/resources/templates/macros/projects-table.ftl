<#-- @ftlvariable name="i18n" type="java.util.Map" -->
<#-- @ftlvariable name="projects" type="java.util.List<pl.miloszgilga.jargonaut.page.project.dto.ProjectTableView>" -->
<#-- @ftlvariable name="langPrefix" type="java.lang.String" -->

<#macro render projects>
  <div class="table-wrapper">
    <table class="table">
      <colgroup>
        <col style="width: 18%;">
        <col style="width: 10%;">
        <col style="width: auto;">
        <col style="width: 15%;">
        <col style="width: 1%;">
      </colgroup>
      <thead class="table__head">
      <tr class="table__row table__row--head">
        <th class="table__cell table__cell--head">${i18n.project.table.project}</th>
        <th class="table__cell table__cell--head">${i18n.project.table.kind}</th>
        <th class="table__cell table__cell--head">${i18n.project.table.description}</th>
        <th class="table__cell table__cell--head">
          <abbr title="${i18n.project.table.technology}">Tech.</abbr>
        </th>
        <th class="table__cell table__cell--head">${i18n.project.table.status}</th>
      </tr>
      </thead>
      <tbody class="table__body">
      <#list projects as project>
        <#assign rowClasses = "table__row table__row--${project.status}">
        <#if project.modifiers?has_content>
          <#assign rowClasses = "${rowClasses} table__row--${project.modifiers?join(' table__row--')}">
        </#if>
        <tr class="${rowClasses}">
          <td class="table__cell">
            <a class="table__link table__link--bold" href="${langPrefix}/${project.id}">
              ${project.name}
            </a>
            <#if project.repoUrls?? && project.repoUrls?has_content>
              <div class="table__sublinks">
                <#list project.repoUrls as url>
                <a href="${url}" class="table__link">
                  Repo<#if project.repoUrls?size &gt; 1> ${url?index + 1}</#if></a><#sep>, </#sep>
                </#list>
              </div>
            </#if>
          </td>
          <td class="table__cell">
            ${project.kind}
          </td>
          <td class="table__cell">
            ${project.description}
          </td>
          <td class="table__cell">
            <#list project.tech as t>
              <a class="table__link table__link--nowrap"
                 href="${langPrefix}/tag/${t.slug}">${t.name}</a><#sep>, </#sep>
            </#list>
          </td>
          <td class="table__cell table__cell--center">
            <span class="status-badge status-badge--${project.status}">
              ${i18n.status[project.status]}
            </span>
            <#if project.recentlyUpdated>
              <span class="status-badge status-badge--updated">
                ${i18n.status.updated}
              </span>
            </#if>
          </td>
        </tr>
      </#list>
      </tbody>
    </table>
  </div>
</#macro>
