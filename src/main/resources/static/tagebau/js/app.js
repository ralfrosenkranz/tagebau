(function () {
    "use strict";
    // ---- Debug: mark dynamically updated DOM elements ----
    // Green = update succeeded (value changed from default and is non-empty),
    // Red   = update attempted but result is unchanged from default OR empty (e.g., server returned empty/invalid data).
    var DEBUG_MARK_DYNAMIC = false;

    function _styleMark(e, ok) {
        if (!DEBUG_MARK_DYNAMIC || !e) return;
        try {
            e.style.backgroundColor = ok ? "rgba(0, 255, 128, 0.18)" : "rgba(255, 64, 64, 0.18)";
            e.style.outline = ok ? "2px solid rgba(0, 255, 128, 0.45)" : "2px solid rgba(255, 64, 64, 0.45)";
            e.style.outlineOffset = "2px";
            e.style.transition = "background-color 180ms ease, outline-color 180ms ease";
            e.setAttribute("data-dynamic", ok ? "ok" : "fail");
        } catch (_e) {
        }
    }

    function _getSnapshot(el) {
        if (!el) return "";
        if (typeof el.value === "string") return el.value;
        // for <img>, track src
        if (el.tagName && el.tagName.toLowerCase() === "img") return el.getAttribute("src") || "";
        return el.textContent || "";
    }

    function markDynamicAttempt(el) {
        if (!DEBUG_MARK_DYNAMIC || !el) return;
        try {
            if (typeof el.length === "number" && typeof el !== "string" && !el.nodeType) {
                for (var i = 0; i < el.length; i++) {
                    markDynamicAttempt(el[i]);
                }
                return;
            }
            var e = el;
            if (typeof el === "string") {
                e = qs(el);
                if (!e) return;
            }
            if (!e.hasAttribute("data-dyn-before")) {
                e.setAttribute("data-dyn-before", _getSnapshot(e));
            }
            e.setAttribute("data-dyn-attempted", "1");
        } catch (_e) {
        }
    }

    function markDynamicResult(el, proposedValue) {
        if (!DEBUG_MARK_DYNAMIC || !el) return;
        try {
            var e = el;
            if (typeof el === "string") {
                e = qs(el);
                if (!e) return;
            }
            if (!e.hasAttribute("data-dyn-before")) {
                e.setAttribute("data-dyn-before", _getSnapshot(e));
            }
            var before = e.getAttribute("data-dyn-before") || "";
            var after = _getSnapshot(e);
            var pv = (proposedValue === undefined || proposedValue === null) ? "" : String(proposedValue);

            // Failure if server value is empty OR DOM unchanged from default
            var fail = (pv.trim() === "") || (before === after);
            _styleMark(e, !fail);
        } catch (_e) {
        }
    }

    function markDynamicSuccess(el) {
        _styleMark(el, true);
    }

    function markDynamicFail(el) {
        _styleMark(el, false);
    }

    function dynText(el, value) {
        markDynamicAttempt(el);
        setText(el, value);
        markDynamicResult(el, value);
    }

    function dynHtml(el, html, hasData) {
        markDynamicAttempt(el);
        if (el) el.innerHTML = html;
        markDynamicResult(el, hasData ? "non-empty" : "");
    }

    function dynAttr(el, attr, value) {
        markDynamicAttempt(el);
        if (el) el.setAttribute(attr, value);
        markDynamicResult(el, value);
    }

    function qs(sel, root) {
        return (root || document).querySelector(sel);
    }

    function qsa(sel, root) {
        return Array.prototype.slice.call((root || document).querySelectorAll(sel));
    }

    function getParam(name) {
        try {
            var u = new URL(window.location.href);
            return u.searchParams.get(name);
        } catch (e) {
            return null;
        }
    }

    function setText(el, txt) {
        if (el) el.textContent = (txt === undefined || txt === null) ? "" : String(txt);
    }

    function fmtMoney(amount, currency) {
        if (amount === undefined || amount === null || amount === "") return "";
        var n = Number(amount);
        var s = (isFinite(n) ? n.toLocaleString("de-DE") : String(amount));
        if (currency) return s + " " + currency;
        return s;
    }

    function safeUrl(u) {
        return u ? String(u) : "";
    }

    function escapeHtml(s) {
        return String(s || "").replace(/[&<>"']/g, function (c) {
            return ({'&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": "&#39;"})[c];
        });
    }

  // Minimal Markdown renderer (safe: escapes HTML first).
  // Supports: headings (#, ##, ###), bold/italic, inline code, fenced code blocks, lists, blockquotes, links.
  function renderMarkdown(md){
    md = (md === undefined || md === null) ? "" : String(md);
    if(md.trim() === "") return "";
    md = md.replace(/\r\n/g, "\n").replace(/\r/g, "\n");

    // Extract fenced code blocks first
    var codeBlocks = [];
    md = md.replace(/```([\s\S]*?)```/g, function(_, code){
      var i = codeBlocks.length;
      codeBlocks.push(code);
      return "§§CODEBLOCK_" + i + "§§";
    });

    // Escape everything (prevents HTML injection)
    md = escapeHtml(md);

    // Blockquotes
    md = md.replace(/^&gt;\s?(.*)$/gm, function(_, line){
      return "<blockquote>" + line + "</blockquote>";
    });

    // Headings
    md = md.replace(/^###\s+(.+)$/gm, "<h3>$1</h3>");
    md = md.replace(/^##\s+(.+)$/gm, "<h2>$1</h2>");
    md = md.replace(/^#\s+(.+)$/gm, "<h1>$1</h1>");

    // Lists (basic)
    function renderList(block, ordered){
      var items = block.split("\n").filter(Boolean).map(function(l){
        if(ordered){ return l.replace(/^\d+\.\s+/, ""); }
        return l.replace(/^[\-\*]\s+/, "");
      }).map(function(t){ return "<li>" + t + "</li>"; }).join("");
      return ordered ? ("<ol>" + items + "</ol>") : ("<ul>" + items + "</ul>");
    }
    md = md.replace(/(^(\d+\.\s+.*)(\n\d+\.\s+.*)+)/gm, function(m0){ return renderList(m0, true); });
    md = md.replace(/(^([\-\*]\s+.*)(\n[\-\*]\s+.*)+)/gm, function(m0){ return renderList(m0, false); });

    // Paragraphs for remaining chunks
    md = md.split("\n\n").map(function(part){
      part = part.trim();
      if(part === "") return "";
      if(part.match(/^<(h1|h2|h3|ul|ol|blockquote|pre)\b/)) return part;
      return "<p>" + part.replace(/\n/g, "<br />") + "</p>";
    }).join("\n");

    // Inline code
    md = md.replace(/`([^`]+)`/g, "<code>$1</code>");
    // Bold and italic
    md = md.replace(/\*\*([^*]+)\*\*/g, "<strong>$1</strong>");
    md = md.replace(/\*([^*]+)\*/g, "<em>$1</em>");

    // Links [text](url)
    md = md.replace(/\[([^\]]+)\]\(([^)]+)\)/g, '<a href="$2" target="_blank" rel="noopener noreferrer">$1</a>');

    // Restore code blocks
    md = md.replace(/§§CODEBLOCK_(\d+)§§/g, function(_, idx){
      var code = (codeBlocks[Number(idx)] || "");
      code = escapeHtml(code);
      return "<pre><code>" + code + "</code></pre>";
    });

    return md;
  }

  // Mark image elements red if they fail to load (helps debugging wrong URLs)
  function attachImageDebug(imgEl){
    if(!imgEl) return;
    try{
      imgEl.addEventListener("error", function(){
        if(typeof markDynamicFail === "function") markDynamicFail(imgEl);
      });
      imgEl.addEventListener("load", function(){
        if(typeof markDynamicResult === "function") markDynamicResult(imgEl, imgEl.getAttribute("src") || "ok");
      });
    }catch(_e){}
  }

    var api = new window.TagebauApi.ApiClient({baseUrl: "/api"});

    async function refreshCartBadge() {
        var badge = qs("#cartCount");
        if (!badge) return;
        try {
            var cart = await api.getCart();
            var count = 0;
            if (cart && cart.items && cart.items.length) {
                cart.items.forEach(function (it) {
                    count += (it.quantity || 0);
                });
            }
            dynText(badge, String(count));
        } catch (e) {
            console.warn("Cart badge not updated (API unavailable):", e.message || e);
            markDynamicFail(badge);
        }
    }

    function renderProductCard(p) {
        var id = p && p.id ? p.id : "";
        var name = (p && (p.nickname || p.technicalName)) ? (p.nickname || p.technicalName) : "Produkt";
        var price = p && p.price ? fmtMoney(p.price.amount, p.price.currency) : "";
        var thumb = safeUrl(p && (p.thumbnailUrl || (p.media && p.media.thumbnailUrl)));
        if (!thumb) thumb = "img/products/excavator-560x320.png";
        var tag = (p && p.condition) ? p.condition : "Maschine";
        return (
            '<article class="card" data-product-id="' + encodeURIComponent(id) + '">' +
            '<div class="media">' +
            '<img src="' + thumb + '" alt="' + escapeHtml(name) + '" />' +
            '<span class="tag">' + escapeHtml(tag) + '</span>' +
            '</div>' +
            '<div class="body">' +
            '<div class="title">' + escapeHtml(name) + '</div>' +
            '<div class="meta">' + escapeHtml(p && p.technicalName ? p.technicalName : "") + '</div>' +
            '<div class="price">' + escapeHtml(price) + '</div>' +
            '<div class="row">' +
            '<a class="btn primary" href="product.xhtml?id=' + encodeURIComponent(id) + '">Details</a>' +
            '<a class="btn ghost" href="#" data-action="bookmark">Merken</a>' +
            '</div>' +
            '</div>' +
            '</article>'
        );
    }

    async function initLanding() {
        var root = qs('[data-page="landing"]');
        if (!root) return;

        try {
            var landing = await api.getLanding(8);
            if (landing && landing.hero) {
                var _hk = qs('[data-hero-kicker]', root);
                dynText(_hk, landing.hero.kicker);
                var _ht = qs('[data-hero-title]', root);
                dynText(_ht, landing.hero.title);
                var _htext = qs('[data-hero-text]', root);
                dynText(_htext, landing.hero.text);
            }
            if (landing && landing.categories && qs("#landingCategories", root)) {
                var cl = qs("#landingCategories", root);
                markDynamicAttempt(cl);
                cl.innerHTML = "";
                landing.categories.forEach(function (c) {
                    var a = document.createElement("a");
                    a.href = "catalog.xhtml?categoryId=" + encodeURIComponent(c.id || "");
                    a.textContent = c.name || "Kategorie";
                    a.className = "chip";
                    cl.appendChild(a);
                });
                markDynamicResult(cl, (landing.categories && landing.categories.length) ? 'non-empty' : '');
            }
            var grid = qs("#topProducts", root);
            if (grid && landing && landing.topProducts) {
                markDynamicAttempt(grid);
                grid.innerHTML = landing.topProducts.map(renderProductCard).join("");
                markDynamicResult(grid, (landing.topProducts && landing.topProducts.length) ? 'non-empty' : '');
            } else if (grid) {
                var tops = await api.listTopProducts(8);
                if (Array.isArray(tops) && tops.length) {
                    markDynamicAttempt(grid);
                    grid.innerHTML = tops.map(renderProductCard).join("");
                    markDynamicResult(grid, (tops && tops.length) ? 'non-empty' : '');
                }
            }
        } catch (e) {
            console.warn("Landing not populated (API unavailable):", e.message || e);
            markDynamicFail(root);
        }
    }

    function renderCategoryLink(c, activeId) {
        var id = c && c.id ? c.id : "";
        var name = c && c.name ? c.name : "Kategorie";
        var cls = (id && activeId && String(id) === String(activeId)) ? "active" : "";
        return '<a class="' + cls + '" href="catalog.xhtml?categoryId=' + encodeURIComponent(id) + '">' + escapeHtml(name) + '</a>';
    }

    function renderRowProduct(p) {
        var id = p && p.id ? p.id : "";
        var name = (p && (p.nickname || p.technicalName)) ? (p.nickname || p.technicalName) : "Produkt";
        var price = p && p.price ? fmtMoney(p.price.amount, p.price.currency) : "";
        var thumb = safeUrl(p && p.thumbnailUrl);
        if (!thumb) thumb = "img/products/excavator-320x180.png";
        var subtitle = p && p.technicalName ? p.technicalName : "";
        return (
            '<div class="rowitem" data-product-id="' + encodeURIComponent(id) + '">' +
            '<img src="' + thumb + '" alt="' + escapeHtml(name) + '" />' +
            '<div>' +
            '<div class="name">' + escapeHtml(name) + '</div>' +
            '<div class="sub">' + escapeHtml(subtitle) + '</div>' +
            '</div>' +
            '<div class="price">' + escapeHtml(price) + '</div>' +
            '<div class="btnwrap" style="text-align:right">' +
            '<a class="btn primary" href="product.xhtml?id=' + encodeURIComponent(id) + '">Produkt</a>' +
            '</div>' +
            '</div>'
        );
    }

    async function initCatalog() {
        var root = qs('[data-page="catalog"]');
        if (!root) return;

        var catList = qs("#categoryList", root);
        var prodList = qs("#productList", root);

        // Debug: these areas are expected to be dynamically populated
        markDynamicAttempt(catList);
        markDynamicAttempt(prodList);
        var activeCategoryId = getParam("categoryId");

        try {
            var cats = await api.listCategories();
            if (Array.isArray(cats) && catList) {
                // green if non-empty, red if empty
                if (!activeCategoryId && cats && cats.length) {
                    activeCategoryId = String(cats[0].id || "");
                }
                var header = catList.querySelector("div");
                var hr = catList.querySelector("hr");
                catList.innerHTML = "";
                if (header) {
                    catList.appendChild(header);
                } else {
                    var h = document.createElement("div");
                    h.style.fontWeight = "900";
                    h.style.paddingTop = "4px";
                    h.textContent = "Kategorien";
                    catList.appendChild(h);
                }
                cats.forEach(function (c) {
                    var tmp = document.createElement("div");
                    tmp.innerHTML = renderCategoryLink(c, activeCategoryId);
                    var a = tmp.firstChild;
                    a.addEventListener("click", function (ev) {
                        ev.preventDefault();
                        var u = new URL(window.location.href);
                        u.searchParams.set("categoryId", c.id);
                        history.replaceState({}, "", u.toString());
                        qsa("a", catList).forEach(function (x) {
                            x.classList.remove("active");
                        });
                        a.classList.add("active");
                        loadProducts(String(c.id || ""));
                    });
                    catList.appendChild(a);
                });
                if (hr) catList.appendChild(hr);
                markDynamicResult(catList, (cats && cats.length) ? 'non-empty' : '');
            }
            await loadProducts(activeCategoryId || "");
        } catch (e) {
            console.warn("Catalog not populated (API unavailable):", e.message || e);
            markDynamicFail(root);
            markDynamicFail(catList);
            markDynamicFail(prodList);
        }

        async function loadProducts(categoryId) {
            if (!prodList) return;
            if (!categoryId) {
                markDynamicFail(prodList);
                return;
            }
            var q = getParam("q") || undefined;
            var sort = getParam("sort") || "popularity";
            var page = Number(getParam("page") || 0);
            var size = Number(getParam("size") || 20);

            try {
                var resp = await api.listProductsByCategory(categoryId, {q: q, sort: sort, page: page, size: size});
                var items = [];
                var total = 0;
                if (Array.isArray(resp)) {
                    items = resp;
                    total = resp.length;
                } else if (resp && Array.isArray(resp.content)) {
                    items = resp.content;
                    total = resp.totalElements || resp.content.length;
                }
                var headerRow = prodList.querySelector("div");
                var headerHtml = headerRow ? headerRow.outerHTML : "";
                markDynamicAttempt(prodList);
                prodList.innerHTML = headerHtml + items.map(renderRowProduct).join("");
                markDynamicResult(prodList, (items && items.length) ? 'non-empty' : '');
                var countEl = prodList.querySelector(".muted");
                if (countEl) countEl.textContent = (total + " Treffer");
            } catch (e) {
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
    var skuEl = qs('[data-product-sku]', root);
    var catEl = qs('[data-product-category]', root);
    var condEl = qs('[data-product-condition]', root);

    var priceEl = qs('[data-product-price]', root);
    var shortEl = qs('[data-product-shortdesc]', root);
    var longEl = qs('[data-product-longdesc]', root);

    var invEl = qs('[data-product-inventory]', root);
    var shipEl = qs('[data-product-shipping]', root);
    var leadEl = qs('[data-product-leadtime]', root);

    var addBtn = qs('[data-action="add-to-cart"]', root);
    var qtyEl = qs('[data-qty]', root);

    var specsGrid = qs('#specsGrid', root);

    var gallery = qs('[data-gallery="product"]', root);
    var mainImg = gallery ? qs('[data-gallery-main]', gallery) : null;
    
    attachImageDebug(mainImg);
var thumbsWrap = gallery ? qs('[data-gallery-thumbs]', gallery) : null;
    var prevBtn = gallery ? qs('[data-gallery-prev]', gallery) : null;
    var nextBtn = gallery ? qs('[data-gallery-next]', gallery) : null;

    var relatedWrap = qs('#relatedProducts', root);

    var inqCompany = qs('[data-inquiry-company]', root);
    var inqEmail = qs('[data-inquiry-email]', root);
    var inqMsg = qs('[data-inquiry-message]', root);
    var inqBtn = qs('[data-action="create-inquiry"]', root);
    var inqStatus = qs('[data-inquiry-status]', root);

    // Mark these as intended dynamic areas (for debug overlays)
    if(typeof markDynamicAttempt === "function"){
      markDynamicAttempt(titleEl);
      markDynamicAttempt(skuEl);
      markDynamicAttempt(catEl);
      markDynamicAttempt(condEl);
      markDynamicAttempt(priceEl);
      markDynamicAttempt(shortEl);
      markDynamicAttempt(longEl);
      markDynamicAttempt(invEl);
      markDynamicAttempt(shipEl);
      markDynamicAttempt(leadEl);
      markDynamicAttempt(specsGrid);
      markDynamicAttempt(gallery);
      markDynamicAttempt(mainImg);
      markDynamicAttempt(thumbsWrap);
      markDynamicAttempt(relatedWrap);
      markDynamicAttempt(inqStatus);
    }

    // Friendly labels for ProductSpecs keys (openapi-product.yaml -> components.schemas.ProductSpecs)
    var SPEC_LABELS = {
      machineType: "Maschinentyp",
      operatingWeightT: "Einsatzgewicht (t)",
      bucketCapacityM3: "Schaufelvolumen (m³)",
      enginePowerKw: "Motorleistung (kW)",
      payloadT: "Nutzlast (t)",
      maxSpeedKmh: "Max. Geschwindigkeit (km/h)",
      maxReachM: "Max. Reichweite (m)",
      maxDigDepthM: "Max. Grabtiefe (m)",
      maxDumpHeightM: "Max. Kipp-/Abwurfhöhe (m)",
      fuelCapacityL: "Tankvolumen (l)",
      rangeKm: "Reichweite (km)",
      throughputTph: "Durchsatz (t/h)",
      beltWidthMm: "Bandbreite (mm)",
      wheelDiameterM: "Raddurchmesser (m)",
      bucketCount: "Schaufelanzahl",
      drumDiameterM: "Trommeldurchmesser (m)",
      bladeWidthM: "Schildbreite (m)",
      bladeCapacityM3: "Schildvolumen (m³)",
      holeDiameterMm: "Bohrdurchmesser (mm)",
      maxHoleDepthM: "Max. Bohrtiefe (m)",
      tireSize: "Reifengröße"
    };

    function fmtSpecValue(v){
      if(v === null || v === undefined) return "";
      if(typeof v === "number"){
        // avoid long floats
        var s = String(v);
        return (s.indexOf(".") >= 0) ? (Math.round(v * 100) / 100).toLocaleString("de-DE") : v.toLocaleString("de-DE");
      }
      return String(v);
    }

    function renderSpecs(specs){
      if(!specsGrid) return;
      var entries = [];
      if(specs && typeof specs === "object"){
        Object.keys(specs).forEach(function(k){
          var v = specs[k];
          if(v === null || v === undefined) return;
          if(typeof v === "string" && v.trim() === "") return;
          entries.push([k, v]);
        });
      }
      if(!entries.length){
        specsGrid.innerHTML = '<div class="muted" style="grid-column:1/-1">Keine technischen Daten verfügbar.</div>';
        if(typeof markDynamicResult === "function") markDynamicResult(specsGrid, "");
        return;
      }

      specsGrid.innerHTML = entries.map(function(ev){
        var key = ev[0], val = ev[1];
        var label = SPEC_LABELS[key] || key;
        return (
          '<div class="spec">' +
            '<div class="k">' + escapeHtml(label) + '</div>' +
            '<div class="v">' + escapeHtml(fmtSpecValue(val)) + '</div>' +
          '</div>'
        );
      }).join("");

      if(typeof markDynamicResult === "function") markDynamicResult(specsGrid, "non-empty");
    }

    function extractImageUrls(mediaImages){
      if(!Array.isArray(mediaImages)) return [];
      return mediaImages
        .map(function(mi){ return safeUrl(mi.file || mi.url || mi.path || mi.full || mi.href); })
        .filter(function(u){ return !!u; });
    }

    function setGallery(urls){
      if(!gallery || !mainImg || !thumbsWrap) return;
      if(!urls || !urls.length){
        if(typeof markDynamicFail === "function"){
          markDynamicFail(gallery); markDynamicFail(mainImg); markDynamicFail(thumbsWrap);
        }
        return;
      }

      // set main
      mainImg.setAttribute("src", urls[0]);
      if(typeof markDynamicResult === "function") markDynamicResult(mainImg, urls[0]);

      // thumbs
      thumbsWrap.innerHTML = "";
      urls.slice(0, 6).forEach(function(u, idx){
        var btn = document.createElement("button");
        btn.type = "button";
        btn.className = idx === 0 ? "active" : "";
        btn.setAttribute("data-gallery-thumb", "true");
        btn.setAttribute("data-src", u);
        var im = document.createElement("img");
        im.src = u;
        im.alt = "Ansicht " + (idx + 1);
        attachImageDebug(im);
        btn.appendChild(im);
        btn.addEventListener("click", function(){
          qsa('button[data-gallery-thumb]', thumbsWrap).forEach(function(b){ b.classList.remove("active"); });
          btn.classList.add("active");
          mainImg.setAttribute("src", u);
          if(typeof markDynamicResult === "function") markDynamicResult(mainImg, u);
        });
        thumbsWrap.appendChild(btn);
      });

      if(typeof markDynamicResult === "function") markDynamicResult(thumbsWrap, "non-empty");

      // nav buttons cycle
      function cycle(dir){
        var btns = qsa('button[data-gallery-thumb]', thumbsWrap);
        if(!btns.length) return;
        var idx = btns.findIndex(function(b){ return b.classList.contains("active"); });
        if(idx < 0) idx = 0;
        var next = (idx + dir + btns.length) % btns.length;
        btns[next].click();
      }
      if(prevBtn) prevBtn.onclick = function(){ cycle(-1); };
      if(nextBtn) nextBtn.onclick = function(){ cycle(1); };
    }

    // ---- Load product detail ----
    var prod = null;
    try{
      prod = await api.getProduct(productId);

      if(prod){
        if(titleEl) dynText(titleEl, prod.technicalName || prod.nickname || prod.id || "");
        if(skuEl) dynText(skuEl, prod.sku || "");
        if(catEl) dynText(catEl, prod.categoryName || prod.categoryId || "");
        if(condEl) dynText(condEl, prod.condition || "");

        // pricing (openapi: ProductDetail.pricing)
        var currency = (prod.pricing && prod.pricing.currency) ? prod.pricing.currency : "€";
        var amount = (prod.pricing && (prod.pricing.priceNormal || prod.pricing.priceCheap || prod.pricing.priceExorbitant)) || "";
        if(priceEl){
          if(typeof markDynamicAttempt === "function") markDynamicAttempt(priceEl);
          priceEl.textContent = fmtMoney(amount, currency);
          if(typeof markDynamicResult === "function") markDynamicResult(priceEl, amount);
        }

        if(shortEl) dynText(shortEl, prod.shortDescription || "");
        if(longEl){
          // keep markdown as-is; scrollable pre box
          if(typeof markDynamicAttempt === "function") markDynamicAttempt(longEl);
          longEl.innerHTML = renderMarkdown(prod.longDescriptionMarkdown || "");
          if(typeof markDynamicResult === "function") markDynamicResult(longEl, prod.longDescriptionMarkdown || "");
        }

        // inventory/shipping
        if(invEl){
          var inv = prod.inventory;
          var invTxt = inv ? ((inv.available === true ? "Verfügbar" : "Nicht verfügbar") + (inv.quantity !== undefined && inv.quantity !== null ? (" (" + inv.quantity + ")") : "")) : "";
          dynText(invEl, invTxt);
        }
        if(shipEl){
          var s = prod.shipping;
          dynText(shipEl, s && s.method ? ("Versand: " + s.method) : "");
        }
        if(leadEl){
          var s2 = prod.shipping;
          dynText(leadEl, s2 && s2.leadTimeDays !== undefined && s2.leadTimeDays !== null ? ("Lieferzeit: " + s2.leadTimeDays + " Tage") : "");
        }

        // specs
        renderSpecs(prod.specs);

        // images (prefer product detail media.images, fallback endpoint)
        var urls = [];
        if(prod.media && Array.isArray(prod.media.images)){
          urls = extractImageUrls(prod.media.images);
        }
        if(urls.length){
          setGallery(urls);
        } else {
          // fallback: separate endpoint
          try{
            var imgs = await api.listProductImages(productId);
            var urls2 = extractImageUrls(imgs);
            setGallery(urls2);
          }catch(_e){
            if(typeof markDynamicFail === "function"){
              markDynamicFail(gallery); markDynamicFail(mainImg); markDynamicFail(thumbsWrap);
            }
          }
        }
      } else {
        if(typeof markDynamicFail === "function") markDynamicFail(root);
      }
    }catch(e){
      console.warn("Product not loaded:", e.message || e);
      if(typeof markDynamicFail === "function") markDynamicFail(root);
    }

    // ---- Related products ----
    async function loadRelated(){
      if(!relatedWrap) return;
      try{
        var rel = await api.listRelatedProducts(productId, 6);
        if(Array.isArray(rel) && rel.length){
          relatedWrap.innerHTML = rel.map(renderProductCard).join("");
          if(typeof markDynamicResult === "function") markDynamicResult(relatedWrap, "non-empty");
        } else {
          relatedWrap.innerHTML = '<div class="muted" style="grid-column:1/-1">Keine ähnlichen Produkte gefunden.</div>';
          if(typeof markDynamicResult === "function") markDynamicResult(relatedWrap, "");
        }
      }catch(e){
        console.warn("Related products not loaded:", e.message || e);
        if(typeof markDynamicFail === "function") markDynamicFail(relatedWrap);
      }
    }
    loadRelated();

    // ---- Add to cart ----
    if(addBtn){
      addBtn.addEventListener("click", async function(ev){
        ev.preventDefault();
        var qty = 1;
        if(qtyEl){
          var n = Number(qtyEl.value);
          if(isFinite(n) && n > 0) qty = Math.floor(n);
        }
        try{
          await api.addCartItem({ productId: productId, quantity: qty });
          if(typeof markDynamicResult === "function") markDynamicResult(addBtn, "ok");
          await refreshCartBadge();
          window.location.href = "cart.xhtml";
        }catch(e){
          console.warn("Add to cart failed:", e.message || e);
          if(typeof markDynamicFail === "function") markDynamicFail(addBtn);
          alert("Konnte nicht zum Warenkorb hinzufügen (API nicht erreichbar).");
        }
      });
    }

    // ---- Create inquiry ----
    if(inqBtn){
      inqBtn.addEventListener("click", async function(){
        try{
          if(inqStatus) dynText(inqStatus, "Sende Anfrage …");
          var body = {
            productId: productId,
            company: inqCompany ? inqCompany.value : "",
            email: inqEmail ? inqEmail.value : "",
            message: inqMsg ? inqMsg.value : ""
          };
          var resp = await api.createInquiry(body);
          if(inqStatus) dynText(inqStatus, "Anfrage gesendet. Referenz: " + (resp && resp.id ? resp.id : "OK"));
          if(typeof markDynamicResult === "function") markDynamicResult(inqBtn, (resp && resp.id) ? String(resp.id) : "ok");
        }catch(e){
          console.warn("Inquiry failed:", e.message || e);
          if(inqStatus) dynText(inqStatus, "Fehler beim Senden der Anfrage.");
          if(typeof markDynamicFail === "function") markDynamicFail(inqBtn);
        }
      });
    }
  }

    function renderCart(cart) {
        var root = qs('[data-page="cart"]');
        if (!root) return;
        var itemsWrap = qs("#cartItems", root);
        var subtotalEl = qs("#cartSubtotal", root);
        var totalEl = qs("#cartTotal", root);
        if (!itemsWrap) return;

        markDynamicAttempt(itemsWrap);
        itemsWrap.innerHTML = "";
        var currency = (cart && cart.currency) ? cart.currency : "€";
        var total = (cart && cart.totalAmount) ? cart.totalAmount : "0";
        var subtotal = total;
        var items = (cart && Array.isArray(cart.items)) ? cart.items : [];

        if (!items.length) {
            itemsWrap.innerHTML = '<div class="muted" style="padding:14px 0">Warenkorb ist leer.</div>';
        } else {
            items.forEach(function (it) {
                var id = it.itemId;
                var name = it.product && (it.product.nickname || it.product.id) ? (it.product.nickname || it.product.id) : "Produkt";
                var thumb = (it.product && it.product.thumbnailUrl) ? it.product.thumbnailUrl : "img/products/excavator-160x90.png";
                var qty = it.quantity || 1;
                var line = it.lineAmount || "";

                var row = document.createElement("div");
                row.className = "cartrow";
                row.innerHTML =
                    '<div class="thumb"><img src="' + safeUrl(thumb) + '" alt="' + escapeHtml(name) + '" /></div>' +
                    '<div><div class="t">' + escapeHtml(name) + '</div><div class="muted" style="font-size:12px">Artikel-ID: ' + escapeHtml(String(id || "")) + '</div></div>' +
                    '<div class="qty">' +
                    '<label>Menge</label>' +
                    '<input type="number" min="1" value="' + qty + '" data-item-qty="true" />' +
                    '</div>' +
                    '<div class="p">' + escapeHtml(fmtMoney(line || "", currency)) + '</div>' +
                    '<div class="btnwrap" style="text-align:right"><a class="btn ghost" href="#" data-action="remove">Entfernen</a></div>';

                var qtyInput = row.querySelector('[data-item-qty]');
                if (qtyInput) {
                    qtyInput.addEventListener("change", async function () {
                        var n = Number(qtyInput.value);
                        if (!isFinite(n) || n < 1) {
                            qtyInput.value = String(qty);
                            return;
                        }
                        try {
                            await api.updateCartItem(id, {quantity: Math.floor(n)});
                            await loadCart();
                        } catch (e) {
                            console.warn("Update cart item failed:", e.message || e);
                            alert("Konnte Menge nicht aktualisieren (API nicht erreichbar).");
                            qtyInput.value = String(qty);
                        }
                    });
                }
                var rm = row.querySelector('[data-action="remove"]');
                if (rm) {
                    rm.addEventListener("click", async function (ev) {
                        ev.preventDefault();
                        try {
                            await api.removeCartItem(id);
                            await loadCart();
                        } catch (e) {
                            console.warn("Remove item failed:", e.message || e);
                            alert("Konnte Artikel nicht entfernen (API nicht erreichbar).");
                        }
                    });
                }
                itemsWrap.appendChild(row);
            });
        }

        markDynamicResult(itemsWrap, (items && items.length) ? 'non-empty' : '');

        if (subtotalEl) {
            markDynamicAttempt(subtotalEl);
            subtotalEl.textContent = fmtMoney(subtotal, currency);
            markDynamicResult(subtotalEl, subtotal);
        }
        if (totalEl) {
            markDynamicAttempt(totalEl);
            totalEl.textContent = fmtMoney(total, currency);
            markDynamicResult(totalEl, total);
        }
    }

    async function loadCart() {
        try {
            var cart = await api.getCart();
            renderCart(cart || {});
            await refreshCartBadge();
            return cart;
        } catch (e) {
            console.warn("Cart not loaded:", e.message || e);
            var _r = qs('[data-page="cart"]');
            if (_r) markDynamicFail(_r);
            return null;
        }
    }

    async function initCart() {
        var root = qs('[data-page="cart"]');
        if (!root) return;
        await loadCart();

        var checkoutBtn = qs('[data-action="checkout"]', root);
        if (checkoutBtn) {
            checkoutBtn.addEventListener("click", async function (ev) {
                ev.preventDefault();
                try {
                    var order = await api.checkout({note: "Demo checkout"});
                    markDynamicResult(checkoutBtn, (order && order.id) ? String(order.id) : "");
                    alert("Checkout erfolgreich. Order-ID: " + (order && order.id ? order.id : "(unbekannt)"));
                    await loadCart();
                } catch (e) {
                    console.warn("Checkout failed:", e.message || e);
                    markDynamicFail(checkoutBtn);
                    alert("Checkout nicht möglich (API nicht erreichbar).");
                }
            });
        }
    }

    document.addEventListener("DOMContentLoaded", function () {
        refreshCartBadge();
        initLanding();
        initCatalog();
        initProduct();
        initCart();
    });
})();
