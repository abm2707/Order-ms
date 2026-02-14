-- 1️⃣ Add column as nullable first
ALTER TABLE orders
ADD COLUMN payment_method VARCHAR(20);

-- 2️⃣ Backfill existing rows
UPDATE orders
SET payment_method = 'PREPAID'
WHERE payment_method IS NULL;

-- 3️⃣ Enforce NOT NULL constraint
ALTER TABLE orders
ALTER COLUMN payment_method SET NOT NULL;
