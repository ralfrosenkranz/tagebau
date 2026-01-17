(function(){
  function qs(sel, root){ return (root||document).querySelector(sel); }
  function qsa(sel, root){ return Array.prototype.slice.call((root||document).querySelectorAll(sel)); }

  // Product gallery: static swap now, later: /api/products/{id}/media/images
  var gallery = qs('[data-gallery="product"]');
  if(gallery){
    var main = qs('[data-gallery-main]', gallery);
    qsa('[data-gallery-thumb]', gallery).forEach(function(btn){
      btn.addEventListener('click', function(){
        qsa('[data-gallery-thumb]', gallery).forEach(function(b){ b.classList.remove('active'); });
        btn.classList.add('active');
        var src = btn.getAttribute('data-src');
        if(src && main){ main.setAttribute('src', src); }
      });
    });
  }
})();
