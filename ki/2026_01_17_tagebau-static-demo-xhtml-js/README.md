# Tagebau Static Demo (XHTML)

Enthält 4 statische XHTML-Seiten (Landing, Katalog, Produkt, Warenkorb),
die optisch an die Mockups angelehnt sind und PNG-Assets verwenden.

Start:
- Öffne `landing.xhtml` im Browser.

Spätere Ausbaustufe (JS/REST):
- Platzhalter sind in den Seiten markiert (Hinweise auf /api/...).
- OpenAPI Dateien liegen unter `openapi/` (falls im ZIP enthalten).


## JavaScript REST Binding

- `js/api.js`: Fetch-Client für die in `openapi/` beschriebenen Endpunkte (Base: `/api`).
- `js/app.js`: Bindet die vier Seiten an die API, füllt Inhalte dynamisch und verdrahtet Cart/Checkout.
- Ohne laufende API bleiben die statischen Inhalte sichtbar; Warnungen erscheinen in der Browser-Konsole.
