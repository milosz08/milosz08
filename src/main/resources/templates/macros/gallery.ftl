<#-- @ftlvariable name="images" type="java.util.List<java.lang.String>" -->
<#-- @ftlvariable name="urlPrefix" type="java.lang.String" -->
<#-- @ftlvariable name="i18n" type="java.util.Map" -->

<#macro render images urlPrefix="">
  <#if images?? && images?has_content>
    <h2>${i18n.gallery.heading}</h2>
    <div class="gallery__container">
      <#list images as image>
        <div class="gallery__item">
          <img class="gallery__image" src="${urlPrefix}${image}" alt="${i18n.gallery.picture} ${image?counter}">
        </div>
      </#list>
    </div>
    <div class="lightbox" id="lightbox">
      <div class="lightbox__content">
        <button class="lightbox__close" id="closeBtn">
          &times;
        </button>
        <button class="lightbox__btn lightbox__btn--prev" id="prevBtn">
          &#10094;
        </button>
        <img class="lightbox__image" id="lightboxImg" src="" alt="${i18n.gallery.magnified}">
        <button class="lightbox__btn lightbox__btn--next" id="nextBtn">
          &#10095;
        </button>
      </div>
    </div>
  </#if>
</#macro>
