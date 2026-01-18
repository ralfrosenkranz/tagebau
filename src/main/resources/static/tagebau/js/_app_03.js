(function(){
  "use strict";
  // ---- Debug: mark dynamically updated DOM elements ----
  // Green = update succeeded (value changed from default and is non-empty),
  // Red   = update attempted but result is unchanged from default OR empty (e.g., server returned empty/invalid data).
  var DEBUG_MARK_DYNAMIC = true;

  function _styleMark(e, ok){
    if(!DEBUG_MARK_DYNAMIC || !e) return;
    try{
      e.style.backgroundColor = ok ? "rgba(0, 255, 128, 0.18)" : "rgba(255, 64, 64, 0.18)";
      e.style.outline = ok ? "2px solid rgba(0, 255, 128, 0.45)" : "2px solid rgba(255, 64, 64, 0.45)";
      e.style.outlineOffset = "2px";
      e.style.transition = "background-color 180ms ease, outline-color 180ms ease";
      e.setAttribute("data-dynamic", ok ? "ok" : "fail");
    }catch(_e){}
  }

  function _getSnapshot(el){
    if(!el) return "";
    // Prefer form value if present, otherwise text
    if(typeof el.value === "string") return el.value;
    return el.textContent || "";
  }

  function markDynamicAttempt(el){
    if(!DEBUG_MARK_DYNAMIC || !el) return;
    try{
      if(typeof el.length === "number" && typeof el !== "string" && !el.nodeType){
        for(var i=0;i<el.length;i++){ markDynamicAttempt(el[i]); }
        return;
      }
      var e = el;
      if(typeof el === "string"){
        e = qs(el);
        if(!e) return;
      }
      if(!e.hasAttribute("data-dyn-before")){
        e.setAttribute("data-dyn-before", _getSnapshot(e));
      }
      e.setAttribute("data-dyn-attempted", "1");
    }catch(_e){}
  }

  function markDynamicResult(el, proposedValue){
    if(!DEBUG_MARK_DYNAMIC || !el) return;
    try{
      var e = el;
      if(typeof el === "string"){
        e = qs(el);
        if(!e) return;
      }
      // If no attempt was registered, register now with current default snapshot
      if(!e.hasAttribute("data-dyn-before")){
        e.setAttribute("data-dyn-before", _getSnapshot(e));
      }
      var before = e.getAttribute("data-dyn-before") || "";
      var after = _getSnapshot(e);
      var pv = (proposedValue === undefined || proposedValue === null) ? "" : String(proposedValue);

      // Failure conditions:
      // - server value empty OR
      // - DOM value unchanged from default (before==after)
      var fail = (pv.trim() === "") || (before === after);
      _styleMark(e, !fail);
    }catch(_e){}
  }

  function markDynamicSuccess(el){ _styleMark(el, true); }
  function markDynamicFail(el){ _styleMark(el, false); }

  function setTextDyn(el, value){
    markDynamicAttempt(el);
    setText(el, value);
    markDynamicResult(el, value);
  }
function qs(sel, root){ return (root||document).querySelector(sel); }
  function qsa(sel, root){ return Array.prototype.slice.call((root||document).querySelectorAll(sel)); }
  function getParam(name){
    try{
      var u = new URL(window.location.href);
      return u.searchParams.get(name);
    }catch(e){ return null; }
  }
  function setText(el, txt){ if(el) el.textContent = (txt===undefined||txt===null) ? "" : String(txt); }
  function fmtMoney(amount, currency){
    if(amount===undefined||amount===null || amount==="") return "";
    var n = Number(amount);
    var s = (isFinite(n) ? n.toLocaleString("de-DE") : String(amount));
    if(currency) return s + " " + currency;
    return s;
  }
  function safeUrl(u){ return u ? String(u) : ""; }
  function escapeHtml(s){
    return String(s||"").replace(/[&<>"']/g, function(c){
      return ({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":"&#39;"})[c];
    });
  }

  var api = new window.TagebauApi.ApiClient({ baseUrl: "/api" });

  async function refreshCartBadge(){
    var badge = qs("#cartCount");
    if(!badge) return;
    try{
      var cart = await api.getCart();
      var count = 0;
      if(cart && cart.items && cart.items.length){
        cart.items.forEach(function(it){ count += (it.quantity || 0); });
      }
      badge.textContent = String(count);
          markDynamicSuccess(badge);
}catch(e){
      console.warn("Cart badge not updated (API unavailable):", e.message || e);
          markDynamicFail(badge);
}
  }

  function renderProductCard(p){
    var id = p && p.id ? p.id : "";
    var name = (p && (p.nickname || p.technicalName)) ? (p.nickname || p.technicalName) : "Produkt";
    var price = p && p.price ? fmtMoney(p.price.amount, p.price.currency) : "";
    var thumb = safeUrl(p && (p.thumbnailUrl || (p.media && p.media.thumbnailUrl)));
    if(!thumb) thumb = "img/products/excavator-560x320.png";
    var tag = (p && p.condition) ? p.condition : "Maschine";
    return (
      '<article class="card" data-product-id="'+ encodeURIComponent(id) +'">' +
        '<div class="media">' +
          '<img src="'+ thumb +'" alt="'+ escapeHtml(name) +'" />' +
          '<span class="tag">'+ escapeHtml(tag) +'</span>' +
        '</div>' +
        '<div class="body">' +
          '<div class="title">'+ escapeHtml(name) +'</div>' +
          '<div class="meta">'+ escapeHtml(p && p.technicalName ? p.technicalName : "") +'</div>' +
          '<div class="price">'+ escapeHtml(price) +'</div>' +
          '<div class="row">' +
            '<a class="btn primary" href="product.xhtml?id='+ encodeURIComponent(id) +'">Details</a>' +
            '<a class="btn ghost" href="#" data-action="bookmark">Merken</a>' +
          '</div>' +
        '</div>' +
      '</article>'
    );
  }

  async function initLanding(){
    var root = qs('[data-page="landing"]');
    if(!root) return;

    try{
      var landing = await api.getLanding(8);
      if(landing && landing.hero){
        var _hk = qs('[data-hero-kicker]', root); setTextDyn(_hk, landing.hero.kicker);
var _ht = qs('[data-hero-title]', root); setTextDyn(_ht, landing.hero.title);
var _htext = qs('[data-hero-text]', root); setTextDyn(_htext, landing.hero.text);
}
      if(landing && landing.categories && qs("#landingCategories", root)){
        var cl = qs("#landingCategories", root);
        markDynamicAttempt(cl); cl.innerHTML = "";
        landing.categories.forEach(function(c){
          var a = document.createElement("a");
          a.href = "catalog.xhtml?categoryId=" + encodeURIComponent(c.id || "");
          a.textContent = c.name || "Kategorie";
          a.className = "chip";
          cl.appendChild(a);
        });
              // Mark red if categories empty or unchanged
        markDynamicResult(cl, landing.categories.length ? 'non-empty' : '');
}
      var grid = qs("#topProducts", root);
      if(grid && landing && landing.topProducts){
        markDynamicAttempt(grid);
        grid.innerHTML = landing.topProducts.map(renderProductCard).join("");
        markDynamicResult(grid, (landing.topProducts && landing.topProducts.length) ? 'non-empty' : '');
      } else if(grid){
        var tops = await api.listTopProducts(8);
        if(Array.isArray(tops) && tops.length){
          markDynamicAttempt(grid);
          grid.innerHTML = tops.map(renderProductCard).join("");
          markDynamicResult(grid, (tops && tops.length) ? 'non-empty' : '');
        }
      }
    }catch(e){
      console.warn("Landing not populated (API unavailable):", e.message || e);
      markDynamicFail(root);
    }
  }

  function renderCategoryLink(c, activeId){
    var id = c && c.id ? c.id : "";
    var name = c && c.name ? c.name : "Kategorie";
    var cls = (id && activeId && String(id) === String(activeId)) ? "active" : "";
    return '<a class="'+cls+'" href="catalog.xhtml?categoryId='+ encodeURIComponent(id) +'">'+ escapeHtml(name) +'</a>';
  }

  function renderRowProduct(p){
    var id = p && p.id ? p.id : "";
    var name = (p && (p.nickname || p.technicalName)) ? (p.nickname || p.technicalName) : "Produkt";
    var price = p && p.price ? fmtMoney(p.price.amount, p.price.currency) : "";
    var thumb = safeUrl(p && p.thumbnailUrl);
    if(!thumb) thumb = "img/products/excavator-320x180.png";
    var subtitle = p && p.technicalName ? p.technicalName : "";
    return (
      '<div class="rowitem" data-product-id="'+ encodeURIComponent(id) +'">' +
        '<img src="'+ thumb +'" alt="'+ escapeHtml(name) +'" />' +
        '<div>' +
          '<div class="name">'+ escapeHtml(name) +'</div>' +
          '<div class="sub">'+ escapeHtml(subtitle) +'</div>' +
        '</div>' +
        '<div class="price">'+ escapeHtml(price) +'</div>' +
        '<div class="btnwrap" style="text-align:right">' +
          '<a class="btn primary" href="product.xhtml?id='+ encodeURIComponent(id) +'">Produkt</a>' +
        '</div>' +
      '</div>'
    );
  }

  async function initCatalog(){
    var root = qs('[data-page="catalog"]');
    if(!root) return;

    var catList = qs("#categoryList", root);
    var prodList = qs("#productList", root);
    var activeCategoryId = getParam("categoryId");

    try{
      var cats = await api.listCategories();
      if(Array.isArray(cats) && cats.length && catList){
        markDynamicAttempt(catList);
        if(!activeCategoryId) activeCategoryId = String(cats[0].id || "");
        var header = catList.querySelector("div");
        var hr = catList.querySelector("hr");
        catList.innerHTML = "";
        if(header){
          catList.appendChild(header);
        } else {
          var h = document.createElement("div");
          h.style.fontWeight = "900";
          h.style.paddingTop = "4px";
          h.textContent = "Kategorien";
          catList.appendChild(h);
        }
        cats.forEach(function(c){
          var tmp = document.createElement("div");
          tmp.innerHTML = renderCategoryLink(c, activeCategoryId);
          var a = tmp.firstChild;
          a.addEventListener("click", function(ev){
            ev.preventDefault();
            var u = new URL(window.location.href);
            u.searchParams.set("categoryId", c.id);
            history.replaceState({}, "", u.toString());
            qsa("a", catList).forEach(function(x){ x.classList.remove("active"); });
            a.classList.add("active");
            loadProducts(String(c.id||""));
          });
          catList.appendChild(a);
        });
        if(hr) catList.appendChild(hr);
              markDynamicResult(catList, (cats && cats.length) ? 'non-empty' : '');
}
      await loadProducts(activeCategoryId || "");
    }catch(e){
      console.warn("Catalog not populated (API unavailable):", e.message || e);
      markDynamicFail(root);
    }

    async function loadProducts(categoryId){
      if(!prodList) return;
      if(!categoryId) return;
      var q = getParam("q") || undefined;
      var sort = getParam("sort") || "popularity";
      var page = Number(getParam("page") || 0);
      var size = Number(getParam("size") || 20);

      try{
        var resp = await api.listProductsByCategory(categoryId, { q: q, sort: sort, page: page, size: size });
        var items = [];
        var total = 0;
        if(Array.isArray(resp)){
          items = resp;
          total = resp.length;
        } else if(resp && Array.isArray(resp.content)){
          items = resp.content;
          total = resp.totalElements || resp.content.length;
        }
        var headerRow = prodList.querySelector("div");
        var headerHtml = headerRow ? headerRow.outerHTML : "";
        markDynamicAttempt(prodList);
        prodList.innerHTML = headerHtml + items.map(renderRowProduct).join("");
        markDynamicResult(prodList, (items && items.length) ? 'non-empty' : '');
        var countEl = prodList.querySelector(".muted");
        if(countEl) countEl.textContent = (total + " Treffer");
      }catch(e){
        console.warn("Category products not loaded:", e.message || e);
        markDynamicFail(prodList);
      }
    }
  }

  async function initProduct(){
    var root = qs('[data-page="product"]');
    if(!root) return;

    var productId = getParam("id") || getParam("productId") || "1";
    var titleEl = qs('[data-product-title]', root);
    var priceEl = qs('[data-product-price]', root);
    var descEl = qs('[data-product-desc]', root);
    var addBtn = qs('[data-action="add-to-cart"]', root);
    var qtyEl = qs('[data-qty]', root);

    try{
      var prod = await api.getProduct(productId);
      if(prod){
        setTextDyn(titleEl, prod.nickname || prod.technicalName || prod.id || "");
        var currency = (prod.currency || (prod.pricing && prod.pricing.currency)) || "€";
        var amount = (prod.price || (prod.pricing && (prod.pricing.priceExorbitant || prod.pricing.priceCheap || prod.pricing.priceNormal))) || "";
        if(priceEl){ markDynamicAttempt(priceEl); priceEl.textContent = fmtMoney(amount, currency); markDynamicResult(priceEl, amount); }
        if(descEl){ setTextDyn(descEl, (prod.description || prod.technicalName || "")); }
      }
    }catch(e){
      console.warn("Product not loaded:", e.message || e);
      markDynamicFail(root);
    }

    var gallery = qs('[data-gallery="product"]', root);
    if(gallery){
      try{
        var imgs = await api.listProductImages(productId);
        if(Array.isArray(imgs) && imgs.length){
          var urls = imgs.map(function(mi){ return safeUrl(mi.file || mi.url || mi.path); }).filter(Boolean);
          if(urls.length){
            var main = qs('[data-gallery-main]', gallery);
            if(main){ markDynamicAttempt(main); main.setAttribute("src", urls[0]); markDynamicResult(main, urls[0]); }
            var thumbs = qs('[data-gallery-thumbs]', gallery);
            if(thumbs){
              markDynamicAttempt(thumbs); thumbs.innerHTML = "";
              urls.slice(0,3).forEach(function(u, idx){
                var btn = document.createElement("button");
                btn.type = "button";
                btn.className = idx===0 ? "active" : "";
                btn.setAttribute("data-gallery-thumb","true");
                btn.setAttribute("data-src", u);
                var im = document.createElement("img");
                im.src = u;
                im.alt = "Ansicht " + (idx+1);
                btn.appendChild(im);
                btn.addEventListener("click", function(){
                  qsa("button", thumbs).forEach(function(b){ b.classList.remove("active"); });
                  btn.classList.add("active");
                  if(main) main.setAttribute("src", u);
                });
                thumbs.appendChild(btn);
              });
            }
          }
        }
                    markDynamicResult(thumbs, (urls && urls.length) ? 'non-empty' : '');
}catch(e){
        console.warn("Product images not loaded:", e.message || e);
        markDynamicFail(gallery);
      }

      var mainImg = qs('[data-gallery-main]', gallery);
      var thumbsWrap = qs('[data-gallery-thumbs]', gallery);
      function cycle(dir){
        if(!thumbsWrap || !mainImg) return;
        var btns = qsa('button[data-gallery-thumb]', thumbsWrap);
        if(!btns.length) return;
        var idx = btns.findIndex(function(b){ return b.classList.contains("active"); });
        if(idx < 0) idx = 0;
        var next = (idx + dir + btns.length) % btns.length;
        btns[next].click();
      }
      var left = qs('[data-gallery-prev]', gallery);
      var right = qs('[data-gallery-next]', gallery);
      if(left) left.addEventListener("click", function(){ cycle(-1); });
      if(right) right.addEventListener("click", function(){ cycle(1); });
    }

    if(addBtn){
      addBtn.addEventListener("click", async function(ev){
        ev.preventDefault();
        var qty = 1;
        if(qtyEl){
          var n = Number(qtyEl.value);
          if(isFinite(n) && n > 0) qty = Math.floor(n);
        }
        try{
          await api.addCartItem({ productId: productId, quantity: qty }); markDynamicSuccess(addBtn);
          await refreshCartBadge();
          window.location.href = "cart.xhtml";
        }catch(e){
          console.warn("Add to cart failed:", e.message || e);
          markDynamicFail(addBtn);
          alert("Konnte nicht zum Warenkorb hinzufügen (API nicht erreichbar).");
        }
      });
    }
  }

  function renderCart(cart){
    var root = qs('[data-page="cart"]');
    if(!root) return;
    var itemsWrap = qs("#cartItems", root);
    var subtotalEl = qs("#cartSubtotal", root);
    var totalEl = qs("#cartTotal", root);
    if(!itemsWrap) return;

    markDynamicAttempt(itemsWrap); itemsWrap.innerHTML = "";
    var currency = (cart && cart.currency) ? cart.currency : "€";
    var total = (cart && cart.totalAmount) ? cart.totalAmount : "0";
    var subtotal = total;
    var items = (cart && Array.isArray(cart.items)) ? cart.items : [];

    if(!items.length){
      itemsWrap.innerHTML = '<div class="muted" style="padding:14px 0">Warenkorb ist leer.</div>';
    } else {
      items.forEach(function(it){
        var id = it.itemId;
        var name = it.product && (it.product.nickname || it.product.id) ? (it.product.nickname || it.product.id) : "Produkt";
        var thumb = (it.product && it.product.thumbnailUrl) ? it.product.thumbnailUrl : "img/products/excavator-160x90.png";
        var qty = it.quantity || 1;
        var line = it.lineAmount || "";

        var row = document.createElement("div");
        row.className = "cartrow";
        row.innerHTML =
          '<div class="thumb"><img src="'+ safeUrl(thumb) +'" alt="'+ escapeHtml(name) +'" /></div>' +
          '<div><div class="t">'+ escapeHtml(name) +'</div><div class="muted" style="font-size:12px">Artikel-ID: '+ escapeHtml(String(id||"")) +'</div></div>' +
          '<div class="qty">' +
            '<label>Menge</label>' +
            '<input type="number" min="1" value="'+ qty +'" data-item-qty="true" />' +
          '</div>' +
          '<div class="p">'+ escapeHtml(fmtMoney(line || "", currency)) +'</div>' +
          '<div class="btnwrap" style="text-align:right"><a class="btn ghost" href="#" data-action="remove">Entfernen</a></div>';

        var qtyInput = row.querySelector('[data-item-qty]');
        if(qtyInput){
          qtyInput.addEventListener("change", async function(){
            var n = Number(qtyInput.value);
            if(!isFinite(n) || n < 1){ qtyInput.value = String(qty); return; }
            try{
              await api.updateCartItem(id, { quantity: Math.floor(n) });
              await loadCart();
            }catch(e){
              console.warn("Update cart item failed:", e.message || e);
              alert("Konnte Menge nicht aktualisieren (API nicht erreichbar).");
              qtyInput.value = String(qty);
            }
          });
        }
        var rm = row.querySelector('[data-action="remove"]');
        if(rm){
          rm.addEventListener("click", async function(ev){
            ev.preventDefault();
            try{
              await api.removeCartItem(id);
              await loadCart();
            }catch(e){
              console.warn("Remove item failed:", e.message || e);
              alert("Konnte Artikel nicht entfernen (API nicht erreichbar).");
            }
          });
        }
        itemsWrap.appendChild(row);
      });
    }

    if(subtotalEl){    markDynamicResult(itemsWrap, itemsWrap.textContent && itemsWrap.textContent.trim() ? 'non-empty' : '');

    if(subtotalEl){ markDynamicAttempt(subtotalEl); subtotalEl.textContent = fmtMoney(subtotal, currency); markDynamicResult(subtotalEl, subtotal); }
    if(totalEl){ markDynamicAttempt(totalEl); totalEl.textContent = fmtMoney(total, currency); markDynamicResult(totalEl, total); }
  }

  async function loadCart(){
    try{
      var cart = await api.getCart();
      renderCart(cart || {});
      await refreshCartBadge();
      return cart;
    }catch(e){
      console.warn("Cart not loaded:", e.message || e);
      var _r = qs('[data-page="cart"]'); if(_r) markDynamicFail(_r);
      return null;
    }
  }

  async function initCart(){
    var root = qs('[data-page="cart"]');
    if(!root) return;
    await loadCart();

    var checkoutBtn = qs('[data-action="checkout"]', root);
    if(checkoutBtn){
      checkoutBtn.addEventListener("click", async function(ev){
        ev.preventDefault();
        try{
          var order = await api.checkout({ note: "Demo checkout" }); markDynamicResult(checkoutBtn, (order && order.id) ? String(order.id) : "");
          alert("Checkout erfolgreich. Order-ID: " + (order && order.id ? order.id : "(unbekannt)"));
          await loadCart();
        }catch(e){
          console.warn("Checkout failed:", e.message || e);
          markDynamicFail(checkoutBtn);
          alert("Checkout nicht möglich (API nicht erreichbar).");
        }
      });
    }
  }

  document.addEventListener("DOMContentLoaded", function(){
    refreshCartBadge();
    initLanding();
    initCatalog();
    initProduct();
    initCart();
  });
})();
