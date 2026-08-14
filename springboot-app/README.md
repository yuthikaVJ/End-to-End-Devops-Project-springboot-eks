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




