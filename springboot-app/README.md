# Product Ledger

Spring Boot product catalog, reworked from your original microservice:

- **Storage**: products are now saved as plain JSON text in `data/products.json`
  (created automatically on first run) instead of MySQL. No database setup needed.
- **API**: same endpoints as before, under `/api/v1`:
  - `POST /api/v1/addproduct`
  - `GET  /api/v1/getproducts`
  - `GET  /api/v1/product/{id}`
  - `PUT  /api/v1/updateproduct`
  - `DELETE /api/v1/deleteproduct/{id}`
- **UI**: a single static page at `/` (served by the same app) to add, view, edit,
  and delete products — no separate frontend server required.

## Run it

```bash
./mvnw spring-boot:run
```

Then open **http://localhost:8080** in your browser.

## What changed from the original

- Removed MySQL, Spring Data JPA, and ModelMapper — the service now reads/writes
  a JSON file directly with Jackson (already bundled with Spring Web).
- `Product` and `ProductDTO` dropped the confusing duplicate `id` / `productId`
  fields from the original — there's now just one `id`, assigned automatically
  when a product is created.
- Added a `data/` JSON-backed static UI at `src/main/resources/static/index.html`.
- The storage file location is configurable in `application.properties` via
  `product.storage.file` (defaults to `data/products.json`, relative to wherever
  you run the app from).

## Note

I wasn't able to run a full Maven build in this sandbox (no access to Maven
Central from here), so give `./mvnw spring-boot:run` a try on your machine —
if anything doesn't compile, paste me the error and I'll fix it right away.
