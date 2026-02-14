-- ==========================================
-- Reconciliation support columns for orders
-- ==========================================

ALTER TABLE orders
    ADD COLUMN last_state_updated_at TIMESTAMP,
    ADD COLUMN reconciliation_attempts INTEGER DEFAULT 0 NOT NULL,
    ADD COLUMN last_reconciliation_at TIMESTAMP,
    ADD COLUMN reconciliation_lock BOOLEAN DEFAULT FALSE NOT NULL,
    ADD COLUMN payment_reference_id VARCHAR(64),
    ADD COLUMN inventory_reservation_id VARCHAR(64);

-- ------------------------------------------
-- Backfill last_state_updated_at for old rows
-- ------------------------------------------
-- Assumption:
--   created_at or updated_at already exists
--   Adjust if your column name is different

UPDATE orders
SET last_state_updated_at = COALESCE(updated_at, created_at, NOW())
WHERE last_state_updated_at IS NULL;

-- ------------------------------------------
-- Enforce NOT NULL after backfill
-- ------------------------------------------
ALTER TABLE orders
    ALTER COLUMN last_state_updated_at SET NOT NULL;

-- ------------------------------------------
-- Helpful indexes for reconciliation scans
-- ------------------------------------------
CREATE INDEX IF NOT EXISTS idx_orders_reconciliation_scan
ON orders (status, last_state_updated_at)
WHERE status IN ('PENDING', 'PAYMENT_PENDING');
