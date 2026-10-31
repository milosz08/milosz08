<#-- @ftlvariable name="groups" type="java.util.List<pl.miloszgilga.jargonaut.core.dto.AlphabeticalGroup>" -->

<#macro render groups>
  <div class="alphabet-grid">
    <#list groups as group>
      <div class="alphabet-group">
        <div class="alphabet-group__header">
          <h2 class="alphabet-group__letter">${group.letter()}</h2>
          <span class="alphabet-group__total">${group.totalItems()}</span>
        </div>
        <ul class="alphabet-group__list">
          <#list group.items() as item>
            <li class="alphabet-group__item">
              <#nested item>
            </li>
          </#list>
        </ul>
      </div>
    </#list>
  </div>
</#macro>
