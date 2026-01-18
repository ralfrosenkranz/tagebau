(function(global){
  "use strict";

  function buildQuery(params){
    if(!params) return "";
    var parts = [];
    Object.keys(params).forEach(function(k){
      var v = params[k];
      if(v === undefined || v === null || v === "") return;
      if(Array.isArray(v)){
        v.forEach(function(it){
          parts.push(encodeURIComponent(k) + "=" + encodeURIComponent(String(it)));
        });
      } else {
        parts.push(encodeURIComponent(k) + "=" + encodeURIComponent(String(v)));
      }
    });
    return parts.length ? ("?" + parts.join("&")) : "";
  }

  function ApiClient(options){
    options = options || {};
    this.baseUrl = options.baseUrl || "/api";
    this.defaultHeaders = options.defaultHeaders || { "Accept": "application/json" };
    this.timeoutMs = options.timeoutMs || 15000;
  }

  ApiClient.prototype._fetchJson = async function(path, options){
    options = options || {};
    var url = this.baseUrl + path;
    var headers = Object.assign({}, this.defaultHeaders, options.headers || {});
    var controller = new AbortController();
    var t = setTimeout(function(){ controller.abort(); }, this.timeoutMs);
    try{
      var res = await fetch(url, Object.assign({}, options, { headers: headers, signal: controller.signal, credentials: "same-origin" }));
      if(!res.ok){
        var text = "";
        try{ text = await res.text(); }catch(e){}
        var err = new Error("HTTP " + res.status + " " + res.statusText + " for " + url);
        err.status = res.status;
        err.body = text;
        throw err;
      }
      if(res.status === 204) return null;
      var ct = res.headers.get("content-type") || "";
      if(ct.indexOf("application/json") >= 0){
        return await res.json();
      }
      return await res.text();
    } finally {
      clearTimeout(t);
    }
  };

  ApiClient.prototype._postJson = function(path, body){
    return this._fetchJson(path, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(body || {})
    });
  };
  ApiClient.prototype._patchJson = function(path, body){
    return this._fetchJson(path, {
      method: "PATCH",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(body || {})
    });
  };
  ApiClient.prototype._delete = function(path){
    return this._fetchJson(path, { method: "DELETE" });
  };

  // Landing / Navigation
  ApiClient.prototype.getLanding = function(topLimit){
    return this._fetchJson("/landing" + buildQuery({ topLimit: topLimit }));
  };
  ApiClient.prototype.listCategories = function(){
    return this._fetchJson("/categories");
  };
  ApiClient.prototype.listTopProducts = function(limit){
    return this._fetchJson("/products/top" + buildQuery({ limit: limit }));
  };
  ApiClient.prototype.searchProducts = function(q, limit){
    return this._fetchJson("/products/search" + buildQuery({ q: q, limit: limit }));
  };

  // Catalog
  ApiClient.prototype.getCatalog = function(){
    return this._fetchJson("/catalog");
  };
  ApiClient.prototype.listProductsByCategory = function(categoryId, params){
    params = params || {};
    return this._fetchJson("/categories/" + encodeURIComponent(categoryId) + "/products" + buildQuery(params));
  };

  // Product
  ApiClient.prototype.getProduct = function(productId){
    return this._fetchJson("/products/" + encodeURIComponent(productId));
  };
  ApiClient.prototype.listProductImages = function(productId){
    return this._fetchJson("/products/" + encodeURIComponent(productId) + "/media/images");
  };
  ApiClient.prototype.listRelatedProducts = function(productId, limit){
    return this._fetchJson("/products/" + encodeURIComponent(productId) + "/related" + buildQuery({ limit: limit }));
  };
  ApiClient.prototype.createInquiry = function(body){
    return this._postJson("/inquiries", body);
  };

  // Cart / Orders
  ApiClient.prototype.getCart = function(){
    return this._fetchJson("/cart");
  };
  ApiClient.prototype.clearCart = function(){
    return this._delete("/cart");
  };
  ApiClient.prototype.addCartItem = function(body){
    return this._postJson("/cart/items", body);
  };
  ApiClient.prototype.updateCartItem = function(itemId, body){
    return this._patchJson("/cart/items/" + encodeURIComponent(itemId), body);
  };
  ApiClient.prototype.removeCartItem = function(itemId){
    return this._delete("/cart/items/" + encodeURIComponent(itemId));
  };
  ApiClient.prototype.checkout = function(body){
    return this._postJson("/cart/checkout", body);
  };
  ApiClient.prototype.listOrders = function(params){
    return this._fetchJson("/orders" + buildQuery(params || {}));
  };
  ApiClient.prototype.getOrder = function(orderId){
    return this._fetchJson("/orders/" + encodeURIComponent(orderId));
  };

  // Users (demo)
  ApiClient.prototype.register = function(body){
    return this._postJson("/users/register", body);
  };
  ApiClient.prototype.login = function(body){
    return this._postJson("/users/login", body);
  };
  ApiClient.prototype.me = function(){
    return this._fetchJson("/users/me");
  };

  global.TagebauApi = { ApiClient: ApiClient };
})(window);
